module com.github.bobcat33.gamepad_to_mouse_keyboard {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.datatransfer;
    requires java.desktop;
    requires jamepad;


    opens com.github.bobcat33.gamepad_to_mouse_keyboard to javafx.fxml;
    exports com.github.bobcat33.gamepad_to_mouse_keyboard;
    opens com.github.bobcat33.gamepad_to_mouse_keyboard.originalfx to javafx.fxml;
    exports com.github.bobcat33.gamepad_to_mouse_keyboard.originalfx;
    exports com.github.bobcat33.gamepad_to_mouse_keyboard.output;
    opens com.github.bobcat33.gamepad_to_mouse_keyboard.output to javafx.fxml;
    exports com.github.bobcat33.gamepad_to_mouse_keyboard.input;
    opens com.github.bobcat33.gamepad_to_mouse_keyboard.input to javafx.fxml;
    exports com.github.bobcat33.gamepad_to_mouse_keyboard.input.components;
    opens com.github.bobcat33.gamepad_to_mouse_keyboard.input.components to javafx.fxml;
    exports com.github.bobcat33.gamepad_to_mouse_keyboard.output.components;
    opens com.github.bobcat33.gamepad_to_mouse_keyboard.output.components to javafx.fxml;
    exports com.github.bobcat33.gamepad_to_mouse_keyboard.output.components.mouse;
    opens com.github.bobcat33.gamepad_to_mouse_keyboard.output.components.mouse to javafx.fxml;
    exports com.github.bobcat33.gamepad_to_mouse_keyboard.output.components.keyboard;
    opens com.github.bobcat33.gamepad_to_mouse_keyboard.output.components.keyboard to javafx.fxml;
    exports com.github.bobcat33.gamepad_to_mouse_keyboard.output.components.binding;
    opens com.github.bobcat33.gamepad_to_mouse_keyboard.output.components.binding to javafx.fxml;
}