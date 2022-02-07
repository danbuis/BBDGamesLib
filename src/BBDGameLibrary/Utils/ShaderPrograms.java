package BBDGameLibrary.Utils;

import BBDGameLibrary.Geometry2d.BBDPoint;
import BBDGameLibrary.Geometry2d.BBDPolygon;
import BBDGameLibrary.OpenGL.ShaderProgram;
import org.joml.Vector3d;
import org.joml.Vector4f;

import java.util.ArrayList;

/**
 * A class to hold several utility functions so that we don't have to have the same function in several places.  All functions
 * should be static so that we don't need to create an object to use them.
 */
public class ShaderPrograms {

    public static ShaderProgram TEXTURED_GENERIC = buildBasicTexturedShaderProgram();

    /**
     * Creates a ShaderProgram, which is essentially a set of instructions to the graphics card on how to render vertices.
     * In this case it takes in a shader to tell it how to handle 3d data points, a shader to tell it how to determine what color
     * to draw for each pixel, and then attaches them to the program.  Then it creates "uniforms" which are basically
     * variables for shaders.  The first 2 are for rendering vertices in the right spot, the third one is for getting the
     * texture put on.  You can make your own shader programs with their own uniforms, but you should probably use this
     * one as a base.
     * @return
     */
    public static ShaderProgram buildBasicTexturedShaderProgram(){
        ShaderProgram returnProgram = null;
        try {
            returnProgram = new ShaderProgram();

            //create and attach shaders
            returnProgram.createVertexShader(BBDGameLibrary.OpenGL.Utils.loadShaderScript("/shaders/vertex.vs"));
            returnProgram.createFragmentShader(BBDGameLibrary.OpenGL.Utils.loadShaderScript("/shaders/fragment.fs"));

            //give the shader program an id
            returnProgram.link();

            // Create uniforms for world and projection matrices and texture
            returnProgram.createUniform("projectionMatrix");
            returnProgram.createUniform("worldMatrix");
            returnProgram.createUniform("texture_sampler");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnProgram;
    }

    /**
     * Create a shader program to draw a given solid color.  Currently has the requirement that a file of the given name
     * be present, although there is no requirement that the name of the file actually reflect the color contained within.
     * @return a ShaderProgram to render things to the screen.
     */
    public static ShaderProgram buildSolidColorShader(){
        ShaderProgram returnProgram = null;
        try {
            returnProgram = new ShaderProgram();

            //create and attach shaders
            returnProgram.createVertexShader(BBDGameLibrary.OpenGL.Utils.loadShaderScript("/shaders/solid_color_vertex.vs"));
            returnProgram.createFragmentShader(BBDGameLibrary.OpenGL.Utils.loadShaderScript("/shaders/solid_color_fragment.fs"));

            //build and compile
            returnProgram.link();

            // Create uniforms for world and projection matrices and texture
            returnProgram.createUniform("projectionMatrix");
            returnProgram.createUniform("worldMatrix");
            returnProgram.createUniform("solidColor");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnProgram;
    }
}
