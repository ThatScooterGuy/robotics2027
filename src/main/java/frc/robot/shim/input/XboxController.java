package frc.robot.shim.input;

import frc.robot.api.input.Controller;
import frc.robot.data.input.NavigationDirection;

public class XboxController implements Controller {
    private edu.wpi.first.wpilibj.XboxController controller = null;

    public XboxController(int port) {
        controller = new edu.wpi.first.wpilibj.XboxController(port);
    }

    @Override public boolean isPrimaryButtonPressed() {
        return controller.getAButton();
    }
    @Override public boolean isSecondaryButtonPressed() {
        return controller.getBButton();
    }
    @Override public boolean isUtilityButtonPressed() {
        return controller.getYButton();
    }
    @Override public boolean isAlternateButtonPressed() {
        return controller.getXButton();
    }
    @Override public boolean isLeftShoulderPressed() {
        return controller.getLeftBumperButton();
    }
    @Override public boolean isRightShoulderPressed() {
        return controller.getRightBumperButton();
    }
    @Override public double getPrimaryAxisX() {
        return controller.getLeftX();
    }
    @Override public double getPrimaryAxisY() {
        return controller.getLeftY();
    }
    @Override public double getSecondaryAxisX() {
        return controller.getRightX();
    }
    @Override public double getSecondaryAxisY() {
        return controller.getRightY();
    }
    @Override public double getLeftAnalogAxis() {
        return controller.getLeftTriggerAxis();
    }
    @Override public double getRightAnalogAxis() {
        return controller.getRightTriggerAxis();
    }
    @Override public NavigationDirection getNavigationDirection() {
        return switch (controller.getPOV()) {
            case 0   -> NavigationDirection.Up;
            case 45  -> NavigationDirection.UpRight;
            case 90  -> NavigationDirection.Right;
            case 135 -> NavigationDirection.DownRight;
            case 180 -> NavigationDirection.Down;
            case 225 -> NavigationDirection.DownLeft;
            case 270 -> NavigationDirection.Left;
            case 315 -> NavigationDirection.UpLeft;
            case -1  -> NavigationDirection.Center;
            default  -> throw new IllegalStateException("direction returned not valid");
        };
    }
    
    @Override public boolean supportsActionCluster() {
        return true;
    }
    @Override public boolean supportsShoulders() {
        return true;
    }
    @Override public boolean supportsPrimaryAxis() {
        return true;
    }
    @Override public boolean supportsSecondaryAxis() {
        return true;
    }
    @Override public boolean supportsAnalogAxis() {
        return true;
    }
    @Override public boolean supportsNavigation() {
        return true;
    }
}