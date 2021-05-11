package BBDGameLibrary.OpenGL;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

/**
 * Class to handle a shader program for a mesh.  Handles all aspects of the graphics pipeline in one location
 *
 */
public class ShaderProgram {

    /**
     * Id of the shader program
     */
    private final int programId;

    /**
     * Id for the vertex shader portion, since multiple programs can use the same shader scripts
     */
    private int vertexShaderId;

    /**
     * Id for the fragment shader portion, since multiple programs can use the same shader scripts
     */
    private int fragmentShaderId;

    /**
     * A list of the uniforms being passed into the program.  Uniforms are bits of data that are not
     * part of the vertex data.  Things like the projection matrix (adjusting for perspective), the world matrix (objects can move
     * and are not centered on their local origin) or texture data.
     */
    private final Map<String, Integer> uniforms;

    /**
     * Generic constructor, takes no arguments and builds the basic foundation for a shader program
     * @throws Exception throws an exception if the program could not be created.
     */
    public ShaderProgram() throws Exception {
        programId = glCreateProgram();
        if (programId == 0) {
            throw new Exception("Could not create Shader");
        }
        uniforms = new HashMap<>();
    }

    /**
     * Creates a uniform and attaches it to the ShaderProgram
     * @param uniformName Name of the uniform.  Should match the uniform given in the shader scripts
     * @throws Exception throws an exception if a uniform of the given name cannot be found
     */
    public void createUniform(String uniformName) throws Exception {
        int uniformLocation = glGetUniformLocation(programId, uniformName);
        if (uniformLocation < 0) {
            throw new Exception("Could not find uniform:" + uniformName);
        }
        uniforms.put(uniformName, uniformLocation);
    }

    /**
     * Assign a matrix value to the given uniform.  Stores the matrix as a memory address
     * @param uniformName Name of the uniform.  Should match the uniform given in the shader scripts
     * @param value matrix to be stored as the uniform
     */
    public void setUniform(String uniformName, Matrix4f value) {
        // Dump the matrix into a float buffer
        try (MemoryStack stack = MemoryStack.stackPush()) {
            glUniformMatrix4fv(uniforms.get(uniformName), false,
                    value.get(stack.mallocFloat(16)));
        }
    }

    /**
     * Assign a color to a uniform
     * @param uniformName Name of the uniform
     * @param color 4 floats for the color.  Follows the format RGBA.
     */
    public void setUniform(String uniformName, float[] color){
        glUniform4fv(uniforms.get(uniformName), color);
    }

    /**
     * Assign an integer value to the given uniform.
     * @param uniformName Name of the uniform.  Should match the uniform given in the shader scripts
     * @param value integer to be stored as the uniform
     */
    public void setUniform(String uniformName, int value) {
        glUniform1i(uniforms.get(uniformName), value);
    }

    /**
     * Attach a script to be used as a vertex shader
     * @param shaderCode string containing the shader script
     * @throws Exception throws an exception if the shader script does not compile
     */
    public void createVertexShader(String shaderCode) throws Exception {
        vertexShaderId = createShader(shaderCode, GL_VERTEX_SHADER);
    }

    /**
     * Attach a script to be used as a fragment shader
     * @param shaderCode string containing the shader script
     * @throws Exception throws an exception if the shader script does not compile
     */
    public void createFragmentShader(String shaderCode) throws Exception {
        fragmentShaderId = createShader(shaderCode, GL_FRAGMENT_SHADER);
    }

    /**
     * Function to create a shader from a script and attach it to the shader program
     * @param shaderCode string containing the shader script
     * @param shaderType what kind of script is this to be used for, ie vertex, fragment etc.
     * @return the memory location of the shader
     * @throws Exception throws an exception if the shader script does not compile
     */
    protected int createShader(String shaderCode, int shaderType) throws Exception {
        int shaderId = glCreateShader(shaderType);
        if (shaderId == 0) {
            throw new Exception("Error creating shader. Type: " + shaderType);
        }

        glShaderSource(shaderId, shaderCode);
        glCompileShader(shaderId);

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(shaderId, 1024));
        }

        glAttachShader(programId, shaderId);

        return shaderId;
    }

    /**
     * Packages all the shader program into an openGL friendly package
     * @throws Exception throws an exception if the shader program is unable to be linked
     */
    public void link() throws Exception {
        glLinkProgram(programId);
        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(programId, 1024));
        }

        if (vertexShaderId != 0) {
            glDetachShader(programId, vertexShaderId);
        }
        if (fragmentShaderId != 0) {
            glDetachShader(programId, fragmentShaderId);
        }

        glValidateProgram(programId);
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(programId, 1024));
        }

    }

    /**
     * Bind this shader program to make it the current shader to be used
     */
    public void bind() {
        glUseProgram(programId);
    }

    /**
     * Unbind this program to stop using it
     */
    public void unbind() {
        glUseProgram(0);
    }

    public void cleanup() {
        unbind();
        if (programId != 0) {
            glDeleteProgram(programId);
        }
    }
}
