import com.skvr.sk_engine.Application;
import com.skvr.sk_engine.Input;
import com.skvr.sk_engine.enums.AnimationLoopTypes;
import com.skvr.sk_engine.rendering.Camera;
import com.skvr.sk_engine.rendering.Window;
import com.skvr.sk_engine.resources.ResourceManager;
import com.skvr.sk_engine.scenes.sprites.AnimatedSprite2D;
import com.skvr.sk_engine.scenes.sprites.Sprite2D;
import com.skvr.sk_engine.scenes.sprites.Sprite3D;
import org.joml.Vector3f;

import java.util.ArrayList;

import static com.skvr.sk_engine.enums.AnimationLoopTypes.SINGLE;
import static java.lang.Math.toDegrees;
import static java.lang.Math.toRadians;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.system.MemoryUtil.memFree;

public class TestApp extends Application {

    @Override
    public void start() {
        setWindowIcon("/wall.png");

        ResourceManager.getInstance().loadTexture("Face1", "/Face1.png", GL_REPEAT, GL_NEAREST);
        ResourceManager.getInstance().loadTexture("Face2", "/Face2.png", GL_REPEAT, GL_NEAREST);

        ArrayList<String> textures = new ArrayList<>();
        textures.add("Face1");
        textures.add("Face2");

        Sprite2D sprite = new Sprite2D("Face1", "Default Sprite 2D");
        sprite.position.x = 40;
        sprite.position.y = 50;
        sprite.scale.x = 6;
        sprite.scale.y = 6;
        baseScene.addChild(sprite);
    }

    @Override
    public void render() {

    }

    @Override
    public void update(float delta) {
        if (Input.wasKeyJustPressed(GLFW_KEY_W))
            System.out.println("Hit");
    }
}
