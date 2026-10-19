package frc.robot;

import com.ctre.phoenix6.CANBus;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.drivetrain.Drivetrain;

public class Robot extends TimedRobot {
    private CANBus canBus = null;
    private Drivetrain drivetrain = null;

    private XboxController controller = null;

    public Robot() {
        canBus = new CANBus("foot");
        drivetrain = new Drivetrain(canBus);
        controller = new XboxController(0);
    }

    @Override
    public void robotPeriodic() {
        double x = controller.getLeftX() * Consts.Wheels.maxVelocity;
        double y = controller.getLeftY() * Consts.Wheels.maxVelocity;
        double omega = controller.getRightX() * Consts.MathConsts.Tau;
        drivetrain.setNewControls(x, y, omega);
        drivetrain.update();

        drivetrain.log();
    }

    @Override
    public void autonomousInit() {
    }

    @Override
    public void autonomousPeriodic() {
    }

    @Override
    public void teleopInit() {
    }

    @Override
    public void teleopPeriodic() {
    }

    @Override
    public void disabledInit() {
    }

    @Override
    public void disabledPeriodic() {
    }

    @Override
    public void testInit() {
    }

    @Override
    public void testPeriodic() {
    }

    @Override
    public void simulationInit() {
    }

    @Override
    public void simulationPeriodic() {
        drivetrain.simUpdate();
    }
}
