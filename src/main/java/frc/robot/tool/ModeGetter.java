package frc.robot.tool;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotState;
import frc.robot.data.mode.Mode;

public class ModeGetter {
    public static Mode getMode() {
                DriverStation.refreshData();
        if (RobotState.isDisabled() ||
                RobotState.isEStopped())
            return Mode.ToDisabled;
        else if (RobotState.isTeleop())
            return Mode.ToTeleop;
        else if (RobotState.isAutonomous())
            return Mode.ToAuto;
        else
            return Mode.ToTest;
    }
}
