package com.skvr.sk_engine;

import com.skvr.sk_engine.rendering.Camera;
import com.skvr.sk_engine.rendering.FrameBuffer;
import com.skvr.sk_engine.rendering.Window;
import com.skvr.sk_engine.resources.ResourceManager;
import com.skvr.sk_engine.scenes.Scene;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;
import static org.lwjgl.stb.STBImage.*;

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

        FrameBuffer.getInstance().init(WIDTH, HEIGHT);

        ResourceManager.getInstance().loadShader("Default Shader 3D", "/defaultShader3D.vert", "/defaultShader3D.frag");
        ResourceManager.getInstance().loadShader("Default Shader 2D", "/defaultShader2D.vert", "/defaultShader2D.frag");
        ResourceManager.getInstance().loadShader("Default Sprite 3D", "/defaultSprite3D.vert", "/defaultSprite3D.frag");
        ResourceManager.getInstance().loadShader("Default Sprite 2D", "/defaultSprite2D.vert", "/defaultSprite2D.frag");
        ResourceManager.getInstance().loadShader("FBO", "/FBOShader.vert", "/FBOShader.frag");

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


        float[] FBOVertices = {
                -1f, -1f, 0.0f,
                -1f, 1f, 0.0f,
                1f, 1f, 0.0f,
                1f, -1f, 0.0f,
        };

        float[] FBOTexCoords = {
                0, 0,
                0, 1,
                1, 1,
                1, 0,
        };

        ResourceManager.getInstance().loadMesh("Default Sprite", vertices, indices, texCoords);
        ResourceManager.getInstance().loadMesh("FBO", FBOVertices, indices, FBOTexCoords);

        baseScene = new Scene();

        start();

        hasStarted = true;

        long lastTime = System.nanoTime();
        float deltaTime;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while (!glfwWindowShouldClose(window.getWindowHandle())) {
            long currentTime = System.nanoTime();
            deltaTime = (currentTime - lastTime) / 1_000_000_000.0f;
            lastTime = currentTime;

            glfwPollEvents();

            glBindFramebuffer(GL_FRAMEBUFFER, FrameBuffer.getInstance().FBO);
            glViewport(0, 0, FrameBuffer.getInstance().width, FrameBuffer.getInstance().height);
            glClear(GL_COLOR_BUFFER_BIT);

            ResourceManager.getInstance().getShader("Default Shader 3D").setMatrix4f("view", Camera.getInstance().getViewMatrix());
            ResourceManager.getInstance().getShader("Default Shader 3D").setMatrix4f("projection", Camera.getInstance().getProjectionMatrix());

            ResourceManager.getInstance().getShader("Default Shader 2D").setMatrix4f("view", Camera.getInstance().get2dViewMatrix());
            ResourceManager.getInstance().getShader("Default Shader 2D").setMatrix4f("projection", Camera.getInstance().getOrthoMatrix());

            ResourceManager.getInstance().getShader("Default Sprite 2D").setMatrix4f("view", Camera.getInstance().get2dViewMatrix());
            ResourceManager.getInstance().getShader("Default Sprite 2D").setMatrix4f("projection", Camera.getInstance().getOrthoMatrix());

            ResourceManager.getInstance().getShader("Default Sprite 3D").setMatrix4f("view", Camera.getInstance().getViewMatrix());
            ResourceManager.getInstance().getShader("Default Sprite 3D").setMatrix4f("projection", Camera.getInstance().getProjectionMatrix());

            Input.update();

            update(deltaTime);
            render();

            baseScene.beginUpdate(deltaTime);
            baseScene.beginRender();

            glBindFramebuffer(GL_FRAMEBUFFER, 0);
            glViewport(0, 0, window.getWidth(), window.getHeight());
            glClear(GL_COLOR_BUFFER_BIT);

            ResourceManager.getInstance().getShader("FBO").use();

            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, FrameBuffer.getInstance().texture);
            ResourceManager.getInstance().getShader("FBO").setInt("screenTexture", 0);

            ResourceManager.getInstance().getMesh("FBO").draw();


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

    public void setWindowIcon(String path) {
        ByteBuffer imageBuffer;

        URL url = getClass().getResource(path);
        if (url == null) {
            throw new IllegalArgumentException("Resource not found: " + path);
        }

        try (
                InputStream inputStream = url.openStream();
                ReadableByteChannel channel = Channels.newChannel(inputStream)
        ) {
            // Get the exact size of the file
            int size = url.openConnection().getContentLength();
            if (size <= 0) {
                throw new RuntimeException("Failed to determine size of resource: " + path);
            }

            imageBuffer = BufferUtils.createByteBuffer(size);

            while (imageBuffer.hasRemaining()) {
                if (channel.read(imageBuffer) == -1) break;
            }
            imageBuffer.flip();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer comp = BufferUtils.createIntBuffer(1);

        ByteBuffer image = stbi_load_from_memory(imageBuffer, width, height, comp, 4);
        if (image == null) {
            throw new RuntimeException("Failed to load image: " + stbi_failure_reason());
        }

        GLFWImage.Buffer icon = GLFWImage.malloc(1);
        icon.width(width.get(0));
        icon.height(height.get(0));
        icon.pixels(image);

        glfwSetWindowIcon(Window.getInstance().getWindowHandle(), icon);

        stbi_image_free(image);
        icon.free();
    }
}
