package BBDGameLibrary.GameEngine.SampleGame;

import BBDGameLibrary.GameEngine.GameItem;
import BBDGameLibrary.GameEngine.MouseBoxSelectionDetector;
import BBDGameLibrary.GameEngine.MouseInput;
import BBDGameLibrary.OpenGL.Mesh;
import BBDGameLibrary.OpenGL.Window;
import BBDGameLibrary.OpenGL.ShaderProgram;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class DummyCube extends GameItem {
    private int displxInc = 0;

    private int displyInc = 0;

    private int displzInc = 0;

    private int scaleInc = 0;

    public DummyCube(Mesh mesh, ShaderProgram shaderProgram) {
        super(mesh, shaderProgram);
    }

    /**
     * Set uniforms to be used by this GameItems shader program.
     * @param projectionMatrix projection matrix
     * @param modelViewMatrix camera and world matrices
     */
    @Override
    public void setUniforms(Matrix4f projectionMatrix, Matrix4f modelViewMatrix) {
        this.shader.setUniform("projectionMatrix", projectionMatrix);
        this.shader.setUniform("modelViewMatrix", modelViewMatrix);
        this.shader.setUniform("texture_sampler", 0);
    }

    /**
     * Keyboard input to change the transformation of the cube.  Up/Down to translate up and down.  Left/Right to translate
     * left and right.  A/Q to translate in and out.  Z/X to scale up and down.  This implementation does not support multiple
     * key presses/input
     * @param window the Window object this GameComponent is using.
     */
    @Override
    public void input(Window window, MouseInput mouseInput){
        displyInc = 0;
        displxInc = 0;
        displzInc = 0;
        scaleInc = 0;
        if (window.isKeyPressed(GLFW_KEY_UP)) {
            displyInc = 3;
        } else if (window.isKeyPressed(GLFW_KEY_DOWN)) {
            displyInc = -3;
        } else if (window.isKeyPressed(GLFW_KEY_LEFT)) {
            displxInc = -3;
        } else if (window.isKeyPressed(GLFW_KEY_RIGHT)) {
            displxInc = 3;
        } else if (window.isKeyPressed(GLFW_KEY_A)) {
            displzInc = -3;
        } else if (window.isKeyPressed(GLFW_KEY_Q)) {
            displzInc = 3;
        } else if (window.isKeyPressed(GLFW_KEY_Z)) {
            scaleInc = -1;
        } else if (window.isKeyPressed(GLFW_KEY_X)) {
            scaleInc = 1;
        }
    }

    /**
     * Connect the keyboard input to the GameComponent's position matrices and such.  This item
     * uses flat values per update and doesn't care about how much time has passed between updates.
     * @param interval elapsed time
     */
    @Override
    public void update(float interval, MouseInput mouseInput, Window window){
        // Update position
        Vector3f itemPos = this.getPosition();
        float posx = itemPos.x + displxInc * 0.01f;
        float posy = itemPos.y + displyInc * 0.01f;
        float posz = itemPos.z + displzInc * 0.01f;
        this.setPosition(posx, posy, posz);

        // Update scale
        float scale = this.getScale();
        scale += scaleInc * 0.05f;
        if (scale < 0) {
            scale = 0;
        }
        this.setScale(scale);

        // Update rotation angle
        float rotation = (float) (this.getRotation().x + Math.toRadians(1.5f));
        if (rotation > Math.PI * 2) {
            rotation = 0;
        }
        //this.setRotation(rotation, rotation, rotation);
    }
}
