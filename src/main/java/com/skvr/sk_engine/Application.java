package com.skvr.sk_engine;

import com.skvr.sk_engine.rendering.Camera;
import com.skvr.sk_engine.rendering.Window;
import com.skvr.sk_engine.resources.ResourceManager;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

public abstract class Application {
    private static Window window;

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

        window = Window.getInstance();

        window.Init(TITLE, WIDTH, HEIGHT, ISFULLSCREEN);

        ResourceManager.getInstance().loadShader("Default Shader 3D", "/defaultShader3D.vert", "/defaultShader3D.frag");
        ResourceManager.getInstance().loadShader("Default Shader 2D", "/defaultShader2D.vert", "/defaultShader2D.frag");

        start();

        hasStarted = true;
        while (!glfwWindowShouldClose(window.getWindowHandle())) {
            glfwPollEvents();

            glClear(GL_COLOR_BUFFER_BIT);

            ResourceManager.getInstance().getShader("Default Shader 3D").setMatrix4f("view", Camera.getInstance().getViewMatrix());
            ResourceManager.getInstance().getShader("Default Shader 3D").setMatrix4f("projection", Camera.getInstance().getProjectionMatrix());

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
