package com.github.bobcat33.gamepadmousecontroller.output.components.binding;

/**
 * Store the type of binding and the actual map of keys/buttons in the binding
 * @param type the type of binding, the way the binding should be processed
 * @param map the keys/buttons in the binding. For multiple keys/buttons separate each with a single "+"
 * @see <a href="https://github.com/bobcat33/GamepadMouseController/blob/master/src/main/java/com/github/bobcat33/gamepadmousecontroller/output/components/binding/Mapping Keywords.txt">List of mapping keywords</a>
 */
public record KeyMap(Type type,
                     String map) {

    /**
     * <ul>
     * <li>KEYBOARD -> A keyboard binding that is immediately pressed and released when activated, cannot be held down.</li>
     * <li>KEYBOARD_HOLDABLE -> A keyboard binding that is held down when activated and released when deactivated.</li>
     * <li>MOUSE_BUTTON -> A button on the mouse. Held down when activated and released when deactivated.</li>
     * <li>TERMINATOR -> A controller button that, when pressed, terminates the program.</li>
     * <li>NONE -> Used in place of null, indicates that the binding has not been assigned and cannot be used.</li>
     * </ul>
     */
    public enum Type {
        KEYBOARD,
        KEYBOARD_HOLDABLE,
        MOUSE_BUTTON,
        TERMINATOR,
        NONE
    }

}
