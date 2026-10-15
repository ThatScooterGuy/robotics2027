package frc.robot.drivetrain;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

public class Drivetrain {
    private MathShit math = null;

    private TalonFX topLeftDriveMotor = null;
    private TalonFX topRightDriveMotor = null;
    private TalonFX bottomLeftDriveMotor = null;
    private TalonFX bottomRightDriveMotor = null;

    private SparkMax topLeftRotationMotor = null;
    private SparkMax topRightRotationMotor = null;
    private SparkMax bottomLeftRotationMotor = null;
    private SparkMax bottomRightRotationMotor = null;
    
    private CANcoder topLeftCANCoder = null;
    private CANcoder topRightCANCoder = null;
    private CANcoder bottomLeftCANCoder = null;
    private CANcoder bottomRightCANCoder = null;

    private double xVelocity = 0;
    private double yVelocity = 0;
    private double omega = 0;

    private double oldXVelocity = Double.NaN;
    private double oldYVelocity = Double.NaN;
    private double oldOmega = Double.NaN;

    public Drivetrain(CANBus canBus) {
        math = new MathShit();

        topLeftDriveMotor = new TalonFX(0, canBus);
        topRightDriveMotor = new TalonFX(1, canBus);
        bottomLeftDriveMotor = new TalonFX(2, canBus);
        bottomRightDriveMotor = new TalonFX(3, canBus);

        topLeftRotationMotor = new SparkMax(4,MotorType.kBrushless);
        topRightRotationMotor = new SparkMax(5,MotorType.kBrushless);
        bottomLeftRotationMotor = new SparkMax(6,MotorType.kBrushless);
        bottomRightRotationMotor = new SparkMax(7,MotorType.kBrushless);

        topLeftCANCoder = new CANcoder(8, canBus);
        topRightCANCoder = new CANcoder(9, canBus);
        bottomLeftCANCoder = new CANcoder(10, canBus);
        bottomRightCANCoder = new CANcoder(11, canBus);
    }

    public void update() {
        if (xVelocity == oldXVelocity || yVelocity == oldYVelocity || omega == oldOmega) {
            return;
        } else {
            oldXVelocity = xVelocity;
            oldYVelocity = yVelocity;
        }

        Vector2[] wheelVectors = math.calculateInverseKinematic(xVelocity, yVelocity, omega);

        double[] wheelRotations = math.extractRotationFromVectors(wheelVectors);
        double[] wheelVelocitys = math.extractVelocityFromVectors(wheelVectors);
        
    }
}
