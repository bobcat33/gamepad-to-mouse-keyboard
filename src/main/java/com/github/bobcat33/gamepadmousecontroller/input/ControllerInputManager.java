package com.github.bobcat33.gamepadmousecontroller.input;

import com.github.bobcat33.gamepadmousecontroller.input.components.ButtonState;
import com.github.bobcat33.gamepadmousecontroller.input.components.Joystick;
import com.studiohartman.jamepad.ControllerManager;
import com.studiohartman.jamepad.ControllerState;

public class ControllerInputManager {

    private final ControllerManager controllers;
    private final ControllerInputListener listener;
    private final Joystick leftJoystick;
    private final Joystick rightJoystick;
    private final int controllerID;
    private boolean running = false;

    public ControllerInputManager(ControllerInputListener listener) {
        this(0, listener);
    }

    public ControllerInputManager(int controllerID, ControllerInputListener listener) {
        controllers = new ControllerManager();
        controllers.initSDLGamepad();
        this.listener = listener;
        this.controllerID = controllerID;
        this.leftJoystick = new Joystick(Joystick.Side.LEFT);
        this.rightJoystick = new Joystick(Joystick.Side.RIGHT);
    }

    /**
     * Start reading inputs from any controller connected
     * @throws AlreadyRunningException Thrown if the input manager is already running
     */
    public void start() throws AlreadyRunningException {
        if (running) throw new AlreadyRunningException();
        running = true;

        ControllerState oldState = controllers.getState(controllerID);
        ControllerState currentState;

        ButtonState previousButtons = new ButtonState(oldState);

        while (running) {
            currentState = controllers.getState(controllerID);

            if (!statesEqual(currentState, oldState)) {
                ButtonState currentButtons = new ButtonState(currentState);

                if (listener.stateChanged(currentButtons, previousButtons)) terminate();

                ButtonState.StateChange change = currentButtons.getStateChange(previousButtons);

                if (change == ButtonState.StateChange.PRESSED) {
                    if (listener.buttonPressed(currentButtons.getButtonChange(previousButtons))) terminate();
                }
                else if (change == ButtonState.StateChange.RELEASED){
                    if (listener.buttonReleased(currentButtons.getButtonChange(previousButtons))) terminate();
                }

                previousButtons = currentButtons;

            }
            if (running) {

                if (oldState.leftStickX != currentState.leftStickX) updateJoystick(leftJoystick, Joystick.Axis.X, currentState.leftStickX);
                if (oldState.leftStickY != currentState.leftStickY) updateJoystick(leftJoystick, Joystick.Axis.Y, currentState.leftStickY);
                if (oldState.rightStickX != currentState.rightStickX) updateJoystick(rightJoystick, Joystick.Axis.X, currentState.rightStickX);
                if (oldState.rightStickY != currentState.rightStickY) updateJoystick(rightJoystick, Joystick.Axis.Y, currentState.rightStickY);

            }
            oldState = currentState;
        }
    }

    /**
     * Update the value of an axis on a joystick and call listener methods if the
     * joystick is {@link Joystick.ActiveState#ACTIVATED activated} or {@link Joystick.ActiveState#DEACTIVATED deactivated}
     */
    private void updateJoystick(Joystick joystick, Joystick.Axis axis, float value) {
        Joystick.ActiveState joystickState = joystick.update(axis, value);
        if (joystickState.equals(Joystick.ActiveState.ACTIVATED)) listener.joyStickActivated(joystick);
        if (joystickState.equals(Joystick.ActiveState.DEACTIVATED)) listener.joyStickDeactivated(joystick.getSide());
    }

    // todo use to update joystick values like speeds from bindings
    public Joystick getJoystick(Joystick.Side side) {
        if (side.equals(Joystick.Side.LEFT)) return leftJoystick;
        return rightJoystick;
    }

    /**
     * End process for closing the input manager, safe to call if already terminated
     */
    public void terminate() {
        listener.listenerClose();
        running = false;
        controllers.quitSDLGamepad();
    }

    /**
     * Check if two controller states are equal, note that this only includes the buttons that are watched by the input
     * manager and does not compare all values in the controller states, it also ignores joystick states as those are
     * compared separately
     * @return true if the states are equal
     */
    public boolean statesEqual(ControllerState state1, ControllerState state2) {
        return state1.a == state2.a
                && state1.b == state2.b
                && state1.x == state2.x
                && state1.y == state2.y
                && state1.lb == state2.lb
                && state1.rb == state2.rb
                && state1.leftTrigger == state2.leftTrigger
                && state1.rightTrigger == state2.rightTrigger
                && state1.dpadUp == state2.dpadUp
                && state1.dpadDown == state2.dpadDown
                && state1.dpadLeft == state2.dpadLeft
                && state1.dpadRight == state2.dpadRight
                && state1.leftStickClick == state2.leftStickClick
                && state1.rightStickClick == state2.rightStickClick
                && state1.back == state2.back
                && state1.start == state2.start;
    }

}