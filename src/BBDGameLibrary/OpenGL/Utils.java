package BBDGameLibrary.OpenGL;

import java.io.InputStream;
import java.util.Scanner;

/**
 * A class for openGL related niceties.
 */
public class Utils {

    /**
     * Load a script from the given location
     * @param fileName relative filepath
     * @return string of the contents of the file.
     */
    public static String loadShaderScript(String fileName) {
        String result = null;
        InputStream in = Utils.class.getResourceAsStream(fileName);
        try (Scanner scanner = new Scanner(in, java.nio.charset.StandardCharsets.UTF_8.name())) {
            result = scanner.useDelimiter("\\A").next();
        } catch (Exception e){
            System.out.println(fileName);
            e.printStackTrace();
        }
        return result;
    }

}
