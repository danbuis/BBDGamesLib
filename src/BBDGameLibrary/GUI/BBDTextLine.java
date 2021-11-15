package BBDGameLibrary.GUI;

import BBDGameLibrary.GameEngine.Camera;
import BBDGameLibrary.GameEngine.GameItem;
import BBDGameLibrary.GameEngine.GameItem2d;
import BBDGameLibrary.OpenGL.Mesh;
import BBDGameLibrary.OpenGL.Renderer;
import BBDGameLibrary.OpenGL.Utils;
import BBDGameLibrary.OpenGL.Window;
import BBDGameLibrary.Utils.ShaderPrograms;

import java.util.ArrayList;

public class BBDTextLine {

    private BBDFont font;
    private String text;
    private ArrayList<GameItem> textMeshes;
    private float totalWidth;
    private float textSize;

    public BBDTextLine (BBDFont font, float textSize, String text){
        this.font = font;
        this.text = text;
        this.textSize = textSize;
        textMeshes = this.setText(text);
    }

    private ArrayList<GameItem> setText(String newText){
        ArrayList<GameItem> returnList = new ArrayList<>();

        int baseFontHeight = Integer.parseInt(font.fontDataTable.get("Cell Height"));
        float fontRatio = textSize / baseFontHeight;

        int totalRunningWidth = 0;
        char[] charArray = newText.toCharArray();
        for(char character : charArray){
            int charCode = character;
            Mesh nextChar = font.charMap.get(charCode);
            GameItem nextCharGameItem = new GameItem(nextChar, ShaderPrograms.buildBasicTexturedShaderProgram());
            float height = baseFontHeight * fontRatio;
            float width = Integer.parseInt(font.fontDataTable.get("Char " + charCode + " Base Width"));
            nextCharGameItem.setPosition(totalRunningWidth + 0.5f * width, 0.5f * height, 0);
            totalRunningWidth += width;
            this.textMeshes.add(nextCharGameItem);
        }
        return returnList;
    }

    public void renderTextLine(Window window, Renderer renderer, Camera camera){
        renderer.renderList(window, textMeshes, camera);
    }

    public void changeText(String newText){
        this.setText(newText);
    }
}
