package com.github.bobcat33.gamepad_to_mouse_keyboard.output.components.binding;

import com.github.bobcat33.gamepad_to_mouse_keyboard.input.components.Button;
import com.github.bobcat33.gamepad_to_mouse_keyboard.output.components.keyboard.Keyboard;
import com.github.bobcat33.gamepad_to_mouse_keyboard.output.components.mouse.MouseButtons;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class Bindings {

    private final HashMap<Button, KeyMap> bindings = new HashMap<>();

    public Bindings() {
        loadDefaultBindings();
    }

    public Bindings(String filename) throws FileNotFoundException {
        this(new File(filename));
    }

    public Bindings(File file) throws FileNotFoundException {
        loadBindingsFromFile(file);
    }


    /**
     * Return the KeyMap associated with the relevant Button
     * @param button the button object with an associated KeyMap
     * @return the KeyMap object containing the type and map of the binding, returns an empty map of type
     * {@link KeyMap.Type#NONE NONE} if there is no association
     */
    public KeyMap getKeyMap(Button button) {
        KeyMap keyMap = bindings.get(button);
        if (keyMap == null) return new KeyMap(KeyMap.Type.NONE, "");
        return keyMap;
    }

    /**
     * Display all bindings currently loaded and active to the console
     */
    public void printBindings() {
        System.out.println("BUTTON: BINDING_TYPE \"key+map\"");

        // Load all the button values and sort them into the correct order
        Button[] correctOrder = Button.values();
        Arrays.sort(correctOrder);

        // For each button type (excluding NONE) find the relevant button in the loaded bindings and display it neatly
        for (Button orderedButton : correctOrder) {
            if (!orderedButton.equals(Button.NONE)) {
                for (Button button : bindings.keySet()) {
                    if (orderedButton.equals(button)) {
                        KeyMap keyMap = getKeyMap(button);
                        System.out.println(button.name() + ": " + keyMap.type().name() + " \"" + keyMap.map() + "\"");
                    }
                }
            }
        }
    }

    public void loadBindingsFromFile(String filename) throws FileNotFoundException {
        loadBindingsFromFile(new File(filename));
    }

    public void loadBindingsFromFile(File file) throws FileNotFoundException {
        System.out.println("Loading bindings from file '" + file.getName() + "'");
        try {
            HashMap<Button, KeyMap> updatedBindings = loadBindingMapFromFile(file);
            // Only empty previous bindings if the new bindings were successfully loaded
            loadEmptyBindings();
            bindings.putAll(updatedBindings);
            System.out.println("Bindings successfully loaded!\n");
            printBindings();
        } catch (InvalidBindingsFileException invalidFile) {
            // Since the file was determined to be invalid and has not caused a runtime error it is safe
            // to display the error to the user and continue to execute the code
            invalidFile.printStackTrace();
        }
    }

    /**
     * Backend method to load the contents of a bindings file.
     * @param file File to load the bindings from
     * @return Map of Buttons to their relevant KeyMaps
     * @throws InvalidBindingsFileException If the bindings file has a syntax error that could not be handled
     * or contains invalid values
     */
    private HashMap<Button, KeyMap> loadBindingMapFromFile(File file)
            throws FileNotFoundException, InvalidBindingsFileException {
        // Map to store the new bindings from the file
        HashMap<Button, KeyMap> updatedBindings = new HashMap<>();

        Scanner scanner = new Scanner(file);
        int lineNum = 0;
        // Used to determine if the bindings file has defined a terminator button.
        // This is required as there is currently no other user-friendly way to terminate the script.
        // If a GUI is added later on remove this and associated lines.
        boolean terminatorFound = false;

        // Tracks the type of the keymaps being read at any given time
        KeyMap.Type currentType = KeyMap.Type.NONE;

        // Iterate through each line in the file
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            lineNum++; // Keep track of line numbers for error messages

            // If the line isn't a comment or blank
            if (!(line.startsWith("#") || line.isBlank())) {
                // Set to lowercase and remove blank space
                String lineLowerNoBlankSpace = line.toLowerCase().replaceAll("\\s", "");

                // If the line is defining a keymap type
                if (lineLowerNoBlankSpace.endsWith(":")) {
                    currentType = switch (lineLowerNoBlankSpace) {
                        case "keyboard-holdable:" -> KeyMap.Type.KEYBOARD_HOLDABLE;
                        case "keyboard:" -> KeyMap.Type.KEYBOARD;
                        case "mouse:", "mouse-button:", "mouse-buttons:" -> KeyMap.Type.MOUSE_BUTTON;
                        default -> throw new InvalidBindingsFileException(file.getName(), "Invalid binding type '" + line.substring(0, line.length() - 1) + "'", lineNum);
                    };
                }

                // If the line is defining a keymap and the type has been defined
                else if (!currentType.equals(KeyMap.Type.NONE) && lineLowerNoBlankSpace.startsWith("-") && lineLowerNoBlankSpace.endsWith("\"")) {
                    // Split the line, excluding the - at the start and the " at the end, by the first ="
                    String[] bindingDefinition = lineLowerNoBlankSpace.substring(1, lineLowerNoBlankSpace.length()-1).split("=\"", 2);
                    // If there aren't two values then the format must be wrong
                    if (bindingDefinition.length != 2) throw new InvalidBindingsFileException(file.getName(), lineNum);
                    String buttonName = bindingDefinition[0];
                    String keyMap = bindingDefinition[1];

                    // If the key map isn't valid throw error
                    if (((currentType.equals(KeyMap.Type.KEYBOARD)
                            || currentType.equals(KeyMap.Type.KEYBOARD_HOLDABLE)) && !Keyboard.validateMap(keyMap))
                            || (currentType.equals(KeyMap.Type.MOUSE_BUTTON) && MouseButtons.getBoundButton(keyMap) == null))
                        throw new InvalidBindingsFileException(file.getName(), "Invalid key map '" + keyMap + "' for binding type " + currentType.name(), lineNum);

                    Button buttonType = getButtonType(file.getName(), lineNum, buttonName);

                    // Add the new binding to the hashmap, if the button has already been assigned a binding throw an error
                    if (updatedBindings.put(buttonType, new KeyMap(currentType, keyMap)) != null)
                        throw new InvalidBindingsFileException(file.getName(), "The controller button '" + buttonType.name() + "' already has a binding assigned to it, error", lineNum);
                }

                // If the line is defining the terminator
                else if (lineLowerNoBlankSpace.startsWith("terminator:")) {
                    String buttonName = lineLowerNoBlankSpace.replaceFirst("terminator:", "");
                    Button buttonType = getButtonType(file.getName(), lineNum, buttonName);

                    // Add the new binding to the array, if the button has already been assigned a binding throw an error
                    if (updatedBindings.put(buttonType, new KeyMap(KeyMap.Type.TERMINATOR, "")) != null)
                        throw new InvalidBindingsFileException(file.getName(), "The controller button '" + buttonType.name() + "' already has a binding assigned to it, error", lineNum);
                    terminatorFound = true;
                }

                // If the line is invalid (it isn't a comment, isn't empty, isn't defining a binding type, isn't defining
                // a terminator, isn't defining a binding, or a binding type hasn't been defined)
                else {
                    throw new InvalidBindingsFileException(file.getName(), lineNum);
                }
            }
        }

        // If there is no terminator defined do not accept the file
        if (!terminatorFound) throw new InvalidBindingsFileException(file.getName(), "Must include one terminator binding,\n" +
                " a terminator is defined by the single line 'terminator: <button>', replacing <button> with the controller" +
                " button used to close the app.");
        // If no bindings were set warn user
        if (updatedBindings.isEmpty()) throw new InvalidBindingsFileException(file.getName(), "Empty bindings file");

        return updatedBindings;
    }

    /**
     * THIS METHOD MUST ONLY BE USED IN {@link Bindings#loadBindingMapFromFile(File)}
     * @param filename for error message
     * @param lineNum for error message
     * @param buttonName to get the type from
     */
    private static Button getButtonType(String filename, int lineNum, String buttonName) throws InvalidBindingsFileException {
        Button buttonType;
        try {
            buttonType = Button.valueOf(buttonName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidBindingsFileException(filename, "Invalid button type '" + buttonName.toUpperCase() + "'", lineNum);
        }
        return buttonType;
    }

    /**
     * Load predefined bindings
     */
    public void loadDefaultBindings() {
        System.out.println("Loading default bindings");
        loadEmptyBindings();

        // Normal keyboard shortcut presses
        bindings.put(Button.A, new KeyMap(KeyMap.Type.KEYBOARD, "space"));
        bindings.put(Button.B, new KeyMap(KeyMap.Type.KEYBOARD, "t"));
        bindings.put(Button.X, new KeyMap(KeyMap.Type.KEYBOARD, "tab"));
        bindings.put(Button.Y, new KeyMap(KeyMap.Type.KEYBOARD, "f"));
        bindings.put(Button.LS, new KeyMap(KeyMap.Type.KEYBOARD, "ctrl+w"));
        bindings.put(Button.UP, new KeyMap(KeyMap.Type.KEYBOARD, "f5"));
        bindings.put(Button.LEFT, new KeyMap(KeyMap.Type.KEYBOARD, "left"));
        bindings.put(Button.RIGHT, new KeyMap(KeyMap.Type.KEYBOARD, "right"));
        bindings.put(Button.BACK, new KeyMap(KeyMap.Type.KEYBOARD, "esc"));
        
        // Holdable keyboard presses
        bindings.put(Button.LB, new KeyMap(KeyMap.Type.KEYBOARD_HOLDABLE, "ctrl"));
        bindings.put(Button.RB, new KeyMap(KeyMap.Type.KEYBOARD_HOLDABLE, "shift"));
        bindings.put(Button.DOWN, new KeyMap(KeyMap.Type.KEYBOARD_HOLDABLE, "alt"));

        // Mouse buttons
        bindings.put(Button.RT, new KeyMap(KeyMap.Type.MOUSE_BUTTON, "mb_left"));
        bindings.put(Button.LT, new KeyMap(KeyMap.Type.MOUSE_BUTTON, "mb_right"));

        // Terminator buttons
        bindings.put(Button.RS, new KeyMap(KeyMap.Type.TERMINATOR, ""));

        System.out.println("Default bindings loaded!\n");
        printBindings();
    }

    /**
     * Empty the loaded bindings. Resets all buttons to null. This must be used when changing bindings if unchanged
     * previous bindings are not supposed to persist.
     */
    public void loadEmptyBindings() {
        bindings.put(Button.A, null);
        bindings.put(Button.B, null);
        bindings.put(Button.X, null);
        bindings.put(Button.Y, null);
        bindings.put(Button.LB, null);
        bindings.put(Button.RB, null);
        bindings.put(Button.LT, null);
        bindings.put(Button.RT, null);
        bindings.put(Button.UP, null);
        bindings.put(Button.DOWN, null);
        bindings.put(Button.LEFT, null);
        bindings.put(Button.RIGHT, null);
        bindings.put(Button.LS, null);
        bindings.put(Button.RS, null);
        bindings.put(Button.BACK, null);
        bindings.put(Button.START, null);
        bindings.put(Button.NONE, null);
    }

}
