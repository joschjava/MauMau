import javafx.scene.paint.Color;

import java.util.Random;

public class Util {

    public static Card.COLOR getRandomColor() {
        int randomInt = getRandomNumberInRange(3);
        Card.COLOR color = Card.COLOR.PIK;
        switch (randomInt) {
            case 0:
                color = Card.COLOR.PIK;
                break;

            case 1:
                color = Card.COLOR.HERZ;
                break;

            case 2:
                color = Card.COLOR.KREUZ;
                break;

            case 3:
                color = Card.COLOR.CARO;
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

}
