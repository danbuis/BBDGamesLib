package GameEngine;

import java.io.InputStream;
import java.util.Scanner;

public class Utils {

    public static String loadResource(String fileName) {
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
