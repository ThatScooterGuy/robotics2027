package frc.robot.system.drivetrain;

import edu.wpi.first.wpilibj.Notifier;
import frc.robot.Consts;
import frc.robot.api.input.Controller;
import frc.robot.system.input.ControllerManager;

public class Tank {
    private static Controller primaryController = null;
    private static boolean isPressed = false;
    private static Notifier notifier = null;

    public static boolean initialize() {
        primaryController = ControllerManager.getControllerFromPort(Consts.ControllerInfo.primaryControllerPort);
        if (!primaryController.supportsActionCluster()) {
            System.err.println("primary controller does not support the actions cluster");
            return false;
        }
        notifier = new Notifier(() -> {
            boolean primePressed = primaryController.isPrimaryButtonPressed();
            if (primePressed != isPressed && primePressed == true) {
                System.out.println("primary button pressed");
            }
            isPressed = primePressed;
        });
        notifier.startPeriodic(0.010);
        return true;
    }
}
