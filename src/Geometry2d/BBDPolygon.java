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

    public BBDPoint[] getPoints(){
        return this.points;
    }

    public BBDSegment[] getSegments(){
        return this.segments;
    }

    /**
     * General constructor that takes in a series of points and
     * creates the polygon object
     * @param inputPoints points used to define the perimeter of the polygon
     */
    public BBDPolygon (BBDPoint[] inputPoints){
        ArrayList<BBDSegment> segments = new ArrayList<BBDSegment>();
        for(int index = 0; index< inputPoints.length; index++){
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
            if(x < minX){minX = x;}
            if(x > maxX){maxX = x;}
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
            if(y < minY){minY = y;}
            if(y > maxY){maxY = y;}

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
        BBDPoint center = this.center();
        for (BBDPoint point: this.points){
            point.scaleFromPoint(center, scaleFactor);
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
            if(x < minX){minX = x;}
            if(x > maxX){maxX = x;}
            if(y < minY){minY = y;}
            if(y > maxY){maxY = y;}
        }
        return new BBDPoint((minX+maxX)/2, (minY+maxY)/2);
    }

    /**
     * Check if the given point is on the perimeter of the polygon.
     * @param pointToCheck
     * @return
     */
    public boolean checkPointOnPerimeter(BBDPoint pointToCheck){
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
     * @return
     */
    public BBDSegment[] segmentIntersectPolygonList(BBDSegment segmentToCheck){
        ArrayList<BBDSegment> intersectingSegments = new ArrayList<BBDSegment>();

        for (BBDSegment segment: segments){
            boolean thisSegmentIntersects = false;
            // by default the segment intersection should check end points
            if(segment.intersects(segmentToCheck)){
                thisSegmentIntersects = true;
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
     * @return
     */
    public boolean checkSegmentIntersectPolygon(BBDSegment segmentToCheck){
        BBDSegment[] intersectionList = segmentIntersectPolygonList(segmentToCheck);

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
     * @return
     */
    public boolean checkPointInside(BBDPoint pointToCheck){
        BBDSegment segmentToCheck = new BBDSegment(pointToCheck, 0, this.width()+10);
        System.out.println(segmentToCheck);
        System.out.println(this.extendedToString());
        BBDSegment[] intersectionList = segmentIntersectPolygonList(segmentToCheck);
        System.out.println("Intersection length: "+intersectionList.length);

        // find unique intersection points
        ArrayList<BBDPoint> intersectionPoints = new ArrayList<BBDPoint>();

        BBDPoint intersection = null;
        for(BBDSegment seg: intersectionList){
            intersection = seg.interceptPoint(segmentToCheck);
            if(!intersectionPoints.contains(intersection)) {
                intersectionPoints.add(intersection);
            }
        }
        

        //if the segment intersects an odd number of things, than it is inside the polygon.
        if (intersectionPoints.size() % 2 ==1){
            return true;
        }else{
            return false;
        }
    }

    /**
     * Test if this polygon intersects another
     * @param otherPolygon
     * @return
     */
    public boolean checkPolygonIntersectsPolygon(BBDPolygon otherPolygon){
        for (BBDSegment otherSegment: otherPolygon.segments){
            if(this.checkSegmentIntersectPolygon(otherSegment)){
                return true;
            }
        }
        return false;
    }

    /**
     * Test if this polygon touches another
     * @param otherPolygon

     * @return
     */
    public boolean checkPolygonTouchesPolygon(BBDPolygon otherPolygon){
        for(BBDPoint otherPoint : otherPolygon.points){
            if (this.checkPointOnPerimeter(otherPoint)){
                return true;
            }
        }

        for(BBDPoint otherPoint : this.points){
            if (otherPolygon.checkPointOnPerimeter(otherPoint)){
                return true;
            }
        }
        return false;
    }

    /**
     * Test if this polygon contains another
     * @param otherPolygon
     * @return
     */
    public boolean checkPolygonContainsPolygon(BBDPolygon otherPolygon){
        for (BBDPoint otherPoint: otherPolygon.points){
            if (! this.checkPointInside(otherPoint)){
                return false;
            }
        }
        return true;
    }

    /**
     * Determine the distance to another polygon
     * @param otherPolygon
     * @return
     */
    public double distanceToPolygon(BBDPolygon otherPolygon){
        double minDist = Double.MAX_VALUE;

        for (BBDSegment thisSegment: this.segments){
            for (BBDSegment otherSegment: otherPolygon.segments){
                if (thisSegment.distanceToSegment(otherSegment) < minDist){
                    minDist = thisSegment.distanceToSegment(otherSegment);
                }
            }
        }
        return minDist;
    }

    /**
     * Determine the distance to another segment
     * @param otherSegment
     * @return
     */
    public double distanceToSegment (BBDSegment otherSegment){
        double minDist = Double.MAX_VALUE;

        for (BBDSegment thisSegment: this.segments){
            if (thisSegment.distanceToSegment(otherSegment) < minDist){
                minDist = thisSegment.distanceToSegment(otherSegment);
            }
        }
        return minDist;
    }

    /**
     * Determine the distance to another point
     * @param otherPoint
     * @return
     */
    public double distanceToPoint (BBDPoint otherPoint){
        double minDist = Double.MAX_VALUE;

        for (BBDSegment thisSegment: this.segments){
            if (thisSegment.distanceToPoint(otherPoint) < minDist){
                minDist = thisSegment.distanceToPoint(otherPoint);
            }
        }
        return minDist;
    }

    public String toString(){
        return "BBDPolygon object consisting of "+this.points.length+" vertices ";
    }

    public String extendedToString(){
        String aggregatedString = this.toString();
        for (BBDSegment seg : this.segments){
            aggregatedString += seg.toString()+" ";
        }
        return aggregatedString;
    }
}
