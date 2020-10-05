package GameEngine.SampleGame;

import GameEngine.GameItem2d;
import Geometry2d.BBDPolygon;
import openGL.Mesh;
import openGL.ShaderProgram;
import org.joml.Matrix4f;

public class DummyRect extends GameItem2d {
    public DummyRect(Mesh mesh, ShaderProgram shaderProgram, BBDPolygon shape, int layer, boolean shapeInteracts) {
        super(mesh, shaderProgram, shape, layer, shapeInteracts);
    }

    @Override
    public void setUniforms(Matrix4f projectionMatrix, Matrix4f worldMatrix) {
        this.shader.setUniform("projectionMatrix", projectionMatrix);
        this.shader.setUniform("worldMatrix", worldMatrix);
        this.shader.setUniform("texture_sampler", 0);
    }
}
