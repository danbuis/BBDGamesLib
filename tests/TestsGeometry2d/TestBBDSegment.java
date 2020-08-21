package TestsGeometry2d;

import Geometry2d.BBDPoint;
import Geometry2d.BBDSegment;

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
}
