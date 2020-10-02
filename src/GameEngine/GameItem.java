package GameEngine;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class GameItem implements GameComponent{

    private final Mesh mesh;

    private final Vector3f position;

    private float scale;

    private final Vector3f rotation;
    public final ShaderProgram shader;

    public GameItem(Mesh mesh, ShaderProgram shaderProgram) {
        this.mesh = mesh;
        this.shader = shaderProgram;
        position = new Vector3f();
        scale = 1;
        rotation = new Vector3f();
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
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

    public void setRotation(float x, float y, float z) {
        this.rotation.x = x;
        this.rotation.y = y;
        this.rotation.z = z;
    }

    public Mesh getMesh() {
        return mesh;
    }

    public void setUniforms(Matrix4f projectionMatrix, Matrix4f worldMatrix) {
        this.shader.setUniform("projectionMatrix", projectionMatrix);
        this.shader.setUniform("worldMatrix", worldMatrix);
    }

    @Override
    public void init(Window window) throws Exception {

    }

    @Override
    public void input(Window window) {

    }

    @Override
    public void update(float interval) {

    }

    @Override
    public void render(Window window) {
        
    }

    @Override
    public void cleanup() {

    }
}