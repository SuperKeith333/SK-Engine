package com.skvr.sk_engine.rendering;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {

    private static Camera instance;

    public Vector3f positon;
    public Vector3f Front;
    public Vector3f Up;
    public Vector3f Right;
    public Vector3f rotation;

    private Camera() {
        positon = new Vector3f();
        rotation = new Vector3f();
    }

    public static Camera getInstance() {
        if (instance == null) {
            instance = new Camera();
        }
        return instance;
    }

    public Matrix4f getViewMatrix() {
        Matrix4f view = new Matrix4f();
        updateCameraVectors();

        return view.lookAt(positon, positon.add(Front), Up);
    }

    public Matrix4f getProjectionMatrix3D(int windowWidth, int windowHeight) {
        Matrix4f projection = new Matrix4f();

        return projection.perspective((float) Math.toRadians(45.0f), (float)windowWidth / (float)windowHeight, 0.1f, 100.0f);
    }

    public Matrix4f getProjectionMatrix2D(int windowWidth, int windowHeight) {
        Matrix4f projection = new Matrix4f();

        return projection.ortho(0.0f, (float)windowWidth, (float)windowHeight, 0.0f, -1.0f, 1.0f);
    }

    private void updateCameraVectors() {
        Vector3f front = new Vector3f();

        front.x = (float) (Math.cos(Math.toRadians(rotation.y)) * Math.cos(Math.toRadians(rotation.x)));
        front.y = (float) Math.sin(Math.toRadians(rotation.x));
        front.z = (float) (Math.sin(Math.toRadians(rotation.y)) * Math.cos(Math.toRadians(rotation.x)));
        Front = front.normalize();

        Right = Front.cross(new Vector3f(0, 1, 0));
        Up = Right.cross(Front);
    }
}

