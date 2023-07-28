package com.github.bobcat33.gamepad_to_mouse_keyboard.input.components;

import com.studiohartman.jamepad.ControllerState;

import java.util.ArrayList;

public class ButtonState {

    public enum StateChange {
        PRESSED,
        RELEASED,
        NONE
    }

    private final ArrayList<Button> activeButtons = new ArrayList<>();

    public ButtonState(ControllerState state) {
        if (state.a) activeButtons.add(Button.A);
        if (state.b) activeButtons.add(Button.B);
        if (state.x) activeButtons.add(Button.X);
        if (state.y) activeButtons.add(Button.Y);
        if (state.lb) activeButtons.add(Button.LB);
        if (state.rb) activeButtons.add(Button.RB);
        if (state.leftTrigger != 0.0f) activeButtons.add(Button.LT);
        if (state.rightTrigger != 0.0f) activeButtons.add(Button.RT);
        if (state.dpadUp) activeButtons.add(Button.UP);
        if (state.dpadDown) activeButtons.add(Button.DOWN);
        if (state.dpadLeft) activeButtons.add(Button.LEFT);
        if (state.dpadRight) activeButtons.add(Button.RIGHT);
        if (state.leftStickClick) activeButtons.add(Button.LS);
        if (state.rightStickClick) activeButtons.add(Button.RS);
        if (state.back) activeButtons.add(Button.BACK);
        if (state.start) activeButtons.add(Button.START);
    }

    /**
     * @return A copy of the ArrayList of active buttons in the ButtonState
     */
    public ArrayList<Button> getActiveButtons() {
        return new ArrayList<>(activeButtons);
    }

    /**
     * Determine whether the difference between two states is a press or a release of a button. Logic errors may occur
     * if there is more than one difference between the two states, so it should be ensured that only one button has
     * been changed before calling this method. {@link StateChange#NONE} should rarely be interpreted as an error and in
     * most cases should be ignored completely as it is likely the result of buttons being pressed/released simultaneously
     */
    public StateChange getStateChange(ButtonState previousState) {
        int oldActiveSize = previousState.getActiveButtons().size();
        int curActiveSize = this.activeButtons.size();

        if (oldActiveSize < curActiveSize) return StateChange.PRESSED;
        else if (oldActiveSize > curActiveSize) return StateChange.RELEASED;
        return StateChange.NONE;

    }

    /**
     * Determine the button that has changed across two states. Logic errors may occur if there is more than one
     * difference between the two states, so it should be ensured that only one button has been changed before calling
     * this method. {@link Button#NONE} should rarely be interpreted as an error and in most cases should be ignored
     * completely as it is likely the result of buttons being pressed/released simultaneously
     * @return The button that has been pressed or released
     */
    public Button getButtonChange(ButtonState previousState) {
        ArrayList<Button> previousButtons = previousState.getActiveButtons();
        for (Button button : activeButtons) {
            if (!previousButtons.contains(button)) return button;
        }
        for (Button button : previousButtons) {
            if (!activeButtons.contains(button)) return button;
        }
        return Button.NONE;
    }

    /**
     * This is an untested method and SHOULD NOT BE USED (as such is marked as deprecated) but is an idea of how to make
     * the above methods more safe for ensuring that only one button has been adjusted, or for allowing multiple buttons
     * changes to be addressed at once. It is unused as it can be assumed that no two buttons will be pressed / released
     * at the exact same time, and due to the creation of two new arrays is inefficient enough to be pointless.
     * Additionally, if by chance this were to occur, then only minor "glitches" would be experienced by the user.
     */
    @Deprecated
    public ArrayList<Button> getButtonDifference(ButtonState previousState) {
        ArrayList<Button> previousButtons = previousState.getActiveButtons();
        ArrayList<Button> differentButtons = new ArrayList<>();
        for (Button button : activeButtons) {
            if (!previousButtons.contains(button)) differentButtons.add(button);
        }
        if (differentButtons.size() == 0) {
            for (Button button : previousButtons) {
                if (!activeButtons.contains(button)) differentButtons.add(button);
            }
        }
        return differentButtons;
    }

}
