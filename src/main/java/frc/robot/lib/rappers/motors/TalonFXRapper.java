package frc.robot.lib.rappers.motors;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.controls.ControlRequest;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.sim.TalonFXSimState;

import edu.wpi.first.units.measure.AngularVelocity;
import lombok.Getter;

public class TalonFXRapper {
    @Getter
    private final TalonFX talonFX;
    private final TalonFXSimState sim;

    public TalonFXRapper(int port, CANBus canBus) {
        talonFX = new TalonFX(port, canBus);
        sim = new TalonFXSimState(talonFX);
    }

    public void setControl(ControlRequest request) {
        talonFX.setControl(request);
    }

    public void advanceSimulation(AngularVelocity angularVelocity) {
        sim.setRotorVelocity(angularVelocity);
    }
}
