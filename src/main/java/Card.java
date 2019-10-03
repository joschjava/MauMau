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
            case CARO:
                value = 1;
                break;
            case HERZ:
                value = 2;
                break;
            case PIK:
                value = 3;
                break;
            case KREUZ:
                value = 4;
                break;
        }
        return value;
    }

    public enum COLOR {PIK, CARO, HERZ, KREUZ}

    public final static int JACK = 11;
    public final static int QUEEN = 12;
    public final static int KING = 13;
    public final static int ACE = 14;

    private COLOR color;
    private int value;

    boolean canBeLayedOn(Card card) {
        if (value == Card.JACK || card == null) {
            return true;
        }
        if (card.getColor() != this.color && card.getValue() != this.value) {
            return false;
        }
        return true;
    }

    //TODO: compare

    /**
     * @deprecated Not yet implemented
     */
    public void compare(Card card) {
        System.out.println("Not implemented yet: Compare");
    }

    public String toPrettyString() {
        String output = "";
        switch (color) {
            case PIK:
                output += "\u2660";
                break;
            case CARO:
                output += "\u2666";
                break;
            case HERZ:
                output += "\u2665";
                break;
            case KREUZ:
                output += "\u2663";
                break;
        }

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
