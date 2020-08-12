package Geometry2d;

public interface BBDGeometry {
    public void translate(double dx, double dy);
    public void scale(double scaleFactor);
    public void scaleFromPoint(BBDPoint centerOfScale, double scaleFactor);
    public void rotate(double degrees);
    public void rotateAroundPoint(BBDPoint centerOfRotation, double degrees);
    public BBDPoint center();
}
