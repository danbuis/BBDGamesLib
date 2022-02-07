package BBDGameLibrary.GUI;

import BBDGameLibrary.GameEngine.Camera;
import BBDGameLibrary.GameEngine.GameItem;
import BBDGameLibrary.GameEngine.GameItem2d;
import BBDGameLibrary.Geometry2d.BBDPoint;
import BBDGameLibrary.Geometry2d.BBDPolygon;
import BBDGameLibrary.OpenGL.*;
import BBDGameLibrary.Utils.ShaderPrograms;
import org.joml.Vector3f;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class BBDTextLine {
    /**
     * Class to render a single line of text to the screen.  It works in conjunction with a BBDFont object to draw
     * a string in a given font and size.
     */

    private BBDFont font;
    private String text;
    private int layer;
    private ArrayList<GameItem2d> textMeshes;
    private float totalWidth;
    private float textSize;
    private BBDPoint origin = new BBDPoint(0,0);

    /**
     * Standard constructor for a line of text
     * @param font What BBDFont to use for this text
     * @param textSize How many world units tall is this text
     * @param text What string is to be rendered
     * @param layer how far on the z axis to render, similar to the layer paramater of GameItem2d objects
     */
    public BBDTextLine (BBDFont font, float textSize, String text, int layer){
        this.font = font;
        this.textSize = textSize;
        this.layer = layer;
        textMeshes = this.createText(text);
    }

    /**
     * Set the text of this line of text
     * @param newText new string to be rendered
     * @return a list of GameItems to be rendered to the screen
     */
    private ArrayList<GameItem2d> createText(String newText){
        ArrayList<GameItem2d> returnList = new ArrayList<>();

        int baseFontHeight = Integer.parseInt(font.fontDataTable.get("Cell Height"));
        float fontRatio = textSize / baseFontHeight;

        float totalRunningWidth = 0;
        char[] charArray = newText.toCharArray();
        for(char character : charArray){
            int charCode = character;
            Mesh nextChar = font.charMap.get(charCode);
            BBDPolygon nextPoly = font.polyMap.get(charCode).copyPolygon();
            GameItem2d nextCharGameItem = new GameItem2d(nextChar, ShaderPrograms.TEXTURED_GENERIC, nextPoly, layer, true);
            nextCharGameItem.setScale(fontRatio);
            float height = baseFontHeight * fontRatio;
            float width = Integer.parseInt(font.fontDataTable.get("Char " + charCode + " Base Width")) * fontRatio;
            nextCharGameItem.setPosition(totalRunningWidth + 0.5f * width, 0.5f * height, -0.001f * layer);
            totalRunningWidth += width;
            returnList.add(nextCharGameItem);
        }

        totalWidth = totalRunningWidth;
        text = newText;
        return returnList;
    }

    /**
     * Helper method for rendering so that the user does not need to access the list of char meshes
     * @param window screen window
     * @param renderer Renderer object that is handling rendering
     * @param camera camera that is currently looking in the world
     */
    public void renderTextLine(Window window, Renderer renderer, Camera camera){
        for (GameItem2d item: textMeshes){
            renderer.renderItem(window, item, camera);
        }
    }
    
    public ArrayList<GameItem2d> getTextItemList(){
        return this.textMeshes;
    }

    /**
     * Change the text being rendered
     * @param newText new string to be rendered
     */
    public void changeText(String newText){
        Vector3f origin = getOrigin();
        this.textMeshes = createText(newText);
        setPosition(origin.x, origin.y);
    }

    /**
     * Translate the string a given amount
     * @param x
     * @param y
     */
    public void translate(float x, float y){
        for (GameItem2d item : textMeshes) {
            item.translate(x, y);
        }
    }

    /**
     * Move the string to a given location in the current X/Y plane
     * @param x
     * @param y
     */
    public void setPosition(float x, float y){
        Vector3f currentPosition = getOrigin();
        translate(x - currentPosition.x, y - currentPosition.y);
    }

    /**
     * Get the lower-left corner of the text.  Useful for ensuring that text lines up with other elements
     * @return origin of string
     */
    public Vector3f getOrigin(){
        return textMeshes.get(0).getMeshVerticesRealLocations()[2];
    }

    /**
     * Get width of string.  Useful for ensuring that text lines up with other elements
     * @return width of string
     */
    public float getTotalWidth(){ return this.totalWidth; }

    /**
     * Get height of string.  Useful for ensuring that text lines up with other elements
     * @return height of string
     */
    public float getHeight(){ return this.textSize; }

    public int getMeshCount(){ return this.textMeshes.size(); }

    public String getText(){
        return this.text;
    }
}
