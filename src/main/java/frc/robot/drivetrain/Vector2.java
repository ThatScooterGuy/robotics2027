package frc.robot.drivetrain;

public class Vector2 {
    public double x;
    public double y;

    public Vector2() {
        x = 0.;
        y = 0.;
    }

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void copy(Vector2 other) {
        x = other.x;
        y = other.y;
    }

    public double dot(Vector2 other) {
        return x * other.x + y * other.y;
    }

    public double length() {
        return Math.sqrt(this.dot(this));
    }

    public void mul(double scalar) {
        x *= scalar;
        y *= scalar;
    }
}
