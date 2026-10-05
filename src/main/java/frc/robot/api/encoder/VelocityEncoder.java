package frc.robot.api.encoder;

import frc.robot.data.encoder.EncoderVelocityType;

public interface VelocityEncoder extends Encoder {
    public abstract double getVelocity(EncoderVelocityType velocityType);
}
