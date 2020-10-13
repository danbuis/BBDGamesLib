package OpenGL;

import Geometry2d.BBDGeometryUtils;
import Geometry2d.BBDPoint;
import Geometry2d.BBDPolygon;
import org.lwjgl.system.MemoryUtil;

import javax.xml.soap.Text;
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
    private final int vaoId;

    /**
     * List of vertex buffer objects.  Created and populatd automatically
     */
    private final List<Integer> vboIdList;

    /**
     * Count of vertices for the mesh
     */
    private final int vertexCount;

    /**
     * What Texture to apply to the mesh
     */
    private final Texture texture;

    /**
     * Position coordinates are created directly from the BBDPoints that define the polygon.
     * @param inputShape shape to use for a mesh.
     * @return array of floats for position coordinates
     */
    public static float[] buildMeshPositions(BBDPolygon inputShape){
        BBDPoint[] points = inputShape.getPoints();
        float[] positions = new float[3*points.length];
        for (int i = 0; i< points.length; i++){
            positions[i * 3] = points[i].getXLoc();
            positions[i * 3 + 1] = points[i].getYLoc();
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
         BBDPoint[] points = inputShape.getPoints();
         float[] textureCoordinates = new float[2*points.length];
         float deltaX;
         float deltaY;
         for(int i = 0; i< points.length; i++){
            deltaY = maxY - points[i].getYLoc();
            deltaX = points[i].getXLoc() - minX;

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
        BBDPoint[] points = inputShape.getPoints();
        BBDPolygon[] triangles = inputShape.decomposeIntoTriangles(BBDGeometryUtils.COUNTERCLOCKWISE_POLYGON);
        int[] output = new int[3 * triangles.length];

        for (int tri = 0; tri < triangles.length; tri++){
            for (int vert = 0; vert < 3; vert++){
                for(int inputIndex = 0; inputIndex < points.length; inputIndex++){
                    if(triangles[tri].getPoints()[vert].equals(points[inputIndex])){
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
        return new Mesh(buildMeshPositions(inputShape), buildTextureCoordinates(inputShape), buildIndices(inputShape), texture);
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
        try {
            this.texture = texture;
            vertexCount = indices.length;
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
        texture.cleanup();

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }
}