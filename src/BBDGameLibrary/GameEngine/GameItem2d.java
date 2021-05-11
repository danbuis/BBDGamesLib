package BBDGameLibrary.GameEngine;

import BBDGameLibrary.Geometry2d.BBDPoint;
import BBDGameLibrary.Geometry2d.BBDPolygon;
import BBDGameLibrary.OpenGL.Mesh;
import BBDGameLibrary.OpenGL.ShaderProgram;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * A class that represents a 2d object in a game.  It extends GameComponent and therefore uses the GameComponent interface
 * and is instantiated with some basic data such as a mesh and the shaderProgram.  It connects to the BBDGameLibrary.Geometry2d package
 * by including a BBDPolygon and having the ability to have it interact with other BBDPolygons and therefore
 * capitalize on the built in functionality for things like distance, area, overlaps etc.  NOTE that a single polygon may
 * behave unpredictably if attached to more than 1 GameItem, particularly if interaction is set to true.
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
     * @param layer what layer to draw the shape on. Higher numbers are further back
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
        super.setPosition(0, 0, -layer*LAYER_INTERVAL);
        super.setScale(1);
        super.setRotation(0,0,0);
    }

    public int getLayer(){
        return this.layer;
    }

    public void setLayer(int newLayer){
        this.layer = newLayer;
        this.setDepth();
    }

    private void setDepth(){
        this.setPosition(this.getPosition().x, this.getPosition().y, -this.layer*LAYER_INTERVAL);
    }

    public void setPosition(float x, float y) {
        if(shapeInteracts){
            BBDPoint currentCenter = shape.center();
            this.translate(x - currentCenter.getXLoc(), y - currentCenter.getYLoc());
        }else{
            this.setPosition(x, y, this.getPosition().z);
        }
    }

    /**
     * Mimics BBDPolygon's translate function to move the shape and update the position matrix accordingly
     * @param x delta X
     * @param y delta Y
     */
    public void translate(float x, float y) {
        if(shapeInteracts){
            shape.translate(x,y);
        }
        this.setPosition(this.getPosition().x + x, this.getPosition().y + y, this.getPosition().z);
    }

    public void setScale(float scale) {
        if(shapeInteracts){
            this.shape.scale(scale/this.getScale());
        }

        super.setScale(scale);
    }

    /**
     * Mimics BBDPolygon's scale function to scale the shape and update the scale value accordingly
     * @param scaleFactor factor by which to scale
     */
    public void scale(float scaleFactor){
        if(shapeInteracts){
            this.shape.scale(scaleFactor);
        }
        super.setScale(this.getScale() * scaleFactor);
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
        float deltaX = this.getPosition().x - point.getXLoc();
        float deltaY = this.getPosition().y - point.getYLoc();
        float newX = point.getXLoc() + deltaX * scaleFactor;
        float newY = point.getYLoc() + deltaY * scaleFactor;
        super.setPosition(newX, newY, this.getPosition().z);

        // scale
        super.setScale(scaleFactor);
    }

    public void setRotation(float z) {
        Vector3f rotation = this.getRotation();
        if (shapeInteracts){
            float currentRotation = rotation.z;
            this.shape.rotate(z - currentRotation);
        }

        this.setRotation(rotation.x, rotation.y, z);
    }

    /**
     * Mimics BBDPolygon's rotate function to rotate the shape and update the rotation matrix accordingly
     * BBDGeometry uses radians, whereas the rendering library uses degrees, so we'll need to convert between them so that
     * continuity is maintained.
     * @param angle angle in radians to rotate
     */
    public void rotate(float angle){
        Vector3f currentRotation = this.getRotation();
        this.setRotation(currentRotation.x, currentRotation.y, (currentRotation.z + angle));
        //BBDPoint center = shape.center();
        //rotateMeshAroundPoint(center, angle);


        //this.setRotation(currentRotation.x, currentRotation.y, currentRotation.z + angle);

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
        //this.getRotation().rotateAxis(angle, point.getXLoc(), point.getYLoc(), 0);
        //Vector3f centerOfRotation = new Vector3f(point.getXLoc(), point.getYLoc(), 0);

        rotateMeshAroundPoint(point, angle);

        Vector3f currentRotation = this.getRotation();
        this.setRotation(currentRotation.x, currentRotation.y, (currentRotation.z + angle));

        if(shapeInteracts){
            this.shape.rotateAroundPoint(point, angle);
        }
    }

    /**
     * Generic method for rotation.  This is useful because meshes rotate about their local origin, which when using
     * BBDPolygons is the origin of that polygon.  Since not all meshes are guaranteed to initialize on the origin we need
     * to account for that.  The function is the same no matter where it is called.
     * @param point point to use as the center
     * @param angle angle to rotate
     */
    private void rotateMeshAroundPoint(BBDPoint point, float angle){
        Matrix4f test = new Matrix4f();
        Vector3f centerOfRotation = new Vector3f(point.getXLoc(), point.getYLoc(), 0);
        test.translate(centerOfRotation)
                .rotate(angle, 0, 0, 1)
                .translate(centerOfRotation.negate())
                .transformPosition(this.getPosition());
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