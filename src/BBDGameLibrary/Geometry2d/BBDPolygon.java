package BBDGameLibrary.Geometry2d;

import BBDGameLibrary.Geometry2d.Exceptions.ParallelLinesException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BBDPolygon implements BBDGeometry{

    /**
     * Class to handle closed polygons.
     */

    // Pieces that define the polygon.
    // Users should not be able to modify these directly.
    private ArrayList<BBDPoint> points;
    private ArrayList<BBDSegment> segments;

    public ArrayList<BBDPoint> getPoints(){
        return this.points;
    }

    public ArrayList<BBDSegment> getSegments(){
        return this.segments;
    }

    /**
     * General constructor that takes in a series of points and
     * creates the polygon object
     * @param inputPoints points used to define the perimeter of the polygon
     */
    public BBDPolygon (ArrayList<BBDPoint> inputPoints){
        ArrayList<BBDSegment> segments = new ArrayList<>();
        for(int index = 0; index < inputPoints.size(); index++){
            int nextIndex = (index + 1) % inputPoints.size();
            segments.add(new BBDSegment(inputPoints.get(index), inputPoints.get(nextIndex)));
        }

        this.points = inputPoints;
        this.segments = segments;
    }

    private void buildSegments(ArrayList<BBDPoint> inputPoints){
        ArrayList<BBDSegment> segments = new ArrayList<>();
        for(int index = 0; index< inputPoints.size(); index++){
            int nextIndex = (index + 1) % inputPoints.size();
            segments.add(new BBDSegment(inputPoints.get(index), inputPoints.get(nextIndex)));
        }

        this.points = inputPoints;
        this.segments = segments;
    }


    /**
     * The horizontal dimension of this polygon
     * @return max width of the polygon
     */
    public float width(){
        float maxX = maxX();
        float minX = minX();

        return maxX - minX;
    }

    /**
     * The horizontal dimension of this polygon
     * @return max height of the polygon
     */
    public float height(){
        float maxY = maxY();
        float minY = minY();

        return maxY - minY;
    }

    /**
     * find the maximum X value
     * @return maximum X value
     */
    public float maxX(){
        float maxX = Float.NEGATIVE_INFINITY;
        for (BBDPoint point : points){
            float x = point.getXLoc();
            if(x > maxX){maxX = x;}
        }
        return maxX;
    }

    /**
     * find the maximum Y value
     * @return maximum Y value
     */
    public float maxY(){
        float maxY = Float.NEGATIVE_INFINITY;
        for (BBDPoint point : points){
            float y = point.getYLoc();
            if(y > maxY){maxY = y;}
        }
        return maxY;
    }

    /**
     * find the minimum X value
     * @return minimum X value
     */
    public float minX(){
        float minX = Float.POSITIVE_INFINITY;
        for (BBDPoint point : points){
            float x = point.getXLoc();
            if(x < minX){minX = x;}
        }
        return minX;
    }

    /**
     * find the minimum Y value
     * @return minimum Y value
     */
    public float minY(){
        float minY = Float.POSITIVE_INFINITY;
        for (BBDPoint point : points){
            float y = point.getYLoc();
            if(y < minY){minY = y;}
        }
        return minY;
    }


    /**
     *
     * Translate the polygon a specified amount in both cardinal directions
     *
     * @param dx distance on x axis
     * @param dy distance on y axis
     */
    @Override
    public void translate(float dx, float dy) {
        for (BBDPoint point: this.points){
            point.translate(dx, dy);
        }
    }

    /**
     * Scale the polygon by a given amount using the center as the location to scale from
     * @param scaleFactor factor to scale by
     */
    @Override
    public void scale(float scaleFactor) {
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
    public void scaleFromPoint(BBDPoint centerOfScale, float scaleFactor) {
        for (BBDPoint point: this.points){
            point.scaleFromPoint(centerOfScale, scaleFactor);
        }
    }

    /**
     * Rotate the polygon around the center.  Positive radians are clockwise,
     * and negative radians are counter-clockwise
     * @param radians how much to rotate
     */
    @Override
    public void rotate(float radians) {
        this.rotateAroundPoint(this.center(), radians);
    }

    /**
     * Rotate the polygon around a specific point.  Positive radians are clockwise,
     * and negative radians are counter-clockwise
     * @param centerOfRotation point from which the polygon is rotated
     * @param radians how much to rotate
     */
    @Override
    public void rotateAroundPoint(BBDPoint centerOfRotation, float radians) {
        for (BBDPoint point: points){
            point.rotateAroundPoint(centerOfRotation, radians);
        }
    }


    /**
     * Calculates the geometric center of the polygon by finding the max/min x/y
     *  and then averaging.
     *  @return geometric center of polygon
     */
    @Override
    public BBDPoint center() {
        float maxX = Float.NEGATIVE_INFINITY;
        float maxY = Float.NEGATIVE_INFINITY;
        float minX = Float.POSITIVE_INFINITY;
        float minY = Float.POSITIVE_INFINITY;

        for (BBDPoint point : points){
            float x = point.getXLoc();
            float y = point.getYLoc();
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
        float aggX = 0;
        float aggY = 0;

        for (BBDPoint point : points){
            aggX += point.getXLoc();
            aggY += point.getYLoc();
        }

        int size = this.points.size();

        return new BBDPoint(aggX/size, aggY / size);
    }

    /**
     * Insert a point into the polygon's perimeter
     * @param point new point
     * @param index where to insert in the order
     */
    public boolean insertPoint(BBDPoint point, int index){
        if(index >= 0 && index < this.points.size()) {
            this.points.add(index, point);
            this.buildSegments(this.points);
            return true;
        }else{
            return false;
        }
    }

    /**
     * Attempt to delete a point at an index
     * @param index index of the point to delte
     * @return was a point successfully deleted
     */
    public boolean deletePoint(int index){
        if(index >= 0 && index < this.points.size()) {
            this.points.remove(index);
            this.buildSegments(this.points);
            return true;
        }else{
            return false;
        }
    }

    /**
     * Attempt to delete a specific point
     * @param point point to delete
     * @return was a point successfully deleted
     */
    public boolean deletePoint(BBDPoint point){
        if(this.points.contains(point)) {
            this.points.remove(point);
            this.buildSegments(this.points);
            return true;
        }else{
            return false;
        }
    }

    /**
     * Translate a single point
     * @param index what point to move
     * @param dx x-axis translation
     * @param dy y-axis translation
     */
    public boolean movePoint(int index, float dx, float dy){
        if(index >= 0 && index < this.points.size()) {
            this.points.get(index).translate(dx, dy);
            this.buildSegments(this.points);
            return true;
        }else{
            return false;
        }
    }

    public boolean moveContiguousPoints(int startIndex, int endIndex, float dx, float dy){
        if(startIndex <= endIndex && startIndex<=0 && endIndex < this.points.size()){
            for (int i=startIndex; i<=endIndex; i++){
                this.points.get(i).translate(dx, dy);
            }
            this.buildSegments(this.points);
            return true;
        }else{
            return false;
        }
    }


    /**
     * Ensure that this polygon's vertices go in a specific direction.
     * @param direction desired direction, should be a relevant constant from BBDGeometryUtils.
     */
    public void enforceDirectionality(int direction){
        int currentDirection = this.determineDirectionality();

        if (currentDirection != direction && (direction == 0 || direction == 1)){
            Collections.reverse(this.points);
        }
    }

    /**
     * Determine if the vertices of the polygon are ordered clockwise or counterclockwise
     * @return an integer from BBDGeometryUtils designating in what direction the vertices are.
     */
    public int determineDirectionality(){
        BBDPolygon[] triangles = null;
        if(this.points.size() !=3){
            triangles = new BBDPolygon[]{this};
        } else{
            triangles = this.decomposeIntoTriangles(null);
        }
        //the first triangle is likely to be the cleanest one that doesn't skip points
        return this.determineDirectionality(triangles[0]);
    }

    /**
     * An internal method to determine the directionality of a polygon.
     * @param triangle A triangle.  It is assumed that this triangle is NOT 3 colinear points.
     * @return an integer from BBDGeometryUtils designating in what direction the vertices are.
     */
    private int determineDirectionality(BBDPolygon triangle){
        BBDPoint averageCenter = triangle.centerAverage();

        float angle1 = averageCenter.angleToOtherPoint(triangle.points.get(0));
        float angle2 = averageCenter.angleToOtherPoint(triangle.points.get(1));

        angle1 += 2 * Math.PI;
        angle2 += 2 * Math.PI;

        float angleDiff1 = angle2 - angle1;
        float angleDiff2 = angle1 - angle2;

        if(angle1 > angle2){
            if(angleDiff2 > Math.PI) return BBDGeometryUtils.COUNTERCLOCKWISE_POLYGON;
            else return BBDGeometryUtils.CLOCKWISE_POLYGON;
        } else{
            if (angleDiff1 > Math.PI) return BBDGeometryUtils.CLOCKWISE_POLYGON;
            else return BBDGeometryUtils.COUNTERCLOCKWISE_POLYGON;
        }

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
     * Create a list of intersection points for a segment and this polygon.
     * Can also include the end points of the segment if they are on the polygon perimeter.
     * @param segmentToCheck  test segment
     * @return list of intersection points
     */
    public BBDPoint[] segmentIntersectPolygonPoints(BBDSegment segmentToCheck){
        BBDSegment[] intersectionList = segmentIntersectPolygonList(segmentToCheck);

        // find unique intersection points
        ArrayList<BBDPoint> intersectionPoints = new ArrayList<>();

        BBDPoint intersection = null;
        for(BBDSegment seg: intersectionList){
            try {
                intersection = seg.interceptPoint(segmentToCheck);
            } catch(ParallelLinesException e){
                /*if we get here, it is because the lines are parallel. If they are colinear, we will do nothing
                 * , because the perimeter segment is guaranteed to have both points on the test segment
                 * due the way the test is created.  Therefore both points will be covered by the
                 * neighboring segments.  If they aren't then there is no intersection anyway, so no change.
                 * But we need the try catch because it is a thrown exception.
                 */
            }
            if(intersection != null && !intersectionPoints.contains(intersection)) {
                intersectionPoints.add(intersection);
            }
        }
        return intersectionPoints.toArray(new BBDPoint[0]);
    }

    /**
     * helper function to determine if a segment intersects the polygon.
     * @param segmentToCheck Segment not part of polygon to check for intersection
     * @return boolean stating if this segment intersects the polygon
     */
    public boolean checkSegmentIntersectPolygon(BBDSegment segmentToCheck){
        BBDPoint[] intersectionList = segmentIntersectPolygonPoints(segmentToCheck);

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
        BBDPoint[] intersectionPoints = this.segmentIntersectPolygonPoints(segmentToCheck);

        //if the segment intersects an odd number of things, than it is inside the polygon.
        return intersectionPoints.length % 2 ==1 || this.checkPointOnPerimeter(pointToCheck);
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
        //if they don't intersect traditionally, we need to make sure that one isn't contained
        //as that would technically count as an intersection.
        boolean thisContainsOther = this.checkPolygonContainsPolygon(otherPolygon);
        boolean otherContainsThis = otherPolygon.checkPolygonContainsPolygon(this);

        return thisContainsOther || otherContainsThis;
    }


    /**
     * Check if a segment touches a polygon.  Touching is defines as just touching the edge and not going into the
     * interior at all.  All touches would also be intersections, but an intersection can include the interior.
     * @param segment the segment that might be touching the polygon
     * @return is the segment touching the polygon
     */
    public boolean checkSegmentTouchesPolygon(BBDSegment segment){
        // find points of intersection, which include endpoints
        // arrange in order
        // check if one or both endpoints is inside & !perimeter
        // check inside & !perimeter for midpoint of subsegments
        return false;
    }


    /**
     * Test if this polygon touches another but don't overlap.
     * @param otherPolygon other polygon that this one might be touching
     * @return boolean stating if these polygons touch
     */
    public boolean checkPolygonTouchesPolygon(BBDPolygon otherPolygon){
        int pointsOnPerimeter = 0;
        int pointsInside = 0;

        for(BBDPoint otherPoint : otherPolygon.points){
            if (this.checkPointOnPerimeter(otherPoint)){
                pointsOnPerimeter++;
            }
            if (this.checkPointInside(otherPoint)){
                pointsInside++;
            }
        }

        for(BBDPoint thisPoint : this.points){
            if (otherPolygon.checkPointOnPerimeter(thisPoint)){
                pointsOnPerimeter++;
            }
            if (otherPolygon.checkPointInside(thisPoint)){
                pointsInside++;
            }
        }

        return (pointsOnPerimeter == pointsInside) && (pointsOnPerimeter != 0);
    }

    /**
     * Test if this polygon contains another
     * @param otherPolygon other polygon that might be contained within this one
     * @return boolean stating if the given polygon is contained within this one
     */
    public boolean checkPolygonContainsPolygon(BBDPolygon otherPolygon){
        for (BBDPoint otherPoint: otherPolygon.points){
            if (!this.checkPointInside(otherPoint)){
                return false;
            }
        }
        return true;
    }

    /**
     * Determine the distance squared to another polygon.  If a polygon is overlapping
     * then the distance will be 0.
     * @param otherPolygon other polygon to measure distance to
     * @return distance to the other polygon
     */
    public float distanceSquaredToPolygon(BBDPolygon otherPolygon){
        if (this.checkPolygonIntersectsPolygon(otherPolygon)){
            return 0;
        }

        float minDist = Float.MAX_VALUE;

        for (BBDSegment thisSegment: this.segments){
            for (BBDSegment otherSegment: otherPolygon.segments){
                if (thisSegment.distanceSquaredToSegment(otherSegment) < minDist){
                    minDist = thisSegment.distanceSquaredToSegment(otherSegment);
                }
            }
        }
        return minDist;
    }

    /**
     * Determine the distance squared to another segment.  If the segment intersects, is contained by, or
     * any other way overlaps the polygon, the distance will be 0.
     * @param otherSegment other segment to measure distance to
     * @return distance to the other segment
     */
    public float distanceSquaredToSegment (BBDSegment otherSegment){
        //check if all or part is inside or touching.
        BBDPoint[] points = otherSegment.getPoints();
        if(this.checkPointInside(points[0]) || this.checkPointInside(points[1])){
            return 0;
        }

        //check for points of intersection
        if(this.segmentIntersectPolygonPoints(otherSegment).length != 0){
            return 0;
        }

        float minDist = Float.MAX_VALUE;

        for (BBDSegment thisSegment: this.segments){
            if (thisSegment.distanceSquaredToSegment(otherSegment) < minDist){
                minDist = thisSegment.distanceSquaredToSegment(otherSegment);
            }
        }
        return minDist;
    }

    /**
     * Determine the distance squared to another point.  If the point is contained in
     * or otherwise in the polygon, the distance will be 0
     * @param otherPoint other point to measure distance to
     * @return distance to the other point
     */
    public float distanceSquaredToPoint (BBDPoint otherPoint){
        if(this.checkPointInside(otherPoint)){
            return 0;
        }

        float minDist = Float.MAX_VALUE;

        for (BBDSegment thisSegment: this.segments){
            if (thisSegment.distanceSquaredToPoint(otherPoint) < minDist){
                minDist = thisSegment.distanceSquaredToPoint(otherPoint);
            }
        }
        return minDist;
    }

    /**
     * Convert the polygon to an array of triangles.  Each triangle is guaranteed to be a
     * part of the overall polygon.
     * @param triangleDirectionality do you want the triangles to have a specific directionality
     * @return array of BBDPolygon triangles
     */
    public BBDPolygon[] decomposeIntoTriangles(Integer triangleDirectionality){
        ArrayList<BBDPoint> remainingPoints = new ArrayList<BBDPoint>(this.points);
        ArrayList<BBDPolygon> triangles = new ArrayList<>();

        while(remainingPoints.size() >= 3){
            BBDPolygon temp = new BBDPolygon(remainingPoints);
            //cycle through 3 adjacent vertices until we find a triangle with an interior inside the polygon
            BBDPolygon test;
            for(int i=1; i< remainingPoints.size()-1; i++){
                test = new BBDPolygon(new ArrayList<BBDPoint>(Arrays.asList(remainingPoints.get(i - 1), remainingPoints.get(i), remainingPoints.get(i + 1))));
                BBDPoint center = test.centerAverage();
                if(temp.checkPointInside(center) || remainingPoints.size() == 3){
                    if(triangleDirectionality != null){
                        test.enforceDirectionality(triangleDirectionality);
                    }
                    triangles.add(test);
                    remainingPoints.remove(i);
                    break;
                }
            }
        }
        return triangles.toArray(new BBDPolygon[0]);
    }

    /**
     * Calculate the area of this polygon
     * @return area
     */
    public float area(){
        float accumulatedTotal = 0;
        BBDPolygon[] triangles = this.decomposeIntoTriangles(null);
        for(BBDPolygon triangle:triangles){
            ArrayList<BBDPoint> points = triangle.getPoints();
            accumulatedTotal += Math.abs((points.get(0).getXLoc()*(points.get(1).getYLoc()-points.get(2).getYLoc())
                                        + points.get(1).getXLoc()*(points.get(2).getYLoc()-points.get(0).getYLoc())
                                        + points.get(2).getXLoc()*(points.get(0).getYLoc()-points.get(1).getYLoc()))
                    / 2.0);
        }
        return accumulatedTotal;
    }

    public String toString(){
        return "BBDPolygon object consisting of "+this.points.size()+" vertices ";
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
        if (otherPolygon.points.size() != this.points.size()){
            return false;
        }

        // then rotate through the points until we have a sequential match
        // checking forward and backward ordered lists
        ArrayList<BBDPoint> forwardList = new ArrayList<>(this.points);
        ArrayList<BBDPoint> backwardList = new ArrayList<>(this.points);
        Collections.reverse(backwardList);

        int count = this.points.size();
        for (int i = 0; i < count; i++){
            int index = 0;
            while(index < count && (otherPolygon.points.get(index).equals(forwardList.get(index))
                    || otherPolygon.points.get(index).equals(backwardList.get(index)))) {
                index++;
            }
            // if we made it all the way through the polygon's points
            if (index == count){
                return true;
            }
            Collections.rotate(forwardList, 1);
            Collections.rotate(backwardList, 1);
            i++;
        }
        return false;
    }
}
