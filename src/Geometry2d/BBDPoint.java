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

        translate(scaleFactor*dx, scaleFactor*dy);
    }

    /**
     * Do nothing because rotating a single point means nothing
     * @param degrees how far to rotate
     */
    @Override
    public void rotate(double degrees) {
        System.out.println("***WARNING*** Rotating a point without defining another point to scale from is meaningless");
    }

    /**
     * Rotations +for counterclockwise, and - for clockwise.  0 degrees is north
     * on the screen, west is 90 degrees, south is 180 degrees, and east is 270 degree
     * @param centerOfRotation point to rotate around
     * @param degrees how far to rotate
     */
    @Override
    public void rotateAroundPoint(BBDPoint centerOfRotation, double degrees) {
        double deltaX = centerOfRotation.xLoc - this.xLoc;
        double deltaY = centerOfRotation.yLoc - this.yLoc;

        //get some polar coordinates
        double angleFromCenterToOrig = -Math.atan2(deltaY, deltaX);
        double distanceToCenter = distanceToPoint(centerOfRotation);

        double angleFromCenterToNew = angleFromCenterToOrig + (Math.PI/180)*degrees;

        this.xLoc = centerOfRotation.getXLoc() + Math.cos(angleFromCenterToNew)*distanceToCenter;
        this.yLoc = centerOfRotation.getYLoc() + Math.sin(angleFromCenterToNew)*distanceToCenter;
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
     * Rotations +for counterclockwise, and - for clockwise.  0 degrees is north
     * on the screen, west is 90 degrees, south is 180 degrees, and east is 270 degree
     * @param otherPoint The other point to measure to
     * @return angle to that point in degrees
     */
    public double angleToOtherPoint(BBDPoint otherPoint){
        double deltaX = otherPoint.xLoc - this.xLoc;
        double deltaY = otherPoint.yLoc - this.yLoc;

        return -Math.atan2(deltaY, deltaX);
    }
}
