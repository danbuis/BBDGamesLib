package TestsGUI;

import BBDGameLibrary.GUI.BBDFont;
import BBDGameLibrary.GUI.BBDTextLine;
import BBDGameLibrary.OpenGL.Window;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import static org.junit.jupiter.api.Assertions.*;


public class TestBBDTextLine {

    Window window = null;
    boolean windowInit = false;
    public void initWindow(){
        window = new Window("test", 5, 5, true, new Window.WindowOptions());
        window.init();
        windowInit = true;
        try{
            testFont = new BBDFont("resources/text/Arial_Bold_White.bmp", "resources/text/Arial_Bold_White.csv");
        }catch(FileNotFoundException e){
            System.out.println(e);
        }
    }

    BBDFont testFont = null;

    @Test
    public void testTextString(){
        if(!windowInit){
            initWindow();
        }
        BBDTextLine testText = new BBDTextLine(testFont, 1, "TEST", 1);
        assertEquals("TEST", testText.getText());

        testText.changeText("NEW TEST");
        assertEquals("NEW TEST", testText.getText());
    }

    @Test
    public void testGetOrigin(){
        if(!windowInit){
            initWindow();
        }
        BBDTextLine testText = new BBDTextLine(testFont, 1, "TEST", 1);
        assertEquals(new Vector3f(-12.74f,-24.5f,-0.001f), testText.getOrigin());
    }

    @Test
    public void testTranslate(){
        if(!windowInit){
            initWindow();
        }
        BBDTextLine testText = new BBDTextLine(testFont, 1, "TEST", 1);
        Vector3f startingOrigin = testText.getOrigin();
        testText.translate(12.74f,24.50f);
        assertEquals(new Vector3f(0,0,-0.001f), testText.getOrigin());
    }

    @Test
    public void testSetPosition(){
        if(!windowInit){
            initWindow();
        }
        BBDTextLine testText = new BBDTextLine(testFont, 1, "TEST", 1);
        testText.setPosition(0,0);
        assertEquals(new Vector3f(0,0,-0.001f), testText.getOrigin());

        testText.setPosition(1,1);
        assertEquals(new Vector2f(1,1), testText.getOrigin());

        testText.changeText("weasels");
        assertEquals(new Vector2f(1,1), testText.getOrigin());
    }

    @Test
    public void voidTestStringCorrelatesToMeshes(){
        if(!windowInit){
            initWindow();
        }
        BBDTextLine testText = new BBDTextLine(testFont, 1, "TEST", 1);
        assertEquals(4, testText.getMeshCount());

        testText.changeText("Guacamole");
        assertEquals(9, testText.getMeshCount());

    }
}
