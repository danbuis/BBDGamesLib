package Geometry2d;

public class BBDGeometryUtils {

    public static double distance(BBDSegment seg, BBDPoint point){
        return seg.distanceToPoint(point);
    }

    public static double distance(BBDPoint point, BBDSegment seg){
        return seg.distanceToPoint(point);
    }

    public static double distance(BBDPolygon poly, BBDPoint point){
        return poly.distanceToPoint(point);
    }

    public static double distance(BBDPoint point, BBDPolygon poly){
        return poly.distanceToPoint(point);
    }

    public static double distance(BBDSegment seg, BBDPolygon poly){
        return poly.distanceToSegment(seg);
    }

    public static double distance(BBDPolygon poly, BBDSegment seg){
        return poly.distanceToSegment(seg);
    }

    public static double distance(BBDSegment seg1, BBDSegment seg2){
        return seg1.distanceToSegment(seg2);
    }

    public static double distance(BBDPoint point1, BBDPoint point2){
        return point1.distanceToPoint(point2);
    }

    public static double distance(BBDPolygon poly1, BBDPolygon poly2){
        return poly1.distanceToPolygon(poly2);
    }

    public static boolean checkParallelSegments(BBDSegment seg1, BBDSegment seg2){
        return seg1.slopeInDegrees() == seg2.slopeInDegrees();
    }
}
