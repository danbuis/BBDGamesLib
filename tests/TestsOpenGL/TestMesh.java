package TestsOpenGL;

import Geometry2d.BBDPoint;
import Geometry2d.BBDPolygon;
import OpenGL.Mesh;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestMesh {

    public BBDPolygon buildSquare(){
        BBDPoint point1 = new BBDPoint(1,1);
        BBDPoint point2 = new BBDPoint(1,-1);
        BBDPoint point3 = new BBDPoint(-1,-1);
        BBDPoint point4 = new BBDPoint(-1,1);

        BBDPoint[] points = {point1, point2, point3, point4};

        return new BBDPolygon(points);
    }

    @Test
    public void testBuildMeshPositionsFromPolygon(){
        BBDPolygon poly = this.buildSquare();
        float[] testPositions = Mesh.buildMeshPositions(poly);
        assertEquals(12, testPositions.length);

        assertEquals(testPositions[0], 1.0);
        assertEquals(testPositions[1], 1.0);
        assertEquals(testPositions[2], 0f);

        assertEquals(testPositions[3], 1.0);
        assertEquals(testPositions[4], -1.0);
        assertEquals(testPositions[5], 0f);

        assertEquals(testPositions[6], -1.0);
        assertEquals(testPositions[7], -1.0);
        assertEquals(testPositions[8], 0f);

        assertEquals(testPositions[9], -1.0);
        assertEquals(testPositions[10], 1.0);
        assertEquals(testPositions[11], 0f);
    }
}
