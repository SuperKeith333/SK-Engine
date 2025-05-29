package com.skvr.sk_engine.resources;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BBModel {

    public HashMap<String, String> meshes = new HashMap<>();
    public HashMap<String, Vector3f> meshPositions = new HashMap<>();
    public HashMap<String, Vector3f> meshScale = new HashMap<>();
    public HashMap<String, Vector3f> meshOrigins = new HashMap<>();
    public HashMap<String, Quaternionf> meshRotations = new HashMap<>();

    public BBModel() {

    }

    public void draw(String shader, Matrix4f model) {
        ResourceManager.getInstance().getShader(shader).use();

        for (Map.Entry<String, String> entry : meshes.entrySet()) {
            Matrix4f newModel = new Matrix4f(model);
            newModel.translate(meshOrigins.get(entry.getKey()));
            newModel.rotate(meshRotations.get(entry.getKey()));
            newModel.translate(new Vector3f(meshOrigins.get(entry.getKey())).negate());
            newModel.translate(meshPositions.get(entry.getKey()));
            newModel.scale(meshScale.get(entry.getKey()));

            ResourceManager.getInstance().getShader(shader).setMatrix4f("model", newModel);

            ResourceManager.getInstance().getMesh(entry.getValue()).draw();
        }
    }
}
