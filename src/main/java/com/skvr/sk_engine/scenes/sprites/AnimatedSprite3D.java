package com.skvr.sk_engine.scenes.sprites;

import com.skvr.sk_engine.enums.AnimationLoopTypes;
import com.skvr.sk_engine.rendering.Camera;
import com.skvr.sk_engine.resources.ResourceManager;
import com.skvr.sk_engine.scenes.Scene;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import java.util.ArrayList;

import static org.joml.Math.toRadians;

public class AnimatedSprite3D extends Scene {
    ArrayList<String> textures;

    String shader;
    boolean alwaysFaceCamera;

    AnimationLoopTypes loopType;
    float speed;
    int currentFrame = 0;
    float timePassed = 0;
    boolean isPlaying = true;
    boolean movingForwards = true;

    public AnimatedSprite3D(ArrayList<String> textures, String shader, AnimationLoopTypes loopType, float speed, boolean alwaysFaceCamera) {
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

        this.alwaysFaceCamera = alwaysFaceCamera;
    }

    public void playAnimation() {
        isPlaying = true;
    }

    public void stopAnimation() {
        isPlaying = false;
    }

    @Override
    public void update(float delta) {
        if (alwaysFaceCamera)
            Camera.getInstance().getViewMatrix().invert().getUnnormalizedRotation(rotation);

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
            timePassed = 0;
        }
    }

    @Override
    public void render() {
        if (!isPlaying)
            return;

        Matrix4f model = getParentMatrix();
        model = model.translate(position);

        model = model.translate(origin);
        model = model.rotate(rotation);
        model = model.translate(origin.negate());

        model = model.scale(scale);

        ResourceManager.getInstance().getShader(shader).use();
        ResourceManager.getInstance().getTexture(textures.get(currentFrame)).bind(0);
        ResourceManager.getInstance().getShader(shader).setMatrix4f("model", model);
        ResourceManager.getInstance().getShader(shader).setInt("currentTexture", 0);

        ResourceManager.getInstance().getMesh("Default Sprite").draw();
    }
}
