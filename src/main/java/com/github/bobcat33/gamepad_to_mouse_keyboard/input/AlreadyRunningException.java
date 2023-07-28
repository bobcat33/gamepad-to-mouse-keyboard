package com.github.bobcat33.gamepad_to_mouse_keyboard.input;

public class AlreadyRunningException extends Exception {

    /**
     * Thrown when {@link ControllerInputManager#start()} is called while the instance is already running
     */
    public AlreadyRunningException() {
        super("Unable to start ControllerInputManager because it is already running.");
    }

}
