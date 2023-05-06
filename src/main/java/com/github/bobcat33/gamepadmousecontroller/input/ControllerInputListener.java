package com.github.bobcat33.gamepadmousecontroller.input;

import com.github.bobcat33.gamepadmousecontroller.input.components.Button;
import com.github.bobcat33.gamepadmousecontroller.input.components.ButtonState;
import com.github.bobcat33.gamepadmousecontroller.input.components.Joystick;

public interface ControllerInputListener {
    /**
     * Called when any change occurs in the watched buttons of the controller
     * @param currentState State of the buttons after change
     * @param previousState State of the buttons before change
     * @return Whether to stop the input manager, if true the input manager is stopped and {@link ControllerInputListener#listenerClose()} is called
     */
    boolean stateChanged(ButtonState currentState, ButtonState previousState);

    /**
     * Called when a watched button is pressed on the controller
     * @param button The button that was pressed
     * @return Whether to stop the input manager, if true the input manager is stopped and {@link ControllerInputListener#listenerClose()} is called
     */
    boolean buttonPressed(Button button);

    /**
     * Called when a watched button is released on the controller
     * @param button The button that was released
     * @return Whether to stop the input manager, if true the input manager is stopped and {@link ControllerInputListener#listenerClose()} is called
     */
    boolean buttonReleased(Button button);

    /**
     * Called when a joystick is first moved and becomes {@link Joystick.ActiveState#ACTIVE active}
     * @param joyStick Joystick that was activated
     */
    void joyStickActivated(Joystick joyStick);

    /**
     * Called when a joystick is returned to its resting position and becomes {@link Joystick.ActiveState#INACTIVE inactive}
     * @param joyStickSide The side of the controller that the deactivated joystick resides
     */
    void joyStickDeactivated(Joystick.Side joyStickSide);

    /**
     * Called when the input manager is stopped
     */
    void listenerClose();
}
