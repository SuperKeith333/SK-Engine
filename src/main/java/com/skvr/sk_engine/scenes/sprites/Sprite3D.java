package com.skvr.sk_engine.scenes.sprites;

import com.skvr.sk_engine.rendering.Camera;
import com.skvr.sk_engine.resources.ResourceManager;
import com.skvr.sk_engine.scenes.Scene;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Sprite3D extends Scene {

    private String texture, shader;
    public boolean alwaysFaceCamera;

    public Sprite3D(String texture, String shader, boolean alwaysFaceCamera) {
        if (!ResourceManager.getInstance().hasTexture(texture))
            throw new IllegalArgumentException("Texture: " + texture + " does not exist!");
        else
            this.texture = texture;

        if (!ResourceManager.getInstance().hasShader(shader))
            throw new IllegalArgumentException("Shader: " + shader + " does not exist!");
        else
            this.shader = shader;

        this.alwaysFaceCamera = alwaysFaceCamera;
    }

    @Override
    public void update(float delta) {
        if (alwaysFaceCamera)
            Camera.getInstance().getViewMatrix().invert().getUnnormalizedRotation(rotation);
    }

    @Override
    public void render() {
        Matrix4f model = new Matrix4f().identity();
        model = model.translate(position);

        model = model.translate(origin);
        model = model.rotate(rotation);
        model = model.translate(origin.negate());

        model = model.scale(scale);

        ResourceManager.getInstance().getShader(shader).use();
        ResourceManager.getInstance().getTexture(texture).bind(0);
        ResourceManager.getInstance().getShader(shader).setMatrix4f("model", model);
        ResourceManager.getInstance().getShader(shader).setInt("currentTexture", 0);

        ResourceManager.getInstance().getMesh("Default Sprite").draw();
    }
}
