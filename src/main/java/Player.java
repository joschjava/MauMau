import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
public class Player {

    private List<Card> handCards = new ArrayList<>();
    private Game game;
    private int playerId;
    private int place = -1;
    private AI ai;


    public Player(Game game, int playerId) {
        this(game, playerId, null);
    }

    public Player(Game game, int playerId, AI ai) {
        this.playerId = playerId;
        this.game = game;
        this.ai = ai;
    }

    public boolean isAi() {
        if (ai == null) {
            return false;
        } else {
            return true;
        }
    }

    public void addCardsToHand(List<Card> cards) {
        handCards.addAll(cards);
        Collections.sort(handCards);
    }

    public void addCardToHand(Card card) {
        handCards.add(card);
        Collections.sort(handCards);
    }

    public Card getCardFromId(int id) {
        return handCards.get(id);
    }

    /**
     * Put card on deck
     *
     * @param cardAction The card to put onto deck
     * @return true if played card was Jack and player needs to pick color, false otherwise
     */
    public void playCard(CardAction cardAction) {
        Card card = cardAction.getCard();
        if (card.getValue() == 7) {
            game.increaseSevenMultiplier();
        } else if (cardAction.isJack()) {
            Card.COLOR wishedColor = cardAction.getJackWishColor();
            game.setWishedColor(wishedColor);
            System.out.println("Setting wished color: " + wishedColor);
        }
        System.out.println("Player "+playerId + " plays "+card);
        game.requestPutCardOnStapel(card);
        handCards.remove(card);
        boolean gameFinished = false;
        if (handCards.size() == 0) {
            gameFinished = setPlayerFinished();
            if (gameFinished) {
                game.finishGame();
            }
        }
        //TODO: Revise!
        if (!gameFinished) {
            game.setNextPlayer();
        }
    }

    public int getPlace() {
        return place;
    }

    public boolean setPlayerFinished() {
        int leftPlayers = game.getLeftPlayers();
        int numPlayers = game.getNumPlayers();
        place = numPlayers - leftPlayers + 1;
        return game.decreaseLeftPlayers();
    }

    public boolean isPlayerFinished() {
        return place != -1;
    }

    public void pass() {
        System.out.println("Player "+playerId+" is passing");
        int numCardsToDraw = 1;
        int sevenMultiplier = game.getSevenMultiplier();
        if (game.getTopStapelCard().getValue() == 7 && sevenMultiplier != 0) {
            numCardsToDraw = 2 * sevenMultiplier;
        }
        List<Card> penaltyCards = game.drawCardsFromDeck(numCardsToDraw);
        game.resetSevenMultiplier();
        addCardsToHand(penaltyCards);
        game.setNextPlayer();
    }
}
