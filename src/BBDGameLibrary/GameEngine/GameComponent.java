package GameEngine;

import OpenGL.Window;

/**
 * Interface defining what a GameComponent is expected to do
 */
public interface GameComponent {

	/**
     * General function to be ran when an object is initialized
     * @param window Window object that will be displaying the GameComponent
     */
    void init(Window window);
    

    /**
     * Handle keyboard or mouse input
     * @param window the Window object this GameComponent is using.
     */
    void input(Window window);

    /**
     * Update the GameComponent
     * @param interval elapsed time
     */
    void update(float interval);

    /**
     * Render the GameComponent
     * @param window the Window object this GameComponent is using.
     */
    void render(Window window);

    /**
     * Cleanup the GameComponent
     */
    void cleanup();
}