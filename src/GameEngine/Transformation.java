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
    private final Matrix4f worldMatrix;

    /**
     * Basic constructor that build some basic matrices.
     */
    public Transformation() {
        worldMatrix = new Matrix4f();
        projectionMatrix = new Matrix4f();
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
     * @param offset how far is the object translated
     * @param rotation how much has it been rotated
     * @param scale how much has it been scaled
     * @return the matrix to go from local coordinates to world coordinates
     */
    public Matrix4f getWorldMatrix(Vector3f offset, Vector3f rotation, float scale) {
        return worldMatrix.translation(offset).
                rotateX((float)Math.toRadians(rotation.x)).
                rotateY((float)Math.toRadians(rotation.y)).
                rotateZ((float)Math.toRadians(rotation.z)).
                scale(scale);
    }
}
