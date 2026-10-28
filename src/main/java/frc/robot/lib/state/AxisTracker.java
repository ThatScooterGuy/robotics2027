package frc.robot.lib.state;

import java.util.function.DoubleSupplier;

public class AxisTracker {
    private DoubleSupplier xSupplier = null;
    private DoubleSupplier ySupplier = null;
    private double changeThreshold = 0;
    private double snapThreshold = 0;
    private double deadband = 0;
    private double refx = 0;
    private double refy = 0;
    private State state = State.Neither;
    


    private static enum State {
        DeadBand,
        Max,
        Neither
    }


    private static double distance(double x, double y, double dx, double dy) {
        return Math.hypot(x - dx, y - dy);
    }

    public AxisTracker(double changeThreshold, double snapThreshold, double deadband, DoubleSupplier xSupplier, DoubleSupplier ySupplier) {
        this.xSupplier = xSupplier;
        this.ySupplier = ySupplier;
        this.changeThreshold = changeThreshold;
        this.snapThreshold = snapThreshold;
        this.deadband = deadband;
    }

    public AxisChangeState getAsState() {
        double newRefx = xSupplier.getAsDouble();
        double newRefy = ySupplier.getAsDouble();

        double changeDistance = distance(refx, refy, newRefx, newRefy);
        double distanceFromCenter = distance(0, 0, newRefx, newRefy);

        boolean isDeadband = distanceFromCenter < deadband;
        boolean isMax = distanceFromCenter > 1 - snapThreshold;

        if (isMax && state != State.Max) {
            refx = newRefx;
            refy = newRefy;
            state = State.Max;
            return AxisChangeState.Max;
        }
        if (isDeadband && state != State.DeadBand) {
            refx = 0;
            refy = 0;
            state = State.DeadBand;
            return AxisChangeState.DeadBand;
        }
        if (changeThreshold < changeDistance && !isDeadband && !isMax) {
            refx = newRefx;
            refy = newRefy;
            state = State.Neither;
            return AxisChangeState.Changed;
        }
        return AxisChangeState.NotChanged;
    }
}