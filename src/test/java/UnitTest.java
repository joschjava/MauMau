import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class UnitTest {

    @Test(expected = RuntimeException.class)
    public void invalidValueTest() {
        new Card(Card.COLOR.CARO, 3);
    }

    @Test(expected = RuntimeException.class)
    public void invalidValueTest2() {
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
        for (int i = 0; i < 4; i++) {
            Card card = game.drawCardFromDeck();
            game.putCardOnStapel(card);
        }
    }

    @Test(expected = RuntimeException.class)
    public void tooManyPlayersNotEnoughCardsTest() {
        new Game(100).initGame();
    }

    @Test
    public void nextPlayerTest() {
        final Game game = new Game(3);
        game.initGame();
        game.setNextPlayer();
        assertEquals(1, game.getCurrentPlayerId());
        Card pik8 = new Card(Card.COLOR.PIK, 8);
        game.putCardOnStapel(pik8);
        game.setNextPlayer();
        assertEquals(0, game.getCurrentPlayerId());
    }

    @Test
    public void jackOnStapelNextCardIsAsWished() {
        Game game = new Game(3);
        prepareJackTest(game);
        game.setWishedColor(Card.COLOR.KREUZ);
        Card card = new Card(Card.COLOR.KREUZ, 9);
        game.requestPutCardOnStapel(card);
    }

    @Test
    public void jackOnStapelNextCardIsAsWishedAndSameColorAsJack() {
        Game game = new Game(3);
        prepareJackTest(game);
        game.setWishedColor(Card.COLOR.PIK);

        Card card = new Card(Card.COLOR.PIK, 9);
        game.requestPutCardOnStapel(card);
    }

    private void prepareJackTest(Game game) {
        game.initGame();
        Card card = new Card(Card.COLOR.PIK, Card.JACK);
        Player currentPlayer = game.getCurrentPlayer();
        currentPlayer.addCardToHand(card);
        currentPlayer.playCard(card);
    }

    @Test(expected = RuntimeException.class)
    public void jackOnStapelNextCardIsNotAsWished() {
        Game game = new Game(3);
        prepareJackTest(game);
        game.setWishedColor(Card.COLOR.KREUZ);
        Card card = new Card(Card.COLOR.PIK, 9);
        game.requestPutCardOnStapel(card);
    }

    @Test
    public void sevenMultiplierTripleTest() {
        Game game = new Game(2);
        game.initGame();
        Card kreuz10 = new Card(Card.COLOR.KREUZ, 10);
        Card kreuz7 = new Card(Card.COLOR.KREUZ, 7);
        Card pik7 = new Card(Card.COLOR.PIK, 7);
        Card herz7 = new Card(Card.COLOR.HERZ, 7);
        game.putCardOnStapel(kreuz10);
        List<Player> players = game.getPlayers();
        Player player0 = players.get(0);
        Player player1 = players.get(1);
        player0.addCardToHand(kreuz7);
        player0.addCardToHand(pik7);
        player1.addCardToHand(herz7);
        player0.playCard(kreuz7);
        player1.playCard(herz7);
        player0.playCard(pik7);
        int sizeBefore = player1.getHandCards().size();
        player1.pass();
        int sizeAfter = player1.getHandCards().size();
        assertEquals(6, sizeAfter - sizeBefore);
    }

    @Test
    public void playerPassesAndDrawsOneCardTest() {
        Game game = new Game(2);
        game.initGame();
        Player player0 = game.getPlayers().get(0);
        int sizeBefore = player0.getHandCards().size();
        player0.pass();
        int sizeAfter = player0.getHandCards().size();
        assertEquals(1, sizeAfter - sizeBefore);
    }

    @Test
    public void sevenMultiplierSingleTest() {
        Game game = new Game(2);
        game.initGame();
        Card kreuz10 = new Card(Card.COLOR.KREUZ, 10);
        Card kreuz7 = new Card(Card.COLOR.KREUZ, 7);
        game.putCardOnStapel(kreuz10);
        List<Player> players = game.getPlayers();
        Player player0 = players.get(0);
        Player player1 = players.get(1);
        player0.addCardToHand(kreuz7);
        player0.playCard(kreuz7);
        int sizeBefore = player1.getHandCards().size();
        player1.pass();
        int sizeAfter = player1.getHandCards().size();
        assertEquals(2, sizeAfter - sizeBefore);
    }

    @Test
    public void playerFinishedTest() {
        Game game = new Game(3);
        game.initGame();
        List<Player> players = game.getPlayers();

        Player player0 = players.get(0);
        Player player1 = players.get(1);
        Player player2 = players.get(2);

        player0.getHandCards().clear();
        player1.getHandCards().clear();
        player2.getHandCards().clear();

        Card herz9 = new Card(Card.COLOR.HERZ, 9);
        Card herz10 = new Card(Card.COLOR.HERZ, 10);
        Card herzKing = new Card(Card.COLOR.HERZ, Card.KING);
        Card herzQueen = new Card(Card.COLOR.HERZ, Card.QUEEN);
        Card herzAce = new Card(Card.COLOR.HERZ, Card.ACE);

        game.putCardOnStapel(herzAce);
        player0.addCardToHand(herz9);
        player0.addCardToHand(herzKing);
        player0.addCardToHand(herzKing);
        player1.addCardToHand(herz10);
        player2.addCardToHand(herzQueen);
        player2.addCardToHand(herzQueen);

        player0.playCard(herzAce);
        player1.playCard(herz10);
        assertEquals(1, player1.getPlace());
        player2.playCard(herzQueen);
        player0.playCard(herzKing);
        assertTrue(game.getCurrentPlayerId() == player2.getPlayerId());
        player2.playCard(herzQueen);
        assertEquals(2, player2.getPlace());
        assertEquals(3, player0.getPlace());
    }

}