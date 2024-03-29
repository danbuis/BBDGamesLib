package BBDGameLibrary.OpenGL;

import BBDGameLibrary.Geometry2d.BBDGeometryHelpers;
import BBDGameLibrary.Geometry2d.BBDPoint;
import BBDGameLibrary.Geometry2d.BBDPolygon;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * A collection of vertices and ancillary information to enable rendering an object to the screen
 */
public class Mesh {

    /**
     * Vertex array object id.  Created automatically
     */
    private  int vaoId;

    /**
     * List of vertex buffer objects.  Created and populatd automatically
     */
    private  List<Integer> vboIdList;

    /**
     * Count of vertices for the mesh
     */
    private int vertexCount;

    /**
     * What Texture to apply to the mesh
     */
    private  Texture texture;

    /**
     * A list of indices to match up with the positions
     */
    private int[] indices;

    public int[] getIndices() {
        return indices;
    }

    public Vector3f[] getVertexPositions() {
        return vertexPositions;
    }

    /**
     * An array of vertex positions.
     */
    private Vector3f[] vertexPositions;

    /**
     * Position coordinates are created directly from the BBDPoints that define the polygon.
     * @param inputShape shape to use for a mesh.
     * @return array of floats for position coordinates
     */
    public static float[] buildMeshPositions(BBDPolygon inputShape){
        ArrayList<BBDPoint> points = inputShape.getPoints();
        float[] positions = new float[3*points.size()];
        for (int i = 0; i< points.size(); i++){
            positions[i * 3] = points.get(i).getXLoc();
            positions[i * 3 + 1] = points.get(i).getYLoc();
            positions[i * 3 + 2] = 0;
        }
        return positions;
    }

    /**
     * Texture coordinates are created with the assumption that the shape should go to the very edges of the given texture.
     * Remember that 0,0 is the upper left corner of the image file, and 1,1 is the lower right.
     * @param inputShape shape to use for a mesh.
     * @return array of floats for texture coordinates
     */
    public static float[] buildTextureCoordinates(BBDPolygon inputShape){
        float maxY = inputShape.maxY();
         float minX = inputShape.minX();
         float width = inputShape.width();
         float height = inputShape.height();
         ArrayList<BBDPoint> points = inputShape.getPoints();
         float[] textureCoordinates = new float[2*points.size()];
         float deltaX;
         float deltaY;
         for(int i = 0; i< points.size(); i++){
            deltaY = maxY - points.get(i).getYLoc();
            deltaX = points.get(i).getXLoc() - minX;

            textureCoordinates[2 * i] = deltaX/width;
            textureCoordinates[2 * i+1] = deltaY/height;
         }
         return textureCoordinates;
    }

    /**
     * Texture coordinates are created with the assumption that the shape should go to the very edges of the given texture.
     * Remember that 0,0 is the upper left corner of the image file, and 1,1 is the lower right.
     * @param inputShape shape to use for a mesh.
     * @return array of floats for texture coordinates
     */
    public static float[] buildTextureCoordinates(BBDPolygon inputShape, float originX, float originY, float oppositeX, float oppositeY){
        float width =  originX - oppositeX;
        float height = originY - oppositeY;
        ArrayList<BBDPoint> points = inputShape.getPoints();
        float[] textureCoordinates = new float[2*points.size()];
        float deltaX;
        float deltaY;
        for(int i = 0; i< points.size(); i++){
            deltaY = oppositeY - points.get(i).getYLoc();
            deltaX = points.get(i).getXLoc() - originX;

            textureCoordinates[2 * i] = deltaX/width;
            textureCoordinates[2 * i+1] = deltaY/height;
        }
        return textureCoordinates;
    }

    /**
     * Build indices array for the mesh
     * @param inputShape BBDPolygon to use to create a mesh
     * @return indices array
     */
    public static int[] buildIndices(BBDPolygon inputShape){
        ArrayList<BBDPoint> points = inputShape.getPoints();
        BBDPolygon[] triangles = inputShape.decomposeIntoTriangles(BBDGeometryHelpers.COUNTERCLOCKWISE_POLYGON);
        int[] output = new int[3 * triangles.length];
        for (int tri = 0; tri < triangles.length; tri++){
            for (int vert = 0; vert < 3; vert++){
                for(int inputIndex = 0; inputIndex < points.size(); inputIndex++){
                    if(triangles[tri].getPoints().get(vert).equals(points.get(inputIndex))){
                        output[3*tri + vert] = inputIndex;
                    }
                }
            }
        }
        return output;
    }

    /**
     * Build a mesh object from a BBDPolygon object and a texture file.
     * @param inputShape BBDPolygon to use to create a mesh
     * @param texture image texture to apply to the mesh
     * @return Mesh object
     */
    public static Mesh buildMeshFromPolygon(BBDPolygon inputShape, Texture texture){
        float[] positions = buildMeshPositions(inputShape);
        float[] textureCoordinates = buildTextureCoordinates(inputShape);
        int[] indices = buildIndices(inputShape);
        return new Mesh(positions, textureCoordinates, indices, texture);
    }

    /**
     * Build a mesh object from a BBDPolygon object.
     * @param inputShape BBDPolygon to use to create a mesh
     * @return Mesh object
     */
    public static Mesh buildMeshFromPolygon(BBDPolygon inputShape){
        float[] positions = buildMeshPositions(inputShape);
        float[] textureCoordinates = buildTextureCoordinates(inputShape);
        int[] indices = buildIndices(inputShape);
        return new Mesh(positions, textureCoordinates, indices);
    }

    /**
     * CAll purpose constructor to pass in vertex data for a mesh that doesn't need to be rendered to the screen.  A mesh
     * created with this constructor won't be able to interact with any of the openGL functions.
     * @param positions positions of vertices
     * @param textCoords texture coordinates of vertices
     * @param indices list of indices created triangles, triangles need to be clockwise
     */
    public Mesh(float[] positions, float[] textCoords, int[] indices) {
        FloatBuffer posBuffer = null;
        FloatBuffer textCoordsBuffer = null;
        IntBuffer indicesBuffer = null;

        this.populateVertexData(positions, indices);
    }

    /**
     * All purpose constructor to pass in vertex and texture data.
     * @param positions positions of vertices
     * @param textCoords texture coordinates of vertices
     * @param indices list of indices created triangles, triangles need to be clockwise
     * @param texture Texture object used to create the texture
     */
    public Mesh(float[] positions, float[] textCoords, int[] indices, Texture texture) {
        FloatBuffer posBuffer = null;
        FloatBuffer textCoordsBuffer = null;
        IntBuffer indicesBuffer = null;

        this.populateVertexData(positions, indices);
        try {
            this.texture = texture;
            vboIdList = new ArrayList<>();

            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);
            // Position VBO
            int vboId = glGenBuffers();
            vboIdList.add(vboId);
            posBuffer = MemoryUtil.memAllocFloat(positions.length);
            posBuffer.put(positions).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(0);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
            // Texture coordinates VBO
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            textCoordsBuffer = MemoryUtil.memAllocFloat(textCoords.length);
            textCoordsBuffer.put(textCoords).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, textCoordsBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(1);
            glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
            // Index VBO
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            indicesBuffer = MemoryUtil.memAllocInt(indices.length);
            indicesBuffer.put(indices).flip();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
        } finally {
            if (posBuffer != null) {
                MemoryUtil.memFree(posBuffer);
            }
            if (textCoordsBuffer != null) {
                MemoryUtil.memFree(textCoordsBuffer);
            }
            if (indicesBuffer != null) {
                MemoryUtil.memFree(indicesBuffer);
            }
        }
    }

    /**
     * Populate the non-changing vertex data so that it is in a form that can be referenced by other classes
     * @param positions vertex positions
     * @param indices indices used to create triangles of the mesh
     */
    private void populateVertexData(float[] positions, int[] indices) {
        //build vertex data
        this.indices = indices;
        this.vertexCount = indices.length;
        this.vertexPositions = new Vector3f[positions.length / 3];
        for(int i = 0; i<vertexPositions.length; i++){
            int startingIndex = i*3;
            vertexPositions[i] = new Vector3f(positions[startingIndex], positions[startingIndex + 1], positions[startingIndex + 2]);
        }
    }

    public int getVaoId() {
        return vaoId;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    /**
     * Actually draw the mesh.
     */
    public void render() {
        if(texture != null) {
            // Activate first texture bank
            glActiveTexture(GL_TEXTURE0);
            // Bind the texture
            glBindTexture(GL_TEXTURE_2D, texture.getId());
        }

        // Draw the mesh
        glBindVertexArray(getVaoId());

        glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);

        // Restore state
        glBindVertexArray(0);
    }

    public void cleanUp() {
        glDisableVertexAttribArray(0);

        // Delete the VBOs
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        for (int vboId : vboIdList) {
            glDeleteBuffers(vboId);
        }

        // Delete the texture
        if(texture != null) {
            texture.cleanup();
        }

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }
}