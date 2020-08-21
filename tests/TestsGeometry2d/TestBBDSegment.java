package TestsGeometry2d;

import Geometry2d.BBDPoint;
import Geometry2d.BBDSegment;
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
        assertEquals(new BBDPoint(1, 3), points2[1]);
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

}
