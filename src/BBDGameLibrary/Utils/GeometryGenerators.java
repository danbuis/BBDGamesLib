package BBDGameLibrary.Utils;

import BBDGameLibrary.Geometry2d.BBDPoint;
import BBDGameLibrary.Geometry2d.BBDPolygon;

import java.util.ArrayList;

public class GeometryGenerators {



    /**
     * Create a circle centered at a given location, with a specified radius and resolution (number of steps/segments)
     * @param centerPoint center of circle
     * @param radius radius of circle
     * @param steps how many segments in the circle
     * @return BBDPolygon shaped like a circle.
     */
    public static BBDPolygon createNGon(BBDPoint centerPoint, float radius, int steps){
        float increment = 360f/steps;
        float centerX = centerPoint.getXLoc();
        float centerY = centerPoint.getYLoc();
        ArrayList<BBDPoint> points = new ArrayList<>();

        for(int i = 0 ; i < steps; i++){
            float angle = i*increment;
            points.add(new BBDPoint(centerX + (float)(Math.sin(Math.toRadians(angle))) * radius,
                    centerY + (float)(Math.cos(Math.toRadians(angle))) * radius));
        }

        return new BBDPolygon(points);
    }

    /**
     * Does what it says on the tin.  Builds a rectangle of the given dimensions centered on the origin.
     * @param width width of the quad
     * @param height height of the quad
     * @return a polygon of the given dimesnions centered at 0,0
     */
    public static BBDPolygon buildQuad(float width, float height) {
        ArrayList<BBDPoint> returnList = new ArrayList<>();
        returnList.add(new BBDPoint(width / 2, height / 2));
        returnList.add(new BBDPoint(-width / 2, height / 2));
        returnList.add(new BBDPoint(-width / 2, -height / 2));
        returnList.add(new BBDPoint(width / 2, -height / 2));
        return new BBDPolygon(returnList);
    }
}
