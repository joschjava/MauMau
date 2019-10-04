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

}
