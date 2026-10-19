package frc.robot.drivetrain;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.CANcoder;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.units.measure.Angle;
import frc.robot.Consts;
import frc.robot.Setup;
import frc.robot.log.Loggable;
import frc.robot.strict.wall.motor.TalonFXMotor;

public class Drivetrain implements Loggable {
    private MathShitDrivetrain math = null;

    private TalonFXMotor[] driveMotors = null;
    private SparkMax[] rotationMotors = null;
    private CANcoder[] canCoders = null;

    private VelocityVoltage[] driveVelocities = null;
    private SparkClosedLoopController[] rotationControllers = null;

    private StatusSignal<Angle>[] rotationSignals = null;

    private Vector2 velocity = new Vector2(1, 0);
    private double omega = 0;

    private Vector2 oldVelocity = new Vector2(Double.NaN, Double.NaN);
    private double oldOmega = Double.NaN;

    @SuppressWarnings("unchecked")
    public Drivetrain(CANBus canBus) {
        math = new MathShitDrivetrain();

        driveMotors = new TalonFXMotor[4];
        rotationMotors = new SparkMax[4];
        canCoders = new CANcoder[4];
        driveVelocities = new VelocityVoltage[4];
        rotationControllers = new SparkClosedLoopController[4];
        rotationSignals = (StatusSignal<Angle>[]) new StatusSignal[4];

        for (int i = 0; i < 4; i += 1) {
            driveMotors[i] = new TalonFXMotor(i, canBus, String.valueOf(i));
            rotationMotors[i] = new SparkMax(i + 4, MotorType.kBrushless);
            canCoders[i] = new CANcoder(i + 8, canBus);
            driveVelocities[i] = new VelocityVoltage(0.).withSlot(0);
            rotationControllers[i] = rotationMotors[i].getClosedLoopController();
            rotationSignals[i] = canCoders[i].getAbsolutePosition();
        }

        Setup.setUpDriveMotors(driveMotors);
    }

    private void getWheelAngles(double[] buffer) {
        for (int i = 0; i < Consts.Wheels.wheelCount; i += 1)
            buffer[i] = rotationSignals[i].refresh().getValueAsDouble() * Consts.MathConsts.Tau;
    }

    private void setWheelAndAngle(double[] wheelVelocities, double[] wheelAngles) {
        for (int i = 0; i < Consts.Wheels.wheelCount; i += 1) {
            driveMotors[i].setControl(driveVelocities[i].withVelocity(wheelVelocities[i]));
            rotationControllers[i].setSetpoint(wheelAngles[i] / (Math.PI * 2), ControlType.kPosition);
        }
    }

    public void setNewControls(double x, double y, double omega) {
        velocity.x = x;
        velocity.y = y;
        this.omega = omega;
    }

    public void update() {
        if (Math.abs(velocity.x - oldVelocity.x) < 0.07 
         && Math.abs(velocity.y - oldVelocity.y) < 0.07  
         && Math.abs(omega - oldOmega) < 0.07) {
            return;
        } else {
            oldVelocity.copy(velocity);
            oldOmega = omega;
        }
        
        double[] wheelAngleBuffer = math.getWheelAngleBuffer();
        getWheelAngles(wheelAngleBuffer);

        math.calculateInverseKinematic(velocity, omega);

        double[] wheelAngles  = math.getAngles();
        double[] wheelVelocities = math.getVelocities();

        setWheelAndAngle(wheelVelocities, wheelAngles);
    }

    public void simUpdate() {
        for (int i = 0; i < Consts.Wheels.wheelCount; i += 1)
            driveMotors[i].advanceSimulation(driveVelocities[i].getVelocityMeasure());
    }

    public void log() {
        for (int i = 0; i < Consts.Wheels.wheelCount; i += 1)
            driveMotors[i].log();
    }
}
