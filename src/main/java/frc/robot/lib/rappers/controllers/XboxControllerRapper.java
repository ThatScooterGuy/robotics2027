package  frc.robot.lib.rappers.controllers;

import edu.wpi.first.wpilibj.XboxController;
import frc.robot.lib.state.AxisTracker;
import lombok.Getter;

public class XboxControllerRapper {
    @Getter
    private XboxController xboxController = null;

    public XboxControllerRapper(int port) {
        xboxController = new XboxController(port);
    }

    public double LeftX() {
        return this.xboxController.getLeftX();
    }
    public double RightX() {
        return this.xboxController.getRightX();
    }
    public double LeftY() {
        return this.xboxController.getLeftY();
    }
    public double RightY() {
        return this.xboxController.getRightY();
    }

    public AxisTracker onAxisChangeLeft(double distance, double snapThreshold, double deadband) {
        return new AxisTracker(distance, deadband, snapThreshold, xboxController::getLeftX, xboxController::getLeftY);
    }
    public AxisTracker onAxisChangeRight(double distance, double snapThreshold, double deadband) {
        return new AxisTracker(distance, deadband, snapThreshold, xboxController::getRightX, xboxController::getRightY);
    }
    public AxisTracker onAxisChangeLeftX(double distance, double snapThreshold, double deadband) {
        return new AxisTracker(distance, deadband, snapThreshold, xboxController::getLeftX, () -> 0);
    }
    public AxisTracker onAxisChangeRightX(double distance,double snapThreshold, double deadband) {
        return new AxisTracker(distance, deadband, snapThreshold, xboxController::getRightX, () -> 0);
    }
    public AxisTracker onAxisChangeLeftY(double distance, double snapThreshold, double deadband) {
        return new AxisTracker(distance, deadband, snapThreshold, () -> 0, xboxController::getLeftY);
    }
    public AxisTracker onAxisChangeRightY(double distance, double snapThreshold, double deadband) {
        return new AxisTracker(distance, deadband, snapThreshold, () -> 0, xboxController::getRightY);
    }
}
