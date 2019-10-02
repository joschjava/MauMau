import org.junit.Test;

import static org.junit.Assert.*;

public class UnitTest {

    @Test(expected = RuntimeException.class)
    public void invalidValueTest(){
         new Card(Card.COLOR.CARO, 3);
    }

    @Test(expected = RuntimeException.class)
    public void invalidValueTest2(){
        new Card(Card.COLOR.CARO, 15);
    }

    @Test
    public void canBeLayedOnTest() {
        Card pikBube = new Card(Card.COLOR.PIK, Card.JACK);
        Card pik7 = new Card(Card.COLOR.PIK, 7);
        Card pikQueen = new Card(Card.COLOR.PIK, Card.QUEEN);
        Card caroJack = new Card(Card.COLOR.CARO, Card.JACK);
        Card any = new Card(Card.COLOR.CARO, 7);

        assertTrue(pikBube.canBeLayedOn(any));
        assertTrue(pik7.canBeLayedOn(pikBube));
        assertTrue(pik7.canBeLayedOn(pikQueen));
        assertFalse(pikQueen.canBeLayedOn(caroJack));
    }

    @Test
    public void deckEmtpyTest() {
        Game game = new Game(5);
        game.initGame();
        for(int i=0;i<4;i++){
            Card card = game.drawCardFromDeck();
            game.requestPutCardOnStapel(card, true);
        }
    }

    @Test(expected = RuntimeException.class)
    public void tooManyPlayersNotEnoughCardsTest(){
        new Game(100).initGame();
    }

    @Test
    public void nextPlayerTest(){
        final Game game = new Game(3);
        game.initGame();
        game.setNextPlayer();
        assertEquals(1, game.getCurrentPlayer());
        Card pik8 = new Card(Card.COLOR.PIK, 8);
        game.requestPutCardOnStapel(pik8, true);
        game.setNextPlayer();
        assertEquals(0, game.getCurrentPlayer());
    }

    @Test
    public void jackTest1(){
        Game game = new Game(3);
        prepareJackTest(game);
        Card card = new Card(Card.COLOR.KREUZ, 9);
        game.requestPutCardOnStapel(card);
    }

    @Test (expected = RuntimeException.class)
    public void jackTest2(){
        Game game = new Game(3);
        prepareJackTest(game);
        Card card = new Card(Card.COLOR.PIK, 9);
        game.requestPutCardOnStapel(card);
    }

    private void prepareJackTest(Game game) {
        game.initGame();
        Card card = new Card(Card.COLOR.PIK, Card.JACK);
        game.requestPutCardOnStapel(card, true);
        game.setWishedColor(Card.COLOR.KREUZ);
    }

}