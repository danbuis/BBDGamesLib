package TestsGameEngine;

import BBDGameLibrary.GameEngine.GameItem;
import BBDGameLibrary.Geometry2d.BBDGeometryHelpers;
import BBDGameLibrary.OpenGL.Mesh;
import BBDGameLibrary.TestUtils;
import org.joml.Vector3f;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestGameItem {

    private Mesh buildSquare(){
        return Mesh.buildMeshFromPolygon(TestUtils.buildSquare());
    }

    // create a basic shader program
    private GameItem buildTestItem(){
        Mesh mesh = this.buildSquare();
        return new GameItem(mesh, null);
    }

    // test to check that the function returns different but equal vectors
    @Test
    public void testBasicDuplication(){
        GameItem test = this.buildTestItem();
        Vector3f original = test.getMesh().getVertexPositions()[0];
        Vector3f duplicate = test.getMeshVerticesRealLocations()[0];
        //check object ids
        assertNotSame(duplicate, original);
        //check properties
        assertTrue(duplicate.equals(original, BBDGeometryHelpers.ALLOWABLE_DELTA));
    }

    // test to check that translation works
    @Test
    public void testTranslation(){
        GameItem test = this.buildTestItem();
        test.setPosition(1,1,1);

        assertEquals(new Vector3f(2,2,1),test.getMeshVerticesRealLocations()[0]);
        assertEquals(new Vector3f(2,0,1),test.getMeshVerticesRealLocations()[1]);
        assertEquals(new Vector3f(0,0,1),test.getMeshVerticesRealLocations()[2]);
        assertEquals(new Vector3f(0,2,1),test.getMeshVerticesRealLocations()[3]);

        assertEquals(new Vector3f(1,1,0), test.getMesh().getVertexPositions()[0]);
    }

    // test to check that rotation works
    @Test
    public void testRotation(){
        GameItem test = this.buildTestItem();
        //rotate counter clockwise
        test.setRotation(0,0,(float)Math.toRadians(90));

        assertEquals(new Vector3f(1,1,0),test.getMeshVerticesRealLocations()[1]);
        assertEquals(new Vector3f(1,-1,0),test.getMeshVerticesRealLocations()[2]);
        assertEquals(new Vector3f(-1,-1,0),test.getMeshVerticesRealLocations()[3]);
        assertEquals(new Vector3f(-1,1,0),test.getMeshVerticesRealLocations()[0]);
    }

    // test to check that both work at the same time
    @Test
    public void testRotationAndTranslation(){
        GameItem test = this.buildTestItem();
        //rotate counter clockwise
        test.setRotation(0,0,(float)Math.toRadians(90));
        test.setPosition(1,1,1);

        assertEquals(new Vector3f(2,2,1),test.getMeshVerticesRealLocations()[1]);
        assertEquals(new Vector3f(2,0,1),test.getMeshVerticesRealLocations()[2]);
        assertEquals(new Vector3f(0,0,1),test.getMeshVerticesRealLocations()[3]);
        assertEquals(new Vector3f(0,2,1),test.getMeshVerticesRealLocations()[0]);
    }

}
