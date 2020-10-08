package OpenGL;

import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.*;

/**
 * Class to handle a specific texture.  This is basically a way to access the texture with a few
 * niceties tacked on for ease of use.
 */
public class Texture {

    /**
     * Memory address for the texture
     */
    private final int id;

    /**
     * Constructor for making a Texture object
     * @param fileName relative file path
     * @throws Exception reason that the constructor fails
     */
    public Texture(String fileName) throws Exception {
        this(loadTexture(fileName));
    }

    /**
     * Constructor to make a constructor based on a location in memory.
     * @param id designated id
     */
    public Texture(int id) {
        this.id = id;
    }

    /**
     * Bind this texture to what is currently being drawn.  Failure to bind a texture means that
     * the most recent texture will be used instead.
     */
    public void bind() {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public int getId() {
        return id;
    }

    /**
     * Load an image file and make a texture object that handles a lot of boilerplace automatically.
     * @param fileName relative file path
     * @return address location of the Texture
     * @throws Exception throw an exceoption if loading the image file doesn't complete correctly
     */
    private static int loadTexture(String fileName) throws Exception {
        int width;
        int height;
        ByteBuffer buf;
        // Load Texture file
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            buf = stbi_load(fileName, w, h, channels, 4);
            if (buf == null) {
                throw new Exception("Image file [" + fileName  + "] not loaded: " + stbi_failure_reason());
            }

            /* Get width and height of image */
            width = w.get();
            height = h.get();
        }

        // Create a new OpenGL texture
        int textureId = glGenTextures();
        // Bind the texture
        glBindTexture(GL_TEXTURE_2D, textureId);

        // Tell OpenGL how to unpack the RGBA bytes. Each component is 1 byte size
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

        //glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        //glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        // Upload the texture data
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0,
                GL_RGBA, GL_UNSIGNED_BYTE, buf);
        // Generate Mip Map
        glGenerateMipmap(GL_TEXTURE_2D);

        stbi_image_free(buf);

        return textureId;
    }

    public void cleanup() {
        glDeleteTextures(id);
    }
}
