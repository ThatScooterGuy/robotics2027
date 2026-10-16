package frc.robot.strict.wall.motor;

import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.RotationsPerSecond;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.controls.ControlRequest;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.sim.TalonFXSimState;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;
import frc.robot.Consts;

public class TalonFXMotor {

    private DoublePublisher velocityPublisher = NetworkTableInstance.getDefault().getDoubleTopic("swerve/motor/velocity").publish();

    private TalonFX motor = null;

    private SimpleMotorFeedforward motorVoltageSim = null;
    private PIDController pidControllerSim = null;
    private DCMotorSim simMotor = null;

    public TalonFXMotor(int port, CANBus canBus) {
        motor = new TalonFX(port, canBus);

        if (RobotBase.isSimulation()) {
            
            simMotor = new DCMotorSim(
                LinearSystemId.createDCMotorSystem(
                    DCMotor.getKrakenX60(1),
                    0.01,
                    1
            ), DCMotor.getKrakenX60(1));

            pidControllerSim  = new PIDController(Consts.Settings.Motor.PID.kP, Consts.Settings.Motor.PID.kI, Consts.Settings.Motor.PID.kD);
            motorVoltageSim = new SimpleMotorFeedforward(Consts.Settings.Motor.PID.kS, Consts.Settings.Motor.PID.kV, Consts.Settings.Motor.PID.kA);
        }
    }

    public void advanceSimulation(AngularVelocity angularVelocity) {

        double voltage = motorVoltageSim.calculate(angularVelocity.in(RotationsPerSecond)) + 
            pidControllerSim.calculate(motor.getVelocity().refresh().getValueAsDouble(), angularVelocity.in(RotationsPerSecond));

        simMotor.setInputVoltage(voltage);

        simMotor.update(0.020);


        TalonFXSimState simHandle = motor.getSimState();
        simHandle.setRotorVelocity(simMotor.getAngularVelocity());

        velocityPublisher.set(simMotor.getAngularVelocity().in(RadiansPerSecond) * Consts.Wheels.radius);
    }

    public void setControl(ControlRequest request) {
        motor.setControl(request);
    }

    public TalonFX inner() {
        return motor;
    }
}
