package BBDGameLibrary.GameEngine;

import BBDGameLibrary.OpenGL.Mesh;
import BBDGameLibrary.OpenGL.Window;
import org.joml.*;

import java.util.ArrayList;

/**
 * A class to select items using the mouse
 */
public class MouseInputHandler extends CameraSelectionDetector {

    private final Matrix4f invProjectionMatrix;

    private final Matrix4f invViewMatrix;

    private final Vector4f tmpVec;

    public MouseInputHandler() {
        super();
        invProjectionMatrix = new Matrix4f();
        invViewMatrix = new Matrix4f();
        tmpVec = new Vector4f();
    }

    /**
     * Select an item using the mouse.
     * @param itemList List of GameItems that are eligible for selection
     * @param window Window object being used for rendering
     * @param mousePos Position of the mouse curser
     * @param camera Camera object to be used for selecting objects
     * @param marginOfError How close to parallel to the dir do you want to have count as an intersection
     * @return Closest GameItem that is intersected
     */
    public GameItem selectItemByMouse(ArrayList<GameItem> itemList, Window window, Vector2d mousePos, Camera camera, float marginOfError) {

        Vector3f mouseDir = getMouseDir(window, mousePos, camera);

        return selectItem(itemList, camera.getPosition(), mouseDir, marginOfError);
    }

    /**
     * Find out where the click location corresponds to on the given  plane.  Intended for getting coordinates in
     * top down type use cases.
     * @param camera camera object being used for rendering and such right now
     * @param mouseDir vector of the mouse click relative to the camera
     * @param intersectionPlane Z-plane we are calculating the intersection with
     * @return
     */
    public Vector2d mouseLocationOnPlane(Camera camera, Vector3f mouseDir, float intersectionPlane){
        float deltaZ = camera.getPosition().z - intersectionPlane;

        return new Vector2d(deltaZ * mouseDir.x, deltaZ * mouseDir.y);
    }

    /**
     * Get the ray projected from the given Camera object, through the mouse curser, and out to infinity.
     * The Z component relative to the camera is always 1, and the other components reflect the real distance
     * traveled for each Z component.
     * @param window Window object being used for rendering
     * @param mousePos Position of the mouse curser
     * @param camera Camera object to be used for selecting objects
     * @return Direction of the ray from the Camera location through the mouse curser
     */
    public Vector3f getMouseDir(Window window, Vector2d mousePos, Camera camera){
        // Transform mouse coordinates into normalized space [-1, 1]
        int wdwWidth = window.getWidth();
        int wdwHeight = window.getHeight();

        float x = (float)(2 * mousePos.x) / (float)wdwWidth - 1.0f;
        float y = 1.0f - (float)(2 * mousePos.y) / (float)wdwHeight;
        float z = -1.0f;

        invProjectionMatrix.set(window.getProjectionMatrix());
        invProjectionMatrix.invert();

        tmpVec.set(x, y, z, 1.0f);
        tmpVec.mul(invProjectionMatrix);
        tmpVec.z = -1.0f;
        tmpVec.w = 0.0f;

        Matrix4f viewMatrix = camera.getViewMatrix();
        invViewMatrix.set(viewMatrix);
        invViewMatrix.invert();
        tmpVec.mul(invViewMatrix);

        return new Vector3f(tmpVec.x, tmpVec.y, tmpVec.z);
    }
}
