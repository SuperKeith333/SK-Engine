package com.skvr.sk_engine.scenes.sprites;

import com.skvr.sk_engine.resources.ResourceManager;
import com.skvr.sk_engine.scenes.Scene;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Sprite2D extends Scene {

    String texture, shader;

    public Sprite2D(String texture, String shader) {
        super();

        if (!ResourceManager.getInstance().hasTexture(texture))
            throw new IllegalArgumentException("Texture: " + texture + " does not exist!");
        else
            this.texture = texture;

        if (!ResourceManager.getInstance().hasShader(shader))
            throw new IllegalArgumentException("Shader: " + shader + " does not exist!");
        else
            this.shader = shader;

        scale = new Vector3f(ResourceManager.getInstance().getTexture(texture).width, ResourceManager.getInstance().getTexture(texture).height, 0);
    }

    @Override
    public void render() {
        Matrix4f model = new Matrix4f().identity();
        model = model.translate(new Vector3f(position.x, position.y, 0));

        model = model.translate(new Vector3f(0.5f * scale.x, 0.5f * scale.y, 0));
        model = model.rotate(rotation);
        model = model.translate(new Vector3f(-0.5f * scale.x, -0.5f * scale.y, 0));

        model = model.scale(scale);

        ResourceManager.getInstance().getShader(shader).use();
        ResourceManager.getInstance().getTexture(texture).bind(0);
        ResourceManager.getInstance().getShader(shader).setMatrix4f("model", model);
        ResourceManager.getInstance().getShader(shader).setInt("currentTexture", 0);

        ResourceManager.getInstance().getMesh("Default Sprite").draw();
    }
}
