module com.github.bobcat33.gamepadmousecontroller {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.datatransfer;
    requires java.desktop;
    requires jamepad;


    opens com.github.bobcat33.gamepadmousecontroller to javafx.fxml;
    exports com.github.bobcat33.gamepadmousecontroller;
    opens com.github.bobcat33.gamepadmousecontroller.originalfx to javafx.fxml;
    exports com.github.bobcat33.gamepadmousecontroller.originalfx;
    exports com.github.bobcat33.gamepadmousecontroller.output;
    opens com.github.bobcat33.gamepadmousecontroller.output to javafx.fxml;
    exports com.github.bobcat33.gamepadmousecontroller.input;
    opens com.github.bobcat33.gamepadmousecontroller.input to javafx.fxml;
    exports com.github.bobcat33.gamepadmousecontroller.input.components;
    opens com.github.bobcat33.gamepadmousecontroller.input.components to javafx.fxml;
    exports com.github.bobcat33.gamepadmousecontroller.output.components;
    opens com.github.bobcat33.gamepadmousecontroller.output.components to javafx.fxml;
    exports com.github.bobcat33.gamepadmousecontroller.output.components.mouse;
    opens com.github.bobcat33.gamepadmousecontroller.output.components.mouse to javafx.fxml;
    exports com.github.bobcat33.gamepadmousecontroller.output.components.keyboard;
    opens com.github.bobcat33.gamepadmousecontroller.output.components.keyboard to javafx.fxml;
    exports com.github.bobcat33.gamepadmousecontroller.output.components.binding;
    opens com.github.bobcat33.gamepadmousecontroller.output.components.binding to javafx.fxml;
}