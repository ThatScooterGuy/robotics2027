package frc.robot.shim.mode;

import java.util.concurrent.ArrayBlockingQueue;

import frc.robot.data.mode.Mode;
import frc.robot.system.mode.ModeChangeBroadcaster;

public class ModeChangeListener {
    private ArrayBlockingQueue<Mode> queue = new ArrayBlockingQueue<>(1);

    public ModeChangeListener() {
        ModeChangeBroadcaster.addListener(this);
    }
    public boolean pushEvent(final Mode event) {
        this.queue.poll();
        return this.queue.offer(event);
    }
    public Mode waitForModeChange() {
        try {
            return this.queue.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        throw new IllegalStateException("unreachable");
    }
}
