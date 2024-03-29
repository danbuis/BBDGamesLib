package TestsGeometry2d;

import BBDGameLibrary.Geometry2d.BBDGeometryHelpers;
import BBDGameLibrary.Geometry2d.BBDPoint;
import BBDGameLibrary.Geometry2d.Exceptions.CoordinateOverflowException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestBBDPoint {
    /**
     * Tests some basics like point creating and
     * field value retrievals
     */
    @Test
    public void testBasics() {
        BBDPoint testPoint = new BBDPoint(1.0f, 3.0f);
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

        testPoint.translate(0,0);
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
        testPoint.scaleFromPoint(new BBDPoint(0,0), 0.1f);
        assertEquals(0.3, testPoint.getXLoc(), BBDGeometryHelpers.ALLOWABLE_DELTA);
        assertEquals(0.3, testPoint.getYLoc(), BBDGeometryHelpers.ALLOWABLE_DELTA);

        testPoint = new BBDPoint(10,10);
        testPoint.scaleFromPoint(new BBDPoint(12, 15), 2);
        assertEquals(8, testPoint.getXLoc(), BBDGeometryHelpers.ALLOWABLE_DELTA);
        assertEquals(5, testPoint.getYLoc(), BBDGeometryHelpers.ALLOWABLE_DELTA);

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
        testPoint.rotateAroundPoint(new BBDPoint(0,0), (float) (Math.PI/2));
        assertEquals(0, testPoint.getXLoc(), BBDGeometryHelpers.ALLOWABLE_DELTA);
        assertEquals(1, testPoint.getYLoc(), BBDGeometryHelpers.ALLOWABLE_DELTA);

        testPoint = new BBDPoint(1,0);
        testPoint.rotateAroundPoint(new BBDPoint(0,0), (float) (Math.PI*6));
        assertEquals(1, testPoint.getXLoc(), BBDGeometryHelpers.ALLOWABLE_DELTA);
        assertEquals(0, testPoint.getYLoc(), BBDGeometryHelpers.ALLOWABLE_DELTA);

        testPoint.rotateAroundPoint(new BBDPoint(-1,1), (float) (Math.PI/-2));
        assertEquals(-2, testPoint.getXLoc(), BBDGeometryHelpers.ALLOWABLE_DELTA);
        assertEquals(-1, testPoint.getYLoc(), BBDGeometryHelpers.ALLOWABLE_DELTA);
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

        assertEquals(4, point1.distanceSquaredToPoint(point2));
        assertEquals(4, point2.distanceSquaredToPoint(point1));
        assertEquals(2.0, point1.distanceSquaredToPoint(point3), BBDGeometryHelpers.ALLOWABLE_DELTA);
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
        assertEquals(Math.PI, point1.angleToOtherPoint(point2), BBDGeometryHelpers.ALLOWABLE_DELTA);
        assertEquals(0, point2.angleToOtherPoint(point1), BBDGeometryHelpers.ALLOWABLE_DELTA);
        assertEquals(Math.PI/4, point2.angleToOtherPoint(point3), BBDGeometryHelpers.ALLOWABLE_DELTA);
        assertEquals(Math.PI/-4, point3.angleToOtherPoint(point1), BBDGeometryHelpers.ALLOWABLE_DELTA);
    }

    @Test
    public void testOverflow(){
        /*Doubles work differently from ints with overflow.  Basically once it has reached
        the maximum or minimum, it stays there, even after subtraction.  The only way it will
        change is by going to Infinity.
         */
        BBDPoint original = new BBDPoint(1,1);
        BBDPoint test1 = new BBDPoint(original);

        //make sure that validate is called...
        Exception exception = assertThrows(CoordinateOverflowException.class, () -> test1.translate(Float.MAX_VALUE, 1));
        String expectedMessage = "has reached the bounds";
        assertTrue(exception.getMessage().contains(expectedMessage));

    }

    @Test
    public void testCopy(){
        BBDPoint original1 = new BBDPoint(0,0);
        BBDPoint original2 = new BBDPoint(4,1);

        assertEquals(new BBDPoint(1,0), new BBDPoint(original1, 1,0));
        assertEquals(new BBDPoint(0,1), new BBDPoint(original1, 1, (float) (Math.PI/2)));
        assertEquals(new BBDPoint(0,-5), new BBDPoint(original1, 5, (float) (Math.PI/-2)));

        assertEquals(new BBDPoint(5,1), new BBDPoint(original2, 1,0));
        assertEquals(new BBDPoint(4,2), new BBDPoint(original2, 1, (float) (Math.PI/2)));
        assertEquals(new BBDPoint(4,-4), new BBDPoint(original2, 5, (float) (Math.PI/-2)));
    }
}
