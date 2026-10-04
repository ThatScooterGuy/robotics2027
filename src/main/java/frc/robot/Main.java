package frc.robot;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.RobotBase;

public final class Main {
  private Main() {
  }

  public static void main(String... args) {
    RobotBase.startRobot(Robot::new);
  }
}

class Robot extends RobotBase {
  @Override
  public void startCompetition() {
    Init.initialize();
    while (true) {
      try {
        Thread.sleep(100);
      } catch (Exception e) {
        throw new RuntimeException("smth", e);
      }
    }
  }

  @Override
  public void endCompetition() {
    HAL.shutdown();
  }
}
