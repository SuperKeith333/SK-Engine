package com.skvr.sk_engine.scenes;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Scene {

    public Vector3f position;
    public Vector3f origin;
    public Quaternionf rotation;
    public Vector3f scale;

    public ArrayList<Scene> children = new ArrayList<>();
    public Scene parent;

    public Scene() {
        position = new Vector3f();
        origin = new Vector3f();
        rotation = new Quaternionf();
        scale = new Vector3f(1);
    }

    public void beginUpdate(float delta) {
        update(delta);

        for (int i = 0; i < children.size(); i++) {
            children.get(i).beginUpdate(delta);
        }
    }
    public void beginRender() {
        render();

        for (int i = 0; i < children.size(); i++) {
            children.get(i).beginRender();
        }
    }

    public void update(float delta) {

    }
    public void render() {}

    public void addChild(Scene child) {
        child.setParent(this);

        children.add(child);
    }

    public void removeChild(Scene child) {
        children.remove(child);
    }

    public void setParent(Scene parent) {
        this.parent = parent;
    }

    public void destroy() {
        for (int i = 0; i < children.size(); i++) {
            children.get(i).destroy();
        }

        parent.removeChild(this);
        parent = null;
    }

    public Matrix4f getParentMatrix() {
        Matrix4f model = new Matrix4f().identity();
        if (parent != null)
            model = parent.getParentMatrix();

        model.translate(position);
        model.translate(origin);
        model.rotate(rotation);
        model.translate(origin.negate());
        model.scale(scale);

        return model;
    }
}
