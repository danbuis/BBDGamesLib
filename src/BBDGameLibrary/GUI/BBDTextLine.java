package BBDGameLibrary.GUI;

import BBDGameLibrary.GameEngine.Camera;
import BBDGameLibrary.GameEngine.GameItem;
import BBDGameLibrary.Geometry2d.BBDPoint;
import BBDGameLibrary.OpenGL.*;
import BBDGameLibrary.Utils.ShaderPrograms;
import org.joml.Vector3f;

import java.util.ArrayList;

public class BBDTextLine {
    /**
     * Class to render a single line of text to the screen.  It works in conjunction with a BBDFont object to draw
     * a string in a given font and size.
     */

    private BBDFont font;
    private String text;
    private int layer;
    private ArrayList<GameItem> textMeshes;
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
    private ArrayList<GameItem> createText(String newText){
        ArrayList<GameItem> returnList = new ArrayList<>();

        int baseFontHeight = Integer.parseInt(font.fontDataTable.get("Cell Height"));
        float fontRatio = textSize / baseFontHeight;

        float totalRunningWidth = 0;
        char[] charArray = newText.toCharArray();
        for(char character : charArray){
            int charCode = character;
            Mesh nextChar = font.charMap.get(charCode);
            GameItem nextCharGameItem = new GameItem(nextChar, ShaderPrograms.TEXTURED_GENERIC);
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
        renderer.renderList(window, textMeshes, camera);
    }

    /**
     * Change the text being rendered
     * @param newText new string to be rendered
     */
    public void changeText(String newText){
        Vector3f origin = getOrigin();
        this.textMeshes = createText(newText);
        setPosition(origin.x, origin.y, origin.z);
    }

    /**
     * Translate the string a given amount
     * @param x
     * @param y
     * @param z
     */
    public void translate(float x, float y, float z){
        for (GameItem item : textMeshes) {
            Vector3f currentPosition = item.getPosition();
            item.setPosition(currentPosition.x + x, currentPosition.y + y, currentPosition.z + z);
        }
    }

    /**
     * Move the string to a given location
     * @param x
     * @param y
     * @param z
     */
    public void setPosition(float x, float y, float z){
        Vector3f currentPosition = getOrigin();
        translate(x - currentPosition.x, y - currentPosition.y, z - currentPosition.z);
    }

    /**
     * Move the string to a given location in the current X/Y plane
     * @param x
     * @param y
     */
    public void setPosition(float x, float y){
        Vector3f currentPosition = getOrigin();
        translate(x - currentPosition.x, y - currentPosition.y, 0);
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
