package com.skvr.sk_engine;

import com.skvr.sk_engine.rendering.Camera;
import com.skvr.sk_engine.rendering.Window;
import com.skvr.sk_engine.resources.ResourceManager;
import com.skvr.sk_engine.scenes.Scene;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

public abstract class Application {
    private static Window window;

    private String TITLE;
    private int WIDTH, HEIGHT;
    private boolean ISFULLSCREEN;
    private boolean hasStarted;

    public Scene baseScene;

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
        ResourceManager.getInstance().loadShader("Default Sprite 3D", "/defaultSprite3D.vert", "/defaultSprite3D.frag");
        ResourceManager.getInstance().loadShader("Default Sprite 2D", "/defaultSprite2D.vert", "/defaultSprite2D.frag");

        float[] vertices = {
                -0.5f, -0.5f, 0.0f,
                -0.5f, 0.5f, 0.0f,
                0.5f, 0.5f, 0.0f,
                0.5f, -0.5f, 0.0f,
        };

        float[] texCoords = {
                0, 0,
                0, 1,
                1, 1,
                1, 0,
        };

        int[] indices = {
                0, 1, 2,
                0, 3, 2
        };

        ResourceManager.getInstance().loadMesh("Default Sprite", vertices, indices, texCoords);

        baseScene = new Scene();

        start();

        hasStarted = true;

        long lastTime = System.nanoTime();
        float deltaTime = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while (!glfwWindowShouldClose(window.getWindowHandle())) {
            long currentTime = System.nanoTime();
            deltaTime = (currentTime - lastTime) / 1_000_000_000.0f;
            lastTime = currentTime;

            glfwPollEvents();

            glClear(GL_COLOR_BUFFER_BIT);

            ResourceManager.getInstance().getShader("Default Shader 3D").setMatrix4f("view", Camera.getInstance().getViewMatrix());
            ResourceManager.getInstance().getShader("Default Shader 3D").setMatrix4f("projection", Camera.getInstance().getProjectionMatrix());

            ResourceManager.getInstance().getShader("Default Shader 2D").setMatrix4f("view", Camera.getInstance().get2dViewMatrix());
            ResourceManager.getInstance().getShader("Default Shader 2D").setMatrix4f("projection", Camera.getInstance().getOrthoMatrix());

            ResourceManager.getInstance().getShader("Default Sprite 2D").setMatrix4f("view", Camera.getInstance().get2dViewMatrix());
            ResourceManager.getInstance().getShader("Default Sprite 2D").setMatrix4f("projection", Camera.getInstance().getOrthoMatrix());

            ResourceManager.getInstance().getShader("Default Sprite 3D").setMatrix4f("view", Camera.getInstance().getViewMatrix());
            ResourceManager.getInstance().getShader("Default Sprite 3D").setMatrix4f("projection", Camera.getInstance().getProjectionMatrix());

            update(deltaTime);
            render();

            baseScene.beginUpdate(deltaTime);
            baseScene.beginRender();

            glfwSwapBuffers(window.getWindowHandle());

            frames++;

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                window.fps = frames;
                frames = 0;
            }
        }
    }

    public abstract void start();
    public abstract void render();
    public abstract void update(float delta);

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
