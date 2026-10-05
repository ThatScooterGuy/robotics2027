package frc.robot.shim.motor;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import frc.robot.api.motor.Motor;

public class Neo implements Motor {
    SparkMax sparkMax = null;
    public Neo(int id) {
        sparkMax = new SparkMax(id, MotorType.kBrushless);
    }
    
    @Override public void setSpeed(double speed) {
        sparkMax.set(speed);
    }
    @Override public void setVoltage(double volts) {
        sparkMax.setVoltage(volts);
    }
    @Override public void setBrakeMode(boolean enable) {
        IdleMode idleMode = sparkMax.configAccessor.getIdleMode();
        if (idleMode == IdleMode.kBrake && enable || idleMode == IdleMode.kCoast && !enable) return;
        SparkMaxConfig config = new SparkMaxConfig();
        config.idleMode(enable ? IdleMode.kBrake : IdleMode.kCoast);
        sparkMax.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }
    @Override public void setInverted(boolean inverted) {
        if (sparkMax.configAccessor.getInverted() == inverted) return;
        SparkMaxConfig config = new SparkMaxConfig();
        config.inverted(inverted);
        sparkMax.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }
    @Override public boolean getInverted() {
        return sparkMax.configAccessor.getInverted();
    }
}
