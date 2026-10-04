package frc.robot.api.input;

import frc.robot.data.input.NavigationDirection;

public interface Controller {
    public abstract boolean isPrimaryButtonPressed();
    public abstract boolean isSecondaryButtonPressed();
    public abstract boolean isUtilityButtonPressed();
    public abstract boolean isAlternateButtonPressed();

    public abstract boolean isLeftShoulderPressed();
    public abstract boolean isRightShoulderPressed();

    public abstract double getPrimaryAxisX();
    public abstract double getPrimaryAxisY();
    
    public abstract double getSecondaryAxisX();
    public abstract double getSecondaryAxisY();

    public abstract double getLeftAnalogAxis();
    public abstract double getRightAnalogAxis();

    public abstract NavigationDirection getNavigationDirection();

    public abstract boolean supportsActionCluster();
    public abstract boolean supportsShoulders();
    public abstract boolean supportsPrimaryAxis();
    public abstract boolean supportsSecondaryAxis();
    public abstract boolean supportsAnalogAxis();
    public abstract boolean supportsNavigation();
}
