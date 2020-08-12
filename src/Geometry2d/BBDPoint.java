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

    @Override
    /** Translate the point a specified amount in both cardinal directions */
    public void translate(double dx, double dy) {
        this.xLoc += dx;
        this.yLoc += dy;
    }

    @Override
    /** Do nothing because scaling a single point means nothing */
    public void scale(double scaleFactor) {
        return;
    }

    @Override
    /**
     * Use a reference point to scale the geometry.
     */
    public void scaleFromPoint(BBDPoint centerOfScale, double scaleFactor) {
        double dx = this.xLoc - centerOfScale.xLoc;
        double dy = this.yLoc - centerOfScale.yLoc;

        translate(scaleFactor*dx, scaleFactor*dy);
    }

    @Override
    /**Do nothing because rotating a single point means nothing */
    public void rotate(double degrees) {
        return;
    }

    @Override
    /**
     * Rotations are +clockwise, and -CCW
     */
    public void rotateAroundPoint(BBDPoint centerOfRotation, double degrees) {
        double deltaX = centerOfRotation.xLoc - this.xLoc;
        double deltaY = centerOfRotation.yLoc - this.yLoc;

        //get some polar coordinates
        double angleFromCenterToOrig = Math.atan2(deltaY, deltaX);
        double distanceToCenter = distanceToPoint(centerOfRotation);

        double angleFromCenterToNew = angleFromCenterToOrig + (Math.PI/180)*degrees;

        translate(Math.cos(angleFromCenterToNew)*distanceToCenter, Math.sin(angleFromCenterToNew)*distanceToCenter);
    }

    @Override
    /**This point is its own center*/
    public BBDPoint center() {
        return this;
    }

    public double distanceToPoint(BBDPoint other){
        double deltaX = this.xLoc - other.xLoc;
        double deltaY = this.yLoc - other.yLoc;

        return Math.sqrt((deltaX*deltaX)+(deltaY*deltaY));
    }
}
