package com.skvr.sk_engine;

import com.skvr.sk_engine.rendering.Window;

import static org.lwjgl.glfw.GLFW.*;

public abstract class Application {
    public Window window;

    private String TITLE;
    private int WIDTH, HEIGHT;
    private boolean ISFULLSCREEN;
    private boolean hasStarted;

    public void begin() {
        if (!glfwInit())
            throw new RuntimeException("Failed to initialize GLFW!");

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        window = new Window(TITLE, WIDTH, HEIGHT, ISFULLSCREEN);

        while (!glfwWindowShouldClose(window.getWindowHandle())) {
            glfwPollEvents();
            glfwSwapBuffers(window.getWindowHandle());
        }
    }

    public void setTitle(String title) {
        TITLE = title;

        if (hasStarted)
            window.setTitle(title);
    }

    public void setWindowDimensions(int width, int height) {
        WIDTH = width;
        HEIGHT = height;

        if (hasStarted)
            window.setWindowSize(width, height);
    }

    public void setFullscreen(boolean isFullscreen) {
        ISFULLSCREEN = isFullscreen;

        if (hasStarted)
            window.setFullscreen(isFullscreen);
    }
}
