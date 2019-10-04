import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Plays jack only when no other card is possible
 * Chooses jack color by color that is most available on his hand
 */
public class AdvancedRandomAI extends AI {

    public AdvancedRandomAI(Game game) {
        super(game);
        aiName = "Advanced Random AI";
    }

    @Override
    CardAction makeMove(List<Card> handCards) {
        List<Card> possiblePlayableCards = super.getPossiblePlayableCards(handCards);
        int size = possiblePlayableCards.size();
        if (size == 0) {
            System.out.println("I can't make any moves! Human expect too much from me...");
            return null;
        }
        Card playCard = playJackOnlyWhenNecessary(possiblePlayableCards);

        CardAction cardAction = new CardAction(playCard);
        if (playCard.getValue() == Card.JACK) {
            Card.COLOR mostColorOnHand = getMostColorOnHandThatIsNotJack(handCards);
            cardAction.setJackWishColor(mostColorOnHand);
        }
        return cardAction;
    }

    private Card playJackOnlyWhenNecessary(List<Card> possiblePlayableCards) {
        List<Card> noJackCards = possiblePlayableCards.stream()
                .filter(card -> card.getValue() != Card.JACK)
                .collect(Collectors.toList());
        int noJackSize = noJackCards.size();
        Card playCard;
        if (noJackSize > 0) {
            int playCardId = Util.getRandomNumberInRange(noJackSize - 1);
            playCard = noJackCards.get(playCardId);
        } else {
            Optional<Card> first = possiblePlayableCards.stream().filter(card -> card.getValue() == Card.JACK).findFirst();
            if (first.isPresent()) {
                playCard = first.get();
            } else {
                throw new RuntimeException("Should have found jack in this list, but didn't" + possiblePlayableCards);
            }
        }
        return playCard;
    }

    public Card.COLOR getMostColorOnHandThatIsNotJack(List<Card> handCards) {
        Map<Card.COLOR, Long> occurrences = handCards.stream()
                .filter(card -> card.getValue() != Card.JACK)
                .collect(Collectors.groupingBy(Card::getColor,
                        Collectors.counting()));
        Long caro = getNumber(occurrences, Card.COLOR.CARO);
        Long pik = getNumber(occurrences, Card.COLOR.PIK);
        Long herz = getNumber(occurrences, Card.COLOR.HERZ);
        Long kreuz = getNumber(occurrences, Card.COLOR.KREUZ);
        return getHighestColorFromValue(caro, pik, herz, kreuz);
    }

    private long getNumber(Map<Card.COLOR, Long> occurrences, Card.COLOR color) {
        Long number = occurrences.get(color);
        if (number == null) {
            return 0;
        } else {
            return number.longValue();
        }
    }

    private Card.COLOR getHighestColorFromValue(long caro, long pik, long herz, long kreuz) {
        long highestNumber = -1;
        Card.COLOR highestColor = null;
        if (caro > highestNumber) {
            highestColor = Card.COLOR.CARO;
            highestNumber = caro;
        }
        if (pik > highestNumber) {
            highestColor = Card.COLOR.PIK;
            highestNumber = pik;
        }
        if (herz > highestNumber) {
            highestColor = Card.COLOR.HERZ;
            highestNumber = herz;
        }
        if (kreuz > highestNumber) {
            highestColor = Card.COLOR.KREUZ;
        }
        return highestColor;
    }

}
