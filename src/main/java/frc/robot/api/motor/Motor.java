package frc.robot.api.motor;

public interface Motor {
    public abstract void setSpeed(double speed);
    public abstract void setVoltage(double volts);

    public abstract void setInverted(boolean inverted);
    public abstract void setBrakeMode(boolean enable);

    public abstract boolean getInverted();
}
