package Geometry2d;

public class BBDPoint implements BBDGeometry{
    /**
     * A class representing a single point in space. It adheres
     * to the BBD geometry expectations and serves as the base
     * for the rest of the BBD geometry classes.
     */

    //cartesian coordinates
    private double xLoc;
    private double yLoc;

    public BBDPoint(double x, double y){
        this.xLoc = x;
        this.yLoc = y;
    }

    public double getXLoc(){
        return this.xLoc;
    }

    public double getYLoc(){
        return this.yLoc;
    }

    /**
     * Translate the point a specified amount in both cardinal directions
     * @param dx distance to translate on the x-axis
     * @param dy distance to translate on the y-axis
     */
    @Override
    public void translate(double dx, double dy) {

        this.xLoc += dx;
        this.yLoc += dy;
    }

    /**
     * Do nothing because scaling a single point means nothing
     * @param scaleFactor factor to scale geometry
     */
    @Override
        public void scale(double scaleFactor) {
        System.out.println("***WARNING*** Scaling a point without defining another point to scale from is meaningless");
    }

    /**
     * Use a reference point to scale the geometry.
     * @param centerOfScale center point to scale from
     * @param scaleFactor factor to scale geometry
     */
    @Override
    public void scaleFromPoint(BBDPoint centerOfScale, double scaleFactor) {
        double dx = this.xLoc - centerOfScale.xLoc;
        double dy = this.yLoc - centerOfScale.yLoc;

        this.xLoc = centerOfScale.getXLoc() + scaleFactor*dx;
        this.yLoc = centerOfScale.getYLoc() + scaleFactor*dy;
    }

    /**
     * Do nothing because rotating a single point means nothing
     * @param degrees how far to rotate
     */
    @Override
    public void rotate(double degrees) {
        System.out.println("***WARNING*** Rotating a point without defining another point to rotate around is meaningless");
    }

    /**
     * Rotations +for counterclockwise, and - for clockwise.
     * @param centerOfRotation point to rotate around
     * @param degrees how far to rotate
     */
    @Override
    public void rotateAroundPoint(BBDPoint centerOfRotation, double degrees) {
        //get some polar coordinates
        double angleFromCenterToOrig = centerOfRotation.angleToOtherPoint(this);
        double distanceToCenter = distanceToPoint(centerOfRotation);

        double angleFromCenterToNew = angleFromCenterToOrig + degrees;

        double newAngleInRadians = Math.PI/180 * angleFromCenterToNew;

        this.xLoc = centerOfRotation.getXLoc() + Math.cos(newAngleInRadians) * distanceToCenter;
        this.yLoc = centerOfRotation.getYLoc() + Math.sin(newAngleInRadians) * distanceToCenter;
    }

    /**
     * Center point.  Should return the point itself
     * @return this point
     */
    @Override
    public BBDPoint center() {
        return this;
    }

    /**
     * Calculate the distance to another point
     * @param other the other point
     * @return how far apart this point and the other point are
     */
    public double distanceToPoint(BBDPoint other){
        double deltaX = this.xLoc - other.xLoc;
        double deltaY = this.yLoc - other.yLoc;

        return Math.sqrt((deltaX*deltaX)+(deltaY*deltaY));
    }

    /**
     * Calculate the angle to another point
     * 0 degrees is east on the screen, north is 90 degrees, west is +180 degrees,
     * and south is -90 degrees
     * @param otherPoint The other point to measure to
     * @return angle to that point in degrees
     */
    public double angleToOtherPoint(BBDPoint otherPoint){
        double deltaX = otherPoint.xLoc - this.xLoc;
        double deltaY = otherPoint.yLoc - this.yLoc;

        double radians =  Math.atan2(deltaY, deltaX);
        double degrees =  radians * 180/Math.PI;
        if (degrees == -180){
            degrees = 180;
        }else if(degrees == -0.0){
            degrees = 0;
        }
        return degrees;
    }

    public String toString(){
        return"BBDPoint object located at ("+this.xLoc+","+this.yLoc+")";
    }

    @Override
    public boolean equals(Object other){
        if (this == other){
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        BBDPoint otherPoint = (BBDPoint)other;
        return (Math.abs(this.xLoc - otherPoint.xLoc) < 0.0000005
                && Math.abs(this.yLoc - otherPoint.yLoc) < 0.0000005);
    }
}
