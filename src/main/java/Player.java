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

    public void playCardId(int i) {
        Card card = handCards.get(i);
        playCard(card);
    }

    public void playCard(Card card) {
        if (card.getValue() == Card.JACK) {
            //TODO: Require User Action
        } else if (card.getValue() == 7){
            game.increaseSevenMultiplier();
        }
        game.requestPutCardOnStapel(card);
        handCards.remove(card);
        game.setNextPlayer();
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

    public List<Card> getHandCards() {
        return this.handCards;
    }

    public Game getGame() {
        return this.game;
    }

    public void setHandCards(List<Card> handCards) {
        this.handCards = handCards;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Player)) return false;
        final Player other = (Player) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$handCards = this.getHandCards();
        final Object other$handCards = other.getHandCards();
        if (this$handCards == null ? other$handCards != null : !this$handCards.equals(other$handCards)) return false;
        final Object this$game = this.getGame();
        final Object other$game = other.getGame();
        if (this$game == null ? other$game != null : !this$game.equals(other$game)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Player;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $handCards = this.getHandCards();
        result = result * PRIME + ($handCards == null ? 43 : $handCards.hashCode());
        final Object $game = this.getGame();
        result = result * PRIME + ($game == null ? 43 : $game.hashCode());
        return result;
    }

    public String toString() {
        return "Player(handCards=" + this.getHandCards() + ", game=" + this.getGame() + ")";
    }
}
