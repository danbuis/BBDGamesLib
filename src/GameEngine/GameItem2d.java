package GameEngine;

import Geometry2d.BBDPoint;
import Geometry2d.BBDPolygon;
import OpenGL.Mesh;
import OpenGL.ShaderProgram;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class GameItem2d extends GameItem{

    private final Mesh mesh;

    private final Vector3f position;

    private float scale;

    private final Vector3f rotation;
    public final ShaderProgram shader;
    private int layer;
    private final float LAYER_INTERVAL = (float)0.001;
    private BBDPolygon shape;
    private boolean shapeInteracts;

    public GameItem2d(Mesh mesh, ShaderProgram shaderProgram, BBDPolygon shape,  int layer, boolean shapeInteracts) {
        super(mesh, shaderProgram);
        this.mesh = mesh;
        this.shader = shaderProgram;
        this.layer = layer;
        this.shape = shape;
        this.shapeInteracts = shapeInteracts;
        BBDPoint center = shape.center();
        position = new Vector3f((float)center.getXLoc(), (float)center.getYLoc(), layer*LAYER_INTERVAL);
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
            this.translate((float)currentCenter.getXLoc() - x, (float)currentCenter.getYLoc() - y);
        }
    }

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

    public void scale(float scaleFactor){
        if(shapeInteracts){
            this.shape.scale(scaleFactor);
        }

        this.scale = scale * scaleFactor;
    }

    public void scaleFromPoint(BBDPoint point, float scaleFactor){
        if(shapeInteracts){
            this.shape.scaleFromPoint(point, scaleFactor);
        }
        // translate
        float deltaX = this.position.x - (float)point.getXLoc();
        float deltaY = this.position.y - (float)point.getYLoc();
        this.position.x = this.position.x + deltaX * scaleFactor;
        this.position.y = this.position.y + deltaY * scaleFactor;
        // scale
        this.scale = scaleFactor;
    }

    public Vector3f getRotation() {
        return rotation;
    }
    //vector3f.rotateAxis is like our rotate around point

    public void setRotation(float z) {
        if (shapeInteracts){
            float currentRotation = this.rotation.z;
            this.shape.rotate(currentRotation - z);
        }

        this.rotation.z = z;
    }

    public void rotate(float angle){
        this.rotation.rotateZ(angle);

        if (shapeInteracts){
            this.shape.rotate(angle);
        }
    }

    public void rotateAroundPoint(BBDPoint point, float angle){
        this.rotation.rotateAxis(angle, (float)point.getXLoc(), (float)point.getYLoc(), 0);

        if(shapeInteracts){
            this.shape.rotateAroundPoint(point, angle);
        }
    }

    public Mesh getMesh() {
        return mesh;
    }

    public void setUniforms(Matrix4f projectionMatrix, Matrix4f worldMatrix) {
        this.shader.setUniform("projectionMatrix", projectionMatrix);
        this.shader.setUniform("worldMatrix", worldMatrix);
    }
}