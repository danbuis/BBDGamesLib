package BBDGameLibrary.GameEngine;

import BBDGameLibrary.OpenGL.Window;

/**
 * The core class to run a game.  Handles things like the game loop, FPS etc.  The main() method of your project
 * will create a new Engine object and give it a GameComponent to hold the game and subsequent logic.  See the SimpleGame
 * package for an implementation.
 */
public class Engine implements Runnable {

    /**
     * How many frames per second will be drawn
     */
    public static int TARGET_FPS = 75;

    /**
     * How many updates per second will be performed
     */
    public static int TARGET_UPS = 30;

    /**
     * Window to draw GameComponents to
     */
    private final Window window;

    /**
     * Timer object to handle update cycles
     */
    private final Timer timer;

    /**
     * Core item to serve as an entry point into our game.
     */
    private final GameComponent gameLogic;

    private final MouseInput mouseInput;

    private double lastFps;

    private int fps;

    private String windowTitle;

    /**
     * General purpose constructor
     * @param windowTitle title of the window
     * @param width width of the window
     * @param height height of the window
     * @param vSync enable vSync?
     * @param gameLogic GameComponent item to be rendered by the engine
     * @throws Exception might throw an exception
     */
    public Engine(String windowTitle, int width, int height, boolean vSync, Window.WindowOptions opts, GameComponent gameLogic) throws Exception {
        this.windowTitle = windowTitle;
        window = new Window(windowTitle, width, height, vSync, opts);
        mouseInput = new MouseInput();
        this.gameLogic = gameLogic;
        timer = new Timer();
    }

    /**
     * Needs to be included due to the runnable interface.  Used to kick off the game loop.
     */
    @Override
    public void run() {
        try {
            init();
            gameLoop();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cleanup();
        }
    }

 /**
     * Actions to be performed when initializing the Engine
     */
    protected void init() {
        window.init();
        timer.init();
        mouseInput.init(window);
        gameLogic.init(window);
        lastFps = timer.getTime();
        fps = 0;
    }

    /**
     * Steps to perform during the game loop.  handles update times and the render/update steps.
     */
    protected void gameLoop() {
        float elapsedTime;
        float accumulator = 0f;
        float interval = 1f / TARGET_UPS;

        boolean running = true;
        while (running && !window.windowShouldClose()) {
            elapsedTime = timer.getElapsedTime();
            accumulator += elapsedTime;

            input();

            while (accumulator >= interval) {
                update(interval);
                accumulator -= interval;
            }

            render();

            if (!window.isvSync()) {
                sync();
            }
        }
    }

    /**
     * Actions to perform when cleaning up this object.  In this case it ensures that the gameLogic item's cleanup
     * function is called.
     */
    protected void cleanup() {
        gameLogic.cleanup();
    }

    /**
     * Used by the game loop if vSync is not enabled
     */
    private void sync() {
        float loopSlot = 1f / TARGET_FPS;
        double endTime = timer.getLastLoopTime() + loopSlot;
        while (timer.getTime() < endTime) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    }

    /**
     * Ensures that the gameLogic item processes the input from the window
     */
    protected void input() {
        mouseInput.input(window);
        gameLogic.input(window, mouseInput);
    }

    /**
     * Ensures that the gameLogic item performs an update
     * @param interval interval since last update
     */
    protected void update(float interval) {
        gameLogic.update(interval, mouseInput, window);
    }

    /**
     * Ensures that the gameLogic item performs a render
     */
    protected void render() {
        if ( window.getWindowOptions().showFps && timer.getLastLoopTime() - lastFps > 1 ) {
            lastFps = timer.getLastLoopTime();
            window.setWindowTitle(windowTitle + " - " + fps + " FPS");
            fps = 0;
        }
        fps++;
        gameLogic.render(window);
        window.update();
    }

    public int getTargetUps(){
        return this.TARGET_UPS;
    }

    public void setTargetUps(int newTarget){
        this.TARGET_UPS = newTarget;
    }

    public int getTargetFps(){
        return this.TARGET_FPS;
    }

    public void setTargetFps(int newTarget){
        this.TARGET_FPS = newTarget;
    }
}