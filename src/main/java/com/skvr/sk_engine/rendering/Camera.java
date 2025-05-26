package com.skvr.sk_engine.rendering;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import static java.lang.Math.*;

public class Camera {

    private static Camera instance;

    public Vector3f position;
    public Quaternionf rotation;

    public Vector3f front;
    public Vector3f up;
    public Vector3f right;

    public float fov = 45.0f;

    private Camera() {
        position = new Vector3f();
        front = new Vector3f();
        up = new Vector3f();
        right = new Vector3f();

        position.z = 3;

        rotation = new Quaternionf();
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

        return view.lookAt(position, position.add(front), up);
    }

    public Matrix4f getProjectionMatrix() {
        Matrix4f projection = new Matrix4f();

        return projection.perspective((float) toRadians(fov), (float) Window.getInstance().getWidth() / (float) Window.getInstance().getHeight(), 0.1f, 100.0f);
    }

    private void updateCameraVectors() {
        Vector3f newFront = new Vector3f();

        newFront.x = (float) (cos(rotation.y) * cos(rotation.x));
        newFront.y = (float) sin(rotation.x);
        newFront.z = (float) (sin(rotation.y) * cos(rotation.x));
        front = newFront.normalize();

        right = front.cross(new Vector3f(0, 1, 0)).normalize();
        up = right.cross(front).normalize();
    }
}
