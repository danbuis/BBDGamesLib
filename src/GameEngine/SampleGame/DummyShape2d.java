package GameEngine.SampleGame;

import GameEngine.GameItem2d;
import Geometry2d.BBDPoint;
import Geometry2d.BBDPolygon;
import OpenGL.Mesh;
import OpenGL.ShaderProgram;
import org.joml.Matrix4f;

public class DummyShape2d extends GameItem2d {
    int rotationType;

    public DummyShape2d(Mesh mesh, ShaderProgram shaderProgram, BBDPolygon shape, int layer, boolean shapeInteracts, int rotationType) {
        super(mesh, shaderProgram, shape, layer, shapeInteracts);
        this.rotationType = rotationType;
    }

    @Override
    public void setUniforms(Matrix4f projectionMatrix, Matrix4f modelViewMatrix) {
        this.shader.setUniform("projectionMatrix", projectionMatrix);
        this.shader.setUniform("modelViewMatrix", modelViewMatrix);
    }

    /**
     * These objects make use of the interval so that no matter how much time passes they will
     * always rotate at 1 radian per second.
     * @param interval elapsed time
     */
    @Override
    public void update(float interval){
        if(rotationType == 0) {
            this.rotate(1 * interval);
        }else{
            this.rotate(0.5f * interval);
        }
    }

}
