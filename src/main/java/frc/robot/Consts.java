package frc.robot;

import java.util.List;

import frc.robot.drivetrain.Vector2;

public class Consts {
    public static class Wheels {
        public static final Vector2[] wheelPositions = new Vector2[] {
            new Vector2(-1, 1),
            new Vector2(1, 1),
            new Vector2(-1, -1),
            new Vector2(1, -1),
        };

        public static final double maxVelocity = 10.;
    }
}
