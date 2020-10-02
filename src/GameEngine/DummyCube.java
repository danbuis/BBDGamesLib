package GameEngine;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class DummyCube extends GameItem{
    private int displxInc = 0;

    private int displyInc = 0;

    private int displzInc = 0;

    private int scaleInc = 0;

    public DummyCube(Mesh mesh, ShaderProgram shaderProgram) {
        super(mesh, shaderProgram);
    }

    @Override
    public void setUniforms(Matrix4f projectionMatrix, Matrix4f worldMatrix) {
        this.shader.setUniform("projectionMatrix", projectionMatrix);
        this.shader.setUniform("worldMatrix", worldMatrix);
        this.shader.setUniform("texture_sampler", 0);
    }

    @Override
    public void input(Window window){
        displyInc = 0;
        displxInc = 0;
        displzInc = 0;
        scaleInc = 0;
        if (window.isKeyPressed(GLFW_KEY_UP)) {
            displyInc = 3;
        } else if (window.isKeyPressed(GLFW_KEY_DOWN)) {
            displyInc = -3;
        } else if (window.isKeyPressed(GLFW_KEY_LEFT)) {
            displxInc = -3;
        } else if (window.isKeyPressed(GLFW_KEY_RIGHT)) {
            displxInc = 3;
        } else if (window.isKeyPressed(GLFW_KEY_A)) {
            displzInc = -3;
        } else if (window.isKeyPressed(GLFW_KEY_Q)) {
            displzInc = 3;
        } else if (window.isKeyPressed(GLFW_KEY_Z)) {
            scaleInc = -1;
        } else if (window.isKeyPressed(GLFW_KEY_X)) {
            scaleInc = 1;
        }
    }

    @Override
    public void update(float interval){
        // Update position
        Vector3f itemPos = this.getPosition();
        float posx = itemPos.x + displxInc * 0.01f;
        float posy = itemPos.y + displyInc * 0.01f;
        float posz = itemPos.z + displzInc * 0.01f;
        this.setPosition(posx, posy, posz);

        // Update scale
        float scale = this.getScale();
        scale += scaleInc * 0.05f;
        if (scale < 0) {
            scale = 0;
        }
        this.setScale(scale);

        // Update rotation angle
        float rotation = this.getRotation().x + 1.5f;
        if (rotation > 360) {
            rotation = 0;
        }
        this.setRotation(rotation, rotation, rotation);
    }
}
