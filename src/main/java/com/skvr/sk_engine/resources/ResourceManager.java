package com.skvr.sk_engine.resources;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skvr.sk_engine.rendering.Mesh;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Objects;

import static java.lang.Math.toRadians;

public class ResourceManager {

    private static ResourceManager instance;

    private final HashMap<String, Shader> shaders;
    private final HashMap<String, Mesh> meshes;
    private final HashMap<String, Texture> textures;
    private final HashMap<String, BBModel> bbmodels;

    private ResourceManager() {
        shaders = new HashMap<>();
        meshes = new HashMap<>();
        textures = new HashMap<>();
        bbmodels = new HashMap<>();
    }

    public static ResourceManager getInstance() {
        if (instance == null) {
            instance = new ResourceManager();
        }
        return instance;
    }

    public void loadBBModel(String name, String path) {
        ObjectMapper mapper = new ObjectMapper();
        ClassLoader classLoader = getClass().getClassLoader();

        try (InputStream inputStream = classLoader.getResourceAsStream(path)) {
            if (inputStream == null) {
                throw new RuntimeException("File not found!");
            }

            JsonNode root = mapper.readTree(inputStream);
            JsonNode elements = root.get("elements");

            BBModel model = new BBModel();

            for (JsonNode element : elements) {
                if (Objects.equals(element.get("type").asText(), "cube")) {

                    model.meshes.put(name + "-" + element.get("uuid").asText(), "Default Cube");
                    model.meshPositions.put(name + "-" + element.get("uuid").asText(), new Vector3f((float) element.get("from").get(0).asDouble(), (float) element.get("from").get(1).asDouble(), (float) element.get("from").get(2).asDouble()).add(new Vector3f((float) element.get("to").get(0).asDouble(), (float) element.get("to").get(1).asDouble(), (float) element.get("to").get(2).asDouble())).div(4));
                    model.meshScale.put(name + "-" + element.get("uuid").asText(), new Vector3f((float) element.get("to").get(0).asDouble(), (float) element.get("to").get(1).asDouble(), (float) element.get("to").get(2).asDouble()).sub(new Vector3f((float) element.get("from").get(0).asDouble(), (float) element.get("from").get(1).asDouble(), (float) element.get("from").get(2).asDouble())));
                    model.meshOrigins.put(name + "-" + element.get("uuid").asText(), new Vector3f((float) element.get("origin").get(0).asDouble(), (float) -element.get("origin").get(1).asDouble(), (float) element.get("origin").get(2).asDouble()));
                    if (element.has("rotation"))
                        model.meshRotations.put(name + "-" + element.get("uuid").asText(), new Quaternionf().rotateX((float) toRadians(element.get("rotation").get(0).asDouble())).rotateY((float) toRadians(element.get("rotation").get(1).asDouble())).rotateZ((float) toRadians(element.get("rotation").get(2).asDouble())));
                    else
                        model.meshRotations.put(name + "-" + element.get("uuid").asText(), new Quaternionf());
                }
            }

            bbmodels.put(name, model);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BBModel getBBModel(String name) {
        return bbmodels.get(name);
    }

    public boolean hasBBModel(String name) {
        return bbmodels.containsKey(name);
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
