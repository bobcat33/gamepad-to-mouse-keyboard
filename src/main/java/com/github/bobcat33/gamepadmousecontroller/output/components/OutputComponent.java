package com.github.bobcat33.gamepadmousecontroller.output.components;

import java.awt.*;

public class OutputComponent {

    protected final Robot robot;

    public OutputComponent() {
        try {
            this.robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }

    public OutputComponent(Robot robot) {
        this.robot = robot;
    }

}
