package TestsGameEngine;

import BBDGameLibrary.GameEngine.Camera;
import BBDGameLibrary.GameEngine.GameItem;
import BBDGameLibrary.GameEngine.MouseSelectionDetector;
import BBDGameLibrary.Geometry2d.BBDPoint;
import BBDGameLibrary.Geometry2d.BBDPolygon;
import BBDGameLibrary.OpenGL.Mesh;
import BBDGameLibrary.OpenGL.Window;
import BBDGameLibrary.TestUtils;
import org.joml.Vector2d;
import org.joml.Vector3f;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class TestSelection {

    Window window = null;
    boolean windowInit = false;
    Camera camera = new Camera();
    MouseSelectionDetector detector = new MouseSelectionDetector();

    public void initWindow(){
        window = new Window("test", 200, 200, true, new Window.WindowOptions());
        window.init();
    }

    public GameItem makeItem(){
        BBDPolygon poly = TestUtils.buildSquare();
        Mesh mesh = Mesh.buildMeshFromPolygon(poly, null);

        return new GameItem(mesh, null);
    }



    @Test
    public void testSingleObjectSelection(){
        if(!windowInit){
            initWindow();
            camera.setPosition(0,0,1);
        }
        GameItem item = makeItem();
        ArrayList<GameItem> itemList= new ArrayList<>();
        itemList.add(item);

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
            camera.setPosition(0,0,1);
        }
        GameItem item = makeItem();
        ArrayList<GameItem> itemList= new ArrayList<>();
        itemList.add(item);

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
            camera.setPosition(0,0,1);
        }
        GameItem item = makeItem();
        GameItem item2 = makeItem();
        ArrayList<GameItem> itemList= new ArrayList<>();
        itemList.add(item);
        itemList.add(item2);
        item2.setPosition(2,0,0);

        GameItem selectedItem = null;
        selectedItem = detector.selectItem(itemList, window, new Vector2d(100,100), camera, 0.00001f);
        assertEquals(item, selectedItem);
    }

    //test stacked items
    @Test
    public void testStacked(){
        if(!windowInit){
            initWindow();
            camera.setPosition(0,0,1);
        }
        GameItem item = makeItem();
        GameItem item2 = makeItem();
        ArrayList<GameItem> itemList= new ArrayList<>();
        itemList.add(item);
        itemList.add(item2);
        item2.setPosition(0,0,-1);

        GameItem selectedItem = null;
        selectedItem = detector.selectItem(itemList, window, new Vector2d(100,100), camera, 0.00001f);
        assertEquals(item, selectedItem);

        item.setPosition(400,400,0);
        selectedItem = detector.selectItem(itemList, window, new Vector2d(100,100), camera, 0.00001f);
        assertEquals(item2, selectedItem);
    }

}
