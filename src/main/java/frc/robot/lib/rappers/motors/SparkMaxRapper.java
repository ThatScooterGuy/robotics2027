package frc.robot.lib.rappers.motors;

import java.util.ArrayList;

import com.revrobotics.sim.SparkMaxSim;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj.RobotBase;
import lombok.Getter;

public class SparkMaxRapper {
    public static enum DeviceType {
        Brushless,
        Brushed,
    }
    
    @Getter
    SparkMax sparkMax = null;

    private static double clamp(double val, double min, double max) {
        return Math.min(max, Math.max(val, min));
    }

    public SparkMaxRapper(int port, DeviceType deviceType) {
        sparkMax = new SparkMax(port, deviceType == SparkMaxRapper.DeviceType.Brushless ? MotorType.kBrushless : MotorType.kBrushed);
        if (RobotBase.isSimulation()) initSimulation(sparkMax);
    }

    public void set(double speed) {
        speed = clamp(speed, -1., 1.);
        sparkMax.set(speed);
    }

    public void setVoltage(double volts) {
        sparkMax.setVoltage(volts);
    }

    private static final double freeSpeed = DCMotor.getNEO(1).freeSpeedRadPerSec * 2;
    private static final ArrayList<SparkMaxSim> sims = new ArrayList<>();

    private void initSimulation(SparkMax sparkMax) {
        SparkMaxSim sim = new SparkMaxSim(sparkMax, DCMotor.getNEO(1));
        sim.enable();
        sims.add(sim);
    }

    public static void update() {
        for (int i = 0; i < sims.size(); i += 1) sims.get(i).iterate(sims.get(i).getAppliedOutput() * freeSpeed, sims.get(i).getBusVoltage(), 0.02);
    }
}