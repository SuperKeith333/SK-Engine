package com.skvr.sk_engine.rendering;

import imgui.ImGui;
import imgui.gl3.ImGuiImplGl3;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import imgui.glfw.ImGuiImplGlfw;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private static Window instance;

    public final ImGuiImplGlfw imGuiGlfw;
    public final ImGuiImplGl3 imGuiG13;

    private long windowHandle;
    private String title;
    private int width, height;
    private IntBuffer prevX = BufferUtils.createIntBuffer(1);
    private IntBuffer prevY = BufferUtils.createIntBuffer(1);
    private boolean isFullscreen;

    public int fps = 0;

    private Window() {
        imGuiGlfw = new ImGuiImplGlfw();
        imGuiG13 = new ImGuiImplGl3();
    }

    public static Window getInstance() {
        if (instance == null) {
            instance = new Window();
        }
        return instance;
    }

    public void Init(String title, int width, int height, boolean isFullscreen) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.isFullscreen = isFullscreen;

        if (isFullscreen) {
            glfwWindowHint(GLFW_DECORATED, GLFW_FALSE);
            this.width = glfwGetVideoMode(glfwGetPrimaryMonitor()).width();
            this.height = glfwGetVideoMode(glfwGetPrimaryMonitor()).height();
        }

        windowHandle = glfwCreateWindow(isFullscreen ? glfwGetVideoMode(glfwGetPrimaryMonitor()).width() : width, isFullscreen ? glfwGetVideoMode(glfwGetPrimaryMonitor()).height() : height, title, isFullscreen ? glfwGetPrimaryMonitor() : NULL, NULL);
        if (windowHandle == 0)
            throw new RuntimeException("Failed to create GLFW window");

        glfwMakeContextCurrent(windowHandle);
        GL.createCapabilities();

        glfwGetWindowPos(windowHandle, prevX, prevY);

        glfwSetWindowSizeCallback(windowHandle, (window, newWidth, newHeight) -> {
            this.width = newWidth;
            this.height = newHeight;
        });

        ImGui.createContext();

        imGuiGlfw.init(windowHandle, true);
        imGuiG13.init("#version 330");
    }

    public void setTitle(String newTitle) {
        title = newTitle;

        glfwSetWindowTitle(windowHandle, title);
    }

    public void setWindowSize(int width, int height) {
        this.width = width;
        this.height = height;

        glfwSetWindowSize(windowHandle, width, height);
    }

    public void setFullscreen(boolean isFullscreen) {
        glfwSetWindowAttrib(windowHandle, GLFW_DECORATED, isFullscreen ? GLFW_FALSE : GLFW_TRUE);

        IntBuffer x = BufferUtils.createIntBuffer(1);
        IntBuffer y = BufferUtils.createIntBuffer(1);

        glfwGetWindowPos(windowHandle, x, y);

        if (x.get(0) != 0) {
            glfwGetWindowPos(windowHandle, prevX, prevY);
        }

        glfwSetWindowMonitor(windowHandle, isFullscreen ? glfwGetPrimaryMonitor() : NULL, isFullscreen ? 0 : prevX.get(0), prevY.get(0), isFullscreen ? glfwGetVideoMode(glfwGetPrimaryMonitor()).width() : width, isFullscreen ? glfwGetVideoMode(glfwGetPrimaryMonitor()).height() : height, GLFW_DONT_CARE);
    }

    public void setMouseMode(int mode) {
        glfwSetInputMode(windowHandle, GLFW_CURSOR, mode);
    }

    public long getWindowHandle() {
        return windowHandle;
    }

    public String getTitle() {
        return title;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getX() {
        return prevX.get(0);
    }

    public int getY() {
        return prevY.get(0);
    }

    public boolean isFullscreen() {
        return isFullscreen;
    }
}
