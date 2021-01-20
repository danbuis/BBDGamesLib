package BBDGameLibrary.GameEngine;

import BBDGameLibrary.OpenGL.Mesh;
import BBDGameLibrary.OpenGL.Window;
import org.joml.Matrix4f;
import org.joml.Vector2d;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;

public class MouseSelectionDetector extends CameraSelectionDetector {

    private final Matrix4f invProjectionMatrix;

    private final Matrix4f invViewMatrix;

    private final Vector4f tmpVec;

    public MouseSelectionDetector() {
        super();
        invProjectionMatrix = new Matrix4f();
        invViewMatrix = new Matrix4f();
        tmpVec = new Vector4f();
    }

    public Mesh selectMesh(ArrayList<Mesh> meshList, Window window, Vector2d mousePos, Camera camera) {

        Vector3f mouseDir = getMouseDir(window, mousePos, camera);

        return selectMesh(meshList, camera.getPosition(), mouseDir);
    }
    
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
