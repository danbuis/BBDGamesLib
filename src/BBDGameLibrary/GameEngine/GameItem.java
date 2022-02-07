package BBDGameLibrary.GameEngine;

import BBDGameLibrary.OpenGL.Mesh;
import BBDGameLibrary.OpenGL.ShaderProgram;
import BBDGameLibrary.OpenGL.Window;
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
 * but if you want to do anything with render or update for example you will need to overwrite those methods.  See
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
     * Mesh to be used for anything related to collisions or selections.  Typically a low-poly version of the regular mesh.
     */
    private Mesh collisionMesh;

    private float[] solidColor = null;

    /**
     * General purpose constructor to create a GameItem.  Instantiates translation, rotation, and scale to neutral
     * values.
     * @param mesh Mesh object to be rendered to the screen
     * @param shaderProgram Shader Program to be used to render this object
     */
    public GameItem(Mesh mesh, ShaderProgram shaderProgram) {
        this.mesh = mesh;
        this.shader = shaderProgram;
        this.collisionMesh = mesh;
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

    /**
     * Calculate the distance in the XY plane
     * @param other other GameItem
     * @return distance squared, using the position vector as reference.  Does not take into account the mesh dimensions
     */
    public float distanceSquaredFlat(GameItem other){
        Vector3f thisPosition = this.getPosition();
        Vector3f otherPosition = other.getPosition();

        float deltaX = thisPosition.x - otherPosition.x;
        float deltaY = thisPosition.y - otherPosition.y;

        return deltaX*deltaX + deltaY*deltaY;
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

    public Mesh getCollisionMesh(){
        return this.collisionMesh;
    }

    /**
     * Set the collision mesh to something other than the default.  This lets the user define their own custom collision
     * mesh using whatever logic they want.
     * @param mesh Mesh object to use for a collision mesh
     */
    public void setCollisionMesh(Mesh mesh){
        this.collisionMesh = mesh;
    }

    /**
     * Set some uniforms for rendering.  This method should be overwritten if you have either
     * additional uniforms, such as textures, OR you don't use "projectionMatrix" and "worldMatrix" as
     * uniform names.
     * @param projectionMatrix projection matrix
     * @param modelViewMatrix combination of the camera and world matrices
     */
    public void setUniforms(Matrix4f projectionMatrix, Matrix4f modelViewMatrix) {
        this.shader.setUniform("projectionMatrix", projectionMatrix);
        this.shader.setUniform("worldMatrix", modelViewMatrix);
    }

    public void setOptionalColorUniform(){
        if (this.solidColor != null){
            this.shader.setUniform("solidColor", this.solidColor);
        }
    }

    public void addSolidColorUniform(float red, float green, float blue, float alpha){
        float[] color = {red, green, blue, alpha};
        this.solidColor = color;
    }

    /**
     * Empty init function to conform to the GameComponent interface.  Intended to be overridden by classes that extend
     * GameItem.
     * @param window Window object that will be displaying the GameComponent
     */
    @Override
    public void init(Window window) {

    }

    /**
     * Empty input function to conform to the GameComponent interface.  Intended to be overridden by classes that extend
     * GameItem.
     * @param window the Window object this GameComponent is using.
     * @param mouseInput object to handle the mouse input.
     */
    @Override
    public void input(Window window, MouseInput mouseInput) {

    }

    /**
     * Empty update function to conform to the GameComponent interface.  Intended to be overridden by classes that extend
     * GameItem.
     * @param interval elapsed time
     * @param mouseInput object to handle the mouse input.
     * @param window the window being used to render the game
     */
    @Override
    public void update(float interval, MouseInput mouseInput, Window window) {

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


    /**
     * Query a mesh and get the real current positions of its vertices.  You will likely want this for collision
     * checking and related logic.
     * @return an set of current vertices
     */
    public Vector3f[] getMeshVerticesRealLocations(){
        Vector3f[] meshVertices = this.collisionMesh.getVertexPositions();
        Vector3f[] currentVertices = new Vector3f[meshVertices.length];
        Vector3f origin = new Vector3f();

        for(int i = 0; i< meshVertices.length; i++){
            currentVertices[i] = new Vector3f(meshVertices[i]);
            new Matrix4f().translate(origin)
                    .translate(this.getPosition())
                    .rotateAffineXYZ(this.getRotation().x, this.getRotation().y, this.getRotation().z)
                    .translate(origin.negate())
                    .transformPosition(currentVertices[i]);
        }

        return currentVertices;
    }
}