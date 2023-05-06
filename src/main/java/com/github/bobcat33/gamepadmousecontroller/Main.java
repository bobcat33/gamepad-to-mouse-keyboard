package com.github.bobcat33.gamepadmousecontroller;

import com.github.bobcat33.gamepadmousecontroller.input.ControllerInputManager;
import com.github.bobcat33.gamepadmousecontroller.input.AlreadyRunningException;
import com.github.bobcat33.gamepadmousecontroller.output.OutputManager;

import java.io.File;
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
