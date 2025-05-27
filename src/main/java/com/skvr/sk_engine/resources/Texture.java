package com.skvr.sk_engine.resources;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public class Texture {

    private int textureID;
    public int width, height;

    public Texture(String path, int wrap, int filter) {
        ByteBuffer imageBuffer;

        try (InputStream in = getClass().getResourceAsStream(path)) {
            if (in == null)
                throw new RuntimeException("Failed to load texture from path: " + path);

            byte[] imageBytes = in.readAllBytes();
            imageBuffer = BufferUtils.createByteBuffer(imageBytes.length);
            imageBuffer.put(imageBytes).flip();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);

        STBImage.stbi_set_flip_vertically_on_load(true);
        ByteBuffer pixels = STBImage.stbi_load_from_memory(imageBuffer, width, height, channels, 4);
        if (pixels == null)
            throw new RuntimeException("Failed to load image: " + STBImage.stbi_failure_reason());

        this.width = width.get();
        this.height = height.get();

        textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, wrap);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, wrap);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, filter);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, filter);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, this.width, this.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);

        STBImage.stbi_image_free(pixels);
    }

    public void bind(int textureSlot) {
        glActiveTexture(GL_TEXTURE0 + textureSlot);
        glBindTexture(GL_TEXTURE_2D, textureID);
    }
}
