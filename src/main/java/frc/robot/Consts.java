package frc.robot;

import static edu.wpi.first.units.Units.Inches;
import static edu.wpi.first.units.Units.Meters;

import frc.robot.drivetrain.Vector2;

public class Consts {
    public static class Wheels {
        public static final Vector2[] wheelPositions = new Vector2[] {
            new Vector2(-1, 1),
            new Vector2(1, 1),
            new Vector2(-1, -1),
            new Vector2(1, -1),
        };
        
        public static final int wheelCount = 4;

        public static final double maxVelocity = 10.;

        public static final double radius = Inches.of(2).in(Meters);
    }

    public static class Settings {
        public static class Motor {
            //every 10 sensor rotations is one motor rotation
            public static final double sensorToMotorRatio = 10.;

            public static final double currentLimit = 60.;
            
            //ms
            public static final double currentLimitMaxOver = 50.;

            public static final double timeToFullSpeed = 0.1;

            public static final double deadband = 0.03;

            public static class PID {
                //kS is the volage needed to start moving
                //measured as voltge
                public static final double kS = .1;

                //kV is the voltage needed to keep a motor moving, 
                //measured as Voltage / Round Per Second
                public static final double kV = 0.313;

                //kA is the voltage needed to overcome current initera of the robot
                //measured as Voltage / Rounds Per Second^2
                public static final double kA = 0.1;

                //kP is the voltage needed to close the error
                //measured as Voltage
                public static final double kP = 0.05;

                //kI is the voltage that is added as the error increases
                //measured as Voltage
                public static final double kI = 0.;

                //kD is the voltage added to counteract overcorrections
                //measured as Voltage
                public static final double kD = 0.;
            }
        }
    }

    public static class MathConsts {
        public static final double Tau = Math.PI * 2;
    }
}
