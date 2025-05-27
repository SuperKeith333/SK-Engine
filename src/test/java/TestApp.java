import com.skvr.sk_engine.Application;
import com.skvr.sk_engine.rendering.Camera;
import com.skvr.sk_engine.resources.ResourceManager;
import com.skvr.sk_engine.scenes.sprites.Sprite2D;
import com.skvr.sk_engine.scenes.sprites.Sprite3D;

import static java.lang.Math.toDegrees;
import static java.lang.Math.toRadians;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.system.MemoryUtil.memFree;

public class TestApp extends Application {

    @Override
    public void start() {
        ResourceManager.getInstance().loadTexture("Test Texture", "/wall.png", GL_REPEAT, GL_LINEAR);

        Sprite3D sprite = new Sprite3D("Test Texture", "Default Sprite 3D", true);
        Sprite3D sprite2 = new Sprite3D("Test Texture", "Default Sprite 3D", false);
        sprite.position.z = -3;
        sprite2.position.z = -3;
        sprite2.position.x = 2;
        baseScene.addChild(sprite);
        baseScene.addChild(sprite2);
        Camera.getInstance().position.x = 1;
    }

    @Override
    public void render() {

    }

    @Override
    public void update(float delta) {
        Camera.getInstance().rotation.rotateY((float) toRadians(40.0f * delta));
    }
}
