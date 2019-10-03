import java.util.List;
import java.util.Random;

public class RandomAI extends AI {

    public RandomAI(Game game) {
        super(game);
    }

    @Override
    CardAction makeMove(List<Card> handCards, Card stapelCard) {
        List<Card> possiblePlayableCards = super.getPossiblePlayableCards(handCards);

        int size = possiblePlayableCards.size();
        if (size == 0) {
            System.out.println("I can't make any moves! Human expect too much from me...");
            return null;
//            throw new RuntimeException("I can't make any moves! Human expect too much from me...");
        }
        int playCardId = getRandomNumberInRange(size - 1);
        Card card = possiblePlayableCards.get(playCardId);
        CardAction cardAction = new CardAction(card);
        if(card.getValue() == Card.JACK){
            Card.COLOR randomColor = getRandomColor();
            cardAction.setJackWishColor(randomColor);
        }
//        System.out.println("Player "+game.getCurrentPlayerId() + ": I'm playing " + card.toString());
        return cardAction;
    }

    private static Card.COLOR getRandomColor() {
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
