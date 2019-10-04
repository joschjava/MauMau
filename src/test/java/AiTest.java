import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;


public class AiTest {


    @Test
    public void AdvancedRandomAiDoesntPlayJackTest(){
        List<Card> handCards = new ArrayList<>();
        handCards.add(new Card(Card.COLOR.PIK, 10));
        handCards.add(new Card(Card.COLOR.PIK, Card.JACK));
        Card stapelCard = new Card(Card.COLOR.PIK, Card.ACE);
        Game game = new Game(3);
        game.initGame();
        game.putCardOnStapel(stapelCard);
        AdvancedRandomAI ai = new AdvancedRandomAI(game);
        Card chosenCard = ai.makeMove(handCards).getCard();
        assertNotEquals(Card.JACK,chosenCard.getValue());
    }

    @Test
    public void AdvancedRandomAiPlaysJackTest(){
        List<Card> handCards = new ArrayList<>();
        handCards.add(new Card(Card.COLOR.CARO, Card.JACK));
        handCards.add(new Card(Card.COLOR.PIK, 10));
        handCards.add(new Card(Card.COLOR.PIK, 7));
        handCards.add(new Card(Card.COLOR.PIK, 8));
        Card stapelCard = new Card(Card.COLOR.CARO, Card.ACE);
        Game game = new Game(3);
        game.initGame();
        game.putCardOnStapel(stapelCard);
        game.setWishedColor(null);
        AdvancedRandomAI ai = new AdvancedRandomAI(game);
        CardAction chosenCardAction = ai.makeMove(handCards);
        Card chosenCard = chosenCardAction.getCard();
        assertEquals(Card.JACK,chosenCard.getValue());
        assertEquals(Card.COLOR.PIK, chosenCardAction.getJackWishColor());
    }

}