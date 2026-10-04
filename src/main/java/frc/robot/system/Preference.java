package frc.robot.system;

import edu.wpi.first.wpilibj.DriverStation;

public class Preference {
    public static boolean initialize() {
        DriverStation.silenceJoystickConnectionWarning(true);
        return true;
    }
}
