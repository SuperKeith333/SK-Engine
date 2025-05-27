import com.skvr.sk_engine.Application;
import com.skvr.sk_engine.rendering.Camera;
import com.skvr.sk_engine.rendering.Shader;
import com.skvr.sk_engine.resources.ResourceManager;
import org.joml.Matrix4f;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.system.MemoryUtil.memAllocFloat;
import static org.lwjgl.system.MemoryUtil.memFree;

public class TestApp extends Application {

    @Override
    public void start() {
        float[] vertices = {
                -0.5f, -0.5f, 0.0f,
                -0.5f, 0.5f, 0.0f,
                0.5f, 0.5f, 0.0f,
                0.5f, -0.5f, 0.0f,
        };

        int[] indices = {
                0, 1, 2,
                0, 3, 2
        };

        ResourceManager.getInstance().loadMesh("Test Mesh", vertices, indices);
        ResourceManager.getInstance().loadShader("Test Shader", "/shaders/test.vert", "/shaders/test.frag");
    }

    @Override
    public void render() {
        ResourceManager.getInstance().getShader("Test Shader").use();
        ResourceManager.getInstance().getShader("Test Shader").setMatrix4f("model", new Matrix4f().identity());
        ResourceManager.getInstance().getShader("Test Shader").setMatrix4f("view", Camera.getInstance().getViewMatrix());
        ResourceManager.getInstance().getShader("Test Shader").setMatrix4f("projection", Camera.getInstance().getProjectionMatrix());

        ResourceManager.getInstance().getMesh("Test Mesh").draw();
    }
}
