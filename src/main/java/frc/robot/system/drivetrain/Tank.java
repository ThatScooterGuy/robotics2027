package frc.robot.system.drivetrain;

import edu.wpi.first.wpilibj.Notifier;
import frc.robot.Consts;
import frc.robot.api.input.Controller;
import frc.robot.system.input.ControllerManager;

public class Tank {
    private static Controller primaryController = null;
    private static Notifier notifier = null;

    public static boolean initialize() {
        primaryController = ControllerManager.getControllerFromPort(Consts.ControllerInfo.primaryControllerPort);
        if (!primaryController.supportsPrimaryAxis()) {
            System.err.println("primary controller does not support the Primary joystick");
            return false;
        }
        if (!primaryController.supportsSecondaryAxis()) {
            System.err.println("primary controller does not support the Secondary joystick");
            return false;
        }
        notifier = new Notifier(() -> {
            
        });
        notifier.startPeriodic(0.010);
        return true;
    }
}
