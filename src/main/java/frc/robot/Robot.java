package frc.robot;

import com.ctre.phoenix6.CANBus;

import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.drivetrain.Drivetrain;

public class Robot extends TimedRobot {
    private CANBus canBus = null;
    private Drivetrain drivetrain = null;

    public Robot() {
        canBus = new CANBus("foot");
        drivetrain = new Drivetrain(canBus);
    }

    @Override
    public void robotPeriodic() {
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
    }
}
