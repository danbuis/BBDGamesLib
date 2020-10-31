package BBDGameLibrary.Geometry2d;

public interface BBDGeometry {
    public void translate(float dx, float dy);
    public void scale(float scaleFactor);
    public void scaleFromPoint(BBDPoint centerOfScale, float scaleFactor);
    public void rotate(float radians);
    public void rotateAroundPoint(BBDPoint centerOfRotation, float radians);
    public BBDPoint center();
}
