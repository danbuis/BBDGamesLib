package BBDGameLibrary.GUI;

import BBDGameLibrary.Geometry2d.BBDPolygon;
import BBDGameLibrary.OpenGL.Mesh;
import BBDGameLibrary.OpenGL.Texture;
import BBDGameLibrary.Utils.GeometryGenerators;
import org.joml.Vector2i;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class BBDFont {

    HashMap<String, String> fontDataTable;
    HashMap<Integer, Mesh> charMap;

    public BBDFont (String imageFile, String fontData) throws FileNotFoundException {
        //Bring in font data
        fontDataTable = new HashMap<>();
        Scanner fileScanner = new Scanner(new File(fontData));
        String nextLine;
        while(fileScanner.hasNext()){
            nextLine = fileScanner.nextLine();
            String[] parts = nextLine.split("\\,");
            String key = parts[0];
            String value = parts[1];
            fontDataTable.put(key, value);
        }

        //bring in image file
        Texture fontBitmap = new Texture(imageFile);

        //create final hash maps per each charcode so that we can rapidly create text
        populateCharMaps(fontBitmap);
    }

    private void populateCharMaps(Texture fontBitmap){
        int startIndex = Integer.parseInt(fontDataTable.get("Start Char"));
        int cellWidth = Integer.parseInt(fontDataTable.get("Cell Width"));
        int cellHeight = Integer.parseInt(fontDataTable.get("Cell Height"));
        int imageWidth = Integer.parseInt(fontDataTable.get("Image Width"));
        int imageHeight = Integer.parseInt(fontDataTable.get("Image Height"));
        for (int charCode = 32; charCode<120; charCode++) {
            int charCodeDelta = charCode - startIndex;
            int col = charCodeDelta % ((int) Math.floor(imageWidth / cellWidth));
            int row = charCodeDelta / (int) Math.floor(imageWidth / cellWidth);

            Vector2i charOrigins = new Vector2i(col * cellWidth, row * cellHeight);
            Vector2i charOpposite = new Vector2i(charOrigins.x + Integer.parseInt(fontDataTable.get("Char " + charCode + " Base Width")), charOrigins.y + cellHeight);

            BBDPolygon poly = GeometryGenerators.buildQuad(charOpposite.x - charOrigins.x, charOpposite.y - charOrigins.y);
            float[] positions = Mesh.buildMeshPositions(poly);
            int[] indices = Mesh.buildIndices(poly);
            float[] textureCoords = new float[8];
            textureCoords[0] = charOrigins.x / imageWidth;
            textureCoords[1] = charOrigins.y / imageHeight;
            textureCoords[2] = charOpposite.x / imageWidth;
            textureCoords[3] = charOrigins.y / imageHeight;
            textureCoords[4] = charOpposite.x / imageWidth;
            textureCoords[5] = charOpposite.y / imageHeight;
            textureCoords[6] = charOrigins.x / imageWidth;
            textureCoords[7] = charOpposite.y / imageHeight;

            Mesh charMesh = new Mesh(positions, textureCoords, indices, fontBitmap);
            charMap.put(charCode, charMesh);
        }
    }
}
