package com.github.bobcat33.gamepad_to_mouse_keyboard.output.components.mouse;

import com.github.bobcat33.gamepad_to_mouse_keyboard.input.components.Joystick;
import com.github.bobcat33.gamepad_to_mouse_keyboard.output.OutputManager;
import com.github.bobcat33.gamepad_to_mouse_keyboard.output.components.OutputComponent;

import java.awt.*;

public class MouseMover extends OutputComponent {

    public enum Speed {
        FAST,
        SLOW
    }

    private final int slowSpeed;
    private final int fastSpeed;
    private final OutputManager manager;
    private boolean running = false;
    private Thread thread = null;

    /**
     * Repeatedly poll {@link Joystick} object for axis data and use to move mouse. Mouse is moved every 10 milliseconds
     * while a joystick is active
     * @param manager {@link OutputManager} object that contains the {@link Joystick} object that the mover polls for axis data
     * @param slowSpeed Amount the cursor moves per 10 milliseconds when the slow speed is used
     * @param fastSpeed Amount the cursor moves per 10 milliseconds when the fast speed is used
     */
    public MouseMover(OutputManager manager, int slowSpeed, int fastSpeed) {
        super();
        this.slowSpeed = slowSpeed;
        this.fastSpeed = fastSpeed;
        this.manager = manager;
    }

    /**
     * Repeatedly poll {@link Joystick} object for axis data and use to move mouse. Mouse is moved every 10 milliseconds
     * while a joystick is active
     * @param manager {@link OutputManager} object that contains the {@link Joystick} object that the mover polls for axis data
     * @param robot {@link Robot} object used to move the mouse
     * @param slowSpeed Amount the cursor moves per 10 milliseconds when the slow speed is used
     * @param fastSpeed Amount the cursor moves per 10 milliseconds when the fast speed is used
     */
    public MouseMover(OutputManager manager, Robot robot, int slowSpeed, int fastSpeed) {
        super(robot);
        this.slowSpeed = slowSpeed;
        this.fastSpeed = fastSpeed;
        this.manager = manager;
    }

    /**
     * Start the mouse mover in a new thread
     */
    public void start() throws AlreadyRunningException {
        if (running) throw new AlreadyRunningException();
        thread = new Thread(this::run);
        thread.start();
    }

    /**
     * Run the mouse mover. In most cases should be executed in new thread.
     */
    public void run() {
        running = true;
        while (running) {
            Joystick joystick = manager.getActiveJoystick();

            if (joystick != null) {
                moveMouse(getSpeed(joystick), joystick.getXAxis(), joystick.getYAxis());
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ignored) {}
            }
        }
    }

    /**
     * Stop the mouse mover, if {@link MouseMover#start()} was used then the thread created will be interrupted
     */
    public void stop() {
        running = false;
        if (thread != null && thread.isAlive()) thread.interrupt();
    }

    /**
     * Move the mouse using one of the mouse speeds
     * @param speed Speed to move the mouse at
     * @param amountX Multiplier for the speed along the X axis (between 1 and -1)
     * @param amountY Multiplier for the speed along the Y axis (between 1 and -1)
     */
    public void moveMouse(Speed speed, float amountX, float amountY) {
        int mouseSpeed;
        // If more speeds are added this can become a switch
        if (speed.equals(Speed.FAST)) mouseSpeed = fastSpeed;
        else mouseSpeed = slowSpeed;

        moveMouse(mouseSpeed, amountX, amountY);
    }

    /**
     * Move the mouse using one of the mouse speeds
     * @param speed Speed to move the mouse at
     * @param amountX Multiplier for the speed along the X axis (between 1 and -1)
     * @param amountY Multiplier for the speed along the Y axis (between 1 and -1)
     */
    public void moveMouse(int speed, float amountX, float amountY) {
        amountX = Joystick.bringInAxisRange(amountX);
        amountY = Joystick.bringInAxisRange(amountY);

        Point position = getPosition();

//        System.out.println("X: " + (int) (amountX * mouseSpeed) + "\nY:" + (int) (amountY * mouseSpeed));
        robot.mouseMove((int) (amountX * speed) + position.x, (int) (amountY * speed) + position.y);

    }

    /**
     * @return The speed of the mouse based on the side of the controller a joystick is positioned
     */
    private Speed getSpeed(Joystick joystick) {

        return switch (joystick.getSide()) {
            case LEFT -> Speed.FAST;
            case RIGHT -> Speed.SLOW;
        };

    }

    /**
     * @return The current position of the cursor
     */
    public Point getPosition() {
        return MouseInfo.getPointerInfo().getLocation();
    }

}
