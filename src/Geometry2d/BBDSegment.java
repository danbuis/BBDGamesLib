package Geometry2d;

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
     * A constructor that takes in a starting point and directions to the 2nd point
     * @param startPoint known point
     * @param angle angle in degrees to the next point
     * @param distance distance to the next point
     */
    public BBDSegment(BBDPoint startPoint, double angle, double distance){
        double radians = Math.PI/180 * angle;
        this.startPoint = startPoint;
        this.endPoint = new BBDPoint(startPoint.getXLoc()+Math.sin(radians)*distance,
                                     startPoint.getYLoc()+Math.cos(radians)*distance);
    }

    /**
     * Translate the segment a specified amount in both cardinal directions
     */
    @Override
    public void translate(double dx, double dy) {
        for(BBDPoint point: this.getPoints()){
            point.translate(dx, dy);
        }
    }

    /**
     * Scale the segment by a given amount
     */
    @Override
    public void scale(double scaleFactor) {
        this.scaleFromPoint(this.center(), scaleFactor);
    }


    /**
     * Scale the segment by a given amount centered on a given location
     */
    @Override
    public void scaleFromPoint(BBDPoint centerOfScale, double scaleFactor) {
        for(BBDPoint point: this.getPoints()){
            point.scaleFromPoint(centerOfScale, scaleFactor);
        }
    }

    /**
     * Rotate the segment around the center.  Positive degrees are clockwise,
     * and negative degrees are counter-clockwise
     */
    @Override
    public void rotate(double degrees) {
        this.rotateAroundPoint(this.center(), degrees);
    }

    /**
     * Rotate the segment around a specific point.  Positive degrees are clockwise,
     * and negative degrees are counter-clockwise
     */
    @Override
    public void rotateAroundPoint(BBDPoint centerOfRotation, double degrees) {
        for (BBDPoint point: this.getPoints()){
            point.rotateAroundPoint(centerOfRotation, degrees);
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
    public double slopeInRatio(){
        double dx = startPoint.getXLoc() - endPoint.getXLoc();
        double dy = startPoint.getYLoc() - endPoint.getYLoc();

        if (dx == 0){
            return Double.POSITIVE_INFINITY;
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
    public double slopeInRadians(){
        return Math.atan(slopeInRatio());
    }

    /**
     * returns the slope in degrees.  Does not account for
     * which side is the start and which is the end.
     * @return the slope of the line in degrees
     */
    public double slopeInDegrees(){
        return 180 / Math.PI * slopeInRadians();
    }

    /**
     * Determines if the given point lies on the segment.  If it does,
     * then the slopes should match.  Need to do comparison with a
     * epsilon value though due to floating point math.  If the point
     * is on the segment it will also fall within the bounds of the 2 points.
     * @param point point to test
     * @return is the point on the segment?
     */
    public boolean pointOnSegment(BBDPoint point){
        BBDSegment seg1 = new BBDSegment(this.startPoint, point);
        BBDSegment seg2 = new BBDSegment(point, this.endPoint);

        boolean sameSlope =  Math.abs(seg1.slopeInRatio()-seg2.slopeInRatio())<=0.001;

        //can we cut out early?
        if ((startPoint.getXLoc() == point.getXLoc() && startPoint.getYLoc() == point.getYLoc())
         || (endPoint.getXLoc() == point.getXLoc() && endPoint.getYLoc() == point.getYLoc())){
            return true;
        }
        //need to check end points first because otherwise
        // dx is 0, leading to one slope being Infinity
        if (!sameSlope){
            return false;
        }

        double maxX, minX, maxY, minY;
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

        return (maxX >= point.getXLoc())
            && (point.getXLoc() >= minX)
            && (maxY >= point.getYLoc())
            && (point.getYLoc() >= minY);
    }

    /**
     * Uses good old fashioned algebra to calculate the intercept point.  Each line is put into
     * point-slope form, with y isolated.  The 2 are set equal and solved for x.  Once x is
     * solved we can solve for the y coordinate of the point.
     * @param otherSegment other segment we want to find an intercept for.
     * @return the point at which these 2 segments would intersect
     */
    public BBDPoint interceptPoint(BBDSegment otherSegment){
        double thisSlope = this.slopeInRatio();
        double otherSlope = otherSegment.slopeInRatio();

        double xLoc = (thisSlope * this.startPoint.getXLoc()
                        - otherSlope * otherSegment.startPoint.getXLoc()
                        + otherSegment.startPoint.getYLoc()
                        - this.startPoint.getYLoc())
                        / (thisSlope-otherSlope);

        double yLoc = thisSlope * (xLoc - this.startPoint.getXLoc()) + this.startPoint.getYLoc();

        return new BBDPoint(xLoc, yLoc);
    }

    /**
     * Does this segment intersect the other one?
     * @param otherSegment the other segment to check against
     * @return intersect?
     */
    public Boolean intersects(BBDSegment otherSegment){
        BBDPoint interceptPoint = this.interceptPoint(otherSegment);
        return (this.pointOnSegment(interceptPoint) && otherSegment.pointOnSegment(interceptPoint));
    }

    /**
     * Distance between 2 segments
     * @param otherSegment the other segment to measure to
     * @return the distance between the segments
     */
    public double distanceToSegment(BBDSegment otherSegment){
        double minDist = Double.MAX_VALUE;

        for (BBDPoint thisPoint: this.getPoints()){
            double distance = otherSegment.distanceToPoint(thisPoint);
            if(distance < minDist){
                minDist = distance;
            }
        }
        for (BBDPoint otherPoint: otherSegment.getPoints()){
            double distance = this.distanceToPoint(otherPoint);
            if(distance < minDist){
                minDist = distance;
            }
        }
        return minDist;
    }

    /**
     * Distance between a point and a segment
     * @param otherPoint the point to check against
     * @return the distance to the other point
     */
    public double distanceToPoint(BBDPoint otherPoint){
        BBDSegment perpendicularSegment = new BBDSegment(otherPoint, this.slopeInDegrees()+90, 1);
        BBDPoint interceptPoint = this.interceptPoint(perpendicularSegment);
        if (this.pointOnSegment(interceptPoint)){
            return interceptPoint.distanceToPoint(otherPoint);
        }

        double startDist = this.startPoint.distanceToPoint(otherPoint);
        double endDist = this.endPoint.distanceToPoint(otherPoint);

        return Math.min(startDist, endDist);
    }

    /**
     * 2 segments are connected if and only if they share an endpoint.
     * @param other the other segment to check against
     * @return are these segments connected?
     */
    public boolean segmentConnected(BBDSegment other){
        return (this.endPoint == other.endPoint
        || this.endPoint == other.startPoint
        || this.startPoint == other.startPoint
        || this.startPoint == other.endPoint);
    }
}
