package com.github.bobcat33.gamepad_to_mouse_keyboard;

import com.github.bobcat33.gamepad_to_mouse_keyboard.input.ControllerInputManager;
import com.github.bobcat33.gamepad_to_mouse_keyboard.input.AlreadyRunningException;
import com.github.bobcat33.gamepad_to_mouse_keyboard.output.OutputManager;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws AlreadyRunningException {
        OutputManager outputManager = new OutputManager();
        ControllerInputManager controllerManager = new ControllerInputManager(outputManager);
        try {
            outputManager.loadBindingsFromFile("./src/main/resources/bindings.txt");
            controllerManager.start();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            outputManager.listenerClose();
        }

        controllerManager.terminate();
    }
}
