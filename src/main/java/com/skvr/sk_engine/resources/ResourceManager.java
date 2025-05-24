package com.skvr.sk_engine.resources;

import com.skvr.sk_engine.rendering.Mesh;
import com.skvr.sk_engine.rendering.Shader;

import java.util.HashMap;

public class ResourceManager {

    private static ResourceManager instance;

    private final HashMap<String, Shader> shaders;
    private final HashMap<String, Mesh> meshes;

    private ResourceManager() {
        shaders = new HashMap<>();
        meshes = new HashMap<>();
    }

    public static ResourceManager getInstance() {
        if (instance == null) {
            instance = new ResourceManager();
        }
        return instance;
    }

    public void loadMesh(String name, float[] vertices) {
        meshes.put(name, new Mesh(vertices));
    }

    public void loadMesh(String name, float[] vertices, int[] indices) {
        meshes.put(name, new Mesh(vertices, indices));
    }

    public Mesh getMesh(String name) {
        return meshes.get(name);
    }

    public boolean containsMesh(String name) {
        return meshes.containsKey(name);
    }

    public void loadShader(String name, String vertexPath, String fragmentPath) {
        shaders.put(name, new Shader(vertexPath, fragmentPath));
    }

    public Shader getShader(String name) {
        return shaders.get(name);
    }

    public boolean hasShader(String name) {
        return shaders.containsKey(name);
    }
}
