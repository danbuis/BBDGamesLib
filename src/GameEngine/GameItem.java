package GameEngine;

import OpenGL.Mesh;
import OpenGL.ShaderProgram;
import OpenGL.Window;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * A class that represents an object in a game.  It uses the GameComponent interface and is instantiated with some basic
 * data such as a mesh and the shaderProgram.  It is able to maintain its internal state for things like translation,
 * rotation and scale, and use that to render correctly on the screen.  This way the data for the mesh is not modified
 * within the mesh object, but is instead essentially copied and modified here.  This allows multiple GameItems to use
 * the same Mesh object instance, but still have them do different things on the screen during render.
 *
 * The intended use of this class us to be used as a base for the end user's game items.  This serves as a decent base,
 * but if you want to do anything with render or upaate for example you will need to overwrite those methods.  See
 * SampleGame.DummyCube for an example.
 */
public class GameItem implements GameComponent{

    /**
     * Mesh object to be used for the GameItem
     */
    private final Mesh mesh;

    /**
     * Data structure to hold the position modification of the object
     */
    private final Vector3f position;

    /**
     * Scale factor of the object
     */
    private float scale;

    /**
     * Data structure to hold the rotation modification of the object
     */
    private final Vector3f rotation;

    /**
     * ShaderProgram to be used to render this object
     */
    public final ShaderProgram shader;

    /**
     * General purpose constructor to create a GameItem.  Instantiates translation, rotation, and scale to neutral
     * values.
     * @param mesh Mesh object to be rendered to the screen
     * @param shaderProgram Shader Program to be used to render this object
     */
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

    /**
     * Set some unifroms for rendering.  This method should be overwritten if you have either
     * additional unifroms, such as textures, OR you don't use "projectionMatrix" and "worldMatrix" as
     * uniform names.
     * @param projectionMatrix projection matrix
     * @param worldMatrix world matrix
     */
    public void setUniforms(Matrix4f projectionMatrix, Matrix4f worldMatrix) {
        this.shader.setUniform("projectionMatrix", projectionMatrix);
        this.shader.setUniform("worldMatrix", worldMatrix);
    }

    /**
     * Empty init function to conform to the GameComponent interface.  Intended to be overridden by classes that extend
     * GameItem.
     * @param window Window object that will be displaying the GameComponent
     * @throws Exception might throw an exception
     */
    @Override
    public void init(Window window) {

    }

    /**
     * Empty input function to conform to the GameComponent interface.  Intended to be overridden by classes that extend
     * GameItem.
     * @param window the Window object this GameComponent is using.
     */
    @Override
    public void input(Window window) {

    }

    /**
     * Empty update function to conform to the GameComponent interface.  Intended to be overridden by classes that extend
     * GameItem.
     * @param interval elapsed time
     */
    @Override
    public void update(float interval) {

    }

    /**
     * Empty render function to conform to the GameComponent interface.  Intended to be overridden by classes that extend
     * GameItem.
     * @param window the Window object this GameComponent is using.
     */
    @Override
    public void render(Window window) {
        
    }

    /**
     * Empty cleanup function to conform to the GameComponent interface.  Intended to be overridden by classes that extend
     * GameItem.
     */
    @Override
    public void cleanup() {

    }
}