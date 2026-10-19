package frc.robot.log;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ArrayBlockingQueue;

import org.agrona.concurrent.AtomicBuffer;
import org.agrona.concurrent.UnsafeBuffer;
import org.agrona.concurrent.ringbuffer.ManyToOneRingBuffer;
import org.agrona.concurrent.ringbuffer.RingBufferDescriptor;

import edu.wpi.first.wpilibj.Filesystem;

public class Logger {
    private static final int MESSAGE_TYPE_OFFSET = 0;
    private static final int TOPIC_LENGTH_OFFSET = MESSAGE_TYPE_OFFSET + 1;
    private static final int TOPIC_OFFSET = TOPIC_LENGTH_OFFSET + 2;

    private static final int BUFFER_SIZE = 1024 * 16;

    private static final byte DOUBLE_TYPE = 0;

    private static FileChannel channel = null;
    private static ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
    private static ManyToOneRingBuffer stagingBuffer = new ManyToOneRingBuffer(
            new UnsafeBuffer(ByteBuffer.allocateDirect(1024 * 16 + RingBufferDescriptor.TRAILER_LENGTH)));
    private static Thread thread = null;

    private static class LogHelper implements Runnable {
        public void run() {
            while (true)
            stagingBuffer.read((msgId, mbuffer, index, length) -> {
                byte messageType = mbuffer.getByte(index + MESSAGE_TYPE_OFFSET);
                short topicLen = mbuffer.getShort(index + TOPIC_LENGTH_OFFSET);
                int valueOffset = getValueOffset(index, topicLen);

                int valueLen = switch (messageType) {
                    case DOUBLE_TYPE -> 8;
                    default          -> 0;
                    
                };
                
                if (buffer.remaining() < getMessageLength(topicLen, valueLen)) {
                    buffer.flip();
                    try {
                        while (buffer.hasRemaining())
                           channel.write(buffer);
                    } catch (IOException e) {
                        System.out.println("failed to write logs");
                        return;
                    } finally {
                        buffer.clear();
                    }
                }
                
                buffer.put(messageType);
                buffer.putShort(topicLen);
                
                for (int i = 0; i < topicLen / 2; i += 1) buffer.putShort(mbuffer.getShort(index + TOPIC_OFFSET + i * 2));

                switch (messageType) {
                    case DOUBLE_TYPE -> buffer.putDouble(mbuffer.getDouble(index + valueOffset));
                }
            });
        }
    }

    static {
        File logDir = new File(Filesystem.getOperatingDirectory(), "logs");
        if (!logDir.exists()) {
            logDir.mkdirs();
        }
        File logFile = new File(logDir, "log_" + System.currentTimeMillis() + ".dat");

        try {
            channel = FileChannel.open(logFile.toPath(), StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
        } catch (IOException e) {
            System.out.println("failed to create log file");
            System.out.println(e.getMessage());
            System.exit(1);
        }

        thread = new Thread(new Logger.LogHelper(), "logger");
        thread.setDaemon(true);
        thread.start();
    }

    private static int getMessageLength(int topicLen, int payloadLength) {
        // message type + topic length + topic + payload
        return 1 + 2 + topicLen * 2 + payloadLength;
    }

    private static void putTopic(AtomicBuffer buffer, String topic, int offset) {
        for (int i = 0; i < topic.length(); i += 1) {
            buffer.putShort(offset + TOPIC_OFFSET + i * 2, (short) topic.charAt(i));
        }
    }

    private static int getValueOffset(int offset, int topicLen) {
        return offset + TOPIC_OFFSET + topicLen * 2;
    }

    public static void log(LogLevel logLevel, String topic, double value) {
        if (topic.length() * 2 > Short.MAX_VALUE)
            return;

        int offset = stagingBuffer.tryClaim(logLevel.getId(), getMessageLength(topic.length(), 8));

        // can't find space, just drop the log. losing the entire program to a exception
        // isn't worth it
        if (offset <= 0)
            return;

        int valueOffset = getValueOffset(offset, topic.length());

        AtomicBuffer buffer = stagingBuffer.buffer();

        buffer.putByte(offset + MESSAGE_TYPE_OFFSET, DOUBLE_TYPE);
        buffer.putShort(offset + TOPIC_LENGTH_OFFSET, (short) (topic.length() * 2));
        putTopic(buffer, topic, offset);
        buffer.putDouble(valueOffset, value);

        stagingBuffer.commit(offset);
    }
}
