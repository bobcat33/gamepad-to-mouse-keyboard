package com.github.bobcat33.gamepadmousecontroller.output.components.mouse;

import com.github.bobcat33.gamepadmousecontroller.output.components.binding.Binding;

public class InvalidMouseButtonException extends RuntimeException {

    public InvalidMouseButtonException(Binding binding) {
        super("The binding \"" + binding.keyMap() + "\" does not have a corresponding mouse button assigned.");
    }

}
