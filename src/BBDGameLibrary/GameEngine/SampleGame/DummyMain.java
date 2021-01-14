package BBDGameLibrary.GameEngine.SampleGame;

import BBDGameLibrary.GameEngine.Engine;
import BBDGameLibrary.GameEngine.GameComponent;
import BBDGameLibrary.OpenGL.Window;

/**
 * A sample entry point for a game using the BBDGameLibrary.GameEngine and BBDGameLibrary.OpenGL packages
 */
public class DummyMain {

    public static void main(String[] args) {
        try {
            GameComponent gameLogic = new DummyGame();
            Engine gameEng = new Engine("GAME",
                    900, 480, true, new Window.WindowOptions(), gameLogic);
            gameEng.run();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

}
