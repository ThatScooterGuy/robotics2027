package frc.robot.drivetrain;

import frc.robot.Consts;

public class MathShit {
    private final Vector2[] vectorResults = new Vector2[] {
        new Vector2(),
        new Vector2(),
        new Vector2(),
        new Vector2(),
    };

    private final double[] velocityResults = new double[] {
        0,
        0,
        0,
        0,
    };

    private final double[] rotationResults = new double[] {
        0,
        0,
        0,
        0,
    };

    public Vector2[] calculateInverseKinematic(double x, double y, double omega) {
        double x_fixed = x;
        double y_fixed = y;

        if (Math.sqrt(x*x + y*y) > Consts.Wheels.maxVelocity) {
            double ratio = Consts.Wheels.maxVelocity / Math.sqrt(x*x + y*y);
            x_fixed = x * ratio;
            y_fixed = y * ratio;
        }

        for (int i = 0; i < 4; i += 1) {
            vectorResults[i].x = x_fixed + -Consts.Wheels.wheelPositions[i].y * omega;
            vectorResults[i].y = y_fixed +  Consts.Wheels.wheelPositions[i].x * omega;
        }

        return vectorResults;
    }

    public double[] extractVelocityFromVectors(Vector2[] vectors) {
        for (int i = 0; i < 4; i += 1) velocityResults[i] = Math.atan2(vectors[i].y, vectors[i].x);
        return velocityResults;
    }

    public double[] extractRotationFromVectors(Vector2[] vectors) {
        for (int i = 0; i < 4; i += 1) 
            rotationResults[i] = Math.sqrt(vectors[i].y * vectors[i].y + vectors[i].x * vectors[i].x);
        return rotationResults;
    }
}
