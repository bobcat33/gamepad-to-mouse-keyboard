package com.github.bobcat33.gamepadmousecontroller.output;

import com.github.bobcat33.gamepadmousecontroller.input.components.Button;
import com.github.bobcat33.gamepadmousecontroller.input.components.ButtonState;
import com.github.bobcat33.gamepadmousecontroller.input.ControllerInputListener;
import com.github.bobcat33.gamepadmousecontroller.input.components.Joystick;
import com.github.bobcat33.gamepadmousecontroller.output.components.binding.Binding;
import com.github.bobcat33.gamepadmousecontroller.output.components.binding.Bindings;
import com.github.bobcat33.gamepadmousecontroller.output.components.keyboard.Keyboard;
import com.github.bobcat33.gamepadmousecontroller.output.components.mouse.AlreadyRunningException;
import com.github.bobcat33.gamepadmousecontroller.output.components.mouse.MouseButtons;
import com.github.bobcat33.gamepadmousecontroller.output.components.mouse.MouseMover;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;

public class OutputManager implements ControllerInputListener {

    private final Robot robot;
    private Keyboard keyboard;
    private MouseButtons mouseButtons;
    private MouseMover mouseMover;
    private Joystick activeJoystick;
    private Bindings bindings;

    public OutputManager() {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
        createComponents();
    }

    public OutputManager(Robot robot) {
        this.robot = robot;
        createComponents();
    }

    /**
     * Generate the output components
     */
    private void createComponents() {
        bindings = new Bindings();
        keyboard = new Keyboard(robot);
        mouseButtons = new MouseButtons(robot);
        mouseMover = new MouseMover(this, robot, 10, 20);
        try {
            mouseMover.start();
        } catch (AlreadyRunningException e) {
            throw new RuntimeException(e);
        }
    }

    // todo docs
    public void loadBindingsFromFile(String filename) throws FileNotFoundException {
        loadBindingsFromFile(new File(filename));
    }

    /// todo docs
    public void loadBindingsFromFile(File file) throws FileNotFoundException {
        bindings.loadBindingsFromFile(file);
    }

    // todo use to update joystick values
    public Bindings getBindings() {
        return bindings;
    }

    /**
     * @return Joystick that data is currently being read from
     */
    public Joystick getActiveJoystick() {
        return activeJoystick;
    }

    @Override
    public boolean stateChanged(ButtonState currentState, ButtonState previousState) {
        return false;
    }

    @Override
    public boolean buttonPressed(Button button) {
        // todo comment removal
//        System.out.println("Pressed: " + button.toString());
        Binding binding = bindings.getBinding(button);
        /*// Determine action for button
        switch (button) {
            // Buttons that can be held
            case LB, RB, DOWN -> keyboard.holdBinding(button);
            // Mouse buttons
            case LT, RT -> mouseButtons.press(button);
            // Any button that cannot be held
            default -> keyboard.pressBinding(button);
        }*/

        boolean terminate = false;
        switch (binding.type()) {
            case KEYBOARD -> keyboard.pressBinding(binding);
            case KEYBOARD_HOLDABLE -> keyboard.holdBinding(binding);
            case MOUSE_BUTTON -> mouseButtons.press(binding);
            case TERMINATOR -> terminate = true;
        }
        // Terminate controller input manager if button pressed is the Right Stick
        return terminate;
    }

    @Override
    public boolean buttonReleased(Button button) {
        // todo comment removal
//        System.out.println("Released: " + button.toString());
        Binding binding = bindings.getBinding(button);
        /*// Determine action for button
        switch (button) {
            // Buttons that can be held
            case LB, RB, DOWN -> keyboard.releaseBinding(button);
            // Mouse buttons
            case LT, RT -> mouseButtons.release(button);
        }*/

        switch (binding.type()) {
            case KEYBOARD_HOLDABLE -> keyboard.releaseBinding(binding);
            case MOUSE_BUTTON -> mouseButtons.release(binding);
        }

        return false;
    }

    @Override
    public void joyStickActivated(Joystick joystick) {
        activeJoystick = joystick;
    }

    @Override
    public void joyStickDeactivated(Joystick.Side joystickSide) {
        // If the joystick is the joystick currently being read from then remove it
        if (activeJoystick != null && activeJoystick.getSide().equals(joystickSide)) activeJoystick = null;
    }

    @Override
    public void listenerClose() {
        keyboard.releaseAllBindings();
        mouseMover.stop();
    }
}
