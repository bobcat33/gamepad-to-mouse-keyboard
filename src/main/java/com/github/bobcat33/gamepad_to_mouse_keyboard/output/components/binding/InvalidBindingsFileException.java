package com.github.bobcat33.gamepad_to_mouse_keyboard.output.components.binding;

class InvalidBindingsFileException extends Exception {

    // todo docs
    InvalidBindingsFileException(String filename, int lineNumber) {
        this(filename, "Invalid line format", lineNumber);
    }

    // todo docs
    InvalidBindingsFileException(String filename, String problem) {
        super("Unable to load the bindings file '" + filename + "' - " + problem + "\nUsing previous bindings instead, if no bindings have been set then resorting to default bindings.");
    }

    // todo docs
    InvalidBindingsFileException(String filename, String problem, int lineNumber) {
        super("Unable to load the bindings file '" + filename + "' - " + problem + " on line " + lineNumber + "\nUsing previous bindings instead, if no bindings have been set then resorting to default bindings.");
    }

}
