package frc.robot.subsystems;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.CANcoder;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkBase.ControlType;

import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Consts;
import frc.robot.Setup;
import frc.robot.lib.mathshit.MathShitDrivetrain;
import frc.robot.lib.mathshit.Vector2;
import frc.robot.lib.rappers.motors.SparkMaxRapper;
import frc.robot.lib.rappers.motors.TalonFXRapper;
import frc.robot.lib.rappers.motors.SparkMaxRapper.DeviceType;

public class Drivetrain extends SubsystemBase {
    private MathShitDrivetrain math = null;

    private TalonFXRapper[] driveMotors = null;
    private SparkMaxRapper[] rotationMotors = null;
    private CANcoder[] canCoders = null;

    private VelocityVoltage[] driveVelocities = null;
    private SparkClosedLoopController[] rotationControllers = null;

    private StatusSignal<Angle>[] rotationSignals = null;

    private Vector2 velocity = new Vector2(1, 0);
    private double omega = 0;

    @SuppressWarnings("unchecked")
    public Drivetrain(CANBus canBus) {
        math = new MathShitDrivetrain();

        driveMotors = new TalonFXRapper[4];
        rotationMotors = new SparkMaxRapper[4];
        canCoders = new CANcoder[4];
        driveVelocities = new VelocityVoltage[4];
        rotationControllers = new SparkClosedLoopController[4];
        rotationSignals = (StatusSignal<Angle>[]) new StatusSignal[4];

        for (int i = 0; i < 4; i += 1) {
            driveMotors[i] = new TalonFXRapper(i, canBus);
            rotationMotors[i] = new SparkMaxRapper(i + 4, DeviceType.Brushless);
            canCoders[i] = new CANcoder(i + 8, canBus);
            driveVelocities[i] = new VelocityVoltage(0.).withSlot(0);
            rotationControllers[i] = rotationMotors[i].getSparkMax().getClosedLoopController();
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

}
