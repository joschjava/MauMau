import lombok.Data;

@Data
public class Card implements Comparable<Card> {

    public Card(Card.COLOR color, int value) {
        this.color = color;
        if (!(7 <= value && value <= 14)) {
            throw new RuntimeException("Invalid card value: " + value + ", " + toString());
        }
        this.value = value;
    }

    @Override
    public int compareTo(Card card) {
        if (card.getValue() > value) {
            return -1;
        } else if (card.getValue() == value) {
            if (colorToValue(color) > colorToValue(card.getColor())) {
                return 1;
            } else {
                return -1;
            }
        } else {
            return 1;
        }
    }

    private int colorToValue(COLOR color) {
        int value = -1;
        switch (color) {
            case DIAMONDS:
                value = 1;
                break;
            case HEARTS:
                value = 2;
                break;
            case SPADES:
                value = 3;
                break;
            case CLUBS:
                value = 4;
                break;
        }
        return value;
    }

    public enum COLOR {SPADES, DIAMONDS, HEARTS, CLUBS}

    public final static int JACK = 11;
    public final static int QUEEN = 12;
    public final static int KING = 13;
    public final static int ACE = 14;

    private COLOR color;
    private int value;

    boolean canBeLayedOn(Card card) {
       return canBeLayedOn(card, false);
    }

    boolean canBeLayedOn(Card card, boolean debug) {
        if (value == Card.JACK || card == null) {
            return true;
        }
        if (card.getColor() != this.color && card.getValue() != this.value) {
            if(debug) {
                System.out.println(this + "can't be layed on" + card);
                System.out.println("Color: " + color + ", " + card.getColor());
                System.out.println("Value: " + value + ", " + card.getValue());
            }
            return false;
        }
        return true;
    }

    @Override
    public String toString(){
        return "Card["+color+"_"+valueToName(value)+"]";
    }

    public String toPrettyString() {
        String output = colorToAsciiSymbol(color);
        output += valueToName(value);
        return output;
    }

    public static String colorToAsciiSymbol(COLOR color) {
        String output = "";
        switch (color) {
            case SPADES:
                output += "\u2660";
                break;
            case DIAMONDS:
                output += "\u2666";
                break;
            case HEARTS:
                output += "\u2665";
                break;
            case CLUBS:
                output += "\u2663";
                break;
        }
        return output;
    }

    public static String valueToName(int value) {
        String output = "";
        if (value < 11) {
            output += String.valueOf(value);
        } else {
            switch (value) {
                case JACK:
                    output += "J";
                    break;

                case QUEEN:
                    output += "Q";
                    break;

                case KING:
                    output += "K";
                    break;

                case ACE:
                    output += "A";
                    break;
            }
        }
        return output;
    }
}
