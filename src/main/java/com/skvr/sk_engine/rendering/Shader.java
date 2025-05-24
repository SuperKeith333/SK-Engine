package com.skvr.sk_engine.rendering;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    public void use() {
        glUseProgram(ID);
    }
}
