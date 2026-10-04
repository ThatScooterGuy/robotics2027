package frc.robot.system.log;

import java.util.concurrent.CompletableFuture;

import frc.robot.data.mode.Mode;
import frc.robot.shim.mode.ModeChangeListener;

public class OnModeChange {
    private static ModeChangeListener modeChangeListener = null; 
    public static boolean initialize() {
        modeChangeListener = new ModeChangeListener();
        CompletableFuture.runAsync(() -> {
            while (true) {
                Mode event = modeChangeListener.waitForModeChange();
                switch (event) {
                    case ToDisabled:
                        System.out.println("mode has been switched to disabled");
                        break;
                    case ToTeleop:
                        System.out.println("mode has been switched to teleoperated");
                        break;
                    case ToAuto:
                        System.out.println("mode has been switched to autonomous");
                        break;
                    case ToTest:
                        System.out.println("mode has been switched to test");
                        break;
                }
            }
        });
        return true;
    }
}
