package com.github.bobcat33.gamepadmousecontroller.output.components.mouse;

public class AlreadyRunningException extends Exception {

    public AlreadyRunningException() {
        super("Unable to start MouseMover because it is already running");
    }

}
