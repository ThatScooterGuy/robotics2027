package frc.robot.system.input;

import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.function.Function;

import frc.robot.Consts;
import frc.robot.api.input.Controller;

public class ControllerManager {
    private static AtomicReferenceArray<Controller> controllers = new AtomicReferenceArray<>(6);
    private static boolean initialized = false;
    private static Object createLock = new Object();

    private static void init() {
        for (int i = 0; i < Consts.ControllerInfo.controllersToInstate.size(); i += 1) {
            allocatePort(i, Consts.ControllerInfo.controllersToInstate.get(i));
        }
        initialized = true;
    }
    public static boolean initialize() {
        if (!initialized) init();
        return true;
    }
    public static void allocatePort(int port, Function<Integer, Controller> constructor) {
        if (port > 5) throw new IndexOutOfBoundsException("port must be a number between 0-5");
        synchronized (createLock) {
            if (controllers.get(port) != null) throw new IllegalStateException("port already initialized");
            controllers.set(port, constructor.apply(port));
        }
    }
    public static Controller getControllerFromPort(int port) {
        if (!initialized) init();
        if (port > 5) throw new IndexOutOfBoundsException("port must be a number between 0-5");
        Controller controller = controllers.get(port);
        if (controller == null) throw new IllegalStateException("Controller must be initialized before access");
        return controller;
    }
}
