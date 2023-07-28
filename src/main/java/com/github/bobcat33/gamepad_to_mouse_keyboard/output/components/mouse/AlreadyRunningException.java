package com.github.bobcat33.gamepad_to_mouse_keyboard.output.components.mouse;

public class AlreadyRunningException extends Exception {

    public AlreadyRunningException() {
        super("Unable to start MouseMover because it is already running");
    }

}
