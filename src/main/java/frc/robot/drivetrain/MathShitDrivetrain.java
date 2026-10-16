package frc.robot.drivetrain;

import frc.robot.Consts;

public class MathShitDrivetrain {
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

    private final double[] wheelAngleBuffer = new double[] {
        0,
        0,
        0,
        0,
    };

    private double normalizeAngle(double omega) {
        return Math.atan2(Math.sin(omega), Math.cos(omega));
    }

    private void optomizeAngles() {
        for (int i = 0; i < Consts.Wheels.wheelCount; i += 1) {
            double delta = normalizeAngle(rotationResults[i] - wheelAngleBuffer[i]);

            if (Math.abs(delta) > Math.PI / 2.) {
                rotationResults[i] += Math.PI;
                velocityResults[i] *= -1.;
            }

            rotationResults[i] = normalizeAngle(rotationResults[i]);
        }
    }

    private void calculateAngles() {
        for (int i = 0; i < Consts.Wheels.wheelCount; i += 1) 
            rotationResults[i] = Math.atan2(vectorResults[i].y, vectorResults[i].x);
    }

    private void calculateVelocities() {
        for (int i = 0; i < Consts.Wheels.wheelCount; i += 1) 
            velocityResults[i] = vectorResults[i].length();
    }

    public void calculateInverseKinematic(Vector2 vector, double omega) {
        double xFixed = vector.x;
        double yFixed = vector.y;

        Vector2 maxVector = vectorResults[0];

        for (int i = 0; i < Consts.Wheels.wheelCount; i += 1) {
            vectorResults[i].x = xFixed + -Consts.Wheels.wheelPositions[i].y * omega;
            vectorResults[i].y = yFixed +  Consts.Wheels.wheelPositions[i].x * omega;
            if (vectorResults[i].length() > maxVector.length()) maxVector = vectorResults[i];
        }

        if (maxVector.length() > Consts.Wheels.maxVelocity) {
            double ratio = Consts.Wheels.maxVelocity / maxVector.length();
            for (int i = 0; i < Consts.Wheels.wheelCount; i += 1) vectorResults[i].mul(ratio);
        }

        calculateAngles();
        calculateVelocities();
        
        optomizeAngles();
    }

    public double[] getWheelAngleBuffer() {
        return wheelAngleBuffer;
    }

    public double[] getAngles() {
        return rotationResults;
    }

    public double[] getVelocities() {
        return velocityResults;
    }
}
