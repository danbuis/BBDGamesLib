package TestsGeometry2d;

import Geometry2d.BBDPoint;
import Geometry2d.BBDPolygon;
import Geometry2d.BBDSegment;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class TestBBDPolygon {

    public BBDPolygon buildSquare(){
        BBDPoint point1 = new BBDPoint(1,1);
        BBDPoint point2 = new BBDPoint(1,-1);
        BBDPoint point3 = new BBDPoint(-1,-1);
        BBDPoint point4 = new BBDPoint(-1,1);

        BBDPoint[] points = {point1, point2, point3, point4};

        return new BBDPolygon(points);
    }

    public BBDPolygon buildDiamond(){
        BBDPoint point1 = new BBDPoint(1,0);
        BBDPoint point2 = new BBDPoint(0,-1);
        BBDPoint point3 = new BBDPoint(-1,0);
        BBDPoint point4 = new BBDPoint(0,1);

        BBDPoint[] points = {point1, point2, point3, point4};

        return new BBDPolygon(points);
    }

    @Test
    public void testConstructor(){
        BBDPolygon test = buildSquare();

        //make sure that the arrays have the right length
        assertEquals(4, test.getPoints().length);
        assertEquals(4, test.getSegments().length);

        //check the 4 sides
        assertEquals(new BBDSegment(new BBDPoint(1,1), new BBDPoint(1,-1)), test.getSegments()[0]);
        assertEquals(new BBDSegment(new BBDPoint(1,-1), new BBDPoint(-1,-1)), test.getSegments()[1]);
        assertEquals(new BBDSegment(new BBDPoint(-1,-1), new BBDPoint(-1,1)), test.getSegments()[2]);
        assertEquals(new BBDSegment(new BBDPoint(-1,1), new BBDPoint(1,1)), test.getSegments()[3]);
    }

    @Test
    public void testDimensions(){
        BBDPolygon square = this.buildSquare();
        BBDPolygon diamond = this.buildDiamond();

        assertEquals(2, square.width());
        assertEquals(2, square.height());
        assertEquals(2, diamond.height());
        assertEquals(2, diamond.height());
    }

    @Test
    public void testTranslate(){
        BBDPolygon square = this.buildSquare();
        square.translate(4,4);
        assertEquals(new BBDPoint(5,5), square.getPoints()[0]);
        assertEquals(new BBDPoint(5,3), square.getPoints()[1]);
        assertEquals(new BBDPoint(3,3), square.getPoints()[2]);
        assertEquals(new BBDPoint(3,5), square.getPoints()[3]);
    }

    @Test
    public void testCenter(){
        BBDPolygon square = this.buildSquare();
        assertEquals(new BBDPoint(0,0), square.center());

        square.translate(5,5);
        assertEquals(new BBDPoint(5,5), square.center());
    }

    @Test
    public void testScale(){
        BBDPolygon square = this.buildSquare();

        square.scale(0.5);
        assertEquals(new BBDPoint(0.5, 0.5), square.getPoints()[0]);

        square.scale(4);
        assertEquals(new BBDPoint(2,2), square.getPoints()[0]);
        assertEquals(new BBDPoint(2,-2), square.getPoints()[1]);
        assertEquals(new BBDPoint(-2,-2), square.getPoints()[2]);
        assertEquals(new BBDPoint(-2,2), square.getPoints()[3]);
    }

    @Test
    public void testScaleFromPoint(){
        BBDPolygon square = this.buildSquare();

        square.scaleFromPoint(new BBDPoint(1,1), 0.5);
        assertEquals(new BBDPoint(1, 1), square.getPoints()[0]);
        assertEquals(new BBDPoint(1, 0), square.getPoints()[1]);
        assertEquals(new BBDPoint(0, 0), square.getPoints()[2]);
        assertEquals(new BBDPoint(0, 1), square.getPoints()[3]);
    }

    @Test
    public void testRotate(){
        BBDPolygon square = this.buildSquare();

        square.rotate(180);
        assertEquals(new BBDPoint(-1, -1), square.getPoints()[0]);
        assertEquals(new BBDPoint(-1, 1), square.getPoints()[1]);
        assertEquals(new BBDPoint(1, 1), square.getPoints()[2]);
        assertEquals(new BBDPoint(1, -1), square.getPoints()[3]);
    }

    @Test
    public void testRotateAroundPoint(){
        BBDPolygon square = this.buildSquare();

        square.rotateAroundPoint(new BBDPoint(1,1), 180);
        assertEquals(new BBDPoint(1, 1), square.getPoints()[0]);
        assertEquals(new BBDPoint(1, 3), square.getPoints()[1]);
        assertEquals(new BBDPoint(3, 3), square.getPoints()[2]);
        assertEquals(new BBDPoint(3, 1), square.getPoints()[3]);
    }

    @Test
    public void testCheckPointOnPerimeter() {
        BBDPolygon diamond = this.buildDiamond();

        //check point on segment and one on a vertex
        assertTrue(diamond.checkPointOnPerimeter(new BBDPoint(0.5, 0.5)));
        assertTrue(diamond.checkPointOnPerimeter(new BBDPoint(0, 1)));
        //check point just off the line
        assertFalse(diamond.checkPointOnPerimeter(new BBDPoint(0.5, 0.51)));
        //check an interior point and an exterior point
        assertFalse(diamond.checkPointOnPerimeter(new BBDPoint(0, 0)));
        assertFalse(diamond.checkPointOnPerimeter(new BBDPoint(-56, 12)));
    }

    @Test
    public void testSegmentIntersectPolygonList(){
        BBDPolygon diamond = this.buildDiamond();
        BBDSegment fullDiagonal = new BBDSegment(new BBDPoint(5,5), new BBDPoint(-5,-5));
        BBDSegment partDiagonal = new BBDSegment(new BBDPoint(0,0), new BBDPoint(-5,-5));

        //check a few lines that cross in 1 and 2 locations and clearly are partially inside
        assertEquals(2, diamond.segmentIntersectPolygonList(fullDiagonal).length);
        assertEquals(1, diamond.segmentIntersectPolygonList(partDiagonal).length);

        //check a line that is touching a segment
        BBDSegment touchSegment = new BBDSegment(new BBDPoint(0.5,0.5), new BBDPoint(4,4));
        assertEquals(1, diamond.segmentIntersectPolygonList(touchSegment).length);

        //check a line that is touching a vertex
        BBDSegment touchVertex = new BBDSegment(new BBDPoint(0,1), new BBDPoint(4,4));
        assertEquals(2, diamond.segmentIntersectPolygonList(touchVertex).length);

        //check a line that is touching a vertex
        BBDSegment colinear = new BBDSegment(new BBDPoint(-1,2), new BBDPoint(2,-1));
        assertEquals(3, diamond.segmentIntersectPolygonList(colinear).length);
    }

    @Test
    public void testCheckPointInside(){
        BBDPolygon diamond = this.buildDiamond();
        BBDPoint inside = new BBDPoint(0.1,0.1);
        BBDPoint insideVertex = new BBDPoint(0,0);
        BBDPoint outside = new BBDPoint(2,1);
        BBDPoint onVertex = new BBDPoint(1,0);
        BBDPoint onEdge = new BBDPoint(0.5, 0.5);

        assertTrue(diamond.checkPointInside(inside));
        assertTrue(diamond.checkPointInside(insideVertex));
        assertFalse(diamond.checkPointInside(outside));
        assertTrue(diamond.checkPointInside(onVertex));
        assertTrue(diamond.checkPointInside(onEdge));
    }

    @Test
    public void testArea(){
        BBDPolygon square = this.buildSquare();
        //check basic method
        assertEquals(4, square.area());
        //check if object is modified
        square.scale(2);
        assertEquals(16, square.area());

        //construct a square that has co-linear points on the edges.
        BBDPoint point1 = new BBDPoint(1,1);
        BBDPoint point15 = new BBDPoint(1,0);
        BBDPoint point2 = new BBDPoint(1,-1);
        BBDPoint point25 = new BBDPoint(0,-1);
        BBDPoint point3 = new BBDPoint(-1,-1);
        BBDPoint point35 = new BBDPoint(-1,0);
        BBDPoint point4 = new BBDPoint(-1,1);
        BBDPoint point45 = new BBDPoint(0,1);

        BBDPoint[] points = {point1, point15, point2, point25, point3, point35, point4, point45};
        assertEquals(4, new BBDPolygon(points).area());

        //make a concave polygon, and make sure that he first 3 points make a triangle NOT inside
        point2 = new BBDPoint(0,0);
        BBDPoint[] points2 = {point15, point2, point25, point3, point35, point4, point45, point1};
        assertEquals(3, new BBDPolygon(points2).area());
    }
}
