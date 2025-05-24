package com.skvr.sk_engine;

import com.skvr.sk_engine.rendering.Window;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

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

        start();

        hasStarted = true;
        while (!glfwWindowShouldClose(window.getWindowHandle())) {
            glfwPollEvents();

            glClear(GL_COLOR_BUFFER_BIT);

            render();

            glfwSwapBuffers(window.getWindowHandle());
        }
    }

    public abstract void start();
    public abstract void render();

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
