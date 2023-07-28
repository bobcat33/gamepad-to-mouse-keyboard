package com.github.bobcat33.gamepad_to_mouse_keyboard.input.components;

public class Joystick {

    public enum Side {
        LEFT,
        RIGHT
    }

    public enum Axis {
        X,
        Y
    }

    public enum ActiveState {
        ACTIVE,
        ACTIVATED,
        DEACTIVATED,
        INACTIVE
    }

    private final float deadZone = 0.2f;
    private float xAxis = 0.0f;
    private float yAxis = 0.0f;

    private final Side side;
    private boolean active;

    public Joystick(Side side) {
        this.side = side;
    }

    /**
     * @return The {@link Side side} of the controller on which the joystick resides
     */
    public Side getSide() {
        return side;
    }

    /**
     * @return true if the joystick is currently active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Update the value of an axis on the joystick
     * @param axis The axis to be updated
     * @param value The new value of the axis (between -1 and 1)
     * @return The {@link ActiveState state} of the joystick after updating the axis
     */
    public ActiveState update(Axis axis, float value) {
        value = bringInAxisRange(value);

        if (value > deadZone || value < deadZone * -1) {
            if (axis.equals(Axis.X)) xAxis = value;
            if (axis.equals(Axis.Y)) yAxis = value;

            if (!active) {
                active = true;
                return ActiveState.ACTIVATED;
            }
        } else {
            if (axis.equals(Axis.X)) xAxis = 0.0f;
            if (axis.equals(Axis.Y)) yAxis = 0.0f;

            if (xAxis == 0.0f && yAxis == 0.0f) {
                if (active) {
                    active = false;
                    return ActiveState.DEACTIVATED;
                }
                return ActiveState.INACTIVE;
            }
        }

        return ActiveState.ACTIVE;

    }

    /**
     * @return The value of the current X axis (-1 to 1)
     */
    public float getXAxis() {
        return xAxis;
    }

    /**
     * @return The value of the current Y axis (-1 to 1)
     */
    public float getYAxis() {
        return yAxis;
    }

    /**
     * Bring a value into the upper and lower limits of a range
     */
    public static float bringInRange(float value, float lower, float upper) {
        if (value < lower) return lower;
        else if (value > upper) return upper;
        else return value;
    }

    /**
     * Bring a value into the limits of the range -1 to 1
     */
    public static float bringInAxisRange(float value) {
        return bringInRange(value, -1, 1);
    }
}
