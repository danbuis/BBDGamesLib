package Geometry2d;

import java.util.ArrayList;

public class BBDPolygon implements BBDGeometry{

    /**
     * Class to handle closed polygons.
     */

    // Pieces that define the polygon.
    // Users should not be able to modify these directly.
    private BBDPoint[] points;
    private BBDSegment[] segments;

    /**
     * General constructor that takes in a series of points and
     * creates the polygon object
     * @param inputPoints
     */
    public BBDPolygon (BBDPoint[] inputPoints){
        ArrayList<BBDSegment> segments = new ArrayList<BBDSegment>();
        for(int index =0; index< inputPoints.length; index++){
            int nextIndex = (index + 1) % inputPoints.length;
            segments.add(new BBDSegment(inputPoints[index], inputPoints[nextIndex]));
        }

        this.points = inputPoints;
        this.segments = segments.toArray(new BBDSegment[segments.size()]);
    }

    /**
     * The horizontal dimension of this polygon
     * @return
     */
    public double width(){
        double maxX = Double.NEGATIVE_INFINITY;
        double minX = Double.POSITIVE_INFINITY;

        for (BBDPoint point : points){
            double x = point.getXLoc();
            if(x > minX){minX = x;}
            if(x < maxX){maxX = x;}

        }
        return maxX - minX;
    }

    /**
     * The horizontal dimension of this polygon
     * @return
     */
    public double height(){
        double maxY = Double.NEGATIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;

        for (BBDPoint point : points){
            double y = point.getYLoc();
            if(y > minY){minY = y;}
            if(y < maxY){maxY = y;}

        }
        return maxY - minY;
    }


    @Override
    /**
     * Translate the polygon a specified amount in both cardinal directions
     */
    public void translate(double dx, double dy) {
        for (BBDPoint point: this.points){
            point.translate(dx, dy);
        }
    }

    @Override
    /**
     * Scale the polygon by a given amount
     */
    public void scale(double scaleFactor) {
        for (BBDPoint point: this.points){
            point.scaleFromPoint(this.center(), scaleFactor);
        }
    }

    @Override
    /**
     * Scale the polygon by a given amount centered on a given location
     */
    public void scaleFromPoint(BBDPoint centerOfScale, double scaleFactor) {
        for (BBDPoint point: this.points){
            point.scaleFromPoint(centerOfScale, scaleFactor);
        }
    }

    @Override
    /**
     * Rotate the polygon around the center.  Positive degrees are clockwise,
     * and negative degrees are counter-clockwise
     */
    public void rotate(double degrees) {
        this.rotateAroundPoint(this.center(), degrees);
    }

    @Override
    /**
     * Rotate the polygon around a specific point.  Positive degrees are clockwise,
     * and negative degrees are counter-clockwise
     */
    public void rotateAroundPoint(BBDPoint centerOfRotation, double degrees) {
        for (BBDPoint point: points){
            point.rotateAroundPoint(centerOfRotation, degrees);
        }
    }

    @Override
    /**
     * Calculates the geometric center of the polygon by finding the max/min x/y
     *  and then averaging.
     */
    public BBDPoint center() {
        double maxX = Double.NEGATIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;
        double minX = Double.POSITIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;

        for (BBDPoint point : points){
            double x = point.getXLoc();
            double y = point.getYLoc();
            if(x > minX){minX = x;}
            if(x < maxX){maxX = x;}
            if(y > minY){minY = y;}
            if(y < maxY){maxY = y;}
        }
        return new BBDPoint((minX+maxX)/2, (minY+maxY)/2);
    }

    /**
     * Check if the given point is on the perimeter of the polygon.
     * @param pointToCheck
     * @return
     */
    public boolean pointOnPerimeter(BBDPoint pointToCheck){
        for(BBDSegment segment: segments){
            if (segment.pointOnSegment(pointToCheck)){
                return true;
            }
        }
        return false;
    }

    /**
     * Function to create a list of polygon segments that intersect a given segment
     *
     * @param segmentToCheck Segment not part of polygon to check for intersection
     * @param countPerimeter Flag to indicate if the edge of the polygon counts as inside
     *                       True means that if the segment just touches the perimeter, such as
     *                       sharing a point, means that it intersects.
     * @return
     */

    public BBDSegment[] segmentIntersectPolygonList(BBDSegment segmentToCheck, boolean countPerimeter){
        ArrayList<BBDSegment> intersectingSegments = new ArrayList<BBDSegment>();

        for (BBDSegment segment: segments){
            boolean thisSegmentIntersects = false;
            // by default the segment intersection should check end points
            if(segment.intersects(segmentToCheck)){
                thisSegmentIntersects = true;
            }
            // if we aren't counting the perimeter as intersecting, see if we need to change
            // boolean back
            if (!countPerimeter){
                BBDPoint[] points = segment.getPoints();
                if (this.pointOnPerimeter(points[0]) || this.pointOnPerimeter(points[1])){
                    thisSegmentIntersects = false;
                }
            }

            if (thisSegmentIntersects){
                intersectingSegments.add(segment);
            }
        }
        return intersectingSegments.toArray(new BBDSegment[intersectingSegments.size()]);
    }

    /**
     * helper function to determine if a segment intersects the polygon.
     * @param segmentToCheck Segment not part of polygon to check for intersection
     * @param countPerimeter Flag to indicate if the edge of the polygon counts as inside
     *                      True means that if the segment just touches the perimeter, such as
     *                      sharing a point, means that it intersects.
     * @return
     */
    public boolean segmentIntersectPolygon(BBDSegment segmentToCheck, boolean countPerimeter){
        BBDSegment[] intersectionList = segmentIntersectPolygonList(segmentToCheck, countPerimeter);

        if (intersectionList.length == 0){
            return false;
        }else{
            return true;
        }
    }

    /**
     * Function to check if a point is inside the polygon.
     *
     * @param pointToCheck Point that is not part of the polygon to check
     * @param perimeterCountsAsInside Flag to indicate if the edge of the polygon counts as inside
     *                                True means that if the segment just touches the perimeter, such as
     *                                sharing a point, means that it intersects.
     * @return
     */
    public boolean pointInside(BBDPoint pointToCheck, boolean perimeterCountsAsInside){
        BBDSegment segmentToCheck = new BBDSegment(pointToCheck, 0, this.width()+10);
        BBDSegment[] intersectionList = segmentIntersectPolygonList(segmentToCheck, perimeterCountsAsInside);

        //if the segment intersects an odd number of things, than it is inside the polygon.
        if (intersectionList.length % 2 ==1){
            return true;
        }else{
            return false;
        }
    }
}
