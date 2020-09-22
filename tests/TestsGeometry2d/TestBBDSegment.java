package TestsGeometry2d;

import Geometry2d.BBDGeometryUtils;
import Geometry2d.BBDPoint;
import Geometry2d.BBDSegment;
import Geometry2d.Exceptions.CoordinateOverflowException;
import Geometry2d.Exceptions.ParallelLinesException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestBBDSegment {
    /**
     * Test the BBD Segment class
     */

    // a few functions to build some standard lines

    private BBDSegment buildVertical(){
        BBDPoint p1 = new BBDPoint(1,0);
        BBDPoint p2 = new BBDPoint(1,1);
        return new BBDSegment(p1, p2);
    }

    private BBDSegment buildHorizontal(){
        BBDPoint p1 = new BBDPoint(1,1);
        BBDPoint p2 = new BBDPoint(0,1);
        return new BBDSegment(p1, p2);
    }

    private BBDSegment buildAngleOne(){
        BBDPoint p1 = new BBDPoint(1,1);
        BBDPoint p2 = new BBDPoint(0,0);
        return new BBDSegment(p1, p2);
    }

    private BBDSegment buildAngleTwo(){
        BBDPoint p1 = new BBDPoint(1,1);
        BBDPoint p2 = new BBDPoint(4,-2);
        return new BBDSegment(p1, p2);
    }

    @Test
    public void testConstructorsAndGetSegments(){
        BBDPoint p1 = new BBDPoint(1,1);
        BBDPoint p2 = new BBDPoint(4,-2);
        BBDSegment seg =  new BBDSegment(p1, p2);

        BBDPoint[] points = seg.getPoints();
        assertEquals(p1, points[0]);
        assertEquals(p2, points[1]);

        BBDSegment seg2 = new BBDSegment(p1, 0, 2);
        BBDPoint[] points2 = seg2.getPoints();
        assertEquals(new BBDPoint(1,1), points2[0]);
        assertEquals(new BBDPoint(3, 1), points2[1]);
    }

    @Test
    public void testTranslate(){
        BBDSegment testSegment = this.buildVertical();
        testSegment.translate(10,10);
        BBDPoint[] points = testSegment.getPoints();
        assertEquals(new BBDPoint(11,10), points[0]);
        assertEquals(new BBDPoint(11,11), points[1]);
    }

    @Test
    public void testScale(){
        BBDSegment testSegment = this.buildVertical();
        testSegment.scale(5);
        BBDPoint[] points = testSegment.getPoints();
        assertEquals(new BBDPoint(1,-2), points[0]);
        assertEquals(new BBDPoint(1,3), points[1]);
    }

    @Test
    public void testScaleFromPoint(){
        BBDSegment testSegment = this.buildVertical();
        BBDPoint[] points = testSegment.getPoints();
        testSegment.scaleFromPoint(points[0],5);
        assertEquals(new BBDPoint(1,0), points[0]);
        assertEquals(new BBDPoint(1,5), points[1]);
    }

    @Test
    public void testRotate(){
        BBDSegment testSegment = this.buildVertical();
        testSegment.rotate(90);
        BBDPoint[] points = testSegment.getPoints();
        assertEquals(new BBDPoint(1.5, 0.5), points[0]);
        assertEquals(new BBDPoint(0.5, 0.5), points[1]);
    }

    @Test
    public void testRotateAroundPoint(){
        BBDSegment testSegment = this.buildVertical();
        BBDPoint[] points = testSegment.getPoints();
        testSegment.rotateAroundPoint(points[0],90);
        assertEquals(new BBDPoint(1, 0), points[0]);
        assertEquals(new BBDPoint(0, 0), points[1]);
    }

    @Test
    public void testCenter(){
        BBDSegment testSegment = this.buildVertical();
        BBDPoint center = testSegment.center();
        assertEquals(new BBDPoint(1, 0.5), center);
    }

    @Test
    public void testSlopeRatio(){
        BBDSegment horizontal = this.buildHorizontal();
        assertEquals(0, horizontal.slopeInRatio());

        BBDSegment vertical = this.buildVertical();
        assertEquals(Double.POSITIVE_INFINITY, vertical.slopeInRatio());

        BBDSegment angled1 = this.buildAngleOne();
        assertEquals(1, angled1.slopeInRatio());

        BBDSegment angled2 = this.buildAngleTwo();
        assertEquals(-1, angled2.slopeInRatio());
    }

    @Test
    public void testSlopeRadians(){
        BBDSegment horizontal = this.buildHorizontal();
        assertEquals(0, horizontal.slopeInRadians());

        BBDSegment vertical = this.buildVertical();
        assertEquals(Math.PI/2, vertical.slopeInRadians(), BBDGeometryUtils.ALLOWABLE_DELTA);

        BBDSegment angled1 = this.buildAngleOne();
        assertEquals(Math.PI/4, angled1.slopeInRadians(), BBDGeometryUtils.ALLOWABLE_DELTA);

        BBDSegment angled2 = this.buildAngleTwo();
        assertEquals(Math.PI/-4, angled2.slopeInRadians(),BBDGeometryUtils.ALLOWABLE_DELTA);

        //test that slope is direction agnostic
        angled2.rotate(180);
        assertEquals(Math.PI/-4, angled2.slopeInRadians(),BBDGeometryUtils.ALLOWABLE_DELTA);
    }

    @Test
    public void testSlopeDegrees(){
        BBDSegment horizontal = this.buildHorizontal();
        assertEquals(0, horizontal.slopeInDegrees());

        BBDSegment vertical = this.buildVertical();
        assertEquals(90, vertical.slopeInDegrees(), BBDGeometryUtils.ALLOWABLE_DELTA);

        BBDSegment angled1 = this.buildAngleOne();
        assertEquals(45, angled1.slopeInDegrees(), BBDGeometryUtils.ALLOWABLE_DELTA);

        BBDSegment angled2 = this.buildAngleTwo();
        assertEquals(-45, angled2.slopeInDegrees(),BBDGeometryUtils.ALLOWABLE_DELTA);

        //test that slope is direction agnostic
        angled2.rotate(180);
        assertEquals(-45, angled2.slopeInDegrees(),BBDGeometryUtils.ALLOWABLE_DELTA);
    }

    @Test
    public void testPointOnSegment(){
        BBDSegment testLine = new BBDSegment(new BBDPoint(0,0), new BBDPoint(5,5));
        //test a point definitely on the line
        assertTrue(testLine.pointOnSegment(new BBDPoint(2,2)));
        //test one of the end points
        assertTrue(testLine.pointOnSegment(new BBDPoint(0,0)));
        //test a point well off the line
        assertFalse(testLine.pointOnSegment(new BBDPoint(10,0)));
        //test a point aligned with the line, but outside its bounds
        assertFalse(testLine.pointOnSegment(new BBDPoint(6,6)));
    }

    @Test
    public void testInterceptPoint(){
        BBDSegment test1 = new BBDSegment(new BBDPoint(0,1), new BBDPoint(1,0));
        BBDSegment test2 = new BBDSegment(new BBDPoint(0,0), new BBDPoint(1,1));
        BBDSegment test3 = new BBDSegment(new BBDPoint(4,4), new BBDPoint(8,8));
        BBDSegment parallel = new BBDSegment(new BBDPoint(0,2), new BBDPoint(2,0));

        try{
            assertEquals(new BBDPoint(0.5, 0.5), test1.interceptPoint(test2));
            assertEquals(new BBDPoint(0.5, 0.5), test1.interceptPoint(test3));
        } catch(ParallelLinesException e){
            System.out.println("You probably shouldn't be getting an exception here");
            e.printStackTrace();
        }


        Exception exception = assertThrows(ParallelLinesException.class, () -> test1.interceptPoint(parallel));

        String expectedMessage = "Can not calculate an intercept point between 2 parallel lines";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testIntersection(){
        BBDSegment test1 = new BBDSegment(new BBDPoint(0,1), new BBDPoint(1,0));
        BBDSegment test2 = new BBDSegment(new BBDPoint(0,0), new BBDPoint(1,1));
        BBDSegment test3 = new BBDSegment(new BBDPoint(4,4), new BBDPoint(8,8));
        BBDSegment test4 = new BBDSegment(new BBDPoint(0.5, 0.5), new BBDPoint(0.6, 4));

        assertTrue(test1.intersects(test2));
        //check reverse
        assertTrue(test2.intersects(test1));

        assertFalse(test1.intersects(test3));
        //check if end point of one is the intersection
        assertTrue(test4.intersects(test1));
    }

    @Test
    public void testDistanceToPoint(){
        BBDSegment testSeg = this.buildVertical();

        BBDPoint point1 = new BBDPoint(1,0);
        assertEquals(0, testSeg.distanceToPoint(point1), BBDGeometryUtils.ALLOWABLE_DELTA);

        testSeg = this.buildVertical();
        BBDPoint point1a = new BBDPoint(1, 0.6);
        assertEquals(0, testSeg.distanceToPoint(point1a), BBDGeometryUtils.ALLOWABLE_DELTA);

        testSeg = this.buildVertical();
        BBDPoint point2 = new BBDPoint(2, 0.5);
        assertEquals(1, testSeg.distanceToPoint(point2), BBDGeometryUtils.ALLOWABLE_DELTA);

        testSeg = this.buildVertical();
        BBDPoint point3 = new BBDPoint(1,4);
        assertEquals(3, testSeg.distanceToPoint(point3), BBDGeometryUtils.ALLOWABLE_DELTA);

        testSeg = this.buildVertical();
        BBDPoint point4 = new BBDPoint(5,5);
        assertEquals(Math.sqrt(2)*4, testSeg.distanceToPoint(point4), BBDGeometryUtils.ALLOWABLE_DELTA);
    }
    
    @Test
    public void testConnected(){
        BBDSegment horizontal = this.buildHorizontal();
        BBDSegment vertical = this.buildVertical();
        BBDSegment other = new BBDSegment(new BBDPoint(-2,-5), new BBDPoint(-5, 12));
        BBDSegment crossing = new BBDSegment(new BBDPoint(0,0), new BBDPoint(4,4));

        assertTrue(horizontal.segmentConnected(vertical));
        assertTrue(vertical.segmentConnected(horizontal));
        assertFalse(horizontal.segmentConnected(other));
        assertFalse(other.segmentConnected(vertical));
        assertFalse(crossing.segmentConnected(horizontal));
    }

    @Test
    public void testEquals(){
        BBDSegment test1 = new BBDSegment(new BBDPoint(0,0), new BBDPoint(1,1));
        BBDSegment test2 = new BBDSegment(new BBDPoint(1,1), new BBDPoint(0,0));
        BBDSegment test3 = new BBDSegment(new BBDPoint(10,10), new BBDPoint(1,1));

        assertEquals(test2, test1);
        assertEquals(test1, test2);
        assertNotEquals(test3, test1);
    }
}
