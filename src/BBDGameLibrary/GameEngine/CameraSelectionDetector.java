package BBDGameLibrary.GameEngine;

import BBDGameLibrary.OpenGL.Mesh;
import org.joml.Intersectionf;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Vector;

/**
 * A class to select items that are directly in front of the center of the camera
 */
public class CameraSelectionDetector {

    private final Vector3f max;

    private final Vector3f min;

    private final Vector2f nearFar;

    private Vector3f dir;

    public CameraSelectionDetector() {
        dir = new Vector3f();
        min = new Vector3f();
        max = new Vector3f();
        nearFar = new Vector2f();
    }

    /**
     * Select the GameItem that is closest to the camera and intersects a ray going straight out from the center.
     * @param itemList  List of GameItems that are eligible for selection
     * @param camera Camera object to be used for selecting objects
     * @return Closest GameItem that is intersected
     */
    public GameItem selectGameItem(ArrayList<GameItem> itemList, Camera camera) {
        dir = camera.getViewMatrix().positiveZ(dir).negate();
        return selectItem(itemList, camera.getPosition(), dir, 0.001f);
    }

    /**
     * Select the GameItem that is closest to the camera and intersects a ray going in the given direction and starting
     * at the given point
     * @param itemList  List of GameItems that are eligible for selection
     * @param origin Origin of the ray to use for intersection
     * @param dir Direction of the ray to use for intersection
     * @param marginOfError How close to parallel to the dir do you want to have count as an intersection
     * @return Closest GameItem that is intersected
     */
    protected GameItem selectItem(ArrayList<GameItem> itemList, Vector3f origin, Vector3f dir, float marginOfError){
        GameItem selectedItem = null;
        float closestDistance = Float.POSITIVE_INFINITY;

        for (GameItem item : itemList){
            Vector3f[] vertexList = item.getMeshVerticesRealLocations();
            int[] indices = item.getMesh().getIndices();
            for(int i=0; i < indices.length; i +=3){
                float distance = Intersectionf.intersectRayTriangleFront(origin, dir,
                        vertexList[indices[i]], vertexList[indices[i+1]],
                        vertexList[indices[i+2]], marginOfError);
                if(distance != -1 && distance < closestDistance){
                    closestDistance = distance;
                    selectedItem = item;
                }
            }
        }
        return selectedItem;
    }
}
