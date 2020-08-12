package Geometry2d;

public class BBDSegment implements BBDGeometry{
    /**
     * A class representing a straight line between 2 points
     */
    private BBDPoint startPoint;
    private BBDPoint endPoint;

    /**
     * Constructor that takes in 2 points
     * @param startPoint
     * @param endPoint
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
        this.endPoint = new BBDPoint(startPoint.getXLoc()+Math.cos(radians)*distance,
                                     startPoint.getYLoc()+Math.sin(radians)*distance);
    }

    @Override
    /**
     * Translate the segment a specified amount in both cardinal directions
     */
    public void translate(double dx, double dy) {
        for(BBDPoint point: this.getPoints()){
            point.translate(dx, dy);
        }
    }

    @Override
    /**
     * Scale the segment by a given amount
     */
    public void scale(double scaleFactor) {
        this.scaleFromPoint(this.center(), scaleFactor);
    }

    @Override
    /**
     * Scale the segment by a given amount centered on a given location
     */
    public void scaleFromPoint(BBDPoint centerOfScale, double scaleFactor) {
        for(BBDPoint point: this.getPoints()){
            point.scaleFromPoint(centerOfScale, scaleFactor);
        }
    }

    @Override
    /**
     * Rotate the segment around the center.  Positive degrees are clockwise,
     * and negative degrees are counter-clockwise
     */
    public void rotate(double degrees) {
        this.rotateAroundPoint(this.center(), degrees);
    }

    @Override
    /**
     * Rotate the segment around a specific point.  Positive degrees are clockwise,
     * and negative degrees are counter-clockwise
     */
    public void rotateAroundPoint(BBDPoint centerOfRotation, double degrees) {
        for (BBDPoint point: this.getPoints()){
            point.rotateAroundPoint(centerOfRotation, degrees);
        }
    }

    @Override
    /**
     * Returns the center of the line segment
     */
    public BBDPoint center() {
        return new BBDPoint((startPoint.getXLoc() + endPoint.getXLoc())/2,
                            (startPoint.getYLoc() + endPoint.getYLoc())/2);
    }

    /**
     * Get a list of the BBDPoints that define the segment
     * @return array of length 2, containing this.startPoint and this.endPoint
     */
    public BBDPoint[] getPoints(){
        BBDPoint[] points = {this.startPoint, this.endPoint};
        return points;
    }
}
