package com.skvr.sk_engine.scenes.sprites;

import com.skvr.sk_engine.enums.AnimationLoopTypes;
import com.skvr.sk_engine.rendering.Window;
import com.skvr.sk_engine.resources.ResourceManager;
import com.skvr.sk_engine.scenes.Scene;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

public class AnimatedSprite2D extends Scene {

    Vector2f size;

    ArrayList<String> textures;

    String shader;

    AnimationLoopTypes loopType;
    float speed;
    int currentFrame = 0;
    float timePassed = 0;
    boolean isPlaying = true;
    boolean movingForwards = true;

    public AnimatedSprite2D(ArrayList<String> textures, String shader, AnimationLoopTypes loopType, float speed) {
        super();

        for (int i = 0; i < textures.size(); i++) {
            if (!ResourceManager.getInstance().hasTexture(textures.get(i)))
                throw new IllegalArgumentException("Texture: " + textures.get(i) + " does not exist!");
        }

        this.textures = textures;

        if (!ResourceManager.getInstance().hasShader(shader))
            throw new IllegalArgumentException("Shader: " + shader + " does not exist!");
        else
            this.shader = shader;

        this.loopType = loopType;
        this.speed = speed;

        size = new Vector2f(ResourceManager.getInstance().getTexture(textures.get(currentFrame)).width, ResourceManager.getInstance().getTexture(textures.get(currentFrame)).height);
    }

    public void playAnimation() {
        isPlaying = true;
    }

    public void stopAnimation() {
        isPlaying = false;
    }

    @Override
    public void update(float delta) {
        timePassed += delta;

        if (timePassed >= speed / 60) {
            if (movingForwards) {
                currentFrame++;
                if (currentFrame >= textures.size()) {
                    switch (loopType) {
                        case REPEAT:
                            currentFrame = 0;
                            break;
                        case SINGLE:
                            currentFrame--;
                            isPlaying = false;
                            break;
                        case PING_PONG:
                            currentFrame--;
                            movingForwards = false;
                            break;
                    }
                }
            } else {
                currentFrame--;
                if (currentFrame <= 0) {
                    currentFrame = 0;
                    movingForwards = true;
                }
            }
            size = new Vector2f(ResourceManager.getInstance().getTexture(textures.get(currentFrame)).width, ResourceManager.getInstance().getTexture(textures.get(currentFrame)).height);
            timePassed = 0;
        }
    }

    @Override
    public void render() {
        if (!isPlaying)
            return;

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
        ResourceManager.getInstance().getTexture(textures.get(currentFrame)).bind(0);
        ResourceManager.getInstance().getShader(shader).setMatrix4f("model", model);
        ResourceManager.getInstance().getShader(shader).setInt("currentTexture", 0);

        glDisable(GL_DEPTH_TEST);

        ResourceManager.getInstance().getMesh("Default Sprite").draw();

        glEnable(GL_DEPTH_TEST);
    }
}