package BBDGameLibrary.OpenGL;

import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * A class for displaying an openGL window on the screen
 */
public class Window {

    /**
     * Field of View in Radians
     */
    private static float FOV = (float) Math.toRadians(60.0f);

    private static float Z_NEAR = 0.01f;

    private static float Z_FAR = 1000.f;

    /**
     * Title for the bar a t the top of the screen
     */
    private final String title;

    /**
     * dimensions of the window
     */
    private int width;

    private int height;

    /**
     * The address in memory for the window.
     */
    private long windowHandle;

    /**
     * Has the window been resized
     */
    private boolean resized;

    /**
     * is vSync enabled
     */
    private boolean vSync;

    private WindowOptions opts;

    private Matrix4f projectionMatrix;

    /**
     * General constructor
     * @param title title of the window
     * @param width width of the window in pixels
     * @param height height of the window in pixels
     * @param vSync turn on vsync
     * @param opts options for the window
     */
    public Window(String title, int width, int height, boolean vSync, WindowOptions opts) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.vSync = vSync;
        this.resized = false;
        this.opts = opts;
        projectionMatrix = new Matrix4f();
    }

    /**
     * Function to initialize a window.  This will set up some default values and get a lot of
     * boilerplate code taken care of, most of which relates to getting some openGL parameters set up.
     */
    public void init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE); // the window will be resizable
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        if (opts.compatibleProfile) {
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_COMPAT_PROFILE);
        } else {
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
            glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        }

        boolean maximized = false;
        // If no size has been specified set it to maximized state
        if (width == 0 || height == 0) {
            // Set up a fixed width and height so window initialization does not fail
            width = 100;
            height = 100;
            glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
            maximized = true;
        }

        // Create the window
        windowHandle = glfwCreateWindow(width, height, title, NULL, NULL);
        if (windowHandle == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        // Setup resize callback
        glfwSetFramebufferSizeCallback(windowHandle, (window, width, height) -> {
            this.width = width;
            this.height = height;
            this.setResized(true);
        });

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
            }
        });

        if (maximized) {
            glfwMaximizeWindow(windowHandle);
        } else {
            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            // Center our window
            glfwSetWindowPos(
                    windowHandle,
                    (vidmode.width() - width) / 2,
                    (vidmode.height() - height) / 2
            );
        }

        // Make the OpenGL context current
        glfwMakeContextCurrent(windowHandle);

        if (isvSync()) {
            // Enable v-sync
            glfwSwapInterval(1);
        }

        // Make the window visible
        glfwShowWindow(windowHandle);

        GL.createCapabilities();

        // Set the clear color
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glEnable(GL_DEPTH_TEST);
        if (opts.showTriangles) {
            glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        }

        // Support for transparencies
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        if (opts.cullFace) {
            glEnable(GL_CULL_FACE);
            glCullFace(GL_BACK);
        }
    }

    /**
     * Set the color to use to fill in the empty background.  If nothing else is drawn to the screen
     * then this is the color that will show.
     * @param r red component
     * @param g green component
     * @param b blue component
     * @param alpha alpha component
     */
    public void setClearColor(float r, float g, float b, float alpha) {
        glClearColor(r, g, b, alpha);
    }

    /**
     * method to determine if a given key is pressed.
     * @param keyCode what key is being checked
     * @return if the key is pressed
     */
    public boolean isKeyPressed(int keyCode) {
        return glfwGetKey(windowHandle, keyCode) == GLFW_PRESS;
    }

    public long getWindowHandle() {
        return windowHandle;
    }

    public WindowOptions getWindowOptions() {
        return opts;
    }

    public String getWindowTitle() {
        return title;
    }

    public void setWindowTitle(String title) {
        glfwSetWindowTitle(windowHandle, title);
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public Matrix4f updateProjectionMatrix() {
        float aspectRatio = (float)width / (float)height;
        return projectionMatrix.setPerspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
    }

    public boolean windowShouldClose() {
        return glfwWindowShouldClose(windowHandle);
    }

    public String getTitle() {
        return title;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isResized() {
        return resized;
    }

    public void setResized(boolean resized) {
        this.resized = resized;
    }

    public boolean isvSync() {
        return vSync;
    }

    public void setvSync(boolean vSync) {
        this.vSync = vSync;
    }

    public void update() {
        glfwSwapBuffers(windowHandle);
        glfwPollEvents();
    }

    public static class WindowOptions {

        public boolean cullFace;
        public boolean showTriangles;
        public boolean showFps;
        public boolean compatibleProfile;
    }

    /**
     * Get the current field of view
     * @return current FOV
     */
    public float getFOV(){
        return this.FOV;
    }

    /**
     * Set a new field of view
     * @param newFOV
     */
    public void setFOV(float newFOV){
        this.FOV = newFOV;
    }

    /**
     * Get the current near clipping distance
     * @return current Z_NEAR
     */
    public float getZNear(){
        return this.Z_NEAR;
    }

    /**
     * Set the near clipping distance
     * @param newZNear
     */
    public void setZNear(float newZNear){
        this.Z_NEAR = newZNear;
    }

    /**
     * Get the current near clipping distance
     * @return current Z_FAR
     */
    public float getZFar(){
        return this.Z_FAR;
    }

    /**
     * Set the far clipping distance
     * @param newZFar
     */
    public void setZFar(float newZFar){
        this.Z_FAR = newZFar;
    }
}