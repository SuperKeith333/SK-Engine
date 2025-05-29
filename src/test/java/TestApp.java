import com.skvr.sk_engine.Application;
import com.skvr.sk_engine.Input;
import com.skvr.sk_engine.enums.AnimationLoopTypes;
import com.skvr.sk_engine.rendering.Camera;
import com.skvr.sk_engine.rendering.Window;
import com.skvr.sk_engine.resources.ResourceManager;
import com.skvr.sk_engine.scenes.sprites.AnimatedSprite2D;
import com.skvr.sk_engine.scenes.sprites.Sprite2D;
import com.skvr.sk_engine.scenes.sprites.Sprite3D;
import imgui.ImGui;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
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

    Matrix4f model = new Matrix4f().identity();
    Quaternionf rotation = new Quaternionf();

    @Override
    public void start() {
        setWindowIcon("/wall.png");

        ResourceManager.getInstance().loadBBModel("Test", "Test.bbmodel");
        Camera.getInstance().position.z = 20;
    }

    @Override
    public void render() {
        model = new Matrix4f().identity();
        model.rotate(rotation);

        ResourceManager.getInstance().getBBModel("Test").draw("Default Shader 3D", model);
    }

    @Override
    public void imgui() {

    }

    @Override
    public void update(float delta) {
        if (Input.isKeyPressed(GLFW_KEY_A))
            rotation.rotateY((float) (toRadians(40) * delta));
        if (Input.isKeyPressed(GLFW_KEY_D))
            rotation.rotateY((float) (toRadians(-40) * delta));
        if (Input.isKeyPressed(GLFW_KEY_W))
            rotation.rotateX((float) (toRadians(40) * delta));
        if (Input.isKeyPressed(GLFW_KEY_S))
            rotation.rotateX((float) (toRadians(-40) * delta));
    }
}
