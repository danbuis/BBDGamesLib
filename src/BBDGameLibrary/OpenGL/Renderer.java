package BBDGameLibrary.OpenGL;

import BBDGameLibrary.GameEngine.Camera;
import BBDGameLibrary.GameEngine.GameItem;
import BBDGameLibrary.GameEngine.Transformation;
import org.joml.Matrix4f;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

/**
 * A class to draw objects on the screen
 */
public class Renderer {

    /**
     * Field of View in Radians
     */
    private static float FOV = (float) Math.toRadians(60.0f);

    /**
     * Near cutoff to render on the z axis
     */
    private static float Z_NEAR = 0.01f;

    /**
     * Far cutoff to render on the z axis
     */
    private static float Z_FAR = 1000.f;

    /**
     * Object ot user for transforming with the world and projection matrices
     */
    private final Transformation transformation;


    /**
     * Constructor
     */
    public Renderer() {
        transformation = new Transformation();
    }


    /**
     * Clear the renderer
     */
    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void resetRenderer(Window window){
        clear();

        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }
    }

    /**
     * Render a list of items to the window
     * @param window window to display items
     * @param gameItems an array of GameItems, each of which is guaranteed to have some sort of render() method.
     */
    public void renderArray(Window window, GameItem[] gameItems, Camera camera) {
        // Update projection Matrix
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);

        // Render each gameItem
        for (GameItem gameItem : gameItems) {
            render(gameItem, projectionMatrix, camera);
        }
    }

    public void renderList(Window window, List<GameItem> gameItems, Camera camera){
        // Update projection Matrix
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);

        // Render each gameItem
        for (GameItem gameItem : gameItems) {
            render(gameItem, projectionMatrix, camera);
        }
    }

    public void renderItem(Window window, GameItem item, Camera camera){
        // Update projection Matrix
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        render(item, projectionMatrix, camera);
    }

    private void render(GameItem item, Matrix4f projectionMatrix, Camera camera){
        // Update view Matrix
        Matrix4f viewMatrix = transformation.getViewMatrix(camera);

        // Set world matrix for this item
        //Matrix4f worldMatrix = transformation.getWorldMatrix(
        //        item.getPosition(),
        //        item.getRotation(),
        //        item.getScale());
        //make sure we are using the correct ShaderProgram

        Matrix4f modelViewMatrix = transformation.getModelViewMatrix(item, viewMatrix);

        item.shader.bind();

        item.setUniforms(projectionMatrix, modelViewMatrix);

        // Render the mesh for this game item
        item.getMesh().render();
        item.shader.unbind();
    }

    public void cleanup() {

    }

    /**
     * Get the current field of view
     * @return current FOV
     */
    public float getFOV(){
        return this.FOV;
    }

    /**
     * Set a new field of view
     * @param newFOV
     */
    public void setFOV(float newFOV){
        this.FOV = newFOV;
    }

    /**
     * Get the current near clipping distance
     * @return current Z_NEAR
     */
    public float getZNear(){
        return this.Z_NEAR;
    }

    /**
     * Set the near clipping distance
     * @param newZNear
     */
    public void setZNear(float newZNear){
        this.Z_NEAR = newZNear;
    }

    /**
     * Get the current near clipping distance
     * @return current Z_FAR
     */
    public float getZFar(){
        return this.Z_FAR;
    }

    /**
     * Set the far clipping distance
     * @param newZFar
     */
    public void setZFar(float newZFar){
        this.Z_FAR = newZFar;
    }
}