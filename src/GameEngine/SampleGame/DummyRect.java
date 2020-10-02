package GameEngine.SampleGame;

import GameEngine.GameItem2d;
import openGL.Mesh;
import openGL.ShaderProgram;
import org.joml.Matrix4f;

public class DummyRect extends GameItem2d {
    public DummyRect(Mesh mesh, ShaderProgram shaderProgram, int layer) {
        super(mesh, shaderProgram, layer);
    }

    @Override
    public void setUniforms(Matrix4f projectionMatrix, Matrix4f worldMatrix) {
        this.shader.setUniform("projectionMatrix", projectionMatrix);
        this.shader.setUniform("worldMatrix", worldMatrix);
        this.shader.setUniform("texture_sampler", 0);
    }
}
