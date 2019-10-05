import javafx.scene.paint.Color;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Random;

public class Util {

    public static Card.COLOR getRandomColor() {
        int randomInt = getRandomNumberInRange(3);
        Card.COLOR color = Card.COLOR.SPADES;
        switch (randomInt) {
            case 0:
                color = Card.COLOR.SPADES;
                break;

            case 1:
                color = Card.COLOR.HEARTS;
                break;

            case 2:
                color = Card.COLOR.CLUBS;
                break;

            case 3:
                color = Card.COLOR.DIAMONDS;
                break;
        }
        return color;
    }

    public static int getRandomNumberInRange(int max) {
        Random r = new Random();
        return r.nextInt((max + 1));
    }

    public static String getHexRepresentation(Color color){
        int r = (int) (color.getRed()*255);
        int g = (int) (color.getGreen()*255);
        int b = (int) (color.getBlue()*255);
        return String.format("#%02x%02x%02x", r, g, b);
    }


    // get file from classpath, resources folder
    public static FileInputStream getFileInputStreamFromResources(Class clazz, String fileName) {
        ClassLoader classLoader = clazz.getClassLoader();
        URL resource = classLoader.getResource(fileName);
        try {
            return new FileInputStream(resource.getPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
