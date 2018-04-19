package util;

import java.awt.*;

public class RobotUtil {
    private static Robot robot;

    static {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException();
        }
    }

    static Robot getRobot() {
        return robot;
    }
}
