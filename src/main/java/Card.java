import lombok.Data;

@Data
public class Card {

    public Card(Card.COLOR color, int value) {
        this.color = color;
        if(!(7 <= value && value <= 14)){
            throw new RuntimeException("Invalid card value: "+value+ ", "+toString());
        }
        this.value = value;
    }

    public enum COLOR { PIK, CARO, HERZ, KREUZ }
    public static int JACK = 11;
    public static int QUEEN = 12;
    public static int KING = 13;
    public static int ACE = 14;

    private COLOR color;
    private int value;

    boolean canBeLayedOn(Card card){
        if(value == Card.JACK  || card == null){
            return true;
        }
        if(card.getColor() != this.color && card.getValue() != this.value ){
            return false;
        }
        return true;
    }

    //TODO: compare
    /**
     * @deprecated Not yet implemented
     */
    public void compare(Card card){
        System.out.println("Not implemented yet: Compare");
    }
}
