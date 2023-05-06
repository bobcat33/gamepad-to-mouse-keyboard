package com.github.bobcat33.gamepadmousecontroller.output.components.binding;

import com.github.bobcat33.gamepadmousecontroller.input.components.Button;
import com.github.bobcat33.gamepadmousecontroller.output.components.keyboard.Keyboard;
import com.github.bobcat33.gamepadmousecontroller.output.components.mouse.MouseButtons;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class Bindings {

    private final HashMap<Button, Binding> bindings = new HashMap<>();

    public Bindings() {
        loadDefaultBindings();
    }

    public Bindings(String filename) throws FileNotFoundException {
        this(new File(filename));
    }

    public Bindings(File file) throws FileNotFoundException {
        loadBindingsFromFile(file);
    }


    // todo docs
    public Binding getBinding(Button button) {
        Binding binding = bindings.get(button);
        if (binding == null) return new Binding(Binding.Type.NONE, "");
        return binding;
    }

    // todo docs
    public void printBindings() {
        System.out.println("BUTTON: BINDING_TYPE \"key+map\"");

        Button[] correctOrder = Button.values();
        Arrays.sort(correctOrder);

        for (Button orderedButton : correctOrder) {
            if (!orderedButton.equals(Button.NONE)) {
                for (Button button : bindings.keySet()) {
                    if (orderedButton.equals(button)) {
                        Binding binding = getBinding(button);
                        System.out.println(button.name() + ": " + binding.type().name() + " \"" + binding.keyMap() + "\"");
                    }
                }
            }
        }
    }

    // todo docs
    public void loadBindingsFromFile(String filename) throws FileNotFoundException {
        loadBindingsFromFile(new File(filename));
    }

    // todo docs
    public void loadBindingsFromFile(File file) throws FileNotFoundException {
        System.out.println("Loading bindings from file '" + file.getName() + "'");
        try {
            HashMap<Button, Binding> updatedBindings = loadBindingMapFromFile(file);
            // Only empty previous bindings if the new bindings were successfully loaded
            loadEmptyBindings();
            bindings.putAll(updatedBindings);
            System.out.println("Bindings successfully loaded!\n");
            printBindings();
        } catch (InvalidBindingsFileException invalidFile) {
            invalidFile.printStackTrace();
        }
    }

    // todo docs
    private HashMap<Button, Binding> loadBindingMapFromFile(File file)
            throws FileNotFoundException, InvalidBindingsFileException {
        HashMap<Button, Binding> updatedBindings = new HashMap<>();

        Scanner scanner = new Scanner(file);
        int lineNum = 0;
        boolean terminatorFound = false;

        Binding.Type currentType = Binding.Type.NONE;

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            lineNum++;
            // If the line isn't a comment or blank
            if (!(line.startsWith("#") || line.isBlank())) {
                // Set to lowercase and remove blank space
                String lineLowerNoBlankSpace = line.toLowerCase().replaceAll("\\s", "");
                // todo remove commented
//                System.out.println("LOADING LINE: " + lineLowerNoBlankSpace);
                // If the line is defining a binding type
                if (lineLowerNoBlankSpace.endsWith(":")) {
                    currentType = switch (lineLowerNoBlankSpace) {
                        case "keyboard-holdable:" -> Binding.Type.KEYBOARD_HOLDABLE;
                        case "keyboard:" -> Binding.Type.KEYBOARD;
                        case "mouse:", "mouse-button:", "mouse-buttons:" -> Binding.Type.MOUSE_BUTTON;
                        default -> throw new InvalidBindingsFileException(file.getName(), "Invalid binding type '" + line.substring(0, line.length() - 1) + "'", lineNum);
                    };
                }
                // If the binding type has been defined and the line is defining a binding
                else if (!currentType.equals(Binding.Type.NONE) && lineLowerNoBlankSpace.startsWith("-") && lineLowerNoBlankSpace.endsWith("\"")) {
                    // Split the line (minus the - at the start and the " at the end) by the first '="'
                    String[] bindingDefinition = lineLowerNoBlankSpace.substring(1, lineLowerNoBlankSpace.length()-1).split("=\"", 2);
                    // If there aren't two values then the format must be wrong
                    if (bindingDefinition.length != 2) throw new InvalidBindingsFileException(file.getName(), lineNum);
                    String buttonName = bindingDefinition[0];
                    String keyMap = bindingDefinition[1];


                    // If the key map isn't valid throw error
                    if (((currentType.equals(Binding.Type.KEYBOARD) || currentType.equals(Binding.Type.KEYBOARD_HOLDABLE)) && !Keyboard.validateMap(keyMap))
                            || (currentType.equals(Binding.Type.MOUSE_BUTTON) && MouseButtons.getBoundButton(keyMap) == null))
                        throw new InvalidBindingsFileException(file.getName(), "Invalid key map '" + keyMap + "' for binding type " + currentType.name(), lineNum);

                    Button buttonType = getButtonType(file, lineNum, buttonName);

                    // Add the new binding to the array, if the button has already been assigned a binding throw an error
                    if (updatedBindings.put(buttonType, new Binding(currentType, keyMap)) != null)
                        throw new InvalidBindingsFileException(file.getName(), "The controller button '" + buttonType.name() + "' already has a binding assigned to it, error", lineNum);
                }
                // If the line is defining the terminator
                else if (lineLowerNoBlankSpace.startsWith("terminator:")) {
                    String buttonName = lineLowerNoBlankSpace.replaceFirst("terminator:", "");
                    Button buttonType = getButtonType(file, lineNum, buttonName);

                    // Add the new binding to the array, if the button has already been assigned a binding throw an error
                    if (updatedBindings.put(buttonType, new Binding(Binding.Type.TERMINATOR, "")) != null)
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

    // todo docs (not to be used by anything other than loadBindingMapFromFile)
    private static Button getButtonType(File file, int lineNum, String buttonName) throws InvalidBindingsFileException {
        Button buttonType;
        try {
            buttonType = Button.valueOf(buttonName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidBindingsFileException(file.getName(), "Invalid button type '" + buttonName.toUpperCase() + "'", lineNum);
        }
        return buttonType;
    }

    // todo docs
    public void loadDefaultBindings() {
        System.out.println("Loading default bindings");
        loadEmptyBindings();

        // Normal keyboard shortcut presses
        bindings.put(Button.A, new Binding(Binding.Type.KEYBOARD, "space"));
        bindings.put(Button.B, new Binding(Binding.Type.KEYBOARD, "t"));
        bindings.put(Button.X, new Binding(Binding.Type.KEYBOARD, "tab"));
        bindings.put(Button.Y, new Binding(Binding.Type.KEYBOARD, "f"));
        bindings.put(Button.LS, new Binding(Binding.Type.KEYBOARD, "ctrl+w"));
        bindings.put(Button.UP, new Binding(Binding.Type.KEYBOARD, "f5"));
        bindings.put(Button.LEFT, new Binding(Binding.Type.KEYBOARD, "left"));
        bindings.put(Button.RIGHT, new Binding(Binding.Type.KEYBOARD, "right"));
        bindings.put(Button.BACK, new Binding(Binding.Type.KEYBOARD, "esc"));
        
        // Holdable keyboard presses
        bindings.put(Button.LB, new Binding(Binding.Type.KEYBOARD_HOLDABLE, "ctrl"));
        bindings.put(Button.RB, new Binding(Binding.Type.KEYBOARD_HOLDABLE, "shift"));
        bindings.put(Button.DOWN, new Binding(Binding.Type.KEYBOARD_HOLDABLE, "alt"));

        // Mouse buttons
        bindings.put(Button.RT, new Binding(Binding.Type.MOUSE_BUTTON, "mb_left"));
        bindings.put(Button.LT, new Binding(Binding.Type.MOUSE_BUTTON, "mb_right"));

        // Terminator buttons
        bindings.put(Button.RS, new Binding(Binding.Type.TERMINATOR, ""));

        System.out.println("Default bindings loaded!\n");
        printBindings();
    }

    // todo docs
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
