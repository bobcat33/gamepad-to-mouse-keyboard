package com.github.bobcat33.gamepadmousecontroller.output.components.keyboard;

import com.github.bobcat33.gamepadmousecontroller.output.components.OutputComponent;
import com.github.bobcat33.gamepadmousecontroller.output.components.binding.KeyMap;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Keyboard extends OutputComponent {

    private final ArrayList<String> heldKeyMaps = new ArrayList<>();

    public Keyboard() {
        super();
    }

    public Keyboard(Robot robot) {
        super(robot);
    }

    /**
     * Press and release the KeyMap. Keys/Buttons are released in reverse order
     */
    public void pressBinding(KeyMap keyMap) {
        pressKeyMap(keyMap.map());
    }

    /**
     * Press down the {@link KeyMap} and don't release
     */
    public void holdBinding(KeyMap keyMap) {
        String map = keyMap.map();
        heldKeyMaps.add(map);
        holdKeyMap(map);
    }

    /**
     * Release the {@link KeyMap}, releases the keys/buttons in reverse order
     */
    public void releaseBinding(KeyMap keyMap) {
        String map = keyMap.map();
        heldKeyMaps.remove(map);
        releaseKeyMap(map);
    }

    /**
     * Release all bindings currently held down, release the bindings in the order they were pressed.
     * Each binding's keys are released in reverse order.
     */
    public void releaseAllBindings() {
        for (String keyMap : heldKeyMaps) releaseKeyMap(keyMap);
        heldKeyMaps.clear();
    }

    /**
     * Press and release the keys in the map. Keys are pressed in order then released in reverse order.
     * @param map The string map of the keys in the binding
     */
    public void pressKeyMap(String map) {
        ArrayList<Integer> keyCodes = getKeyCodesFromMap(map);

        // This whole paragraph was just a useless attempt to mitigate user error, just gonna let the user press what they wanna press

        holdKeyMap(keyCodes);

        releaseKeyMap(keyCodes);
    }

    /**
     * Press and hold the keys in the map in order
     * @param map The string map of the keys in the binding
     */
    public void holdKeyMap(String map) {
        holdKeyMap(getKeyCodesFromMap(map));
    }

    /**
     * Press and hold the keys in the map in order
     * @param keyCodes The int map of the keycodes in the binding
     */
    private void holdKeyMap(ArrayList<Integer> keyCodes) {
        for (int key : keyCodes)
            robot.keyPress(key);
    }

    /**
     * Release the keys in the map in reverse order
     * @param map The string map of the keys in the binding
     */
    public void releaseKeyMap(String map) {
        releaseKeyMap(getKeyCodesFromMap(map));
    }

    /**
     * Release the keys in the map in reverse order
     * @param keyCodes The int map of the keycodes in the binding
     */
    private void releaseKeyMap(ArrayList<Integer> keyCodes) {
        for (int i = keyCodes.size() - 1; i >= 0; i--)
            robot.keyRelease(keyCodes.get(i));
    }

    /**
     * Translate a string map of keys into their int keycodes, causes runtime error if any of the keys in
     * the map aren't valid
     * @return Integer ArrayList of the keycodes associated with the keys in the map
     */
    private ArrayList<Integer> getKeyCodesFromMap(String map) {
        ArrayList<Integer> keyCodes = new ArrayList<>();
        if (map.equals("")) return keyCodes;

        // todo untested change from " " to "\\s"
        map = map.replaceAll("\\s", "").toLowerCase();
        String[] keys = map.split("\\+");

        for (String key : keys) {
            Integer keyCode = getKeyCodeFromKey(key);

            if (keyCode == null)
                throw new InvalidKeyException(key);

            keyCodes.add(keyCode);
        }

        return keyCodes;
    }

    /**
     * Translate a string key into its int keycode
     * @return Integer keycode associated with the key string, returns null if key has no association
     */
    private static Integer getKeyCodeFromKey(String key) {
        return switch (key) {
            case "a" -> KeyEvent.VK_A;
            case "b" -> KeyEvent.VK_B;
            case "c" -> KeyEvent.VK_C;
            case "d" -> KeyEvent.VK_D;
            case "e" -> KeyEvent.VK_E;
            case "f" -> KeyEvent.VK_F;
            case "g" -> KeyEvent.VK_G;
            case "h" -> KeyEvent.VK_H;
            case "i" -> KeyEvent.VK_I;
            case "j" -> KeyEvent.VK_J;
            case "k" -> KeyEvent.VK_K;
            case "l" -> KeyEvent.VK_L;
            case "m" -> KeyEvent.VK_M;
            case "n" -> KeyEvent.VK_N;
            case "o" -> KeyEvent.VK_O;
            case "p" -> KeyEvent.VK_P;
            case "q" -> KeyEvent.VK_Q;
            case "r" -> KeyEvent.VK_R;
            case "s" -> KeyEvent.VK_S;
            case "t" -> KeyEvent.VK_T;
            case "u" -> KeyEvent.VK_U;
            case "v" -> KeyEvent.VK_V;
            case "w" -> KeyEvent.VK_W;
            case "x" -> KeyEvent.VK_X;
            case "y" -> KeyEvent.VK_Y;
            case "z" -> KeyEvent.VK_Z;
            case "`" -> KeyEvent.VK_BACK_QUOTE;
            case "0" -> KeyEvent.VK_0;
            case "1" -> KeyEvent.VK_1;
            case "2" -> KeyEvent.VK_2;
            case "3" -> KeyEvent.VK_3;
            case "4" -> KeyEvent.VK_4;
            case "5" -> KeyEvent.VK_5;
            case "6" -> KeyEvent.VK_6;
            case "7" -> KeyEvent.VK_7;
            case "8" -> KeyEvent.VK_8;
            case "9" -> KeyEvent.VK_9;
            case "-" -> KeyEvent.VK_MINUS;
            case "=" -> KeyEvent.VK_EQUALS;
            case "!" -> KeyEvent.VK_EXCLAMATION_MARK;
            case "@" -> KeyEvent.VK_AT;
            case "#" -> KeyEvent.VK_NUMBER_SIGN;
            case "$" -> KeyEvent.VK_DOLLAR;
            case "^" -> KeyEvent.VK_CIRCUMFLEX;
            case "&" -> KeyEvent.VK_AMPERSAND;
            case "*" -> KeyEvent.VK_ASTERISK;
            case "(" -> KeyEvent.VK_LEFT_PARENTHESIS;
            case ")" -> KeyEvent.VK_RIGHT_PARENTHESIS;
            case "_" -> KeyEvent.VK_UNDERSCORE;
            case "[" -> KeyEvent.VK_OPEN_BRACKET;
            case "]" -> KeyEvent.VK_CLOSE_BRACKET;
            case "\\" -> KeyEvent.VK_BACK_SLASH;
            case ";" -> KeyEvent.VK_SEMICOLON;
            case ":" -> KeyEvent.VK_COLON;
            case "\"" -> KeyEvent.VK_QUOTE;
            case "'" -> KeyEvent.VK_QUOTEDBL;
            case "," -> KeyEvent.VK_COMMA;
            case "." -> KeyEvent.VK_PERIOD;
            case "/" -> KeyEvent.VK_SLASH;
            // SPECIAL CASES
            case "plus", "add" -> KeyEvent.VK_PLUS;
            case "sp", "space" -> KeyEvent.VK_SPACE;
            case "tab" -> KeyEvent.VK_TAB;
            case "rtrn", "return", "enter" -> KeyEvent.VK_ENTER;
            // CONTROL KEYS
            case "ctrl", "control" -> KeyEvent.VK_CONTROL;
            case "shift" -> KeyEvent.VK_SHIFT;
            case "alt" -> KeyEvent.VK_ALT;
            case "altgr" -> KeyEvent.VK_ALT_GRAPH;
            case "bsp", "backspace" -> KeyEvent.VK_BACK_SPACE;
            case "del", "delete" -> KeyEvent.VK_DELETE;
            case "esc", "escape" -> KeyEvent.VK_ESCAPE;
            case "win", "windows" -> KeyEvent.VK_WINDOWS;
            case "caps", "capslock" -> KeyEvent.VK_CAPS_LOCK;
            case "prtsc", "prntscrn", "printscreen" -> KeyEvent.VK_PRINTSCREEN;
            case "ins", "insert" -> KeyEvent.VK_INSERT;
            // NAVIGATION KEYS
            case "left" -> KeyEvent.VK_LEFT;
            case "right" -> KeyEvent.VK_RIGHT;
            case "up" -> KeyEvent.VK_UP;
            case "down" -> KeyEvent.VK_DOWN;
            case "home" -> KeyEvent.VK_HOME;
            case "end" -> KeyEvent.VK_END;
            case "pgup", "pageup" -> KeyEvent.VK_PAGE_UP;
            case "pgdn", "pagedown" -> KeyEvent.VK_PAGE_DOWN;
            // FUNCTION KEYS
            case "f1" -> KeyEvent.VK_F1;
            case "f2" -> KeyEvent.VK_F2;
            case "f3" -> KeyEvent.VK_F3;
            case "f4" -> KeyEvent.VK_F4;
            case "f5" -> KeyEvent.VK_F5;
            case "f6" -> KeyEvent.VK_F6;
            case "f7" -> KeyEvent.VK_F7;
            case "f8" -> KeyEvent.VK_F8;
            case "f9" -> KeyEvent.VK_F9;
            case "f10" -> KeyEvent.VK_F10;
            case "f11" -> KeyEvent.VK_F11;
            case "f12" -> KeyEvent.VK_F12;
            default -> null;
        };
    }

    /**
     * Verify that a keymap string is valid
     * @see <a href="https://github.com/bobcat33/GamepadMouseController/blob/master/src/main/java/com/github/bobcat33/gamepadmousecontroller/output/components/binding/Mapping Keywords.txt">List of mapping keywords</a>
     */
    public static boolean validateMap(String map) {
        if (map.equals("")) return false;

        map = map.replace(" ", "").toLowerCase();

        String[] keys = map.split("\\+");

        for (String key : keys) {
            if (key.equals("") || getKeyCodeFromKey(key) == null) return false;
        }

        return true;
    }
}
