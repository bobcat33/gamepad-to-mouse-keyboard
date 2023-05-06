package com.github.bobcat33.gamepadmousecontroller.output.components.keyboard;

public class InvalidKeyException extends RuntimeException {

    public InvalidKeyException(String invalidKey) {
        super("The key \"" + invalidKey + "\" was invalid.");
    }

}
