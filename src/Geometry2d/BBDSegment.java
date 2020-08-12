package Geometry2d;

public class BBDSegment implements BBDGeometry{
    private BBDPoint startPoint;
    private BBDPoint endPoint;

    public BBDSegment(BBDPoint startPoint, BBDPoint endPoint){

    }

    public BBDSegment(BBDPoint startPoint, double angle, float distance){
        
    }

    @Override
    public void translate(double dx, double dy) {

    }

    @Override
    public void scale(double scaleFactor) {

    }

    @Override
    public void scaleFromPoint(double scaleFactor, BBDPoint centerOfScale) {

    }

    @Override
    public void rotate(double degrees) {

    }

    @Override
    public void rotateAroundPoint(BBDPoint centerOfRotation, double degrees) {

    }

    @Override
    public BBDPoint center() {
        return null;
    }
}
