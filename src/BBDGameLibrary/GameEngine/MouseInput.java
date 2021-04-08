package BBDGameLibrary.GameEngine;

import BBDGameLibrary.OpenGL.Window;
import org.joml.Vector2d;
import org.joml.Vector2f;

import java.util.Vector;

import static org.lwjgl.glfw.GLFW.*;

public class MouseInput {

    private final Vector2d previousPos;

    private final Vector2d currentPos;

    private final Vector2f displVec;

    private boolean inWindow = false;

    private boolean leftButtonPressed = false;
    private boolean rightButtonPressed = false;

    private double scrollAmount = 0;

    public MouseInput() {
        previousPos = new Vector2d(-1, -1);
        currentPos = new Vector2d(0, 0);
        displVec = new Vector2f();
    }

    public void init(Window window) {
        glfwSetCursorPosCallback(window.getWindowHandle(), (windowHandle, xpos, ypos) -> {
            currentPos.x = xpos;
            currentPos.y = ypos;
        });
        glfwSetCursorEnterCallback(window.getWindowHandle(), (windowHandle, entered) -> {
            inWindow = entered;
        });
        glfwSetMouseButtonCallback(window.getWindowHandle(), (windowHandle, button, action, mode) -> {
            leftButtonPressed = ((button == GLFW_MOUSE_BUTTON_1) && (action == GLFW_PRESS));
            rightButtonPressed = ((button == GLFW_MOUSE_BUTTON_2) && (action == GLFW_PRESS));
        });
        glfwSetScrollCallback(window.getWindowHandle(), (windowHandle, xOffset, yOffset) ->{
            scrollAmount = yOffset;
        });
    }

    public Vector2f getDisplVec() {
        return displVec;
    }

    /**
     * Get the location of the mouse.  Returns the location right now as a new object, not a reference to the class variable.
     * @return Current location of the mouse
     */
    public Vector2d getCurrentPos() {
        return new Vector2d(currentPos.x, currentPos.y);
    }

    /**
     * Get a reference to the current position.  This one will update in real time.
     * @return Current location of the mouse
     */
    public Vector2d getCurrentPosReference(){
        return currentPos;
    }

    public void input(Window window) {
        displVec.x = 0;
        displVec.y = 0;
        if (previousPos.x > 0 && previousPos.y > 0 && inWindow) {
            double deltax = currentPos.x - previousPos.x;
            double deltay = currentPos.y - previousPos.y;
            boolean rotateX = deltax != 0;
            boolean rotateY = deltay != 0;
            if (rotateX) {
                displVec.y = (float) deltax;
            }
            if (rotateY) {
                displVec.x = (float) deltay;
            }
        }
        previousPos.x = currentPos.x;
        previousPos.y = currentPos.y;
    }

    /**
     * Current setup captures the latest input, button presses change on a per frame basis, but scroll wheel does not,
     * so we need to clear it to 0 regularly so that it doesn't get "stuck"
     */
    public void clearScrollInput(){
        this.scrollAmount = 0;
    }

    public double getScrollAmount() {
        return scrollAmount;
    }

    public boolean isLeftButtonPressed() {
        return leftButtonPressed;
    }

    public boolean isRightButtonPressed() {
        return rightButtonPressed;
    }

}

