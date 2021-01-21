package TestsGameEngine;

import BBDGameLibrary.GameEngine.Camera;
import BBDGameLibrary.GameEngine.GameItem;
import BBDGameLibrary.GameEngine.MouseSelectionDetector;
import BBDGameLibrary.Geometry2d.BBDPoint;
import BBDGameLibrary.Geometry2d.BBDPolygon;
import BBDGameLibrary.OpenGL.Mesh;
import BBDGameLibrary.OpenGL.Window;
import org.joml.Vector2d;
import org.joml.Vector3f;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class TestSelection {

    Window window = null;
    boolean windowInit = false;
    public void initWindow(){
        window = new Window("test", 200, 200, true, new Window.WindowOptions());
        window.init();
    }

    public BBDPolygon buildSquare(){
        BBDPoint point1 = new BBDPoint(1,1);
        BBDPoint point2 = new BBDPoint(1,-1);
        BBDPoint point3 = new BBDPoint(-1,-1);
        BBDPoint point4 = new BBDPoint(-1,1);

        ArrayList<BBDPoint> points = new ArrayList<>(Arrays.asList(point1, point2, point3, point4));

        return new BBDPolygon(points);
    }

    @Test
    public void testSingleObjectSelection(){
        if(!windowInit){
            initWindow();
        }
        BBDPolygon poly = this.buildSquare();
        Mesh mesh = Mesh.buildMeshFromPolygon(poly, null);

        GameItem item = new GameItem(mesh, null);
        ArrayList<GameItem> itemList= new ArrayList<>();
        itemList.add(item);
        Camera camera = new Camera();
        camera.setPosition(0,0,1);
        MouseSelectionDetector detector = new MouseSelectionDetector();
        GameItem selectedItem = null;

        selectedItem = detector.selectItem(itemList, window, new Vector2d(100,100), camera, 0.00001f);

        assertEquals(item, selectedItem);

        item.setPosition(400,400,0);
        selectedItem = detector.selectItem(itemList, window, new Vector2d(100,100), camera, 0.00001f);
        assertNull(selectedItem);

        item.setPosition(0,0,0);
        selectedItem = detector.selectItem(itemList, window, new Vector2d(100,100), camera, 0.00001f);
        assertEquals(item, selectedItem);
    }

    //test a rotated item where we check the old corner, but now there isn't anything there.
    @Test
    public void testRotatedItem(){
        if(!windowInit){
            initWindow();
        }
        BBDPolygon poly = this.buildSquare();
        Mesh mesh = Mesh.buildMeshFromPolygon(poly, null);

        GameItem item = new GameItem(mesh, null);
        ArrayList<GameItem> itemList= new ArrayList<>();
        itemList.add(item);
        Camera camera = new Camera();
        camera.setPosition(0,0,1);
        MouseSelectionDetector detector = new MouseSelectionDetector();
        GameItem selectedItem = null;

        item.setPosition(0.95f, 0.95f, 0);
        System.out.println(item.getMeshVerticesRealLocations()[0]);
        System.out.println(item.getMeshVerticesRealLocations()[1]);
        System.out.println(item.getMeshVerticesRealLocations()[2]);
        System.out.println(item.getMeshVerticesRealLocations()[3]);
        selectedItem = detector.selectItem(itemList, window, new Vector2d(100,100), camera, 0);
        assertEquals(item, selectedItem);

        item.setRotation(0,0, (float)Math.PI/4);
        selectedItem = detector.selectItem(itemList, window, new Vector2d(100,100), camera, 0.00001f);
        assertNull(selectedItem);
    }

    //test nearby item
    @Test
    public void nearbyTest(){
        if(!windowInit){
            initWindow();
        }
        BBDPolygon poly = this.buildSquare();
        Mesh mesh = Mesh.buildMeshFromPolygon(poly, null);

        GameItem item = new GameItem(mesh, null);
        GameItem item2 = new GameItem(mesh, null);
        ArrayList<GameItem> itemList= new ArrayList<>();
        itemList.add(item);
        itemList.add(item2);
        Camera camera = new Camera();
        camera.setPosition(0,0,1);
        item2.setPosition(2,0,0);
        MouseSelectionDetector detector = new MouseSelectionDetector();
        GameItem selectedItem = null;
        selectedItem = detector.selectItem(itemList, window, new Vector2d(100,100), camera, 0.00001f);
        assertEquals(item, selectedItem);
    }

    //test stacked items
    @Test
    public void testStacked(){
        if(!windowInit){
            initWindow();
        }
        BBDPolygon poly = this.buildSquare();
        Mesh mesh = Mesh.buildMeshFromPolygon(poly, null);

        GameItem item = new GameItem(mesh, null);
        GameItem item2 = new GameItem(mesh, null);
        ArrayList<GameItem> itemList= new ArrayList<>();
        itemList.add(item);
        itemList.add(item2);
        Camera camera = new Camera();
        camera.setPosition(0,0,1);
        item2.setPosition(0,0,-1);

        MouseSelectionDetector detector = new MouseSelectionDetector();
        GameItem selectedItem = null;
        selectedItem = detector.selectItem(itemList, window, new Vector2d(100,100), camera, 0.00001f);
        assertEquals(item, selectedItem);

        item.setPosition(400,400,0);
        selectedItem = detector.selectItem(itemList, window, new Vector2d(100,100), camera, 0.00001f);
        assertEquals(item2, selectedItem);
    }

}
