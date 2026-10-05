package frc.robot.system.mode;

import java.util.concurrent.atomic.AtomicReferenceArray;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.RobotState;
import frc.robot.data.mode.Mode;
import frc.robot.shim.mode.ModeChangeListener;

public class ModeChangeBroadcaster {
    private static LastEvent lastEvent = null;
    private static Notifier notifier = null;
    private static AtomicReferenceArray<ModeChangeListener> listeners = null;
    private static int listener_len = 0;
    private static boolean initialized = false;

    private static LastEvent getEvent() {
        DriverStation.refreshData();
        if (RobotState.isDisabled() ||
                RobotState.isEStopped())
            return LastEvent.Disabled;
        else if (RobotState.isTeleop())
            return LastEvent.Teleop;
        else if (RobotState.isAutonomous())
            return LastEvent.Auto;
        else
            return LastEvent.Test;
    }

    private static void init() {
        listeners = new AtomicReferenceArray<>(256);
        notifier = new Notifier(() -> {
            final LastEvent event = getEvent();
            if (lastEvent != event) {
                update(event.toEvent());
            }
            lastEvent = event;
        });
        notifier.startPeriodic(0.010);
        initialized = true;
    }

    private static void update(final Mode event) {
        for (int i = 0; i < listener_len; i += 1) {
            final ModeChangeListener listener = listeners.get(i);
            listener.pushEvent(event);
        }
    }

    public static boolean addListener(final ModeChangeListener eventListener) {
        if (!initialized) init();
        if (listener_len == listeners.length())
            return false;
        listeners.getAndSet(listener_len, eventListener);
        listener_len += 1;
        return true;
    }
    public static boolean initialize() {
        if (!initialized) init();
        return true;
    }
}

enum LastEvent {
    Disabled,
    Teleop,
    Auto,
    Test;

    public Mode toEvent() {
        switch (this) {
            case Disabled:
                return Mode.ToDisabled;
            case Teleop:
                return Mode.ToTeleop;
            case Auto:
                return Mode.ToAuto;
            case Test:
                return Mode.ToTest;
            default:
                throw new IllegalStateException("event not handled: " + this);
        }
    }
}
