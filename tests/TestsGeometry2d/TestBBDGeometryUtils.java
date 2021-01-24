package TestsGeometry2d;

import BBDGameLibrary.Geometry2d.BBDGeometryUtils;
import BBDGameLibrary.Geometry2d.BBDPoint;
import BBDGameLibrary.Geometry2d.BBDPolygon;
import BBDGameLibrary.Geometry2d.BBDSegment;
import BBDGameLibrary.Geometry2d.Exceptions.ParallelLinesException;
import BBDGameLibrary.TestUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


public class TestBBDGeometryUtils {

    @Test
    public void testOffsetOutwardConvex() {
        BBDPolygon square = TestUtils.buildSquare();
        try {
            //try offsetting.  It is already CW by default
            BBDPolygon offsetZero = BBDGeometryUtils.offsetPolygon(square,1f, 0, 0);
            BBDPolygon expectedZero = new BBDPolygon(new BBDPoint[]{new BBDPoint(2, 1), new BBDPoint(2, -1), new BBDPoint(-1, -1), new BBDPoint(-1, 1)});
            assertEquals(new BBDPoint(2, 1), offsetZero.getPoints().get(0));
            assertEquals(expectedZero, offsetZero);

            //reverse it and try the same thing to make sure the right offset modifier is created
            BBDPoint point1 = new BBDPoint(1, 1);
            BBDPoint point2 = new BBDPoint(1, -1);
            BBDPoint point3 = new BBDPoint(-1, -1);
            BBDPoint point4 = new BBDPoint(-1, 1);

            BBDPoint[] points = {point2, point1, point4, point3};

            BBDPolygon reverseSquare = new BBDPolygon(points);
            BBDPolygon expectedReverse = new BBDPolygon(new BBDPoint[]{new BBDPoint(2, -1), new BBDPoint(2, 1), new BBDPoint(-1, 1), new BBDPoint(-1, -1)});

            offsetZero = BBDGeometryUtils.offsetPolygon(reverseSquare, 1, 0, 0);
            assertEquals(expectedReverse, offsetZero);

            //offset a few single sides
            BBDPolygon offsetOne = BBDGeometryUtils.offsetPolygon(square, 1, 1, 1);
            BBDPolygon expectedOne = new BBDPolygon(new BBDPoint[]{new BBDPoint(1, 1), new BBDPoint(1, -2), new BBDPoint(-1, -2), new BBDPoint(-1, 1)});
            assertEquals(expectedOne, offsetOne);

            BBDPolygon offsetTwo = BBDGeometryUtils.offsetPolygon(square,1, 2, 2);
            BBDPolygon expectedTwo = new BBDPolygon(new BBDPoint[]{new BBDPoint(1, 1), new BBDPoint(1, -1), new BBDPoint(-2, -1), new BBDPoint(-2, 1)});
            System.out.println(offsetTwo.extendedToString());
            assertEquals(expectedTwo, offsetTwo);

            BBDPolygon offsetThree = BBDGeometryUtils.offsetPolygon(square,1, 3, 3);
            BBDPolygon expectedThree = new BBDPolygon(new BBDPoint[]{new BBDPoint(1, 2), new BBDPoint(1, -1), new BBDPoint(-1, -1), new BBDPoint(-1, 2)});
            System.out.println(offsetThree.extendedToString());
            assertEquals(expectedThree, offsetThree);

            //offset 2 sides
            BBDPolygon offsetTwoContig = BBDGeometryUtils.offsetPolygon(square,1, 0, 1);
            BBDPolygon expectedTwoContig = new BBDPolygon(new BBDPoint[]{new BBDPoint(2, 1), new BBDPoint(2, -2), new BBDPoint(-1, -2), new BBDPoint(-1, 1)});
            assertEquals(expectedTwoContig, offsetTwoContig);

            //offset 3 sides
            BBDPolygon offsetThreeContig = BBDGeometryUtils.offsetPolygon(square,1, 0, 2);
            BBDPolygon expectedThreeContig = new BBDPolygon(new BBDPoint[]{new BBDPoint(2, 1), new BBDPoint(2, -2), new BBDPoint(-2, -2), new BBDPoint(-2, 1)});
            assertEquals(expectedThreeContig, offsetThreeContig);

            //offset 4 sides
            BBDPolygon offsetFourContig = BBDGeometryUtils.offsetPolygon(square,1, 0, 3);
            BBDPolygon expectedFourContig = new BBDPolygon(new BBDPoint[]{new BBDPoint(2, 2), new BBDPoint(2, -2), new BBDPoint(-2, -2), new BBDPoint(-2, 2)});
            assertEquals(expectedFourContig, offsetFourContig);

            //offset 4 sides going around the 0 index
            BBDPolygon offsetOverflow = BBDGeometryUtils.offsetPolygon(square,1, 2, 1);
            assertEquals(expectedFourContig, offsetOverflow);

            //offset 4 sides with alt method signature
            BBDPolygon offsetWhole = BBDGeometryUtils.offsetPolygon(square,1);
            assertEquals(expectedFourContig, offsetWhole);

            //do a test to ensure that the original shape hasn't had its segments rotated.
            square = TestUtils.buildSquare();
            BBDSegment firstSeg = square.getSegments().get(0);
            BBDGeometryUtils.offsetPolygon(square,1);
            BBDSegment newFirstSeg = square.getSegments().get(0);
            assertEquals(firstSeg, newFirstSeg);
        } catch (ParallelLinesException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testOffsetInwardConvex() {
        BBDPolygon square = TestUtils.buildSquare();
        try {
            //try offsetting.  It is already CW by default
            BBDPolygon offsetZero = BBDGeometryUtils.offsetPolygon(square,-0.5f, 0, 0);
            BBDPolygon expectedZero = new BBDPolygon(new BBDPoint[]{new BBDPoint(0.5f, 1), new BBDPoint(0.5f, -1), new BBDPoint(-1, -1), new BBDPoint(-1, 1)});
            assertEquals(new BBDPoint(0.5f, 1), offsetZero.getPoints().get(0));
            assertEquals(expectedZero, offsetZero);

            //reverse it and try the same thing to make sure the right offset modifier is created
            BBDPoint point1 = new BBDPoint(1, 1);
            BBDPoint point2 = new BBDPoint(1, -1);
            BBDPoint point3 = new BBDPoint(-1, -1);
            BBDPoint point4 = new BBDPoint(-1, 1);

            BBDPoint[] points = {point2, point1, point4, point3};

            BBDPolygon reverseSquare = new BBDPolygon(points);
            BBDPolygon expectedReverse = new BBDPolygon(new BBDPoint[]{new BBDPoint(0.5f, -1), new BBDPoint(0.5f, 1), new BBDPoint(-1, 1), new BBDPoint(-1, -1)});

            offsetZero = BBDGeometryUtils.offsetPolygon(reverseSquare, -0.5f, 0, 0);
            assertEquals(expectedReverse, offsetZero);

            //offset a few single sides
            BBDPolygon offsetOne = BBDGeometryUtils.offsetPolygon(square,-0.5f, 1, 1);
            BBDPolygon expectedOne = new BBDPolygon(new BBDPoint[]{new BBDPoint(1, 1), new BBDPoint(1, -0.5f), new BBDPoint(-1, -0.5f), new BBDPoint(-1, 1)});
            assertEquals(expectedOne, offsetOne);

            BBDPolygon offsetTwo = BBDGeometryUtils.offsetPolygon(square,-0.5f, 2, 2);
            BBDPolygon expectedTwo = new BBDPolygon(new BBDPoint[]{new BBDPoint(1, 1), new BBDPoint(1, -1), new BBDPoint(-0.5f, -1), new BBDPoint(-0.5f, 1)});
            assertEquals(expectedTwo, offsetTwo);

            BBDPolygon offsetThree = BBDGeometryUtils.offsetPolygon(square,-0.5f, 3, 3);
            BBDPolygon expectedThree = new BBDPolygon(new BBDPoint[]{new BBDPoint(1, 0.5f), new BBDPoint(1, -1), new BBDPoint(-1, -1), new BBDPoint(-1, 0.5f)});
            assertEquals(expectedThree, offsetThree);

            //offset 2 sides
            BBDPolygon offsetTwoContig = BBDGeometryUtils.offsetPolygon(square,-0.5f, 0, 1);
            BBDPolygon expectedTwoContig = new BBDPolygon(new BBDPoint[]{new BBDPoint(0.5f, 1), new BBDPoint(0.5f, -0.5f), new BBDPoint(-1, -0.5f), new BBDPoint(-1, 1)});
            assertEquals(expectedTwoContig, offsetTwoContig);

            //offset 3 sides
            BBDPolygon offsetThreeContig = BBDGeometryUtils.offsetPolygon(square,-0.5f, 0, 2);
            BBDPolygon expectedThreeContig = new BBDPolygon(new BBDPoint[]{new BBDPoint(0.5f, 1), new BBDPoint(0.5f, -0.5f), new BBDPoint(-0.5f, -0.5f), new BBDPoint(-0.5f, 1)});
            assertEquals(expectedThreeContig, offsetThreeContig);

            //offset 4 sides
            BBDPolygon offsetFourContig = BBDGeometryUtils.offsetPolygon(square,-0.5f, 0, 3);
            BBDPolygon expectedFourContig = new BBDPolygon(new BBDPoint[]{new BBDPoint(0.5f, 0.5f), new BBDPoint(0.5f, -0.5f), new BBDPoint(-0.5f, -0.5f), new BBDPoint(-0.5f, 0.5f)});
            assertEquals(expectedFourContig, offsetFourContig);

            //offset 4 sides going around the 0 index
            BBDPolygon offsetOverflow = BBDGeometryUtils.offsetPolygon(square,-0.5f, 2, 1);
            assertEquals(expectedFourContig, offsetOverflow);

            //offset 4 sides with alt method signature
            BBDPolygon offsetWhole = BBDGeometryUtils.offsetPolygon(square,-0.5f);
            assertEquals(expectedFourContig, offsetWhole);

            //do a test to ensure that the original shape hasn't had its segments rotated.
            square = TestUtils.buildSquare();
            BBDSegment firstSeg = square.getSegments().get(0);
            BBDGeometryUtils.offsetPolygon(square,-0.5f);
            BBDSegment newFirstSeg = square.getSegments().get(0);
            assertEquals(firstSeg, newFirstSeg);
        } catch (ParallelLinesException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCircleCreation(){
        BBDPolygon test = BBDGeometryUtils.createCircle(new BBDPoint(0,0), 3, 360);

        assertEquals(360, test.getPoints().size());
        for(BBDPoint point: test.getPoints()){
            System.out.println(point);
        }
        assertEquals(new BBDPoint(0,3), test.getPoints().get(0));
        assertEquals(new BBDPoint(3,0), test.getPoints().get(90));
    }

    @Test
    public void testConvexPolygonIntersection(){
        BBDPolygon controlPolygon = TestUtils.buildSquare();
        BBDPolygon overlapping = TestUtils.buildSquare();
        BBDPolygon adjacent = TestUtils.buildSquare();
        BBDPolygon contains = TestUtils.buildSquare();
        BBDPolygon separate = TestUtils.buildSquare();

        //modify most of them to create several types of scenarios
        overlapping.translate(0.2f, 0.2f);
        adjacent.translate(0.5f, 2);
        contains.scale(0.5f);
        separate.translate(3,3);

        ArrayList<BBDPoint> overlappingPoints = new ArrayList<>();
        overlappingPoints.add(new BBDPoint(1,1));
        overlappingPoints.add(new BBDPoint(1,-0.8f));
        overlappingPoints.add(new BBDPoint(-0.8f,-0.8f));
        overlappingPoints.add(new BBDPoint(-0.8f, 1));

        assertEquals(new BBDPolygon(overlappingPoints), BBDGeometryUtils.createPolygonIntersection(controlPolygon, overlapping));

        // one contains the other
        assertEquals(contains, BBDGeometryUtils.createPolygonIntersection(controlPolygon, contains));
        // check symmetry of the above
        assertEquals(contains, BBDGeometryUtils.createPolygonIntersection(contains, controlPolygon));

        //check no intersection
        assertNull(BBDGeometryUtils.createPolygonIntersection(controlPolygon, separate));

        //check if polygons are the same
        assertEquals(controlPolygon, BBDGeometryUtils.createPolygonIntersection(controlPolygon, controlPolygon));

        //test that using the copyPolygon() has indeed made copies and not impacted the originals
        assertEquals(4, controlPolygon.getPoints().size());

        //check polygons that have more than 2 intersection points
        ArrayList<BBDPoint> wideList = new ArrayList<>();
        wideList.add(new BBDPoint(2, 0.5f));
        wideList.add(new BBDPoint(-2, 0.5f));
        wideList.add(new BBDPoint(-2, -0.5f));
        wideList.add(new BBDPoint(2, -0.5f));
        BBDPolygon widePoly = new BBDPolygon(wideList);

        ArrayList<BBDPoint> testList = new ArrayList<>();
        testList.add(new BBDPoint(1, 0.5f));
        testList.add(new BBDPoint(-1, 0.5f));
        testList.add(new BBDPoint(-1, -0.5f));
        testList.add(new BBDPoint(1, -0.5f));
        BBDPolygon testPoly = new BBDPolygon(testList);

        assertEquals(testPoly, BBDGeometryUtils.createPolygonIntersection(controlPolygon, widePoly));
    }
}
