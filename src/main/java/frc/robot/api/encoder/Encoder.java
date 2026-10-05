package frc.robot.api.encoder;
import frc.robot.data.encoder.EncoderPositionType;

public interface Encoder {
    public abstract double getPosition(EncoderPositionType positionType);
    
    public abstract double setInverted(boolean inverted);
    public abstract void setPosition(double pos);

    public abstract boolean isInverted();
}
