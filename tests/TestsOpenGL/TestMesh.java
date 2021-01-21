package TestsOpenGL;

import BBDGameLibrary.Geometry2d.BBDGeometryUtils;
import BBDGameLibrary.Geometry2d.BBDPoint;
import BBDGameLibrary.Geometry2d.BBDPolygon;
import BBDGameLibrary.OpenGL.Mesh;
import BBDGameLibrary.TestUtils;
import org.joml.Vector3f;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class TestMesh {

    @Test
    public void testBuildMeshPositionsFromPolygon(){
        BBDPolygon poly = TestUtils.buildSquare();
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

    @Test
    public void testBuildMeshTextureCoordinatesFromPolygon(){
        //construct a square that has co-linear points on the edges.
        BBDPoint point1 = new BBDPoint(1,1);
        BBDPoint point15 = new BBDPoint(1,0);
        BBDPoint point2 = new BBDPoint(0,0);
        BBDPoint point25 = new BBDPoint(0,-1);
        BBDPoint point3 = new BBDPoint(-1,-1);
        BBDPoint point35 = new BBDPoint(-0.8f,0);
        BBDPoint point4 = new BBDPoint(-0.8f,0.8f);
        BBDPoint point45 = new BBDPoint(0,1);

        ArrayList<BBDPoint> points = new ArrayList<>(Arrays.asList(point1, point15, point2, point25, point3, point35, point4, point45));
        BBDPolygon poly = new BBDPolygon(points);

        float[] testCoords = Mesh.buildTextureCoordinates(poly);
        assertEquals(16, testCoords.length);

        assertEquals(1, testCoords[0], BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(0,testCoords[1], BBDGeometryUtils.ALLOWABLE_DELTA);

        assertEquals(1, testCoords[2], BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(0.5, testCoords[3], BBDGeometryUtils.ALLOWABLE_DELTA);

        assertEquals(0.5, testCoords[4], BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(0.5, testCoords[5], BBDGeometryUtils.ALLOWABLE_DELTA);

        assertEquals(0.5, testCoords[6], BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(1, testCoords[7], BBDGeometryUtils.ALLOWABLE_DELTA);

        assertEquals(0, testCoords[8], BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(1, testCoords[9], BBDGeometryUtils.ALLOWABLE_DELTA);

        assertEquals(0.1, testCoords[10], BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(0.5, testCoords[11], BBDGeometryUtils.ALLOWABLE_DELTA);

        assertEquals(0.1, testCoords[12], BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(0.1, testCoords[13], BBDGeometryUtils.ALLOWABLE_DELTA);

        assertEquals(0.5, testCoords[14], BBDGeometryUtils.ALLOWABLE_DELTA);
        assertEquals(0, testCoords[15], BBDGeometryUtils.ALLOWABLE_DELTA);
    }

    @Test
    public void testBuildMeshIndicesFromPolygon(){
        BBDPolygon poly = TestUtils.buildSquare();
        int[] testPositions = Mesh.buildIndices(poly);
        assertEquals(6, testPositions.length);
        assertEquals(testPositions[0], 2);
        assertEquals(testPositions[1], 1);
        assertEquals(testPositions[2], 0);

        assertEquals(testPositions[3], 3);
        assertEquals(testPositions[4], 2);
        assertEquals(testPositions[5], 0);
    }

    @Test
    public void testMeshVertPositions(){
        BBDPolygon poly = TestUtils.buildSquare();
        Mesh test = Mesh.buildMeshFromPolygon(poly);
        Vector3f[] testVertices = test.getVertexPositions();
        assertEquals(4, testVertices.length);
        assertEquals(new Vector3f(1,1,0), testVertices[0]);
        assertEquals(new Vector3f(1,-1,0), testVertices[1]);
        assertEquals(new Vector3f(-1,-1,0), testVertices[2]);
        assertEquals(new Vector3f(-1,1,0), testVertices[3]);
    }
}
