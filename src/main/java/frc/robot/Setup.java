package frc.robot;

import com.ctre.phoenix6.configs.ClosedLoopGeneralConfigs;
import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.FeedbackConfigs;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.OpenLoopRampsConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.SoftwareLimitSwitchConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.FeedbackSensorSourceValue;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import frc.robot.strict.wall.motor.TalonFXMotor;

public class Setup {
    private static boolean checkDriveMotorConfigStale(TalonFX motor) {
        TalonFXConfiguration config = new TalonFXConfiguration();
        motor.getConfigurator().refresh(config);

        return config.MotorOutput.DutyCycleNeutralDeadband != Consts.Settings.Motor.deadband ||
               config.CurrentLimits.StatorCurrentLimit != Consts.Settings.Motor.currentLimit ||
               config.CurrentLimits.SupplyCurrentLowerTime != Consts.Settings.Motor.currentLimitMaxOver ||
               config.OpenLoopRamps.DutyCycleOpenLoopRampPeriod != Consts.Settings.Motor.timeToFullSpeed ||
               config.OpenLoopRamps.VoltageOpenLoopRampPeriod != Consts.Settings.Motor.timeToFullSpeed ||
               config.OpenLoopRamps.TorqueOpenLoopRampPeriod != Consts.Settings.Motor.timeToFullSpeed ||
               config.Slot0.kS != Consts.Settings.Motor.PID.kS ||
               config.Slot0.kV != Consts.Settings.Motor.PID.kV ||
               config.Slot0.kA != Consts.Settings.Motor.PID.kA ||
               config.Slot0.kP != Consts.Settings.Motor.PID.kP ||
               config.Slot0.kI != Consts.Settings.Motor.PID.kI ||
               config.Slot0.kD != Consts.Settings.Motor.PID.kD;

    }

    private static void setUpDriveMotor(TalonFX motor) {
        if (!checkDriveMotorConfigStale(motor)) return;

        TalonFXConfiguration config = new TalonFXConfiguration()
            .withFeedback(new FeedbackConfigs()
                .withRotorToSensorRatio(Consts.Settings.Motor.sensorToMotorRatio)
                .withFeedbackSensorSource(FeedbackSensorSourceValue.FusedCANcoder))
            .withMotorOutput(new MotorOutputConfigs()
                .withNeutralMode(NeutralModeValue.Brake)
                .withInverted(InvertedValue.Clockwise_Positive)
                .withDutyCycleNeutralDeadband(Consts.Settings.Motor.deadband))
            .withSlot0(new Slot0Configs()
                .withKS(Consts.Settings.Motor.PID.kS)
                .withKV(Consts.Settings.Motor.PID.kV)
                .withKA(Consts.Settings.Motor.PID.kA)
                .withKP(Consts.Settings.Motor.PID.kP)
                .withKI(Consts.Settings.Motor.PID.kI)
                .withKD(Consts.Settings.Motor.PID.kD))
            .withCurrentLimits(new CurrentLimitsConfigs()
                .withStatorCurrentLimit(Consts.Settings.Motor.currentLimit)
                .withSupplyCurrentLowerTime(Consts.Settings.Motor.currentLimitMaxOver)
                .withStatorCurrentLimitEnable(true))
            .withClosedLoopGeneral(new ClosedLoopGeneralConfigs()
                .withContinuousWrap(true))
            .withOpenLoopRamps(new OpenLoopRampsConfigs()
                .withDutyCycleOpenLoopRampPeriod(Consts.Settings.Motor.timeToFullSpeed)
                .withVoltageOpenLoopRampPeriod(Consts.Settings.Motor.timeToFullSpeed)
                .withTorqueOpenLoopRampPeriod(Consts.Settings.Motor.timeToFullSpeed))
            .withSoftwareLimitSwitch(new SoftwareLimitSwitchConfigs()
                .withForwardSoftLimitEnable(false));

        motor.getConfigurator().apply(config);

        System.out.println("config changed");
    }
    public static void setUpDriveMotors(TalonFXMotor[] motors) {
        for (int i = 0; i < Consts.Wheels.wheelCount; i += 1) setUpDriveMotor(motors[i].inner());
    }
}
