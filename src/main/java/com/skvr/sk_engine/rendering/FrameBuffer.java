package com.skvr.sk_engine.rendering;

import static org.lwjgl.opengl.GL30.*;

public class FrameBuffer {

    private static FrameBuffer instance;

    private FrameBuffer() {

    }

    public static FrameBuffer getInstance() {
        if (instance == null) {
            instance = new FrameBuffer();
        }
        return instance;
    }

    public int FBO;
    public int texture;

    public int width, height;

    public void init(int width, int height) {
        this.width = width;
        this.height = height;

        FBO = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, FBO);

        texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, 0);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, texture, 0);

        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
            throw new RuntimeException("Framebuffer is not complete");
    }
}
