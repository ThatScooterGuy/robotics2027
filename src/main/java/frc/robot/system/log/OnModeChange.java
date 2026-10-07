package frc.robot.system.log;

import frc.robot.data.mode.Mode;
import frc.robot.system.loop.Repeat;
import frc.robot.tool.ModeGetter;

public class OnModeChange {
    private static Mode lastMode = null;
    public static boolean initialize() {
        Repeat.addTask(() -> {
            final Mode mode = ModeGetter.getMode();
            if (lastMode == null);
            else if (mode != lastMode)
                if (lastMode == Mode.ToDisabled)
                    switch (mode) {
                        case ToTeleop   -> System.out.println("robot is now enabled and switched to being teleoperated");
                        case ToAuto     -> System.out.println("robot is now enabled and switched to being autonomous");
                        case ToTest     -> System.out.println("robot is now enabled and switched to test mode");
                        default         -> throw new IllegalStateException("disabled yet not disabled");
                    } 
                else
                    switch (mode) {
                        case ToTeleop   -> System.out.println("robot has switched to being teleoperated");
                        case ToAuto     -> System.out.println("robot has switched to being autonomous");
                        case ToTest     -> System.out.println("robot has switched to test mode");
                        case ToDisabled -> System.out.println("robot has been disabled");
                    };
            lastMode = mode;
        });
        return true;
    }
}
