package com.github.bobcat33.gamepad_to_mouse_keyboard.output;

import com.github.bobcat33.gamepad_to_mouse_keyboard.input.components.Button;
import com.github.bobcat33.gamepad_to_mouse_keyboard.input.components.ButtonState;
import com.github.bobcat33.gamepad_to_mouse_keyboard.input.ControllerInputListener;
import com.github.bobcat33.gamepad_to_mouse_keyboard.input.components.Joystick;
import com.github.bobcat33.gamepad_to_mouse_keyboard.output.components.binding.KeyMap;
import com.github.bobcat33.gamepad_to_mouse_keyboard.output.components.binding.Bindings;
import com.github.bobcat33.gamepad_to_mouse_keyboard.output.components.keyboard.Keyboard;
import com.github.bobcat33.gamepad_to_mouse_keyboard.output.components.mouse.AlreadyRunningException;
import com.github.bobcat33.gamepad_to_mouse_keyboard.output.components.mouse.MouseButtons;
import com.github.bobcat33.gamepad_to_mouse_keyboard.output.components.mouse.MouseMover;

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

    public void loadBindingsFromFile(String filename) throws FileNotFoundException {
        loadBindingsFromFile(new File(filename));
    }

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
        KeyMap keyMap = bindings.getKeyMap(button);

        boolean terminate = false;
        switch (keyMap.type()) {
            case KEYBOARD -> keyboard.pressBinding(keyMap);
            case KEYBOARD_HOLDABLE -> keyboard.holdBinding(keyMap);
            case MOUSE_BUTTON -> mouseButtons.press(keyMap);
            case TERMINATOR -> terminate = true;
        }
        // Terminate controller input manager if button pressed is the Right Stick
        return terminate;
    }

    @Override
    public boolean buttonReleased(Button button) {
        KeyMap keyMap = bindings.getKeyMap(button);

        switch (keyMap.type()) {
            case KEYBOARD_HOLDABLE -> keyboard.releaseBinding(keyMap);
            case MOUSE_BUTTON -> mouseButtons.release(keyMap);
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
