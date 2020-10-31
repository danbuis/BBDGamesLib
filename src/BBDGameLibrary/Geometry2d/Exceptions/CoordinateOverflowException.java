package BBDGameLibrary.Geometry2d.Exceptions;

public class CoordinateOverflowException extends RuntimeException{
    public CoordinateOverflowException(String errorMessage ) {
        super(errorMessage);
    }
}
