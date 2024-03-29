package com.github.bobcat33.gamepad_to_mouse_keyboard.output.components.mouse;

import com.github.bobcat33.gamepad_to_mouse_keyboard.output.components.binding.KeyMap;

public class InvalidMouseButtonException extends RuntimeException {

    public InvalidMouseButtonException(KeyMap keyMap) {
        super("The binding \"" + keyMap.map() + "\" does not have a corresponding mouse button assigned.");
    }

}
