package BBDGameLibrary.Geometry2d;

import BBDGameLibrary.Geometry2d.Exceptions.ParallelLinesException;

public class BBDSegment implements BBDGeometry{
    /**
     * A class representing a straight line between 2 points
     */
    private BBDPoint startPoint;
    private BBDPoint endPoint;

    /**
     * Constructor that takes in 2 points
     * @param startPoint start point for the segment
     * @param endPoint end point for the segment
     */
    public BBDSegment(BBDPoint startPoint, BBDPoint endPoint){
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    /**
     * A constructor that takes in a starting point and directions to the 2nd point.  0 degrees is east, 90 degrees is
     * north, and -90 is south
     * @param startPoint known point
     * @param angle angle in degrees to the next point
     * @param distance distance to the next point
     */
    public BBDSegment(BBDPoint startPoint, float angle, float distance){
        float radians = (float) (Math.PI/180 * angle);
        this.startPoint = startPoint;
        this.endPoint = new BBDPoint(startPoint.getXLoc()+(float)Math.cos(radians)*distance,
                startPoint.getYLoc()+(float)Math.sin(radians)*distance);
    }

    public BBDSegment(BBDSegment toCopy){
        this.startPoint = new BBDPoint(toCopy.startPoint);
        this.endPoint = new BBDPoint(toCopy.endPoint);
    }

    /**
     * Create a new segment based off an existing segment
     * @param baseSegment original base segment
     * @param distance distance to offset
     * @param direction direction to offset
     */
    public BBDSegment(BBDSegment baseSegment, float distance, float direction){
        this.startPoint = new BBDPoint(baseSegment.startPoint, distance, direction);
        this.endPoint = new BBDPoint(baseSegment.endPoint, distance, direction);
    }

    public BBDPoint getStartPoint(){
        return this.startPoint;
    }

    public BBDPoint getEndPoint(){
        return  this.endPoint;
    }

    /**
     * Translate the segment a specified amount in both cardinal directions
     */
    @Override
    public void translate(float dx, float dy) {
        for(BBDPoint point: this.getPoints()){
            point.translate(dx, dy);
        }
    }

    /**
     * Scale the segment by a given amount
     */
    @Override
    public void scale(float scaleFactor) {
        this.scaleFromPoint(this.center(), scaleFactor);
    }


    /**
     * Scale the segment by a given amount centered on a given location
     */
    @Override
    public void scaleFromPoint(BBDPoint centerOfScale, float scaleFactor) {
        for(BBDPoint point: this.getPoints()){
            point.scaleFromPoint(centerOfScale, scaleFactor);
        }
    }

    /**
     * Rotate the segment around the center.  Positive radians are clockwise,
     * and negative radians are counter-clockwise
     */
    @Override
    public void rotate(float radians) {
        this.rotateAroundPoint(this.center(), radians);
    }

    /**
     * Rotate the segment around a specific point.  Positive radians are clockwise,
     * and negative radians are counter-clockwise
     */
    @Override
    public void rotateAroundPoint(BBDPoint centerOfRotation, float radians) {
        for (BBDPoint point: this.getPoints()){
            point.rotateAroundPoint(centerOfRotation, radians);
        }
    }


    /**
     * Returns the center of the line segment
     */
    @Override
    public BBDPoint center() {
        return new BBDPoint((startPoint.getXLoc() + endPoint.getXLoc())/2,
                            (startPoint.getYLoc() + endPoint.getYLoc())/2);
    }

    /**
     * Get a list of the BBDPoints that define the segment
     * @return array of length 2, containing this.startPoint and this.endPoint
     */
    public BBDPoint[] getPoints(){
        return new BBDPoint[]{this.startPoint, this.endPoint};
    }

    /**
     * Calculate slope as rise over run
     * @return a ratio representing the slope
     */
    public float slopeInRatio(){
        float dx = startPoint.getXLoc() - endPoint.getXLoc();
        float dy = startPoint.getYLoc() - endPoint.getYLoc();

        if (dx == 0){
            return Float.POSITIVE_INFINITY;
        }
        return dy/dx;
    }

    /**
     * Calculate slope as radians, with right being 0.
     * Since this uses the ratio as a base, the slope calc
     * is agnostic of which side is the start and which
     * is the end
     * @return slope of the line as dy/dx
     */
    public float slopeInRadians(){
        return (float)Math.atan(slopeInRatio());
    }

    /**
     * returns the slope in degrees.  Does not account for
     * which side is the start and which is the end.
     * @return the slope of the line in degrees
     */
    public float slopeInDegrees(){
        return (float) (180 / Math.PI * slopeInRadians());
    }

    /**
     * Determines if the given point lies on the segment.  If it does,
     * then the slopes should match.  Need to do comparison with a
     * epsilon value though due to floating point math.  If the point
     * is on the segment it will also fall within the bounds of the 2 points.
     * @param point point to test
     * @return is the point on the segment  ?
     */
    public boolean pointOnSegment(BBDPoint point){
        //can we cut out early?
        if (startPoint.equals(point)
                || endPoint.equals(point)){
            return true;
        }

        BBDSegment seg1 = new BBDSegment(this.startPoint, point);
        BBDSegment seg2 = new BBDSegment(point, this.endPoint);

        // if delta of slopes is effectively 0, then they are the same
        // if both slopes are basically vertical, then they are the same, but they might be +- of vert
        // depending on floating point drift
        float seg1deg = seg1.slopeInDegrees();
        float seg2deg = seg2.slopeInDegrees();
        float seg1rad = seg1.slopeInRadians();
        float seg2rad = seg2.slopeInRadians();
        float degreeDif = Math.abs(seg1deg - seg2deg);
        boolean condition1 = (degreeDif<= BBDGeometryUtils.ALLOWABLE_DELTA_COARSE);
        boolean sameSlope =  (condition1
                || ((1.57079-Math.abs(seg1rad) <= 0.001) && (1.57079-Math.abs(seg2rad) <= 0.001)));

        //need to check end points first because otherwise
        // dx is 0, leading to one slope being Infinity
        if (!sameSlope){
            return false;
        }

        float maxX, minX, maxY, minY;
        if (startPoint.getXLoc()>endPoint.getXLoc()){
            maxX = startPoint.getXLoc();
            minX = endPoint.getXLoc();
        }else{
            minX = startPoint.getXLoc();
            maxX = endPoint.getXLoc();
        }
        if (startPoint.getYLoc()>endPoint.getYLoc()){
            maxY = startPoint.getYLoc();
            minY = endPoint.getYLoc();
        }else{
            minY = startPoint.getYLoc();
            maxY = endPoint.getYLoc();
        }

        boolean within = withinSegmentBounds(point, maxX, minX, maxY, minY);

        if(within){
            return true;
        }else{
            //apply a delta around the min and max due to floating point math drift
            maxX += BBDGeometryUtils.ALLOWABLE_DELTA;
            maxY += BBDGeometryUtils.ALLOWABLE_DELTA;
            minX -= BBDGeometryUtils.ALLOWABLE_DELTA;
            minY -= BBDGeometryUtils.ALLOWABLE_DELTA;

            return withinSegmentBounds(point, maxX, minX, maxY, minY);
        }
    }

    /**
     * Check if a point is within the box defined by the given bounds
     * @param point point to check
     * @param maxX max X value
     * @param minX min X value
     * @param maxY max Y value
     * @param minY min Y value
     * @return is the point within the bounds
     */
    private boolean withinSegmentBounds(BBDPoint point, float maxX, float minX, float maxY, float minY){
        return (maxX >= point.getXLoc())
                && (point.getXLoc() >= minX)
                && (maxY >= point.getYLoc())
                && (point.getYLoc() >= minY);
    }

    /**
     * Uses good old fashioned algebra to calculate the intercept point.  Each line is put into
     * point-slope form, with y isolated.  The 2 are set equal and solved for x.  Once x is
     * solved we can solve for the y coordinate of the point.  Ensure that you don't send two parallel
     * lines into this function as it will throw an exception.  Using intersects() is an insufficient
     * filter because that one returns true for parallel co-linear overlapping segments.
     * @param otherSegment other segment we want to find an intercept for.
     * @return the point at which these 2 segments would intersect
     */
    public BBDPoint interceptPoint(BBDSegment otherSegment) throws ParallelLinesException{
        if(BBDGeometryUtils.checkParallelSegments(this, otherSegment)){
            throw new ParallelLinesException("Can not calculate an intercept point between 2 parallel lines: "
                    + this.toString()+ " and "+ otherSegment.toString());
        }

        BBDSegment thisCopy = new BBDSegment(this);
        BBDSegment otherCopy = new BBDSegment(otherSegment);

        //will we be rotating the whole assembly?
        boolean verticalLineInvolved = false;
        BBDSegment verticalLine = null;
        //if we are kinda close to vertical, let's just call it and do some rotation.
        if( 90-Math.abs(this.slopeInDegrees()) <= 0.1) {
            verticalLineInvolved = true;
            verticalLine = thisCopy;
        }else if(90-Math.abs(otherSegment.slopeInDegrees()) <= 0.1){
            verticalLineInvolved = true;
            verticalLine = otherCopy;
        }

        BBDPoint intercept;

        //if we are kinda close to vertical, let's just call it and do some rotation.
        if(verticalLineInvolved){
            BBDSegment nonVertical;
            if(verticalLine == thisCopy){
                nonVertical = otherCopy;
            }else{
                nonVertical = thisCopy;
            }

            float xCoord = verticalLine.startPoint.getXLoc();
            //use point-slope form of a line (dy = m(dx))

            float yCoord = nonVertical.slopeInRatio()*(xCoord-nonVertical.startPoint.getXLoc()) + nonVertical.startPoint.getYLoc();

            intercept = new BBDPoint(xCoord, yCoord);
        }else{
            intercept = calculateIntercept(thisCopy, otherCopy);
        }

        return intercept;
    }

    /**
     * Private function to remove math logic from the interceptPoint function
     * to improve its logic flow
     * @param thisCopy copy of this segment
     * @param otherCopy copy of a different segment
     * @return the point of intercept for these segments
     */
    private BBDPoint calculateIntercept(BBDSegment thisCopy, BBDSegment otherCopy){
        float thisSlope = thisCopy.slopeInRatio();
        float otherSlope = otherCopy.slopeInRatio();

        float xLoc = (thisSlope * thisCopy.startPoint.getXLoc()
                - otherSlope * otherCopy.startPoint.getXLoc()
                + otherCopy.startPoint.getYLoc()
                - thisCopy.startPoint.getYLoc())
                / (thisSlope-otherSlope);

        float yLoc = thisSlope * (xLoc - thisCopy.startPoint.getXLoc()) + thisCopy.startPoint.getYLoc();

        return new BBDPoint(xLoc, yLoc);
    }

    /**
     * Does this segment intersect the other one?  Do not use this method to determine
     * the location of intersection since parallel lines will eventually trip you up
     * @param otherSegment the other segment to check against
     * @return intersect?
     */
    public boolean intersects(BBDSegment otherSegment){
        // if segments are co-linear, then if at least one vertex is on the other segment than
        // they intersect
        BBDPoint interceptPoint;
        try{
            interceptPoint = this.interceptPoint(otherSegment);
        } catch (ParallelLinesException e){
            return (this.pointOnSegment(otherSegment.startPoint) || this.pointOnSegment(otherSegment.endPoint)
                    || otherSegment.pointOnSegment(this.startPoint) || otherSegment.pointOnSegment(this.endPoint));
        }
        boolean bool1 = this.pointOnSegment(interceptPoint);
        boolean bool2 = otherSegment.pointOnSegment(interceptPoint);
        return (bool1 && bool2);
    }

    /**
     * 2 segments are connected if and only if they share an endpoint.
     * @param other the other segment to check against
     * @return are these segments connected?
     */
    public boolean segmentConnected(BBDSegment other){
        return (this.endPoint.equals(other.getEndPoint())
                || this.endPoint.equals(other.getStartPoint())
                || this.startPoint.equals(other.getStartPoint())
                || this.startPoint.equals(other.getEndPoint()));
    }

    /**
     * Distance squared between 2 segments
     * @param otherSegment the other segment to measure to
     * @return the distance between the segments
     */
    public float distanceSquaredToSegment(BBDSegment otherSegment){
        float minDist = Float.MAX_VALUE;

        for (BBDPoint thisPoint: this.getPoints()){
            float distance = otherSegment.distanceSquaredToPoint(thisPoint);
            if(distance < minDist){
                minDist = distance;
            }
        }
        for (BBDPoint otherPoint: otherSegment.getPoints()){
            float distance = this.distanceSquaredToPoint(otherPoint);
            if(distance < minDist){
                minDist = distance;
            }
        }
        return minDist;
    }

    /**
     * Distance squared between a point and a segment
     * @param otherPoint the point to check against
     * @return the distance to the other point
     */
    public float distanceSquaredToPoint(BBDPoint otherPoint){
        BBDSegment perpendicularSegment = new BBDSegment(otherPoint, this.slopeInDegrees()+90, 1);
        BBDPoint interceptPoint = null;
        try{
            interceptPoint = this.interceptPoint(perpendicularSegment);
        } catch(ParallelLinesException e){
            System.out.println("Somehow you managed to make what should be a perpendicular segment be parallel...");
            e.printStackTrace();
        }

        if (interceptPoint != null && this.pointOnSegment(interceptPoint)){
            return interceptPoint.distanceSquaredToPoint(otherPoint);
        }

        float startDist = this.startPoint.distanceSquaredToPoint(otherPoint);
        float endDist = this.endPoint.distanceSquaredToPoint(otherPoint);

        return Math.min(startDist, endDist);
    }




    /**
     * Return the length squared of the segment.  Using squared distance to be consistent with the other distance items.
     * @return the length squared of the segment
     */
    public float lengthSquared(){
        return this.startPoint.distanceSquaredToPoint(this.endPoint);
    }

    @Override
    public boolean equals(Object other){
        if (this == other){
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        BBDSegment otherSegment = (BBDSegment) other;
        if (this.startPoint.equals(otherSegment.getStartPoint())){
            return this.endPoint.equals(otherSegment.getEndPoint());
        }
        if (this.startPoint.equals(otherSegment.getEndPoint())){
            return this.endPoint.equals(otherSegment.getStartPoint());
        }

        return false;
    }

    public String toString(){
        return ("Segment from "+this.startPoint+" to "+this.endPoint);
    }
}
