package frc.robot.system.loop;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;

import edu.wpi.first.wpilibj.Notifier;
import frc.robot.Consts;

public class Repeat {
    private static final AtomicBoolean initialized = new AtomicBoolean(false);
    private static AtomicReferenceArray<Runnable> tasks = null;
    private static AtomicInteger tasks_len = new AtomicInteger(0);
    private static Notifier notifier = null;

    private static void init() {
        tasks = new AtomicReferenceArray<>(256);
        
        notifier = new Notifier(() -> {
            int len = tasks_len.get();
            for (int i = 0; i < len; i += 1) {
                tasks.get(i).run();
            }
        });
        notifier.startPeriodic(Consts.Repeat.loopPeriod);
    }

    public static boolean initialize() {
        if(!initialized.getAndSet(true)) init();
        return true;
    }

    public static boolean addTask(Runnable func) {
        if(!initialized.getAndSet(true)) init();
        int len = tasks_len.getAndIncrement();
        if (len >= tasks.length()) return false;

        tasks.set(len, func);
        return true;
    }
}
