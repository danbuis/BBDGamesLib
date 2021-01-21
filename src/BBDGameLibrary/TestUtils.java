package BBDGameLibrary;

import BBDGameLibrary.Geometry2d.BBDPoint;
import BBDGameLibrary.Geometry2d.BBDPolygon;

import java.util.ArrayList;
import java.util.Arrays;

public class TestUtils {

    public static BBDPolygon buildSquare(){
        BBDPoint point1 = new BBDPoint(1,1);
        BBDPoint point2 = new BBDPoint(1,-1);
        BBDPoint point3 = new BBDPoint(-1,-1);
        BBDPoint point4 = new BBDPoint(-1,1);

        ArrayList<BBDPoint> points = new ArrayList<BBDPoint>(Arrays.asList(point1, point2, point3, point4));

        return new BBDPolygon(points);
    }
}
