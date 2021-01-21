package TestsGameEngine;

import BBDGameLibrary.GameEngine.GameItem2d;
import BBDGameLibrary.Geometry2d.BBDGeometryUtils;
import BBDGameLibrary.Geometry2d.BBDPoint;
import BBDGameLibrary.Geometry2d.BBDPolygon;
import BBDGameLibrary.OpenGL.Mesh;
import BBDGameLibrary.OpenGL.Window;
import BBDGameLibrary.TestUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class TestGameItem2d {
    Window window = null;
    boolean windowInit = false;
    public void initWindow(){
        window = new Window("test", 5, 5, true, new Window.WindowOptions());
        window.init();
    }

    public BBDPolygon buildSquareOffCenter(){
        BBDPoint point1 = new BBDPoint(5,5);
        BBDPoint point2 = new BBDPoint(5,3);
        BBDPoint point3 = new BBDPoint(3,3);
        BBDPoint point4 = new BBDPoint(3,5);

        ArrayList<BBDPoint> points = new ArrayList<>(Arrays.asList(point1, point2, point3, point4));

        return new BBDPolygon(points);
    }

    @Test
    public void testGameItemTranslate(){
        if(!windowInit){
            initWindow();
        }
        BBDPolygon poly = TestUtils.buildSquare();
        Mesh mesh = Mesh.buildMeshFromPolygon(poly, null);

        GameItem2d item = new GameItem2d(mesh, null, poly, 3000, true);
        item.translate(4,4);

        assertEquals(new BBDPoint(4,4), poly.center());
        assertEquals(4.0, item.getPosition().x, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(4.0, item.getPosition().y, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(-3.0, item.getPosition().z, BBDGeometryUtils.ALLOWABLE_DELTA);
    }

    @Test
    public void testSetPosition(){
        if(!windowInit){
            initWindow();
        }

        BBDPolygon poly = TestUtils.buildSquare();
        Mesh mesh = Mesh.buildMeshFromPolygon(poly, null);

        GameItem2d item = new GameItem2d(mesh, null, poly, 3000, true);
        item.setPosition(12,12);

        assertEquals(13, poly.getPoints().get(0).getXLoc(), BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(13, poly.getPoints().get(0).getYLoc(), BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(12, item.getPosition().x, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(12, item.getPosition().y, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(-3.0, item.getPosition().z, BBDGeometryUtils.ALLOWABLE_DELTA);

        item.setPosition(0,0);

        assertEquals(1, poly.getPoints().get(0).getXLoc(), BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(1, poly.getPoints().get(0).getYLoc(), BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(0, item.getPosition().x, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(0, item.getPosition().y, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(-3.0, item.getPosition().z, BBDGeometryUtils.ALLOWABLE_DELTA);

        //test the "else" logic flow
        GameItem2d otherItem = new GameItem2d(mesh, null, poly, 3000, false);
        otherItem.setPosition(12,12);

        assertEquals(1, poly.getPoints().get(0).getXLoc(), BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(1, poly.getPoints().get(0).getYLoc(), BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(12, otherItem.getPosition().x, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(12, otherItem.getPosition().y, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(-3.0, otherItem.getPosition().z, BBDGeometryUtils.ALLOWABLE_DELTA);
    }

    @Test
    public void testSetDepth(){
        if(!windowInit){
            initWindow();
        }

        BBDPolygon poly = TestUtils.buildSquare();
        Mesh mesh = Mesh.buildMeshFromPolygon(poly, null);

        GameItem2d item = new GameItem2d(mesh, null, poly, 3000, true);
        assertEquals(-3.0, item.getPosition().z, BBDGeometryUtils.ALLOWABLE_DELTA);
        item.setLayer(5000);
        assertEquals(-5.0, item.getPosition().z, BBDGeometryUtils.ALLOWABLE_DELTA);
    }

    @Test
    public void testOffCenterInitialize(){
        if(!windowInit){
            initWindow();
        }

        BBDPolygon poly = this.buildSquareOffCenter();
        Mesh mesh = Mesh.buildMeshFromPolygon(poly, null);

        GameItem2d item = new GameItem2d(mesh, null, poly, 3500, true);
        assertEquals(5, poly.getPoints().get(0).getXLoc(), BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(5, poly.getPoints().get(0).getYLoc(), BBDGeometryUtils.ALLOWABLE_DELTA);
        //should still be 0 because the square hasn't moved yet.  The origin of local space is still the same.
        assertEquals(0, item.getPosition().x, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(0, item.getPosition().y, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(-3.5, item.getPosition().z, BBDGeometryUtils.ALLOWABLE_DELTA);
    }

    @Test
    public void testRotate(){
        if(!windowInit){
            initWindow();
        }

        BBDPolygon poly = TestUtils.buildSquare();
        Mesh mesh = Mesh.buildMeshFromPolygon(poly, null);

        GameItem2d item = new GameItem2d(mesh, null, poly, 3500, true);
        //check initial position
        assertEquals(1, poly.getPoints().get(0).getXLoc(), BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(1, poly.getPoints().get(0).getYLoc(), BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(0, item.getPosition().x, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(0, item.getPosition().y, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(-3.5, item.getPosition().z, BBDGeometryUtils.ALLOWABLE_DELTA);
        //check rotation
        assertEquals(0, item.getRotation().x, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(0, item.getRotation().y, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(0, item.getRotation().z, BBDGeometryUtils.ALLOWABLE_DELTA);

        //actually rotate
        item.rotate((float)Math.PI / 2);

        //check end position and rotation
        //check initial position
        assertEquals(-1, poly.getPoints().get(0).getXLoc(), BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(1, poly.getPoints().get(0).getYLoc(), BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(0, item.getPosition().x, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(0, item.getPosition().y, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(-3.5, item.getPosition().z, BBDGeometryUtils.ALLOWABLE_DELTA);
        //check rotation
        assertEquals(0, item.getRotation().x, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(0, item.getRotation().y, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(Math.PI/2, item.getRotation().z, BBDGeometryUtils.ALLOWABLE_DELTA);
    }

    @Test
    public void testSetRotation(){
        if(!windowInit){
            initWindow();
        }

        BBDPolygon poly = TestUtils.buildSquare();
        Mesh mesh = Mesh.buildMeshFromPolygon(poly, null);

        GameItem2d item = new GameItem2d(mesh, null, poly, 3500, true);
        //check initial position
        assertEquals(1, poly.getPoints().get(0).getXLoc(), BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(1, poly.getPoints().get(0).getYLoc(), BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(0, item.getPosition().x, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(0, item.getPosition().y, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(-3.5, item.getPosition().z, BBDGeometryUtils.ALLOWABLE_DELTA);
        //check rotation
        assertEquals(0, item.getRotation().x, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(0, item.getRotation().y, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(0, item.getRotation().z, BBDGeometryUtils.ALLOWABLE_DELTA);

        //actually rotate
        item.setRotation((float)Math.PI / 2);

        //check end position and rotation
        //check initial position
        assertEquals(-1, poly.getPoints().get(0).getXLoc(), BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(1, poly.getPoints().get(0).getYLoc(), BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(0, item.getPosition().x, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(0, item.getPosition().y, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(-3.5, item.getPosition().z, BBDGeometryUtils.ALLOWABLE_DELTA);
        //check rotation
        assertEquals(0, item.getRotation().x, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(0, item.getRotation().y, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(Math.PI/2, item.getRotation().z, BBDGeometryUtils.ALLOWABLE_DELTA);

        //reset rotation
        item.setRotation((float)Math.PI);

        //check end position and rotation
        //check initial position
        assertEquals(-1, poly.getPoints().get(0).getXLoc(), BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(-1, poly.getPoints().get(0).getYLoc(), BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(0, item.getPosition().x, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(0, item.getPosition().y, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(-3.5, item.getPosition().z, BBDGeometryUtils.ALLOWABLE_DELTA);
        //check rotation
        assertEquals(0, item.getRotation().x, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(0, item.getRotation().y, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(Math.PI, item.getRotation().z, BBDGeometryUtils.ALLOWABLE_DELTA);
    }

    @Test
    public void testRotateAroundPoint(){
        if(!windowInit){
            initWindow();
        }

        BBDPolygon poly = TestUtils.buildSquare();
        Mesh mesh = Mesh.buildMeshFromPolygon(poly, null);

        GameItem2d item = new GameItem2d(mesh, null, poly, 3000, true);
        //check initial position
        assertEquals(1, poly.getPoints().get(0).getXLoc(), BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(1, poly.getPoints().get(0).getYLoc(), BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(0, item.getPosition().x, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(0, item.getPosition().y, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(-3.0, item.getPosition().z, BBDGeometryUtils.ALLOWABLE_DELTA);
        //check rotation
        assertEquals(0, item.getRotation().x, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(0, item.getRotation().y, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(0, item.getRotation().z, BBDGeometryUtils.ALLOWABLE_DELTA);

        BBDPoint firstCenterOfRotation = new BBDPoint(1,1);

        //actually rotate
        item.rotateAroundPoint(firstCenterOfRotation, (float)Math.PI);
        //check initial position
        assertEquals(1, poly.getPoints().get(0).getXLoc(), BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(1, poly.getPoints().get(0).getYLoc(), BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(1, poly.getPoints().get(1).getXLoc(), BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(3, poly.getPoints().get(1).getYLoc(), BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(2, item.getPosition().x, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(2, item.getPosition().y, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(-3.0, item.getPosition().z, BBDGeometryUtils.ALLOWABLE_DELTA);
        //check rotation
        assertEquals(0, item.getRotation().x, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(0, item.getRotation().y, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(Math.PI, item.getRotation().z, BBDGeometryUtils.ALLOWABLE_DELTA);
    }

    @Test
    public void testSetScale(){
        if(!windowInit){
            initWindow();
        }

        BBDPolygon poly = TestUtils.buildSquare();
        Mesh mesh = Mesh.buildMeshFromPolygon(poly, null);

        GameItem2d item = new GameItem2d(mesh, null, poly, 3000, true);
        //check initial position
        assertEquals(1, poly.getPoints().get(0).getXLoc(), BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(1, poly.getPoints().get(0).getYLoc(), BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(0, item.getPosition().x, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(0, item.getPosition().y, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(-3.0, item.getPosition().z, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(1, item.getScale());

        //scale first time
        item.setScale(5);
        assertEquals(5, poly.getPoints().get(0).getXLoc(), BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(5, poly.getPoints().get(0).getYLoc(), BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(0, item.getPosition().x, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(0, item.getPosition().y, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(-3.0, item.getPosition().z, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(5, item.getScale());

        //scale second time
        item.setScale(2);
        assertEquals(2, poly.getPoints().get(0).getXLoc(), BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(2, poly.getPoints().get(0).getYLoc(), BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(0, item.getPosition().x, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(0, item.getPosition().y, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(-3.0, item.getPosition().z, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(2, item.getScale());
    }

    @Test
    public void testScale(){
        if(!windowInit){
            initWindow();
        }

        BBDPolygon poly = TestUtils.buildSquare();
        Mesh mesh = Mesh.buildMeshFromPolygon(poly, null);

        GameItem2d item = new GameItem2d(mesh, null, poly, 3000, true);
        //check initial position
        assertEquals(1, poly.getPoints().get(0).getXLoc(), BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(1, poly.getPoints().get(0).getYLoc(), BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(0, item.getPosition().x, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(0, item.getPosition().y, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(-3.0, item.getPosition().z, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(1, item.getScale());

        //scale
        item.scale(5);
        assertEquals(5, poly.getPoints().get(0).getXLoc(), BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(5, poly.getPoints().get(0).getYLoc(), BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(0, item.getPosition().x, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(0, item.getPosition().y, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(-3.0, item.getPosition().z, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(5, item.getScale());

        //scale
        item.scale(2);
        assertEquals(10, poly.getPoints().get(0).getXLoc(), BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(10, poly.getPoints().get(0).getYLoc(), BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(0, item.getPosition().x, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(0, item.getPosition().y, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(-3.0, item.getPosition().z, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(10, item.getScale());

        //scale
        item.scale(0.2f);
        assertEquals(2, poly.getPoints().get(0).getXLoc(), BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(2, poly.getPoints().get(0).getYLoc(), BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(0, item.getPosition().x, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(0, item.getPosition().y, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(-3.0, item.getPosition().z, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(2, item.getScale());
    }

    @Test
    public void testScaleFromPoint(){
        if(!windowInit){
            initWindow();
        }

        BBDPolygon poly = TestUtils.buildSquare();
        Mesh mesh = Mesh.buildMeshFromPolygon(poly, null);

        GameItem2d item = new GameItem2d(mesh, null, poly, 3000, true);
        //check initial position
        assertEquals(1, poly.getPoints().get(0).getXLoc(), BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(1, poly.getPoints().get(0).getYLoc(), BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(0, item.getPosition().x, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(0, item.getPosition().y, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(-3.0, item.getPosition().z, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(1, item.getScale());

        //scaleFromPoint
        BBDPoint centerOfScale = new BBDPoint(-1,-1);
        item.scaleFromPoint(centerOfScale, 5);
        assertEquals(9, poly.getPoints().get(0).getXLoc(), BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(9, poly.getPoints().get(0).getYLoc(), BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(4, item.getPosition().x, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(4, item.getPosition().y, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(-3.0, item.getPosition().z, BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(5, item.getScale());
    }
}
