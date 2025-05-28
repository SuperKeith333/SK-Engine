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

        return view.lookAt(new Vector3f(position), new Vector3f(position).add(new Vector3f(front)), up);
    }

    public Matrix4f get2dViewMatrix() {
        return new Matrix4f().identity().translate(-position.x, -position.y, 0);
    }

    public Matrix4f getProjectionMatrix() {
        Matrix4f projection = new Matrix4f();

        return projection.perspective((float) toRadians(fov), (float) FrameBuffer.getInstance().width / (float) FrameBuffer.getInstance().height, 0.1f, 100.0f);
    }

    public Matrix4f getOrthoMatrix() {
        return new Matrix4f().ortho(0, FrameBuffer.getInstance().width, 0, FrameBuffer.getInstance().height, -1, 1);
    }

    private void updateCameraVectors() {
        front = new Vector3f(0, 0, -1).rotate(rotation);
        up = new Vector3f(0, 1, 0).rotate(rotation);
        right = new Vector3f(front).cross(up).normalize();
    }
}
