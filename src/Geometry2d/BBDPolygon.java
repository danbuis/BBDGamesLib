package Geometry2d;

import java.util.ArrayList;

public class BBDPolygon implements BBDGeometry{

    /**
     * Class to handle closed polygons.
     */

    // Pieces that define the polygon.
    // Users should not be able to modify these directly.
    private BBDPoint[] points;
    private BBDSegment[] segments;

    /**
     * General constructor that takes in a series of points and
     * creates the polygon object
     * @param inputPoints
     */
    public BBDPolygon (BBDPoint[] inputPoints){
        ArrayList<BBDSegment> segments = new ArrayList<BBDSegment>();
        for(int index =0; index< inputPoints.length; index++){
            int nextIndex = (index + 1) % inputPoints.length;
            segments.add(new BBDSegment(inputPoints[index], inputPoints[nextIndex]));
        }

        this.points = inputPoints;
        this.segments = segments.toArray(new BBDSegment[segments.size()]);
    }


    @Override
    /**
     * Translate the polygon a specified amount in both cardinal directions
     */
    public void translate(double dx, double dy) {
        for (BBDPoint point: this.points){
            point.translate(dx, dy);
        }
    }

    @Override
    /**
     * Scale the polygon by a given amount
     */
    public void scale(double scaleFactor) {
        for (BBDPoint point: this.points){
            point.scaleFromPoint(this.center(), scaleFactor);
        }
    }

    @Override
    /**
     * Scale the polygon by a given amount centered on a given location
     */
    public void scaleFromPoint(BBDPoint centerOfScale, double scaleFactor) {
        for (BBDPoint point: this.points){
            point.scaleFromPoint(centerOfScale, scaleFactor);
        }
    }

    @Override
    /**
     * Rotate the polygon around the center.  Positive degrees are clockwise,
     * and negative degrees are counter-clockwise
     */
    public void rotate(double degrees) {
        this.rotateAroundPoint(this.center(), degrees);
    }

    @Override
    /**
     * Rotate the polygon around a specific point.  Positive degrees are clockwise,
     * and negative degrees are counter-clockwise
     */
    public void rotateAroundPoint(BBDPoint centerOfRotation, double degrees) {
        for (BBDPoint point: points){
            point.rotateAroundPoint(centerOfRotation, degrees);
        }
    }

    @Override
    /**
     * Calculates the geometric center of the polygon by finding the max/min x/y
     *  and then averaging.
     */
    public BBDPoint center() {
        double maxX = Double.NEGATIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;
        double minX = Double.POSITIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;

        for (BBDPoint point : points){
            double x = point.getXLoc();
            double y = point.getYLoc();
            if(x > minX){minX = x;}
            if(x < maxX){maxX = x;}
            if(y > minY){minY = y;}
            if(y < maxY){maxY = y;}
        }
        return new BBDPoint((minX+maxX)/2, (minY+maxY)/2);
    }
}
