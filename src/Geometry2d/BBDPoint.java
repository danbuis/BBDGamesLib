package Geometry2d;

import Geometry2d.Exceptions.CoordinateOverflowException;

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

    public BBDPoint(BBDPoint toCopy) {
        this.xLoc = toCopy.xLoc;
        this.yLoc = toCopy.yLoc;
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

        validateCoordinates();
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

        validateCoordinates();
    }

    /**
     * Do nothing because rotating a single point means nothing
     * @param radians how far to rotate
     */
    @Override
    public void rotate(double radians) {
        System.out.println("***WARNING*** Rotating a point without defining another point to rotate around is meaningless");
    }

    /**
     * Rotations +for counterclockwise, and - for clockwise.
     * @param centerOfRotation point to rotate around
     * @param radians how far to rotate
     */
    @Override
    public void rotateAroundPoint(BBDPoint centerOfRotation, double radians) {
        //get some polar coordinates
        double angleFromCenterToOrig = centerOfRotation.angleToOtherPoint(this);
        double distanceToCenter = Math.sqrt(distanceSquaredToPoint(centerOfRotation));

        double angleFromCenterToNew = angleFromCenterToOrig + radians;

        this.xLoc = centerOfRotation.getXLoc() + Math.cos(angleFromCenterToNew) * distanceToCenter;
        this.yLoc = centerOfRotation.getYLoc() + Math.sin(angleFromCenterToNew) * distanceToCenter;

        validateCoordinates();
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
     * Calculate the angle to another point
     * 0 degrees is east on the screen, north is 90 degrees, west is +180 degrees,
     * and south is -90 degrees
     * @param otherPoint The other point to measure to
     * @return angle to that point in degrees
     */
    public double angleToOtherPoint(BBDPoint otherPoint){
        double deltaX = otherPoint.xLoc - this.xLoc;
        double deltaY = otherPoint.yLoc - this.yLoc;

        return  Math.atan2(deltaY, deltaX);
    }

    /**
     * Calculate the distance squared to another point
     * @param other the other point
     * @return how far apart this point and the other point are
     */
    public double distanceSquaredToPoint(BBDPoint other){
        double deltaX = this.xLoc - other.xLoc;
        double deltaY = this.yLoc - other.yLoc;

        return (deltaX*deltaX)+(deltaY*deltaY);
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
        return (Math.abs(this.xLoc - otherPoint.xLoc) < BBDGeometryUtils.ALLOWABLE_DELTA
                && Math.abs(this.yLoc - otherPoint.yLoc) < BBDGeometryUtils.ALLOWABLE_DELTA);
    }

    private void validateCoordinates(){
        //validate xLoc
        if(this.xLoc == Double.MAX_VALUE
                || this.xLoc == Double.POSITIVE_INFINITY
                || this.xLoc == Double.MIN_VALUE
                || this.xLoc == Double.NEGATIVE_INFINITY){
            throw new CoordinateOverflowException("X coordinate of a point has reached the bounds provided by the Double type");
        }

        //validate yLoc
        if(this.yLoc == Double.MAX_VALUE
                || this.yLoc == Double.POSITIVE_INFINITY
                || this.yLoc == Double.MIN_VALUE
                || this.yLoc == Double.NEGATIVE_INFINITY){
            throw new CoordinateOverflowException("Y coordinate of a point has reached the bounds provided by the Double type");
        }
    }
}
