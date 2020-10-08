package OpenGL;

import GameEngine.GameItem;
import GameEngine.Transformation;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL11.*;

/**
 * A class to draw objects on the screen
 */
public class Renderer {

    /**
     * Field of View in Radians
     */
    private static final float FOV = (float) Math.toRadians(60.0f);

    /**
     * Near cutoff to render on the z axis
     */
    private static final float Z_NEAR = 0.01f;

    /**
     * Far cutoff to render on the z axis
     */
    private static final float Z_FAR = 1000.f;

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

    /**
     * Render a list of items to the window
     * @param window window to display items
     * @param gameItems an array of GameItems, each of which is guaranteed to have some sort of render() method.
     */
    public void render(Window window, GameItem[] gameItems) {
        clear();

        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        // Update projection Matrix
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);

        // Render each gameItem
        for (GameItem gameItem : gameItems) {
            // Set world matrix for this item
            Matrix4f worldMatrix = transformation.getWorldMatrix(
                    gameItem.getPosition(),
                    gameItem.getRotation(),
                    gameItem.getScale());
            //make sure we are using the correct ShaderProgram
            gameItem.shader.bind();

            gameItem.setUniforms(projectionMatrix, worldMatrix);

            // Render the mesh for this game item
            gameItem.getMesh().render();
            gameItem.shader.unbind();
        }

    }

    public void cleanup() {

    }
}