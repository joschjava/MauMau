import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class RandomAI extends AI {

    public RandomAI(Game game) {
        super(game);
    }

    @Override
    Card makeMove(List<Card> handCards, Card stapelCard) {
        List<Card> possiblePlayableCards = super.getPossiblePlayableCards(handCards)
                .stream()
                .filter(
                        card -> card.getValue() != Card.JACK
                )
                .collect(Collectors.toList());

        int size = possiblePlayableCards.size();
        if (size == 0) {
            System.out.println("I can't make any moves! Human expect too much from me...");
            return null;
//            throw new RuntimeException("I can't make any moves! Human expect too much from me...");
        }
        int playCardId = getRandomNumberInRange(size - 1);
        Card card = possiblePlayableCards.get(playCardId);
        System.out.println("I'm playing " + card.toString());
        return card;
    }

    public static int getRandomNumberInRange(int max) {
        Random r = new Random();
        return r.nextInt((max + 1));
    }

}
