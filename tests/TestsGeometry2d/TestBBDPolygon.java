package TestsGeometry2d;

import BBDGameLibrary.Geometry2d.BBDGeometryUtils;
import BBDGameLibrary.Geometry2d.BBDPoint;
import BBDGameLibrary.Geometry2d.BBDPolygon;
import BBDGameLibrary.Geometry2d.BBDSegment;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;


public class TestBBDPolygon {

    public BBDPolygon buildSquare(){
        BBDPoint point1 = new BBDPoint(1,1);
        BBDPoint point2 = new BBDPoint(1,-1);
        BBDPoint point3 = new BBDPoint(-1,-1);
        BBDPoint point4 = new BBDPoint(-1,1);

        ArrayList<BBDPoint> points = new ArrayList<>(Arrays.asList(point1, point2, point3, point4));

        return new BBDPolygon(points);
    }

    public BBDPolygon buildDiamond(){
        BBDPoint point1 = new BBDPoint(1,0);
        BBDPoint point2 = new BBDPoint(0,-1);
        BBDPoint point3 = new BBDPoint(-1,0);
        BBDPoint point4 = new BBDPoint(0,1);

        ArrayList<BBDPoint> points = new ArrayList<>(Arrays.asList(point1, point2, point3, point4));

        return new BBDPolygon(points);
    }

    @Test
    public void testConstructor(){
        BBDPolygon test = buildSquare();

        //make sure that the arrays have the right length
        assertEquals(4, test.getPoints().size());
        assertEquals(4, test.getSegments().size());

        //check the 4 sides
        assertEquals(new BBDSegment(new BBDPoint(1,1), new BBDPoint(1,-1)), test.getSegments().get(0));
        assertEquals(new BBDSegment(new BBDPoint(1,-1), new BBDPoint(-1,-1)), test.getSegments().get(1));
        assertEquals(new BBDSegment(new BBDPoint(-1,-1), new BBDPoint(-1,1)), test.getSegments().get(2));
        assertEquals(new BBDSegment(new BBDPoint(-1,1), new BBDPoint(1,1)), test.getSegments().get(3));
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
        assertEquals(new BBDPoint(5,5), square.getPoints().get(0));
        assertEquals(new BBDPoint(5,3), square.getPoints().get(1));
        assertEquals(new BBDPoint(3,3), square.getPoints().get(2));
        assertEquals(new BBDPoint(3,5), square.getPoints().get(3));
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

        square.scale(0.5f);
        assertEquals(new BBDPoint(0.5f, 0.5f), square.getPoints().get(0));

        square.scale(4);
        assertEquals(new BBDPoint(2,2), square.getPoints().get(0));
        assertEquals(new BBDPoint(2,-2), square.getPoints().get(1));
        assertEquals(new BBDPoint(-2,-2), square.getPoints().get(2));
        assertEquals(new BBDPoint(-2,2), square.getPoints().get(3));
    }

    @Test
    public void testScaleFromPoint(){
        BBDPolygon square = this.buildSquare();

        square.scaleFromPoint(new BBDPoint(1,1), 0.5f);
        assertEquals(new BBDPoint(1, 1), square.getPoints().get(0));
        assertEquals(new BBDPoint(1, 0), square.getPoints().get(1));
        assertEquals(new BBDPoint(0, 0), square.getPoints().get(2));
        assertEquals(new BBDPoint(0, 1), square.getPoints().get(3));

        BBDPolygon otherSquare = this.buildSquare();

        otherSquare.scaleFromPoint(new BBDPoint(-1,-1), 5);
        assertEquals(9, otherSquare.getPoints().get(0).getXLoc(), BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(9, otherSquare.getPoints().get(0).getYLoc(), BBDGeometryUtils.ALLOWABLE_DELTA);

    }

    @Test
    public void testRotate(){
        BBDPolygon square = this.buildSquare();

        square.rotate((float) Math.PI);
        assertEquals(new BBDPoint(-1, -1), square.getPoints().get(0));
        assertEquals(new BBDPoint(-1, 1), square.getPoints().get(1));
        assertEquals(new BBDPoint(1, 1), square.getPoints().get(2));
        assertEquals(new BBDPoint(1, -1), square.getPoints().get(3));
    }

    @Test
    public void testRotateAroundPoint(){
        BBDPolygon square = this.buildSquare();

        square.rotateAroundPoint(new BBDPoint(1,1), (float) Math.PI);
        assertEquals(new BBDPoint(1, 1), square.getPoints().get(0));
        assertEquals(new BBDPoint(1, 3), square.getPoints().get(1));
        assertEquals(new BBDPoint(3, 3), square.getPoints().get(2));
        assertEquals(new BBDPoint(3, 1), square.getPoints().get(3));
    }

    @Test
    public void testCheckPointOnPerimeter() {
        BBDPolygon diamond = this.buildDiamond();

        //check point on segment and one on a vertex
        assertTrue(diamond.checkPointOnPerimeter(new BBDPoint(0.5f, 0.5f)));
        assertTrue(diamond.checkPointOnPerimeter(new BBDPoint(0, 1)));
        //check point just off the line
        assertFalse(diamond.checkPointOnPerimeter(new BBDPoint(0.5f, 0.51f)));
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
        BBDSegment touchSegment = new BBDSegment(new BBDPoint(0.5f,0.5f), new BBDPoint(4,4));
        assertEquals(1, diamond.segmentIntersectPolygonList(touchSegment).length);

        //check a line that is touching a vertex
        BBDSegment touchVertex = new BBDSegment(new BBDPoint(0,1), new BBDPoint(4,4));
        assertEquals(2, diamond.segmentIntersectPolygonList(touchVertex).length);

        //check a line that is touching a vertex
        BBDSegment colinear = new BBDSegment(new BBDPoint(-1,2), new BBDPoint(2,-1));
        assertEquals(3, diamond.segmentIntersectPolygonList(colinear).length);

        //a block of tests to help pinpoint an issue elsewhere
        BBDPolygon square = this.buildSquare();
        square.segmentIntersectPolygonList(new BBDSegment(new BBDPoint(0.5f, -0.5f), new BBDPoint(12, -0.5f)));
        assertEquals(1, square.segmentIntersectPolygonList(new BBDSegment(new BBDPoint(0.5f, 0.5f), new BBDPoint(12, 0.5f))).length);
        assertEquals(1, square.segmentIntersectPolygonList(new BBDSegment(new BBDPoint(0.5f, -0.5f), new BBDPoint(12, -0.5f))).length);
        assertEquals(1, square.segmentIntersectPolygonList(new BBDSegment(new BBDPoint(-0.5f, -0.5f), new BBDPoint(12, -0.5f))).length);
        assertEquals(1, square.segmentIntersectPolygonList(new BBDSegment(new BBDPoint(-0.5f, 0.5f), new BBDPoint(12, 0.5f))).length);

    }

    @Test
    public void testCheckPointInside(){
        BBDPolygon diamond = this.buildDiamond();
        BBDPoint inside = new BBDPoint(0.1f,0.1f);
        BBDPoint insideVertex = new BBDPoint(0,0);
        BBDPoint outside = new BBDPoint(2,1);
        BBDPoint onVertex = new BBDPoint(1,0);
        BBDPoint onEdge = new BBDPoint(0.5f, 0.5f);

        assertTrue(diamond.checkPointInside(inside));
        assertTrue(diamond.checkPointInside(insideVertex));
        assertFalse(diamond.checkPointInside(outside));
        assertTrue(diamond.checkPointInside(onVertex));
        assertTrue(diamond.checkPointInside(onEdge));

        BBDPolygon square = this.buildSquare();
        assertTrue(square.checkPointInside(new BBDPoint(0.5f, 0.5f)));
        assertTrue(square.checkPointInside(new BBDPoint(0.5f, -0.5f)));
        assertTrue(square.checkPointInside(new BBDPoint(-0.5f, -0.5f)));
        assertTrue(square.checkPointInside(new BBDPoint(-0.5f, 0.5f)));

        // this one was found as an error in production and traced to this simple case.  Putting here for simplified unit testing and
        // to ensure that it doesn't happen in regression.
        BBDPolygon bugged = new BBDPolygon(new ArrayList<>(Arrays.asList(new BBDPoint(-3, -2), new BBDPoint(-2, -2), new BBDPoint(-2, 0))));
        BBDPoint center = bugged.centerAverage();
        assertEquals(-2.333333, center.getXLoc(), BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(-1.333334, center.getYLoc(), BBDGeometryUtils.ALLOWABLE_DELTA);
        assertTrue(bugged.checkPointInside(center));
    }

    @Test
    public void testArea(){
        BBDPolygon square = this.buildSquare();
        //check basic method
        assertEquals(4, square.area(), BBDGeometryUtils.ALLOWABLE_DELTA);
        //check if object is modified
        square.scale(2);
        assertEquals(16, square.area(), BBDGeometryUtils.ALLOWABLE_DELTA);

        //construct a square that has co-linear points on the edges.
        BBDPoint point1 = new BBDPoint(1,1);
        BBDPoint point15 = new BBDPoint(1,0);
        BBDPoint point2 = new BBDPoint(1,-1);
        BBDPoint point25 = new BBDPoint(0,-1);
        BBDPoint point3 = new BBDPoint(-1,-1);
        BBDPoint point35 = new BBDPoint(-1,0);
        BBDPoint point4 = new BBDPoint(-1,1);
        BBDPoint point45 = new BBDPoint(0,1);

        ArrayList<BBDPoint> points = new ArrayList<>(Arrays.asList(point1, point15, point2, point25, point3, point35, point4, point45));
        assertEquals(4, new BBDPolygon(points).area(), BBDGeometryUtils.ALLOWABLE_DELTA);

        //make a concave polygon, and make sure that the first 3 points make a triangle NOT inside
        point2 = new BBDPoint(0,0);
        ArrayList<BBDPoint> points2 = new ArrayList<>(Arrays.asList(point15, point2, point25, point3, point35, point4, point45, point1));
        assertEquals(3, new BBDPolygon(points2).area(), BBDGeometryUtils.ALLOWABLE_DELTA);
    }

    @Test
    public void testPolygonTouchesPolygon(){
        //set up some squares
        BBDPolygon controlPolygon = this.buildSquare();
        BBDPolygon copy = this.buildSquare();
        BBDPolygon overlapping = this.buildSquare();
        BBDPolygon adjacent = this.buildSquare();
        BBDPolygon insideEdge = this.buildSquare();
        BBDPolygon shareVertex = this.buildSquare();
        BBDPolygon contains = this.buildSquare();
        BBDPolygon separate = this.buildSquare();

        //modify most of them to create several types of scenarios
        overlapping.translate(0.2f, 0.2f);
        adjacent.translate(0.5f, 2);
        insideEdge.scaleFromPoint(controlPolygon.getPoints().get(0), 0.5f);
        shareVertex.translate(2,2);
        contains.scale(0.5f);
        separate.translate(3,3);

        //test each one with the control, and vice versa just to make sure
        assertTrue(controlPolygon.checkPolygonTouchesPolygon(copy));
        assertTrue(copy.checkPolygonTouchesPolygon(controlPolygon));

        assertFalse(controlPolygon.checkPolygonTouchesPolygon(overlapping));
        assertFalse(overlapping.checkPolygonTouchesPolygon(controlPolygon));

        assertTrue(controlPolygon.checkPolygonTouchesPolygon(adjacent));
        assertTrue(adjacent.checkPolygonTouchesPolygon(controlPolygon));

        assertFalse(controlPolygon.checkPolygonTouchesPolygon(insideEdge));
        assertFalse(insideEdge.checkPolygonTouchesPolygon(controlPolygon));

        assertTrue(controlPolygon.checkPolygonTouchesPolygon(shareVertex));
        assertTrue(shareVertex.checkPolygonTouchesPolygon(controlPolygon));

        assertFalse(controlPolygon.checkPolygonTouchesPolygon(contains));
        assertFalse(contains.checkPolygonTouchesPolygon(controlPolygon));

        assertFalse(controlPolygon.checkPolygonTouchesPolygon(separate));
        assertFalse(separate.checkPolygonTouchesPolygon(controlPolygon));
    }

    @Test
    public void testPolygonIntersectingPolygon(){
        //set up some squares
        BBDPolygon controlPolygon = this.buildSquare();
        BBDPolygon copy = this.buildSquare();
        BBDPolygon overlapping = this.buildSquare();
        BBDPolygon adjacent = this.buildSquare();
        BBDPolygon insideEdge = this.buildSquare();
        BBDPolygon shareVertex = this.buildSquare();
        BBDPolygon contains = this.buildSquare();
        BBDPolygon separate = this.buildSquare();

        //modify most of them to create several types of scenarios
        overlapping.translate(0.2f, 0.2f);
        adjacent.translate(0.5f, 2);
        insideEdge.scaleFromPoint(controlPolygon.getPoints().get(0), 0.5f);
        shareVertex.translate(2,2);
        contains.scale(0.5f);
        separate.translate(3,3);

        //test each one with the control, and vice versa just to make sure
        assertTrue(controlPolygon.checkPolygonIntersectsPolygon(copy));
        assertTrue(copy.checkPolygonIntersectsPolygon(controlPolygon));

        assertTrue(controlPolygon.checkPolygonIntersectsPolygon(overlapping));
        assertTrue(overlapping.checkPolygonIntersectsPolygon(controlPolygon));

        assertTrue(controlPolygon.checkPolygonIntersectsPolygon(adjacent));
        assertTrue(adjacent.checkPolygonIntersectsPolygon(controlPolygon));

        assertTrue(controlPolygon.checkPolygonIntersectsPolygon(insideEdge));
        assertTrue(insideEdge.checkPolygonIntersectsPolygon(controlPolygon));

        assertTrue(controlPolygon.checkPolygonIntersectsPolygon(shareVertex));
        assertTrue(shareVertex.checkPolygonIntersectsPolygon(controlPolygon));

        assertTrue(controlPolygon.checkPolygonIntersectsPolygon(contains));
        assertTrue(contains.checkPolygonIntersectsPolygon(controlPolygon));

        assertFalse(controlPolygon.checkPolygonIntersectsPolygon(separate));
        assertFalse(separate.checkPolygonIntersectsPolygon(controlPolygon));
    }

    @Test
    public void testPolygonContainsPolygon(){
        //set up some squares
        BBDPolygon controlPolygon = this.buildSquare();
        BBDPolygon copy = this.buildSquare();
        BBDPolygon overlapping = this.buildSquare();
        BBDPolygon adjacent = this.buildSquare();
        BBDPolygon insideEdge = this.buildSquare();
        BBDPolygon shareVertex = this.buildSquare();
        BBDPolygon contains = this.buildSquare();
        BBDPolygon separate = this.buildSquare();

        //modify most of them to create several types of scenarios
        overlapping.translate(0.2f, 0.2f);
        adjacent.translate(0.5f, 2);
        insideEdge.scaleFromPoint(controlPolygon.getPoints().get(0), 0.5f);
        shareVertex.translate(2,2);
        contains.scale(0.5f);
        separate.translate(3,3);

        //test each one with the control, and vice versa just to make sure
        assertTrue(controlPolygon.checkPolygonContainsPolygon(copy));
        assertTrue(copy.checkPolygonContainsPolygon(controlPolygon));

        assertFalse(controlPolygon.checkPolygonContainsPolygon(overlapping));
        assertFalse(overlapping.checkPolygonContainsPolygon(controlPolygon));

        assertFalse(controlPolygon.checkPolygonContainsPolygon(adjacent));
        assertFalse(adjacent.checkPolygonContainsPolygon(controlPolygon));

        assertTrue(controlPolygon.checkPolygonContainsPolygon(insideEdge));
        assertFalse(insideEdge.checkPolygonContainsPolygon(controlPolygon));

        assertFalse(controlPolygon.checkPolygonContainsPolygon(shareVertex));
        assertFalse(shareVertex.checkPolygonContainsPolygon(controlPolygon));

        assertTrue(controlPolygon.checkPolygonContainsPolygon(contains));
        assertFalse(contains.checkPolygonContainsPolygon(controlPolygon));

        assertFalse(controlPolygon.checkPolygonContainsPolygon(separate));
        assertFalse(separate.checkPolygonContainsPolygon(controlPolygon));
    }

    @Test
    public void testDetermineDirectionalityFairlyEvenTri(){
        BBDPoint point1 = new BBDPoint(1, 1);
        BBDPoint point2 = new BBDPoint(1, -1);
        BBDPoint point3 = new BBDPoint(-1, 0.2f);

        BBDPolygon cw1 = new BBDPolygon(new ArrayList<>(Arrays.asList(point1, point2, point3)));
        BBDPolygon cw2 = new BBDPolygon(new ArrayList<>(Arrays.asList(point2, point3, point1)));
        BBDPolygon cw3 = new BBDPolygon(new ArrayList<>(Arrays.asList(point3, point1, point2)));

        assertEquals(BBDGeometryUtils.CLOCKWISE_POLYGON, cw1.determineDirectionality());
        assertEquals(BBDGeometryUtils.CLOCKWISE_POLYGON, cw2.determineDirectionality());
        assertEquals(BBDGeometryUtils.CLOCKWISE_POLYGON, cw3.determineDirectionality());

        BBDPolygon ccw1 = new BBDPolygon(new ArrayList<>(Arrays.asList(point3, point2, point1)));
        BBDPolygon ccw2 = new BBDPolygon(new ArrayList<>(Arrays.asList(point2, point1, point3)));
        BBDPolygon ccw3 = new BBDPolygon(new ArrayList<>(Arrays.asList(point1, point3, point2)));

        assertEquals(BBDGeometryUtils.COUNTERCLOCKWISE_POLYGON, ccw1.determineDirectionality());
        assertEquals(BBDGeometryUtils.COUNTERCLOCKWISE_POLYGON, ccw2.determineDirectionality());
        assertEquals(BBDGeometryUtils.COUNTERCLOCKWISE_POLYGON, ccw3.determineDirectionality());
    }

    @Test
    public void testDetermineDirectionalityFairlyLopsidedTri(){
        BBDPoint point1 = new BBDPoint(-1, 1);
        BBDPoint point2 = new BBDPoint(7, -5);
        BBDPoint point3 = new BBDPoint(6.5f, -5);

        BBDPolygon cw1 = new BBDPolygon(new ArrayList<>(Arrays.asList(point1, point2, point3)));
        BBDPolygon cw2 = new BBDPolygon(new ArrayList<>(Arrays.asList(point2, point3, point1)));
        BBDPolygon cw3 = new BBDPolygon(new ArrayList<>(Arrays.asList(point3, point1, point2)));

        assertEquals(BBDGeometryUtils.CLOCKWISE_POLYGON, cw1.determineDirectionality());
        assertEquals(BBDGeometryUtils.CLOCKWISE_POLYGON, cw2.determineDirectionality());
        assertEquals(BBDGeometryUtils.CLOCKWISE_POLYGON, cw3.determineDirectionality());

        BBDPolygon ccw1 = new BBDPolygon(new ArrayList<>(Arrays.asList(point3, point2, point1)));
        BBDPolygon ccw2 = new BBDPolygon(new ArrayList<>(Arrays.asList(point2, point1, point3)));
        BBDPolygon ccw3 = new BBDPolygon(new ArrayList<>(Arrays.asList(point1, point3, point2)));

        assertEquals(BBDGeometryUtils.COUNTERCLOCKWISE_POLYGON, ccw1.determineDirectionality());
        assertEquals(BBDGeometryUtils.COUNTERCLOCKWISE_POLYGON, ccw2.determineDirectionality());
        assertEquals(BBDGeometryUtils.COUNTERCLOCKWISE_POLYGON, ccw3.determineDirectionality());
    }

    @Test
    public void testEnforceDirectionality(){
        BBDPoint point1 = new BBDPoint(1, 1);
        BBDPoint point2 = new BBDPoint(1, -1);
        BBDPoint point3 = new BBDPoint(-1, 0.2f);

        BBDPolygon polygon = new BBDPolygon(new ArrayList<>(Arrays.asList(point1, point2, point3)));

        //test beforehand
        assertEquals(BBDGeometryUtils.CLOCKWISE_POLYGON, polygon.determineDirectionality());

        //check that it doesn't change anything
        polygon.enforceDirectionality(BBDGeometryUtils.CLOCKWISE_POLYGON);
        assertEquals(BBDGeometryUtils.CLOCKWISE_POLYGON, polygon.determineDirectionality());

        //try to change it
        polygon.enforceDirectionality(BBDGeometryUtils.COUNTERCLOCKWISE_POLYGON);
        assertEquals(BBDGeometryUtils.COUNTERCLOCKWISE_POLYGON, polygon.determineDirectionality());

        //try to leave it
        polygon.enforceDirectionality(BBDGeometryUtils.COUNTERCLOCKWISE_POLYGON);
        assertEquals(BBDGeometryUtils.COUNTERCLOCKWISE_POLYGON, polygon.determineDirectionality());

        //change it back
        polygon.enforceDirectionality(BBDGeometryUtils.CLOCKWISE_POLYGON);
        assertEquals(BBDGeometryUtils.CLOCKWISE_POLYGON, polygon.determineDirectionality());

        //try some invalid values
        polygon.enforceDirectionality(-3);
        assertEquals(BBDGeometryUtils.CLOCKWISE_POLYGON, polygon.determineDirectionality());

        polygon.enforceDirectionality(45);
        assertEquals(BBDGeometryUtils.CLOCKWISE_POLYGON, polygon.determineDirectionality());
    }

    @Test
    public void testDistanceToPolygon(){
        //set up some squares
        BBDPolygon controlPolygon = this.buildSquare();
        BBDPolygon copy = this.buildSquare();
        BBDPolygon overlapping = this.buildSquare();
        BBDPolygon adjacent = this.buildSquare();
        BBDPolygon contains = this.buildSquare();
        BBDPolygon separate = this.buildSquare();

        //modify most of them to create several types of scenarios
        overlapping.translate(0.2f, 0.2f);
        adjacent.translate(0.5f, 2);
        contains.scale(0.5f);
        separate.translate(3,3);

        assertEquals(0, controlPolygon.distanceSquaredToPolygon(copy));
        assertEquals(0, controlPolygon.distanceSquaredToPolygon(overlapping));
        assertEquals(0, controlPolygon.distanceSquaredToPolygon(adjacent));
        assertEquals(0, controlPolygon.distanceSquaredToPolygon(contains));
        assertEquals(2, controlPolygon.distanceSquaredToPolygon(separate));
    }

    @Test
    public void testDistanceToSegment(){
        BBDPolygon square = this.buildSquare();

        BBDSegment adjacentShorter = new BBDSegment(new BBDPoint(0,1), new BBDPoint(0.5f, 1));
        BBDSegment adjacentLonger = new BBDSegment(new BBDPoint(-4,1), new BBDPoint(4,1));
        BBDSegment contains = new BBDSegment(new BBDPoint(0.5f, 0.5f), new BBDPoint(0.5f, 0));
        BBDSegment crosses = new BBDSegment(new BBDPoint(-4,0), new BBDPoint(4,0));
        BBDSegment separate1 = new BBDSegment(new BBDPoint(-4,2), new BBDPoint(4,2));

        assertEquals(0, square.distanceSquaredToSegment(adjacentShorter), BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(0, square.distanceSquaredToSegment(adjacentLonger), BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(0, square.distanceSquaredToSegment(contains), BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(0, square.distanceSquaredToSegment(crosses), BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(1, square.distanceSquaredToSegment(separate1), BBDGeometryUtils.ALLOWABLE_DELTA);
    }

    @Test
    public void testDistanceToPoint(){
        BBDPolygon square = this.buildSquare();

        assertEquals(0, square.distanceSquaredToPoint(new BBDPoint(0,0)), BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(0, square.distanceSquaredToPoint(new BBDPoint(1,1)), BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(0, square.distanceSquaredToPoint(new BBDPoint(0,1)), BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(18, square.distanceSquaredToPoint(new BBDPoint(4,4)), BBDGeometryUtils.ALLOWABLE_DELTA);
    }

    @Test
    public void testEquals(){
        BBDPolygon square1 = this.buildSquare();
        BBDPolygon square2 = this.buildSquare();
        BBDPolygon square3 = this.buildSquare();
        BBDPolygon square4 = this.buildSquare();
        square3.enforceDirectionality(BBDGeometryUtils.COUNTERCLOCKWISE_POLYGON);
        square4.enforceDirectionality(BBDGeometryUtils.CLOCKWISE_POLYGON);

        assertEquals(square1, square1);
        assertEquals(square2, square1);
        assertEquals(square4, square3);

        assertNotEquals(square1, this.buildDiamond());

        ArrayList<BBDPoint> fiveLong = new ArrayList<>(square1.getPoints());
        fiveLong.add(new BBDPoint(0,3));
        BBDPolygon penta = new BBDPolygon(fiveLong);

        assertNotEquals(square1, penta);
    }

}
