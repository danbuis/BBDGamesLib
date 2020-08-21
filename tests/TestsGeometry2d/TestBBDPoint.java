package TestsGeometry2d;

import Geometry2d.BBDPoint;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestBBDPoint {
    /**
     * Tests some basics like point creating and
     * field value retrievals
     */
    @Test
    public void testBasics() {
        BBDPoint testPoint = new BBDPoint(1.0, 3.0);
        assertEquals(1.0, testPoint.getXLoc());
        assertEquals (3.0, testPoint.getYLoc());

        testPoint = new BBDPoint(-1, 0);
        assertEquals(-1, testPoint.getXLoc());
        assertEquals(0, testPoint.getYLoc());
    }

    /**
     * Basically making sure that these functions are tested
     * so that they don't accidentally crash or something
     */
    @Test
    public void testUselessMethods(){
        BBDPoint testPoint = new BBDPoint(0,0);
        testPoint.scale(20);
        testPoint.rotate(20);

        BBDPoint center = testPoint.center();
        assertEquals(0, center.getXLoc());
        assertEquals(0, center.getYLoc());
    }

    /**
     * Test that a point is translated correctly, can be
     * translated multiple times, and in different directions
     */
    @Test
    public void testTranslate(){
        BBDPoint testPoint = new BBDPoint(0,0);
        testPoint.translate(3,4);
        assertEquals(3, testPoint.getXLoc());
        assertEquals (4, testPoint.getYLoc());

        testPoint = new BBDPoint(10,-10);
        testPoint.translate(-15, 0);
        assertEquals(-5, testPoint.getXLoc());
        assertEquals (-10, testPoint.getYLoc());
        testPoint.translate(12,12);
        assertEquals(7, testPoint.getXLoc());
        assertEquals(2, testPoint.getYLoc());
    }

    /**
     * Test that a point can be scaled multiple times, from the origin,
     * from a non-origin point, and from different sides.  Test that negative
     * scaling works as expected
     */
    @Test
    public void testScaleFromPoint(){
        BBDPoint testPoint = new BBDPoint(1,1);
        testPoint.scaleFromPoint(new BBDPoint(0,0), 3);
        assertEquals(3, testPoint.getXLoc());
        assertEquals(3, testPoint.getYLoc());
        testPoint.scaleFromPoint(new BBDPoint(0,0), 0.1);
        assertEquals(0.3, testPoint.getXLoc(), 0.0005);
        assertEquals(0.3, testPoint.getYLoc(), 0.0005);

        testPoint = new BBDPoint(10,10);
        testPoint.scaleFromPoint(new BBDPoint(12, 15), 2);
        assertEquals(8, testPoint.getXLoc(), 0.0005);
        assertEquals(5, testPoint.getYLoc(), 0.0005);

        testPoint = new BBDPoint(1,1);
        testPoint.scaleFromPoint(new BBDPoint(0,0), -1);
        assertEquals(-1, testPoint.getXLoc());
        assertEquals(-1, testPoint.getYLoc());
    }

    /**
     * Test that a point can be rotated around another reference
     * point correctly.  Test that values over 360 degrees work as expected.
     * Check that negative rotations work as expected
     */
    @Test
    public void testRotateAroundPoint(){
        BBDPoint testPoint = new BBDPoint(1,0);
        testPoint.rotateAroundPoint(new BBDPoint(0,0), 90);
        assertEquals(0, testPoint.getXLoc(), 0.0005);
        assertEquals(1, testPoint.getYLoc(), 0.0005);

        testPoint = new BBDPoint(1,0);
        testPoint.rotateAroundPoint(new BBDPoint(0,0), 720);
        assertEquals(1, testPoint.getXLoc(), 0.0005);
        assertEquals(0, testPoint.getYLoc(), 0.0005);

        testPoint.rotateAroundPoint(new BBDPoint(-1,1), -90);
        System.out.println(testPoint);
        assertEquals(-2, testPoint.getXLoc(), 0.0005);
        assertEquals(-1, testPoint.getYLoc(), 0.0005);
    }

    /**
     * Test that distance calculations are as expected and don't care which
     * direction the calculation goes.
     */
    @Test
    public void testDistanceToPont(){
        BBDPoint point1 = new BBDPoint(1,0);
        BBDPoint point2 = new BBDPoint(-1,0);
        BBDPoint point3 = new BBDPoint(0,1);

        assertEquals(2, point1.distanceToPoint(point2));
        assertEquals(2, point2.distanceToPoint(point1));
        assertEquals(1.414, point1.distanceToPoint(point3), 0.0005);
    }

    /**
     * Test angle calculations and verify that 0 degrees is up and that the
     * angles increase counterclockwise
     */
    @Test
    public void testAngleToOtherPoints(){
        BBDPoint point1 = new BBDPoint(1,0);
        BBDPoint point2 = new BBDPoint(-1,0);
        BBDPoint point3 = new BBDPoint(0,1);
        assertEquals(180, point1.angleToOtherPoint(point2));
        assertEquals(0, point2.angleToOtherPoint(point1));
        assertEquals(45, point2.angleToOtherPoint(point3));
        assertEquals(-45, point3.angleToOtherPoint(point1));
    }
}
