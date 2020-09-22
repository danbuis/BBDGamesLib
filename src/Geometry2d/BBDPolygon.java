package Geometry2d;

import Geometry2d.Exceptions.ParallelLinesException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

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
        ArrayList<BBDSegment> segments = new ArrayList<>();
        for(int index = 0; index< inputPoints.length; index++){
            int nextIndex = (index + 1) % inputPoints.length;
            segments.add(new BBDSegment(inputPoints[index], inputPoints[nextIndex]));
        }

        this.points = inputPoints;
        this.segments = segments.toArray(new BBDSegment[0]);
    }

    /**
     * The horizontal dimension of this polygon
     * @return max width of the polygon
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
     * @return max height of the polygon
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


    /**
     *
     * Translate the polygon a specified amount in both cardinal directions
     *
     * @param dx distance on x axis
     * @param dy distance on y axis
     */
    @Override
    public void translate(double dx, double dy) {
        for (BBDPoint point: this.points){
            point.translate(dx, dy);
        }
    }

    /**
     * Scale the polygon by a given amount using the center as the location to scale from
     * @param scaleFactor factor to scale by
     */
    @Override
    public void scale(double scaleFactor) {
        BBDPoint center = this.center();
        for (BBDPoint point: this.points){
            point.scaleFromPoint(center, scaleFactor);
        }
    }

    /**
     * Scale the polygon by a given amount centered on a given location
     * @param centerOfScale point from which the polygon is scaled
     * @param scaleFactor factor to scale by
     */
    @Override
    public void scaleFromPoint(BBDPoint centerOfScale, double scaleFactor) {
        for (BBDPoint point: this.points){
            point.scaleFromPoint(centerOfScale, scaleFactor);
        }
    }

    /**
     * Rotate the polygon around the center.  Positive degrees are clockwise,
     * and negative degrees are counter-clockwise
     * @param degrees how much to rotate
     */
    @Override
    public void rotate(double degrees) {
        this.rotateAroundPoint(this.center(), degrees);
    }

    /**
     * Rotate the polygon around a specific point.  Positive degrees are clockwise,
     * and negative degrees are counter-clockwise
     * @param centerOfRotation point from which the polygon is rotated
     * @param degrees how much to rotate
     */
    @Override
    public void rotateAroundPoint(BBDPoint centerOfRotation, double degrees) {
        for (BBDPoint point: points){
            point.rotateAroundPoint(centerOfRotation, degrees);
        }
    }


    /**
     * Calculates the geometric center of the polygon by finding the max/min x/y
     *  and then averaging.
     *  @return geometric center of polygon
     */
    @Override
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
     * A different method for determining the center of a poly.  This one does an
     * average of all the coordinates, rather than an absolute center using the max and min
     * bounds like center() does.
     * @return the average of all the points
     */
    public BBDPoint centerAverage(){
        double aggX = 0;
        double aggY = 0;

        for (BBDPoint point : points){
            aggX += point.getXLoc();
            aggY += point.getYLoc();
        }

        int size = this.points.length;

        return new BBDPoint(aggX/size, aggY / size);
    }

    /**
     * Check if the given point is on the perimeter of the polygon.
     * @param pointToCheck point to check
     * @return boolean stating is the pointToCheck on the perimeter
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
     * @return list of segments that the given segment intersects
     */
    public BBDSegment[] segmentIntersectPolygonList(BBDSegment segmentToCheck){
        ArrayList<BBDSegment> intersectingSegments = new ArrayList<>();

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
        return intersectingSegments.toArray(new BBDSegment[0]);
    }

    /**
     * helper function to determine if a segment intersects the polygon.
     * @param segmentToCheck Segment not part of polygon to check for intersection
     * @return boolean stating if this segment intersects the polygon
     */
    public boolean checkSegmentIntersectPolygon(BBDSegment segmentToCheck){
        BBDSegment[] intersectionList = segmentIntersectPolygonList(segmentToCheck);

        return (intersectionList.length != 0);
    }

    /**
     * Function to check if a point is inside the polygon.
     *
     * @param pointToCheck Point that is not part of the polygon to check
     * @return boolean stating if the point is inside the polygon.
     */
    public boolean checkPointInside(BBDPoint pointToCheck){
        BBDSegment segmentToCheck = new BBDSegment(pointToCheck, 0, this.width()+10);
        BBDSegment[] intersectionList = segmentIntersectPolygonList(segmentToCheck);

        // find unique intersection points
        ArrayList<BBDPoint> intersectionPoints = new ArrayList<>();

        BBDPoint intersection = null;
        for(BBDSegment seg: intersectionList){
            try {
                intersection = seg.interceptPoint(segmentToCheck);
            } catch(ParallelLinesException e){
                if(seg.intersects(segmentToCheck)){

                }
            }
            if(intersection != null && !intersectionPoints.contains(intersection)) {
                intersectionPoints.add(intersection);
            }
        }

        //if the segment intersects an odd number of things, than it is inside the polygon.
        return intersectionPoints.size() % 2 ==1;
    }

    /**
     * Test if this polygon intersects another
     * @param otherPolygon the other polygon that this one might be intersecting
     * @return boolean stating if these polygons intersect
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
     * @param otherPolygon other polygon that this one might be touching
     * @return boolean stating if these polygons touch
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
     * @param otherPolygon other polygon that might be contained within this one
     * @return boolean stating if the given polygon is contained within this one
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
     * @param otherPolygon other polygon to measure distance to
     * @return distance to the other polygon
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
     * @param otherSegment other segment to measure distance to
     * @return distance to the other segment
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
     * @param otherPoint other point to measure distance to
     * @return distance to the other point
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

    /**
     * Calculate the area of this polygon
     * @return area
     */
    public double area(){
        double accumulatedTotal = 0;
        ArrayList<BBDPoint> remainingPoints = new ArrayList<>(Arrays.asList(this.points));
        
        while(remainingPoints.size() >= 3){
            BBDPolygon temp = new BBDPolygon(remainingPoints.toArray(new BBDPoint[0]));
            //cycle through 3 adjacent vertices until we find a triangle with an interior inside the polygon
            BBDPolygon test = null;
            for(int i=1; i< remainingPoints.size()-1; i++){
                test = new BBDPolygon(new BBDPoint[]{remainingPoints.get(i - 1), remainingPoints.get(i), remainingPoints.get(i + 1)});
                BBDPoint center = test.centerAverage();
                if(temp.checkPointOnPerimeter(center) || temp.checkPointInside(center)){
                    BBDPoint A = remainingPoints.get(i - 1);
                    BBDPoint B = remainingPoints.get(i);
                    BBDPoint C = remainingPoints.get(i + 1);

                    accumulatedTotal += Math.abs((A.getXLoc()*(B.getYLoc()-C.getYLoc())
                            + B.getXLoc()*(C.getYLoc()-A.getYLoc())
                            + C.getXLoc()*(A.getYLoc()-B.getYLoc()))/ 2.0);
                    remainingPoints.remove(i);
                    break;
                }
            }
        }

        return accumulatedTotal;
    }

    public String toString(){
        return "BBDPolygon object consisting of "+this.points.length+" vertices ";
    }

    public String extendedToString(){
        StringBuilder aggregatedString = new StringBuilder(this.toString());
        for (BBDPoint point : this.points){
            aggregatedString.append(point.toString()).append(" ");
        }
        return aggregatedString.toString();
    }

    @Override
    public boolean equals(Object other){
        if (this == other){
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        BBDPolygon otherPolygon = (BBDPolygon)other;
        // check size first
        if (otherPolygon.points.length != this.points.length){
            return false;
        }

        // then rotate through the points until we have a sequential match
        // checking forward and backward ordered lists
        ArrayList<BBDPoint> forwardList = new ArrayList<>(Arrays.asList(this.points));
        ArrayList<BBDPoint> backwardList = new ArrayList<>(Arrays.asList(this.points));
        Collections.reverse(backwardList);

        int count = this.points.length;
        for (int i = 0; i< count; i++){
            int index = 0;
            while(otherPolygon.points[index].equals(forwardList.get(index))
                    || otherPolygon.points[index].equals(backwardList.get(index))) {
                index++;
            }
            // if we made it all the way through the polygon's points
            if (index == count-1){
                return true;
            }
            Collections.rotate(forwardList, 1);
            Collections.rotate(backwardList, 1);
            i++;
        }

        return false;
    }
}
