package Geometry2d;

public class BBDGeometryUtils {
    public static float ALLOWABLE_DELTA = 0.0001f;

    public static float distance(BBDSegment seg, BBDPoint point){
        return seg.distanceSquaredToPoint(point);
    }

    public static float distance(BBDPoint point, BBDSegment seg){
        return seg.distanceSquaredToPoint(point);
    }

    public static float distance(BBDPolygon poly, BBDPoint point){
        return poly.distanceSquaredToPoint(point);
    }

    public static float distance(BBDPoint point, BBDPolygon poly){
        return poly.distanceSquaredToPoint(point);
    }

    public static float distance(BBDSegment seg, BBDPolygon poly){
        return poly.distanceSquaredToSegment(seg);
    }

    public static float distance(BBDPolygon poly, BBDSegment seg){
        return poly.distanceSquaredToSegment(seg);
    }

    public static float distance(BBDSegment seg1, BBDSegment seg2){
        return seg1.distanceSquaredToSegment(seg2);
    }

    public static float distance(BBDPoint point1, BBDPoint point2){
        return point1.distanceSquaredToPoint(point2);
    }

    public static float distance(BBDPolygon poly1, BBDPolygon poly2){
        return poly1.distanceSquaredToPolygon(poly2);
    }

    public static boolean checkParallelSegments(BBDSegment seg1, BBDSegment seg2){
        return seg1.slopeInDegrees() == seg2.slopeInDegrees();
    }
}
