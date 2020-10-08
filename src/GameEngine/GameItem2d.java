package GameEngine;

import Geometry2d.BBDPoint;
import Geometry2d.BBDPolygon;
import OpenGL.Mesh;
import OpenGL.ShaderProgram;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * A class that represents a 2d object in a game.  It extends GameComponent and therefore uses the GameComponent interface
 * and is instantiated with some basic data such as a mesh and the shaderProgram.  It connects to the Geometry2d package
 * by including a BBDPolygon and having the ability to have it interact with other BBDPolygons and therefore
 * capitalize on the built in functionality for things like distance, area, overlaps etc.
 *
 * The intended use of this class us to be used as a base for the end user's game items.  This serves as a decent base,
 * but if you want to do anything with render or update for example you will need to overwrite those methods.  See
 * SampleGame.DummyRect for an example.
 */
public class GameItem2d extends GameItem{

    /**
     * Mesh object to be used for the GameItem
     */
    private final Mesh mesh;

    /**
     * Data structure to hold the position modification of the object
     */
    private final Vector3f position;

    /**
     * Scale factor of the object
     */
    private float scale;

    /**
     * Data structure to hold the rotation modification of the object
     */
    private final Vector3f rotation;

    /**
     * ShaderProgram to be used to render this object
     */
    public final ShaderProgram shader;

    /**
     * What layer to draw this shape on
     */
    private int layer;

    /**
     * How much distance is between layers
     */
    private final float LAYER_INTERVAL = 0.001f;

    /**
     * What shape we are using for rendering
     */
    private final BBDPolygon shape;

    /**
     * Does this shape need to interact with other BBDPolygons?
     */
    private final boolean shapeInteracts;

    /**
     * General purpose constructor to create a GameItem2d object.  Will initialize translation, scale and position to
     * neutral values.
     * @param mesh Mesh object to be rendered to the screen
     * @param shaderProgram Shader Program to be used to render this object
     * @param shape BBDPolygon object to be used as the shape
     * @param layer what layer to draw the shape on. Higher numbers are further back and will be overlapped by more items
     * @param shapeInteracts flag to denote if this shape needs to interact with other shapes.  If not, then we can bypass
     *                       lots of logic keeping the shape updated with its position in world space.
     */
    public GameItem2d(Mesh mesh, ShaderProgram shaderProgram, BBDPolygon shape,  int layer, boolean shapeInteracts) {
        super(mesh, shaderProgram);
        this.mesh = mesh;
        this.shader = shaderProgram;
        this.layer = layer;
        this.shape = shape;
        this.shapeInteracts = shapeInteracts;
        BBDPoint center = shape.center();
        position = new Vector3f(center.getXLoc(), center.getYLoc(), layer*LAYER_INTERVAL);
        scale = 1;
        rotation = new Vector3f();
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(float x, float y) {
        this.position.x = x;
        this.position.y = y;

        if(shapeInteracts){
            BBDPoint currentCenter = shape.center();
            this.translate(currentCenter.getXLoc() - x, currentCenter.getYLoc() - y);
        }
    }

    /**
     * Mimics BBDPolygon's translate function to move the shape and update the position matrix accordingly
     * @param x delta X
     * @param y delta Y
     */
    public void translate(float x, float y) {
        this.position.x += x;
        this.position.y += y;

        if(shapeInteracts){
            shape.translate(x,y);
        }
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        if(shapeInteracts){
            this.shape.scale(scale/this.scale);
        }

        this.scale = scale;
    }

    /**
     * Mimics BBDPolygon's scale function to scale the shape and update the scale value accordingly
     * @param scaleFactor factor by which to scale
     */
    public void scale(float scaleFactor){
        if(shapeInteracts){
            this.shape.scale(scaleFactor);
        }

        this.scale = scale * scaleFactor;
    }

    /**
     * Mimics BBDPolygon's scaleFromPoint function to scale and move the shape and update the position matrix
     * and scale value accordingly
     * @param point BBDPoint to serve as the origin for scaling.  Everything will scale from this point
     * @param scaleFactor factor by which to scale
     */
    public void scaleFromPoint(BBDPoint point, float scaleFactor){
        if(shapeInteracts){
            this.shape.scaleFromPoint(point, scaleFactor);
        }
        // translate
        float deltaX = this.position.x - point.getXLoc();
        float deltaY = this.position.y - point.getYLoc();
        this.position.x = this.position.x + deltaX * scaleFactor;
        this.position.y = this.position.y + deltaY * scaleFactor;
        // scale
        this.scale = scaleFactor;
    }

    public Vector3f getRotation() {
        return rotation;
    }


    public void setRotation(float z) {
        if (shapeInteracts){
            float currentRotation = this.rotation.z;
            this.shape.rotate(currentRotation - z);
        }

        this.rotation.z = z;
    }

    /**
     * Mimics BBDPolygon's rotate function to rotate the shape and update the rotation matrix accordingly
     * @param angle angle in radians to rotate
     */
    public void rotate(float angle){
        this.rotation.rotateZ(angle);

        if (shapeInteracts){
            this.shape.rotate(angle);
        }
    }

    /**
     * Mimics BBDPolygon's rotateAroundPoint function to rotate and move the shape and update the rotation
     * matrix accordingly
     * @param point center of rotation
     * @param angle angle in radians to rotate
     */
    public void rotateAroundPoint(BBDPoint point, float angle){
        this.rotation.rotateAxis(angle, point.getXLoc(), point.getYLoc(), 0);

        if(shapeInteracts){
            this.shape.rotateAroundPoint(point, angle);
        }
    }

    public Mesh getMesh() {
        return mesh;
    }

    /**
     * Set some uniforms for rendering.  This method should be overwritten if you have either
     * additional uniforms, such as textures, OR you don't use "projectionMatrix" and "worldMatrix" as
     * uniform names.
     * @param projectionMatrix projection matrix
     * @param worldMatrix world matrix
     */
    public void setUniforms(Matrix4f projectionMatrix, Matrix4f worldMatrix) {
        this.shader.setUniform("projectionMatrix", projectionMatrix);
        this.shader.setUniform("worldMatrix", worldMatrix);
    }
}