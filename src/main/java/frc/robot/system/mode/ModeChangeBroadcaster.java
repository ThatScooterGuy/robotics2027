package frc.robot.system.mode;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;
import edu.wpi.first.wpilibj.Notifier;
import frc.robot.data.mode.Mode;
import frc.robot.shim.mode.ModeChangeListener;
import frc.robot.tool.ModeGetter;

public class ModeChangeBroadcaster {
    private static Mode lastEvent = null;
    private static Notifier notifier = null;
    private static AtomicReferenceArray<ModeChangeListener> listeners = null;
    private static AtomicInteger listener_len = new AtomicInteger(0);
    private static AtomicBoolean initialized = new AtomicBoolean(false);

    private static void init() {
        listeners = new AtomicReferenceArray<>(256);
        notifier = new Notifier(() -> {
            final Mode mode = ModeGetter.getMode();
            if (lastEvent != mode) {
                update(mode);
            }
            lastEvent = mode;
        });
        notifier.startPeriodic(0.010);
    }

    private static void update(final Mode event) {
        for (int i = 0; i < listener_len.get(); i += 1) {
            final ModeChangeListener listener = listeners.get(i);
            listener.pushEvent(event);
        }
    }

    public static boolean addListener(final ModeChangeListener eventListener) {
        if (!initialized.getAndSet(true)) init();
        int len = listener_len.getAndIncrement();
        if (len >= listeners.length())
            return false;
        listeners.getAndSet(len, eventListener);
        return true;
    }
    public static boolean initialize() {
        if (!initialized.getAndSet(true)) init();
        return true;
    }
}
