package frc.robot;

import java.util.List;
import java.util.function.Function;

import frc.robot.api.input.Controller;
import frc.robot.shim.input.XboxController;

public class Consts {
    public static class ControllerInfo {
        //ControllerManager automatically  creates all of these controllers
        public static final List<Function<Integer, Controller>> controllersToInstate = List.of(
            XboxController::new,
            XboxController::new
        );
        public static final int primaryControllerPort = 0;
        public static final int secondaryControllerPort = 1;
    }
    public static class Repeat {
        //how often loop.Repeat runs code
        public static final double loopPeriod = 0.020;
    }
}
