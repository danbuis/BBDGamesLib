package BBDGameLibrary.GameEngine;

/** Simple class to handle some time based logic so that the game engine
 * can smoothly handle different update speeds.
 */
public class Timer {

    /**
     * Timestamp of the last update
     */
    private double lastLoopTime;

    /**
     * Initialize the timer
     */
    public void init() {
        lastLoopTime = getTime();
    }

    /**
     * Get the current time
     * @return current time
     */
    public double getTime() {
        return System.nanoTime() / 1000000000.0;
    }

    /**
     * determine the elapsed time based on `lastLoopTime`
     * @return
     */
    public float getElapsedTime() {
        double time = getTime();
        float elapsedTime = (float) (time - lastLoopTime);
        lastLoopTime = time;
        return elapsedTime;
    }

    /**
     * get the timestamp stored
     * @return lastLoopTime stored
     */
    public double getLastLoopTime() {
        return lastLoopTime;
    }
}