package GameEngine;

import openGL.Mesh;
import openGL.ShaderProgram;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class GameItem2d extends GameItem{

    private final Mesh mesh;

    private final Vector3f position;

    private float scale;

    private final Vector3f rotation;
    public final ShaderProgram shader;
    private int layer;
    private final float LAYER_INTERVAL = (float)0.001;

    public GameItem2d(Mesh mesh, ShaderProgram shaderProgram, int layer) {
        super(mesh, shaderProgram);
        this.mesh = mesh;
        this.shader = shaderProgram;
        position = new Vector3f();
        scale = 1;
        rotation = new Vector3f();
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(float x, float y) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = this.layer * LAYER_INTERVAL;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(float z) {
        this.rotation.z = z;
    }

    public Mesh getMesh() {
        return mesh;
    }

    public void setUniforms(Matrix4f projectionMatrix, Matrix4f worldMatrix) {
        this.shader.setUniform("projectionMatrix", projectionMatrix);
        this.shader.setUniform("worldMatrix", worldMatrix);
    }
}