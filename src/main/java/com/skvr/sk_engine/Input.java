package com.skvr.sk_engine;

import com.skvr.sk_engine.rendering.Window;
import org.lwjgl.BufferUtils;

import java.nio.DoubleBuffer;
import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.*;

public class Input {

    private static final HashMap<Integer, Boolean> currentKeys = new HashMap<>();
    private static final HashMap<Integer, Boolean> lastKeys = new HashMap<>();

    private static final HashMap<Integer, Boolean> currentMouseButtons = new HashMap<>();
    private static final HashMap<Integer, Boolean> lastMouseButtons = new HashMap<>();

    private static double mouseX, mouseY;

    public static void update() {
        lastKeys.putAll(currentKeys);
        lastMouseButtons.putAll(currentMouseButtons);

        // Update mouse position
        DoubleBuffer xBuf = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer yBuf = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(Window.getInstance().getWindowHandle(), xBuf, yBuf);
        mouseX = xBuf.get(0);
        mouseY = yBuf.get(0);

        // Update key states (could loop all if needed)
        for (Integer key : lastKeys.keySet()) {
            currentKeys.put(key, isKeyPressed(key));
        }

        // Update mouse buttons
        for (Integer button : lastMouseButtons.keySet()) {
            currentMouseButtons.put(button, isMouseButtonPressed(button));
        }
    }

    public static boolean isKeyPressed(int key) {
        return glfwGetKey(Window.getInstance().getWindowHandle(), key) == GLFW_PRESS;
    }

    public static boolean wasKeyJustPressed(int key) {
        boolean current = isKeyPressed(key);
        boolean last = lastKeys.getOrDefault(key, false);
        lastKeys.put(key, current);
        return current && !last;
    }

    public static boolean isMouseButtonPressed(int button) {
        return glfwGetMouseButton(Window.getInstance().getWindowHandle(), button) == GLFW_PRESS;
    }

    public static boolean wasMouseButtonJustPressed(int button) {
        boolean current = isMouseButtonPressed(button);
        boolean last = lastMouseButtons.getOrDefault(button, false);
        lastMouseButtons.put(button, current);
        return current && !last;
    }

    public static double getMouseX() {
        return mouseX;
    }

    public static double getMouseY() {
        return mouseY;
    }
}
