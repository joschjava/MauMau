import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
public class Player {

    private List<Card> handCards = new ArrayList<>();
    private Game game;
    private int playerId;
    //TODO: place
    private int place = -1;

    public Player(Game game, int playerId) {
        this.playerId = playerId;
        this.game = game;
    }

    public void addCardsToHand(List<Card> cards) {
        handCards.addAll(cards);
        Collections.sort(handCards);
    }

    public void addCardToHand(Card card) {
        handCards.add(card);
        Collections.sort(handCards);
    }

    /**
     * Put card on deck
     * @param id Id of handcard of player
     * @return true if played card was Jack and player needs to pick color, false otherwise
     */
    public boolean playCardId(int id) {
        Card card = handCards.get(id);
        return playCard(card);
    }

    /**
     * Put card on deck
     * @param card The card to put onto deck
     * @return true if played card was Jack and player needs to pick color, false otherwise
     */
    public boolean playCard(Card card) {
         if (card.getValue() == 7){
            game.increaseSevenMultiplier();
        }
        game.requestPutCardOnStapel(card);
        handCards.remove(card);
        if (card.getValue() != Card.JACK) {
            game.setNextPlayer();
            return false;
        }
        return true;
    }

    public void pass() {
        int numCardsToDraw = 1;
        int sevenMultiplier = game.getSevenMultiplyer();
        if (game.getTopStapelCard().getValue() == 7 && sevenMultiplier != 0) {
            numCardsToDraw = 2*sevenMultiplier;
        }
        List<Card> penaltyCards = game.drawCardsFromDeck(numCardsToDraw);
        game.resetSevenMultiplier();
        addCardsToHand(penaltyCards);
        game.setNextPlayer();
    }
}
