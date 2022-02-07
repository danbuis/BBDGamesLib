package BBDGameLibrary.GameEngine.SampleGame;

import BBDGameLibrary.GUI.BBDFont;
import BBDGameLibrary.GUI.BBDTextLine;
import BBDGameLibrary.GameEngine.*;
import BBDGameLibrary.Geometry2d.BBDPoint;
import BBDGameLibrary.Geometry2d.BBDPolygon;
import BBDGameLibrary.OpenGL.*;
import BBDGameLibrary.Utils.ShaderPrograms;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

public class DummyGame implements GameComponent {

    private final Renderer renderer;

    private GameItem[] gameItems;

    private Camera camera = new Camera();

    private BBDFont font;
    private BBDTextLine text;

    public DummyGame() {
        renderer = new Renderer();
    }

    /**
     * An implementation of the init function expected by the GameComponent interface.
     * This particular function builds a cube from vertex information, maps a texture to it, and creates a new Mesh object
     * and adds it to a list of items to be rendered.
     *
     * Vertex position and vertex texture coordinates map 1:1 to each other.  You should have the same number of position
     * vertices as texture coordinates.  In this instance the number of position verts is driven by the number of texture
     * coordinates.  Since each vertex can only have one texture coordinate, and we are making a cube, vertexes will need
     * to be repeated so that  different faces can use different parts of the texture.  In this case the back 2 vertical
     * edges of the cube are sharing textures with adjacent sides, so we only have 20 verts for a cube instead of 24.  You
     * can see the difference by scaling up the cube so that the view is inside the cube, that way you can see the back
     * face and the mirrored texture at the edges.
     *
     * The indices array takes the list of positions and makes triangles out of the indices.  Those triangles define what
     * gets rendered.
     * @param window Window object that will be displaying the GameComponent
     */
    @Override
    public void init(Window window) {
        try {
            font = new BBDFont("resources/text/Arial_Bold_White.bmp", "resources/text/Arial_Bold_White.csv");
            text = new BBDTextLine(font, 1, "Hi Player! ", 4000);
        } catch(FileNotFoundException e){
            System.out.println("font file not found : " + e);
        }

        // Create the Mesh
        float[] positions = new float[] {
                // V0
                -0.5f, 0.5f, 0.5f,
                // V1
                -0.5f, -0.5f, 0.5f,
                // V2
                0.5f, -0.5f, 0.5f,
                // V3
                0.5f, 0.5f, 0.5f,
                // V4
                -0.5f, 0.5f, -0.5f,
                // V5
                0.5f, 0.5f, -0.5f,
                // V6
                -0.5f, -0.5f, -0.5f,
                // V7
                0.5f, -0.5f, -0.5f,

                // For text coords in top face
                // V8: V4 repeated
                -0.5f, 0.5f, -0.5f,
                // V9: V5 repeated
                0.5f, 0.5f, -0.5f,
                // V10: V0 repeated
                -0.5f, 0.5f, 0.5f,
                // V11: V3 repeated
                0.5f, 0.5f, 0.5f,

                // For text coords in right face
                // V12: V3 repeated
                0.5f, 0.5f, 0.5f,
                // V13: V2 repeated
                0.5f, -0.5f, 0.5f,

                // For text coords in left face
                // V14: V0 repeated
                -0.5f, 0.5f, 0.5f,
                // V15: V1 repeated
                -0.5f, -0.5f, 0.5f,

                // For text coords in bottom face
                // V16: V6 repeated
                -0.5f, -0.5f, -0.5f,
                // V17: V7 repeated
                0.5f, -0.5f, -0.5f,
                // V18: V1 repeated
                -0.5f, -0.5f, 0.5f,
                // V19: V2 repeated
                0.5f, -0.5f, 0.5f,
        };
        float[] textCoords = new float[]{
                0.0f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.5f,
                0.5f, 0.0f,

                0.0f, 0.0f,
                0.5f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.5f,

                // For text coords in top face
                0.0f, 0.5f,
                0.5f, 0.5f,
                0.0f, 1.0f,
                0.5f, 1.0f,

                // For text coords in right face
                0.0f, 0.0f,
                0.0f, 0.5f,

                // For text coords in left face
                0.5f, 0.0f,
                0.5f, 0.5f,

                // For text coords in bottom face
                0.5f, 0.0f,
                1.0f, 0.0f,
                0.5f, 0.5f,
                1.0f, 0.5f,
        };
        int[] indices = new int[]{
                // Front face
                0, 1, 3, 3, 1, 2,
                // Top Face
                8, 10, 11, 9, 8, 11,
                // Right face
                12, 13, 7, 5, 12, 7,
                // Left face
                14, 15, 6, 4, 14, 6,
                // Bottom face
                16, 18, 19, 17, 16, 19,
                // Back face
                4, 6, 7, 5, 4, 7,};
        Texture texture = new Texture("resources/textures/grassblock.png");
        Mesh mesh = new Mesh(positions, textCoords, indices, texture);
        ShaderProgram exampleShader = ShaderPrograms.TEXTURED_GENERIC;
        GameItem item1 = new DummyCube(mesh, exampleShader);
        item1.setPosition(0, 0, -2);

        GameItem item1a = new DummyCube(mesh, exampleShader);
        item1a.setPosition(2,0,-2);
        /*
        Build a background 2d object to demo and test with
        BBDGameLibrary.OpenGL objects rotate around their local origin.  This object is centered at the origin, but then we move it away
        by updating its position.  In openGL land this means that the object internally thinks it is centered at the origin
        but at render time the position matrix shifts the position in world space.
         */
        BBDPolygon poly1 = new BBDPolygon(new BBDPoint[]{new BBDPoint(-1.5f, 0), new BBDPoint(0.5f, 2),
                new BBDPoint(1.5f, 1), new BBDPoint(1.5f, 0), new BBDPoint(-0.5f, -2)});
        Mesh shape1 = Mesh.buildMeshFromPolygon(poly1, null);
        ShaderProgram example2 = ShaderPrograms.buildSolidColorShader();
        float[] color = {0.06f, 0.47f, 0.03f, 1.0f};
        GameItem2d item2 = new DummyShape2d(shape1, example2, poly1, 5000, false, 7);
        item2.addSolidColorUniform(color[0], color[1], color[2], color[3]);
        item2.translate(1.5f, 0);

        /*
        Build a background 2d object to demo and test with
         */
        BBDPolygon poly2 = new BBDPolygon(new BBDPoint[]{new BBDPoint(0, 0), new BBDPoint(0, 2),
                new BBDPoint(-3,2), new BBDPoint(-3,-2), new BBDPoint(-2,-2), new BBDPoint(-2,0)});
        Mesh shape2 = Mesh.buildMeshFromPolygon(poly2, null);
        float[] color2 = {0.46f, 0.37f, 0.24f, 1.0f};
        ShaderProgram example3 = ShaderPrograms.buildSolidColorShader();
        GameItem2d item3 = new DummyShape2d(shape2, example3, poly2, 5100, false, 0);
        item3.addSolidColorUniform(color2[0], color2[1], color2[2], color2[3]);

        //populate list of items to be rendered
        gameItems = new GameItem[]{item1, item1a, item2, item3};
    }


    @Override
    public void input(Window window, MouseInput mouseInput) {
        for (GameItem gameItem: gameItems){
            gameItem.input(window, mouseInput);
        }
    }

    @Override
    public void update(float interval, MouseInput mouseInput, Window window) {
        for (GameItem gameItem : gameItems) {
            gameItem.update(interval, mouseInput, window);
        }
    }

    public void render(Window window) {
        renderer.resetRenderer(window);
        renderer.renderArray(window, gameItems, camera);
        text.renderTextLine(window, renderer, camera);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        for (GameItem gameItem : gameItems) {
            gameItem.getMesh().cleanUp();
        }
    }

}