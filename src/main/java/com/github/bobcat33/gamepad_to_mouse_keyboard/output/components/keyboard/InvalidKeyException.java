package com.github.bobcat33.gamepad_to_mouse_keyboard.output.components.keyboard;

public class InvalidKeyException extends RuntimeException {

    public InvalidKeyException(String invalidKey) {
        super("The key \"" + invalidKey + "\" was invalid.");
    }

}
