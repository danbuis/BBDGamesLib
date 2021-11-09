package BBDGameLibrary.Geometry2d;

import BBDGameLibrary.Geometry2d.Exceptions.CoordinateOverflowException;
import org.joml.Vector2d;

public class BBDPoint implements BBDGeometry{
    /**
     * A class representing a single point in space. It adheres
     * to the BBD geometry expectations and serves as the base
     * for the rest of the BBD geometry classes.
     */

    //cartesian coordinates
    private float xLoc;
    private float yLoc;

    public BBDPoint(float x, float y){
        this.xLoc = x;
        this.yLoc = y;
    }

    public BBDPoint(BBDPoint toCopy) {
        this.xLoc = toCopy.xLoc;
        this.yLoc = toCopy.yLoc;
    }

    public BBDPoint(Vector2d vector){
        this.xLoc = (float)vector.x;
        this.yLoc = (float)vector.y;
    }

    /**
     * create a point in a direction based off an existing point
     * @param distance how far away to put the copy
     * @param direction what direction to shift the copy
     * @return the new point
     */
    public BBDPoint (BBDPoint basePoint, float distance, float direction){
        this.xLoc = (float) (basePoint.xLoc + Math.cos(direction) * distance);
        this.yLoc = (float) (basePoint.yLoc + Math.sin(direction) * distance);
    }

    public float getXLoc(){
        return this.xLoc;
    }

    public float getYLoc(){
        return this.yLoc;
    }

    /**
     * Translate the point a specified amount in both cardinal directions
     * @param dx distance to translate on the x-axis
     * @param dy distance to translate on the y-axis
     */
    @Override
    public void translate(float dx, float dy) {
        this.xLoc += dx;
        this.yLoc += dy;

        validateCoordinates();
    }

    /**
     * Do nothing because scaling a single point means nothing
     * @param scaleFactor factor to scale geometry
     */
    @Override
        public void scale(float scaleFactor) {
        System.out.println("***WARNING*** Scaling a point without defining another point to scale from is meaningless");
    }

    /**
     * Use a reference point to scale the geometry.
     * @param centerOfScale center point to scale from
     * @param scaleFactor factor to scale geometry
     */
    @Override
    public void scaleFromPoint(BBDPoint centerOfScale, float scaleFactor) {
        float dx = this.xLoc - centerOfScale.xLoc;
        float dy = this.yLoc - centerOfScale.yLoc;

        this.xLoc = centerOfScale.getXLoc() + scaleFactor*dx;
        this.yLoc = centerOfScale.getYLoc() + scaleFactor*dy;

        validateCoordinates();
    }

    /**
     * Do nothing because rotating a single point means nothing
     * @param radians how far to rotate
     */
    @Override
    public void rotate(float radians) {
        System.out.println("***WARNING*** Rotating a point without defining another point to rotate around is meaningless");
    }

    /**
     * Rotations +for counterclockwise, and - for clockwise.
     * @param centerOfRotation point to rotate around
     * @param radians how far to rotate
     */
    @Override
    public void rotateAroundPoint(BBDPoint centerOfRotation, float radians) {
        //get some polar coordinates
        float angleFromCenterToOrig = centerOfRotation.angleToOtherPoint(this);
        float distanceToCenter = (float) Math.sqrt(distanceSquaredToPoint(centerOfRotation));

        float angleFromCenterToNew = angleFromCenterToOrig + radians;

        this.xLoc = (float) (centerOfRotation.getXLoc() + Math.cos(angleFromCenterToNew) * distanceToCenter);
        this.yLoc = (float) (centerOfRotation.getYLoc() + Math.sin(angleFromCenterToNew) * distanceToCenter);

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
     * @return angle to that point in radians
     */
    public float angleToOtherPoint(BBDPoint otherPoint){
        float deltaX = otherPoint.xLoc - this.xLoc;
        float deltaY = otherPoint.yLoc - this.yLoc;

        return (float) Math.atan2(deltaY, deltaX);
    }

    public float angleToOtherPointDegrees(BBDPoint otherPoint){
        return (float) (180 / Math.PI * angleToOtherPoint(otherPoint));
    }

    /**
     * Calculate the distance squared to another point
     * @param other the other point
     * @return how far apart this point and the other point are
     */
    public float distanceSquaredToPoint(BBDPoint other){
        float deltaX = this.xLoc - other.xLoc;
        float deltaY = this.yLoc - other.yLoc;

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
        return (Math.abs(this.xLoc - otherPoint.xLoc) < BBDGeometryHelpers.ALLOWABLE_DELTA
                && Math.abs(this.yLoc - otherPoint.yLoc) < BBDGeometryHelpers.ALLOWABLE_DELTA);
    }

    private void validateCoordinates(){
        //validate xLoc
        if(this.xLoc == Float.MAX_VALUE
                || this.xLoc == Float.POSITIVE_INFINITY
                || this.xLoc == Float.MIN_VALUE
                || this.xLoc == Float.NEGATIVE_INFINITY){
            throw new CoordinateOverflowException("X coordinate of a point has reached the bounds provided by the Float type");
        }

        //validate yLoc
        if(this.yLoc == Float.MAX_VALUE
                || this.yLoc == Float.POSITIVE_INFINITY
                || this.yLoc == Float.MIN_VALUE
                || this.yLoc == Float.NEGATIVE_INFINITY){
            throw new CoordinateOverflowException("Y coordinate of a point has reached the bounds provided by the float type");
        }
    }
}
