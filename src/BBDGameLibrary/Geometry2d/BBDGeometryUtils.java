package BBDGameLibrary.Geometry2d;

import BBDGameLibrary.Geometry2d.Exceptions.ParallelLinesException;

import java.util.ArrayList;
import java.util.Collections;

public class BBDGeometryUtils {
    public static final float ALLOWABLE_DELTA_COARSE = 0.002f;
    public static final float ALLOWABLE_DELTA = 0.0001f;
    public static final int CLOCKWISE_POLYGON = 0;
    public static final int COUNTERCLOCKWISE_POLYGON = 1;

    public static float distance(BBDSegment seg, BBDPoint point){
        return seg.distanceSquaredToPoint(point);
    }

    public static float distance(BBDPoint point, BBDSegment seg){
        return seg.distanceSquaredToPoint(point);
    }

    public static float distance(BBDPolygon poly, BBDPoint point){
        return poly.distanceSquaredToPoint(point);
    }

    public static float distance(BBDPoint point, BBDPolygon poly){
        return poly.distanceSquaredToPoint(point);
    }

    public static float distance(BBDSegment seg, BBDPolygon poly){
        return poly.distanceSquaredToSegment(seg);
    }

    public static float distance(BBDPolygon poly, BBDSegment seg){
        return poly.distanceSquaredToSegment(seg);
    }

    public static float distance(BBDSegment seg1, BBDSegment seg2){
        return seg1.distanceSquaredToSegment(seg2);
    }

    public static float distance(BBDPoint point1, BBDPoint point2){
        return point1.distanceSquaredToPoint(point2);
    }

    public static float distance(BBDPolygon poly1, BBDPolygon poly2){
        return poly1.distanceSquaredToPolygon(poly2);
    }

    public static boolean checkParallelSegments(BBDSegment seg1, BBDSegment seg2){
        return seg1.slopeInDegrees() == seg2.slopeInDegrees();
    }

    /**
     * Create a circle centered at a given location, with a specified radius and resolution (number of steps/segments)
     * @param centerPoint center of circle
     * @param radius radius of circle
     * @param steps how many segments in the circle
     * @return BBDPolygon shaped like a circle.
     */
    public static BBDPolygon createCircle(BBDPoint centerPoint, float radius, int steps){
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

    public static BBDPolygon createPolygonIntersection(BBDPolygon polygon1, BBDPolygon polygon2){
        //let's catch some corner cases first
        if(polygon1.checkPolygonContainsPolygon(polygon2)){
            return polygon2.copyPolygon();
        }
        if(polygon2.checkPolygonContainsPolygon(polygon1)){
            return polygon1.copyPolygon();
        }

        if(!polygon1.checkPolygonIntersectsPolygon(polygon2)){
            return null;
        }

        BBDPolygon polyThis = polygon1.prepPolygonForBooleanOperations(polygon2);
        BBDPolygon polyOther = polygon2.prepPolygonForBooleanOperations(polygon1);

        ArrayList<BBDPoint> newPolygonList = new ArrayList<>();

        //find the first point that is inside while the preceding one is outside
        int startingIndexThis = 0;
        int currentIndexThis = 0;
        int currentIndexOther;
        int thisLength = polyThis.getPoints().size();
        int otherLength = polyOther.getPoints().size();
        for (int i=0; i<polyThis.getPoints().size(); i++){
            if(polyOther.checkPointInside(polyThis.getPoints().get(i)) && !polyOther.checkPointInside(polyThis.getPoints().get((i + thisLength-1)%thisLength))){
                startingIndexThis = i;
                currentIndexThis = i;
                break;
            }
        }

        // the goal is to make a full circuit around polyThis, so once we get all the way around we are done
        // we can't use that as the only conditional because otherwise we will skip the whole block initially since
        // the start and current index begin the same.  Therefore we check if it is empty initially, and once that becomes
        // false on all other loops we will be checking if we have completed the full circuit
        while (newPolygonList.isEmpty() || currentIndexThis-1 != startingIndexThis){
            while(polyOther.checkPointInside(polyThis.getPoints().get(currentIndexThis % thisLength))){
                newPolygonList.add(polyThis.getPoints().get(currentIndexThis % thisLength));
                currentIndexThis++;
            }
            //at this point we are just added an intersection point, so let's find the same intersection on the other one, and then use the next point
            currentIndexOther = polyOther.getPoints().indexOf(polyThis.getPoints().get((currentIndexThis-1)  % thisLength)) + 1;
            while(polyThis.checkPointInside(polyOther.getPoints().get(currentIndexOther % otherLength))){
                newPolygonList.add(polyOther.getPoints().get(currentIndexOther % otherLength));
                currentIndexOther++;
            }
            currentIndexThis = polyThis.getPoints().indexOf(polyOther.getPoints().get((currentIndexOther-1) % otherLength)) + 1;
        }
        //remove the last one because it is a repeat of the first one
        newPolygonList.remove(newPolygonList.size()-1);
        return new BBDPolygon(newPolygonList);
    }


    /**
     * Offset an entire polygon with a simpler function call.  Positive offsets make the shape bigger.  Negative values
     * can be used, but may behave unpredictable.  Convex polygons are the only ones supported right now cancave polygons
     * may behave unpredictably
     * @param offsetDistance How far to offset the shape.  Positive numbers shift the edge away from the center.  Negative
     *                       values are not supported right now and may behave unpredictably
     * @return  new BBDPolygon that has the offset added to the base shape
     * @throws ParallelLinesException if you are trying to calculate the intercept of parallel lines.  If you encounter this
     *                                it means you have some colinear lines and they should probably be combined
     */
    public static BBDPolygon offsetPolygon(BBDPolygon poly1, float offsetDistance) throws ParallelLinesException {
        return offsetPolygon(poly1, offsetDistance, 0, poly1.getSegments().size() -1);
    }


    /**
     * Offset a contiguous portion of a polygon.  Positive offsets make the shape bigger.  Negative values can be used,
     * but may behave unpredictably.  Convex polygons are the only ones supported right now, concave polygons may behave
     * unpredictably.
     *
     * @param poly1 polygon to offset
     * @param offsetDistance How far to offset the shape.  Positive numbers shift the edge away from the center.  Negative
     *                       values are not supported right now and may behave unpredictably
     * @param startIndex The segment index from which to start the offset.  The offset will proceed to the next highest
     *                   index and wrap to 0 when it reaches the end
     * @param endIndex The segment index to terminate the offset.
     * @return A new BBDPolygon that has the offset added to the base shape
     * @throws ParallelLinesException if you are trying to calculate the intercept of parallel lines.  If you encounter this
     *                                it means you have some colinear lines and they should probably be combined
     */
    public static BBDPolygon offsetPolygon(BBDPolygon poly1, float offsetDistance, int startIndex, int endIndex) throws ParallelLinesException {
        //check polygon direction so we know which way to offset
        float offsetAngleModifier;
        if (poly1.determineDirectionality() == BBDGeometryUtils.COUNTERCLOCKWISE_POLYGON){
            offsetAngleModifier = (float)Math.PI/2;
        }else{
            offsetAngleModifier = (float)-Math.PI/2;
        }

        //loop through the segments from the start to the end and copy
        //build a list off offsets and a list of not, which is 2 contiguous lists
        ArrayList<BBDSegment> offsetSegments = new ArrayList<>();
        ArrayList<BBDSegment> otherSegments = new ArrayList<>();
        float currentAngle;
        BBDSegment segment;

        //deep copy to avoid rotating the original list.
        ArrayList<BBDSegment> segmentList = new ArrayList<>();
        for (BBDSegment seg: poly1.getSegments()){
            segmentList.add(new BBDSegment(seg));
        }

        Collections.rotate(segmentList, -startIndex);

        for(int i = 0; i< segmentList.size() ; i++){
            segment = segmentList.get(i);
            if(i<=(endIndex-startIndex + poly1.getSegments().size())%poly1.getSegments().size()) {
                currentAngle = segment.getStartPoint().angleToOtherPoint(segment.getEndPoint());
                BBDSegment segmentToAdd = new BBDSegment(segment, offsetDistance, (float) (currentAngle-offsetAngleModifier));
                offsetSegments.add(segmentToAdd);
            }else{
                otherSegments.add(segment);
            }
        }

        ArrayList<BBDPoint> offsetPoints = new ArrayList<>();
        //find new vertices
        if(otherSegments.size() != 0) {
            //get the first
            offsetPoints.add(offsetSegments.get(0).interceptPoint(otherSegments.get(otherSegments.size() - 1)));
            //get the middles
            if (offsetSegments.size() > 1) {
                for (int i = 0; i < offsetSegments.size() - 1; i++) {
                    offsetPoints.add(offsetSegments.get(i).interceptPoint(offsetSegments.get(i + 1)));
                }
            }
            //get the last
            offsetPoints.add(offsetSegments.get(offsetSegments.size() - 1).interceptPoint(otherSegments.get(0)));
        }else{
            for(int i = 0; i<offsetSegments.size() -1; i++){
                offsetPoints.add(offsetSegments.get(i).interceptPoint(offsetSegments.get(i + 1)));
            }
            offsetPoints.add(offsetSegments.get(0).interceptPoint(offsetSegments.get(offsetSegments.size()-1)));
        }

        //find original verts to carry over
        ArrayList<BBDPoint> otherPoints = new ArrayList<>();
        if(otherSegments.size() != 0) {
            for (BBDSegment nonOffsetSegment : otherSegments) {
                if (!otherPoints.contains(nonOffsetSegment.getStartPoint())) {
                    otherPoints.add(nonOffsetSegment.getStartPoint());
                }
                if (!otherPoints.contains(nonOffsetSegment.getEndPoint())) {
                    otherPoints.add(nonOffsetSegment.getEndPoint());
                }
            }
            //remove last and first as they will be effectively repeats of the first and last of the offsets
            otherPoints.remove(otherPoints.size() - 1);
            otherPoints.remove(0);
        }

        //combine and create new BBDPolygon
        offsetPoints.addAll(otherPoints);
        return new BBDPolygon(offsetPoints);
    }

    public static BBDPolygon offsetPolygonWithRadius(BBDPolygon poly1, float offsetDistance, int resolution) throws ParallelLinesException {
        //check polygon direction so we know which way to offset
        int polygonDirection = poly1.determineDirectionality();
        float offsetAngleModifier;
        if (polygonDirection == BBDGeometryUtils.COUNTERCLOCKWISE_POLYGON){
            offsetAngleModifier = (float)Math.PI/2;
        }else{
            offsetAngleModifier = (float)-Math.PI/2;
        }

        //build a list off offsets and a list of not, which is 2 contiguous lists
        ArrayList<BBDSegment> offsetSegments = new ArrayList<>();
        ArrayList<BBDSegment> otherSegments = new ArrayList<>();
        float currentAngle;

        //deep copy to avoid rotating the original list.
        ArrayList<BBDSegment> segmentList = new ArrayList<>();
        for (BBDSegment seg: poly1.getSegments()){
            segmentList.add(new BBDSegment(seg));
        }

        //loop through the segments from the start to the end and copy
        for(BBDSegment segment : poly1.getSegments()){
            currentAngle = segment.getStartPoint().angleToOtherPoint(segment.getEndPoint());
            BBDSegment segmentToAdd = new BBDSegment(segment, offsetDistance, (currentAngle-offsetAngleModifier));
            offsetSegments.add(segmentToAdd);
        }
        float fullCircleRadians = 2 * (float)Math.PI;
        float angleIncrement = fullCircleRadians/resolution;
        ArrayList<BBDPoint> newPoints = new ArrayList<>();
        //Build output by grabbing segments and building arc to next segment
        for (int i=0; i<offsetSegments.size(); i++){
            BBDPoint centerPoint = poly1.getSegments().get(i).getEndPoint();

            BBDSegment currentSegment = offsetSegments.get(i);
            BBDSegment nextSegment = offsetSegments.get((i+1)%offsetSegments.size());

            newPoints.add(currentSegment.getEndPoint());

            float startAngle = centerPoint.angleToOtherPoint(currentSegment.getEndPoint());
            float endAngle = centerPoint.angleToOtherPoint(nextSegment.getStartPoint());
            int numberOfSteps = (int)Math.floor(((startAngle+4)-(endAngle+4))/angleIncrement); //+4 to avoid the +/- flip at 180 degrees
            if (numberOfSteps < 0){
                numberOfSteps = (int)Math.floor(((startAngle+4+2*Math.PI)-(endAngle+4))/angleIncrement); //+4 to avoid the +/- flip at 180 degrees

            }
            float tempAngle = startAngle;
            for (int count = 0; count < numberOfSteps; count++){
                if (polygonDirection == BBDGeometryUtils.COUNTERCLOCKWISE_POLYGON){
                    tempAngle -= angleIncrement;
                }else{
                    tempAngle += angleIncrement;
                }
                newPoints.add(new BBDPoint(centerPoint, offsetDistance, tempAngle));
            }

            newPoints.add(nextSegment.getStartPoint());
        }

        //return new polygon
        return new BBDPolygon(newPoints);
    }
}
