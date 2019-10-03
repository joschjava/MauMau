import lombok.Data;

@Data
public class CardAction {

    Card card;
    Card.COLOR jackWishColor;

    CardAction(Card card){
        this.card = card;
    }

    CardAction(Card card, Card.COLOR jackWishColor){
        this.jackWishColor = jackWishColor;
    }

    public boolean isJack(){
        return card.getValue() == Card.JACK;
    }

}
