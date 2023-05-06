package com.github.bobcat33.gamepadmousecontroller.output.components.mouse;

import com.github.bobcat33.gamepadmousecontroller.output.components.OutputComponent;
import com.github.bobcat33.gamepadmousecontroller.output.components.binding.Binding;

import java.awt.*;
import java.awt.event.InputEvent;

public class MouseButtons extends OutputComponent {

    public MouseButtons() {
        super();
    }

    public MouseButtons(Robot robot) {
        super(robot);
    }

    /**
     * Translate a controller button into an integer mask of the associated mouse button, returns null if there is no
     * translation
     */
    public static Integer getBoundButton(String binding) {

        return switch (binding) {
            case "mb_left" -> InputEvent.BUTTON1_DOWN_MASK; // Left mouse button
            case "mb_right" -> InputEvent.BUTTON3_DOWN_MASK; // Right mouse button
            default -> null;
        };

    }

    /**
     * Press mouse button associated with the controller button
     * @throws InvalidMouseButtonException if the binding does not have an associated button mask
     */
    public void press(Binding binding) {
        Integer buttonMask = getBoundButton(binding.keyMap());
        if (buttonMask == null) throw new InvalidMouseButtonException(binding);
        robot.mousePress(buttonMask);
    }

    /**
     * Release mouse button associated with the controller button
     * @throws InvalidMouseButtonException if the binding does not have an associated button mask
     */
    public void release(Binding binding) {
        Integer buttonMask = getBoundButton(binding.keyMap());
        if (buttonMask == null) throw new InvalidMouseButtonException(binding);
        robot.mouseRelease(buttonMask);
    }
}