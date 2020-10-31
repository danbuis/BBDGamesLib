package GameEngine;

import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Class to handle matrix math for transformations incorporating the world and projection matrices
 */
public class Transformation {

    /**
     * Matrix for how perspective will alter the base vertices
     */
    private final Matrix4f projectionMatrix;

    /**
     * matrix representing the location in real space for an object
     */
    private final Matrix4f modelViewMatrix;

    /**
     * matrix representing the location and orientation with which to view the scene
     */
    private final Matrix4f viewMatrix;

    /**
     * Basic constructor that build some basic matrices.
     */
    public Transformation() {
        modelViewMatrix = new Matrix4f();
        projectionMatrix = new Matrix4f();
        viewMatrix = new Matrix4f();
    }

    /**
     * Create a projection matrix from several parameters
     * @param fov field of view in radians
     * @param width width of window
     * @param height height of window
     * @param zNear near z cutoff
     * @param zFar far z cutoff
     * @return matrix used for projection calculations
     */
    public final Matrix4f getProjectionMatrix(float fov, float width, float height, float zNear, float zFar) {
        return projectionMatrix.setPerspective(fov, width / height, zNear, zFar);
    }

    /**
     * get the matrix used to move a vertex based on its translation, rotation and scale.  We use this so that the
     * original data is clean and untouched
     * @param gameItem item to work with
     * @param viewMatrix the matrix for the camera's location
     * @return the matrix combining the view and model matrices
     */
    public Matrix4f getModelViewMatrix(GameItem gameItem, Matrix4f viewMatrix) {
        Vector3f rotation = gameItem.getRotation();
        modelViewMatrix.set(viewMatrix).translate(gameItem.getPosition()).
                rotateX(-rotation.x).
                rotateY(-rotation.y).
                rotateZ(-rotation.z).
                scale(gameItem.getScale());
        return modelViewMatrix;
    }

    /**
     * Get a matrix representing the camera position in the world.
     * @param camera
     * @return
     */
    public Matrix4f getViewMatrix(Camera camera) {
        Vector3f cameraPos = camera.getPosition();
        Vector3f rotation = camera.getRotation();

        viewMatrix.identity();
        // First do the rotation so camera rotates over its position
        viewMatrix.rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0, 0))
                .rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1, 0));
        // Then do the translation
        viewMatrix.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        return viewMatrix;
        }
    }
