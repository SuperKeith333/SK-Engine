package com.skvr.sk_engine.resources;

import com.skvr.sk_engine.rendering.Mesh;

import java.util.HashMap;

public class ResourceManager {

    private static ResourceManager instance;

    private final HashMap<String, Shader> shaders;
    private final HashMap<String, Mesh> meshes;
    private final HashMap<String, Texture> textures;

    private ResourceManager() {
        shaders = new HashMap<>();
        meshes = new HashMap<>();
        textures = new HashMap<>();
    }

    public static ResourceManager getInstance() {
        if (instance == null) {
            instance = new ResourceManager();
        }
        return instance;
    }

    public void loadTexture(String name, String path, int wrap, int filter) {
        textures.put(name, new Texture(path, wrap, filter));
    }

    public Texture getTexture(String name) {
        return textures.get(name);
    }

    public boolean hasTexture(String name) {
        return textures.containsKey(name);
    }

    public void loadMesh(String name, float[] vertices) {
        meshes.put(name, new Mesh(vertices));
    }

    public void loadMesh(String name, float[] vertices, int[] indices) {
        meshes.put(name, new Mesh(vertices, indices));
    }

    public void loadMesh(String name, float[] vertices, int[] indices, float[] texCoords) {
        meshes.put(name, new Mesh(vertices, indices, texCoords));
    }

    public void loadMesh(String name, float[] vertices, float[] texCoords) {
        meshes.put(name, new Mesh(vertices, texCoords));
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
