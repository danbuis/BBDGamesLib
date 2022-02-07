package BBDGameLibrary.GameEngine.SampleGame;

import BBDGameLibrary.GameEngine.GameItem2d;
import BBDGameLibrary.GameEngine.MouseInput;
import BBDGameLibrary.Geometry2d.BBDPolygon;
import BBDGameLibrary.OpenGL.Mesh;
import BBDGameLibrary.OpenGL.ShaderProgram;
import BBDGameLibrary.OpenGL.Window;
import org.joml.Matrix4f;

import java.awt.event.MouseAdapter;

public class DummyShape2d extends GameItem2d {
    int rotationType;
    float[] colors;

    public DummyShape2d(Mesh mesh, ShaderProgram shaderProgram, BBDPolygon shape, int layer, boolean shapeInteracts, int rotationType) {
        super(mesh, shaderProgram, shape, layer, shapeInteracts);
        this.rotationType = rotationType;
        //this.colors = colors;
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
    public void update(float interval, MouseInput mouseInput, Window window){
        if(rotationType == 0) {
            this.rotate(1 * interval);
        }else{
            this.rotate(0.5f * interval);
        }
    }

}
