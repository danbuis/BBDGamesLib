package Geometry2d;

public interface BBDGeometry {
    public void translate(double dx, double dy);
    public void scale(double scaleFactor);
    public void scaleFromPoint(double scaleFactor, BBDPoint centerOfScale);
    public void rotate(double degrees);
    public void rotateAroundPoint(BBDPoint centerOfRotation, double degrees);
    public BBDPoint center();
}
