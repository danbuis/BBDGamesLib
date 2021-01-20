package BBDGameLibrary.GameEngine;

import BBDGameLibrary.OpenGL.Mesh;
import org.joml.Intersectionf;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;

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

    public GameItem selectGameItem(GameItem[] gameItems, Camera camera) {
        dir = camera.getViewMatrix().positiveZ(dir).negate();
        return selectGameItem(gameItems, camera.getPosition(), dir);
    }

    protected GameItem selectItem(ArrayList<GameItem> itemList, Vector3f center, Vector3f dir, float marginOfError){
        GameItem selectedItem = null;
        float closestDistance = Float.POSITIVE_INFINITY;

        for (GameItem item : itemList){
            Vector3f[] vertexList = item.getMeshVerticesRealLocations();
            int[] indices = item.getMesh().getIndices();
            for(int i=0; i < indices.length; i +=3){
                int startingIndex = i * 3;
                float distance = Intersectionf.intersectRayTriangleFront(center, dir,
                        vertexList[indices[startingIndex]], vertexList[indices[startingIndex+1]],
                        vertexList[indices[startingIndex+2]], marginOfError);
                if(distance < closestDistance){
                    closestDistance = distance;
                    selectedItem = item;
                }
            }
        }

        return selectedItem;
    }

    protected GameItem selectGameItem(GameItem[] gameItems, Vector3f center, Vector3f dir) {
        GameItem selectedGameItem = null;
        float closestDistance = Float.POSITIVE_INFINITY;

        System.out.println("Start of select");
        for (GameItem gameItem : gameItems) {
            min.set(gameItem.getPosition());
            max.set(gameItem.getPosition());
            min.add(-gameItem.getScale(), -gameItem.getScale(), -gameItem.getScale());
            max.add(gameItem.getScale(), gameItem.getScale(), gameItem.getScale());
            System.out.println("Checking : "+gameItem);
            System.out.println("min : "+min);
            System.out.println("max : "+max);
            System.out.println("nearFar : "+nearFar);
            if (Intersectionf.intersectRayAab(center, dir, min, max, nearFar) && nearFar.x < closestDistance) {
                System.out.println("Intersected : "+gameItem);
                closestDistance = nearFar.x;
                selectedGameItem = gameItem;
            }
        }

        return selectedGameItem;
    }
}
