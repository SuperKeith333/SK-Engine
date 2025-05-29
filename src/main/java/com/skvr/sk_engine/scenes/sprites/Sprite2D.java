package com.skvr.sk_engine.scenes.sprites;

import com.skvr.sk_engine.resources.ResourceManager;
import com.skvr.sk_engine.scenes.Scene;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Sprite2D extends Scene {

    String texture, shader;

    Vector2f size;

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

        size = new Vector2f(ResourceManager.getInstance().getTexture(texture).width, ResourceManager.getInstance().getTexture(texture).height);
    }

    @Override
    public void render() {
        Vector2f originalSize = new Vector2f(size);
        size = new Vector2f(size.x * scale.x, size.y * scale.y);

        Matrix4f model = getParentMatrix();
        model = model.translate(new Vector3f(position.x, position.y, 0));

        model = model.translate(new Vector3f(0.5f * size.x, 0.5f * size.y, 0));
        model = model.rotate(rotation);
        model = model.translate(new Vector3f(-0.5f * size.x, -0.5f * size.y, 0));

        model = model.scale(new Vector3f(size, 0));

        size = originalSize;

        ResourceManager.getInstance().getShader(shader).use();
        ResourceManager.getInstance().getTexture(texture).bind(0);
        ResourceManager.getInstance().getShader(shader).setMatrix4f("model", model);
        ResourceManager.getInstance().getShader(shader).setInt("currentTexture", 0);

        ResourceManager.getInstance().getMesh("Default Sprite").draw();
    }
}
