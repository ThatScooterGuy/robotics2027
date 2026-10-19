package frc.robot.strict.wall.motor;

import static edu.wpi.first.units.Units.RotationsPerSecond;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.controls.ControlRequest;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.sim.TalonFXSimState;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;
import frc.robot.Consts;
import frc.robot.log.LogLevel;
import frc.robot.log.Loggable;
import frc.robot.log.Logger;

public class TalonFXMotor implements Loggable {

    private TalonFX motor = null;

    private String name = null;

    private SimpleMotorFeedforward motorVoltageSim = null;
    private PIDController pidControllerSim = null;
    private DCMotorSim simMotor = null;

    public TalonFXMotor(int port, CANBus canBus, String name) {
        this.name = name;
        motor = new TalonFX(port, canBus);

        if (RobotBase.isSimulation()) {

            simMotor = new DCMotorSim(
                    LinearSystemId.createDCMotorSystem(
                            DCMotor.getKrakenX60(1),
                            0.01,
                            1),
                    DCMotor.getKrakenX60(1));

            pidControllerSim = new PIDController(Consts.Settings.Motor.PID.kP, Consts.Settings.Motor.PID.kI,
                    Consts.Settings.Motor.PID.kD);
            motorVoltageSim = new SimpleMotorFeedforward(Consts.Settings.Motor.PID.kS, Consts.Settings.Motor.PID.kV,
                    Consts.Settings.Motor.PID.kA);
        }
    }

    public void advanceSimulation(AngularVelocity angularVelocity) {

        double voltage = motorVoltageSim.calculate(angularVelocity.in(RotationsPerSecond)) +
                pidControllerSim.calculate(motor.getVelocity().refresh().getValueAsDouble(),
                        angularVelocity.in(RotationsPerSecond));

        simMotor.setInputVoltage(voltage);

        simMotor.update(0.020);

        TalonFXSimState simHandle = motor.getSimState();
        simHandle.setRotorVelocity(simMotor.getAngularVelocity());
    }

    public void setControl(ControlRequest request) {
        motor.setControl(request);
    }

    public TalonFX inner() {
        return motor;
    }

    public String getName() {
        return name;
    }

    public void log() {
        Logger.log(LogLevel.VALUE, "motors/velocity/" + name,
                motor.getVelocity().getValueAsDouble() * Consts.MathConsts.Tau * Consts.Wheels.radius);
    }
}
