package GameEngine;

import openGL.Window;

public interface GameComponent {

    void init(Window window) throws Exception;

    void input(Window window);

    void update(float interval);

    void render(Window window);

    void cleanup();
}