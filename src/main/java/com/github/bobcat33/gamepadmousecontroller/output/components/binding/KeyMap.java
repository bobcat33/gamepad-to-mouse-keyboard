package com.github.bobcat33.gamepadmousecontroller.output.components.binding;

public record KeyMap(Type type,
                     String keyMap) {

    // todo docs
    public enum Type {
        KEYBOARD,
        KEYBOARD_HOLDABLE,
        MOUSE_BUTTON,
        TERMINATOR,
        NONE
    }

}
