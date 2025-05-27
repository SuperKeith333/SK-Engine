package com.skvr.sk_engine.resources;

import org.joml.*;
import org.lwjgl.BufferUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.stream.Collectors;

import static org.lwjgl.opengl.GL20.*;

public class Shader {

    private int ID;

    public Shader(String vertexPath, String fragmentPath) {
        int success;

        int vertex = glCreateShader(GL_VERTEX_SHADER);
        int fragment = glCreateShader(GL_FRAGMENT_SHADER);

        glShaderSource(vertex, loadShader(vertexPath));
        glShaderSource(fragment, loadShader(fragmentPath));

        glCompileShader(vertex);
        glCompileShader(fragment);

        success = glGetShaderi(vertex, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            String log = glGetShaderInfoLog(vertex);
            throw new RuntimeException("Failed to compile vertex shader, error: " + log);
        }

        success = glGetShaderi(fragment, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            String log = glGetShaderInfoLog(fragment);
            throw new RuntimeException("Failed to compile fragment shader, error: " + log);
        }

        ID = glCreateProgram();

        glAttachShader(ID, vertex);
        glAttachShader(ID, fragment);

        glLinkProgram(ID);

        success = glGetProgrami(ID, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            String log = glGetProgramInfoLog(ID);
            throw new RuntimeException("Failed to compile shader program, error: " + log);
        }

        glDeleteShader(vertex);
        glDeleteShader(fragment);
    }

    private String loadShader(String path) {
        InputStream inputStream = getClass().getResourceAsStream(path);
        if (inputStream == null)
            throw new IllegalArgumentException("Shader file not found, path: " + path);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read shader file, path :" + path + ", error :" + e);
        }
    }

    public void setMatrix4f(String name, Matrix4f matrix) {
        use();
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        matrix.get(buffer);
        glUniformMatrix4fv(glGetUniformLocation(ID, name), false, buffer);
    }

    public void setMatrix3f(String name, Matrix3f matrix) {
        use();
        FloatBuffer buffer = BufferUtils.createFloatBuffer(9);
        matrix.get(buffer);
        glUniformMatrix3fv(glGetUniformLocation(ID, name), false, buffer);
    }

    public void setMatrix2f(String name, Matrix2f matrix) {
        use();
        FloatBuffer buffer = BufferUtils.createFloatBuffer(4);
        matrix.get(buffer);
        glUniformMatrix2fv(glGetUniformLocation(ID, name), false, buffer);
    }

    public void setVec2f(String name, Vector2f vector) {
        use();
        FloatBuffer buffer = BufferUtils.createFloatBuffer(2);
        vector.get(buffer);
        glUniform2fv(glGetUniformLocation(ID, name), buffer);
    }
    public void setVec2f(String name, float x, float y) {
        use();
        glUniform2f(glGetUniformLocation(ID, name), x, y);
    }

    public void setVec3f(String name, Vector3f vector) {
        use();
        FloatBuffer buffer = BufferUtils.createFloatBuffer(3);
        vector.get(buffer);
        glUniform3fv(glGetUniformLocation(ID, name), buffer);
    }
    public void setVec3f(String name, float x, float y, float z) {
        use();
        glUniform3f(glGetUniformLocation(ID, name), x, y, z);
    }

    public void setVec4f(String name, Vector4f vector) {
        use();
        FloatBuffer buffer = BufferUtils.createFloatBuffer(4);
        vector.get(buffer);
        glUniform4fv(glGetUniformLocation(ID, name), buffer);
    }
    public void setVec4f(String name, float x, float y, float z, float w) {
        use();
        glUniform4f(glGetUniformLocation(ID, name), x, y, z, w);
    }

    public void setInt(String name, int value) {
        use();
        glUniform1i(glGetUniformLocation(ID, name), value);
    }
    public void setBool(String name, boolean value) {
        use();
        glUniform1i(glGetUniformLocation(ID, name), value ? 1 : 0);
    }
    public void setFloat(String name, float value) {
        use();
        glUniform1f(glGetUniformLocation(ID, name), value);
    }

    public void use() {
        glUseProgram(ID);
    }
}
