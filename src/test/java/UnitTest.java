//import org.junit.Test;
//
//import java.util.List;
//
//import static org.junit.Assert.*;
//
//public class UnitTest {
//
//
//
//    @Test(expected = RuntimeException.class)
//    public void invalidValueTest() {
//        new Card(Card.COLOR.DIAMONDS, 3);
//    }
//
//    @Test(expected = RuntimeException.class)
//    public void invalidValueTest2() {
//        new Card(Card.COLOR.DIAMONDS, 15);
//    }
//
//    @Test
//    public void canBeLayedOnTest() {
//        Card pikBube = new Card(Card.COLOR.SPADES, Card.JACK);
//        Card pik7 = new Card(Card.COLOR.SPADES, 7);
//        Card pikQueen = new Card(Card.COLOR.SPADES, Card.QUEEN);
//        Card caroJack = new Card(Card.COLOR.DIAMONDS, Card.JACK);
//        Card any = new Card(Card.COLOR.DIAMONDS, 7);
//
//        assertTrue(pikBube.canBeLayedOn(any));
//        assertTrue(pik7.canBeLayedOn(pikBube));
//        assertTrue(pik7.canBeLayedOn(pikQueen));
//        assertFalse(pikQueen.canBeLayedOn(caroJack));
//    }
//
//    @Test
//    public void deckEmtpyTest() {
//        Game game = new Game(5);
//        game.initGame();
//        for (int i = 0; i < 4; i++) {
//            Card card = game.drawCardFromDeck();
//            game.putCardOnStapel(card);
//        }
//    }
//
//    @Test(expected = RuntimeException.class)
//    public void tooManyPlayersNotEnoughCardsTest() {
//        new Game(100).initGame();
//    }
//
//    @Test
//    public void nextPlayerTest() {
//        final Game game = new Game(3);
//        game.initGame();
//        Card neutralCard = new Card(Card.COLOR.CLUBS, 10);
//        game.putCardOnStapel(neutralCard);
//        game.setNextPlayer();
//
//        try {
//            assertEquals(1, game.getCurrentPlayerId());
//        } catch (AssertionError ae) {
//            System.err.println(game.getTopStapelCard());
//            throw ae;
//        }
//        Card pik8 = new Card(Card.COLOR.SPADES, 8);
//        game.putCardOnStapel(pik8);
//        game.setNextPlayer();
//        assertEquals(0, game.getCurrentPlayerId());
//    }
//
//    @Test
//    public void jackOnStapelNextCardIsAsWished() {
//        Game game = new Game(3);
//        prepareJackTest(game);
//        game.setWishedColor(Card.COLOR.CLUBS);
//        Card card = new Card(Card.COLOR.CLUBS, 9);
//        game.requestPutCardOnStapel(card);
//    }
//
//    @Test
//    public void jackOnStapelNextCardIsAsWishedAndSameColorAsJack() {
//        Game game = new Game(3);
//        prepareJackTest(game);
//        game.setWishedColor(Card.COLOR.SPADES);
//
//        Card card = new Card(Card.COLOR.SPADES, 9);
//        game.requestPutCardOnStapel(card);
//    }
//
//    @Test
//    public void jackHerzLayingHerz8() {
//        Game game = new Game(3);
//        game.initGame();
//        Card neutralCard = new Card(Card.COLOR.CLUBS, 10);
//        game.putCardOnStapel(neutralCard);
//        Card card = new Card(Card.COLOR.HEARTS, Card.JACK);
//        Player currentPlayer = game.getCurrentPlayer();
//        currentPlayer.addCardToHand(card);
//        currentPlayer.playCard(card);
//        game.setWishedColor(Card.COLOR.HEARTS);
//        Card herz8 = new Card(Card.COLOR.HEARTS, 8);
//        game.requestPutCardOnStapel(herz8);
//    }
//
//    private void prepareJackTest(Game game) {
//        game.initGame();
//        Card card = new Card(Card.COLOR.SPADES, Card.JACK);
//        Card neutralCard = new Card(Card.COLOR.SPADES, 10);
//        game.putCardOnStapel(neutralCard);
//        Player currentPlayer = game.getCurrentPlayer();
//        currentPlayer.addCardToHand(card);
//        currentPlayer.playCard(card);
//    }
//
//    @Test(expected = RuntimeException.class)
//    public void jackOnStapelNextCardIsNotAsWished() {
//        Game game = new Game(3);
//        prepareJackTest(game);
//        game.setWishedColor(Card.COLOR.CLUBS);
//        Card card = new Card(Card.COLOR.SPADES, 9);
//        game.requestPutCardOnStapel(card);
//    }
//
//    @Test
//    public void sevenMultiplierTripleTest() {
//        Game game = new Game(2);
//        game.initGame();
//        Card kreuz10 = new Card(Card.COLOR.CLUBS, 10);
//        Card kreuz7 = new Card(Card.COLOR.CLUBS, 7);
//        Card pik7 = new Card(Card.COLOR.SPADES, 7);
//        Card herz7 = new Card(Card.COLOR.HEARTS, 7);
//        game.putCardOnStapel(kreuz10);
//        List<Player> players = game.getPlayers();
//        Player player0 = players.get(0);
//        Player player1 = players.get(1);
//        player0.addCardToHand(kreuz7);
//        player0.addCardToHand(pik7);
//        player1.addCardToHand(herz7);
//        player0.playCard(kreuz7);
//        player1.playCard(herz7);
//        player0.playCard(pik7);
//        int sizeBefore = player1.getHandCards().size();
//        player1.pass();
//        int sizeAfter = player1.getHandCards().size();
//        assertEquals(6, sizeAfter - sizeBefore);
//    }
//
//    @Test
//    public void playerPassesAndDrawsOneCardTest() {
//        Game game = new Game(2);
//        game.initGame();
//        Player player0 = game.getPlayers().get(0);
//        int sizeBefore = player0.getHandCards().size();
//        player0.pass();
//        int sizeAfter = player0.getHandCards().size();
//        assertEquals(1, sizeAfter - sizeBefore);
//    }
//
//    @Test
//    public void sevenMultiplierSingleTest() {
//        Game game = new Game(2);
//        game.initGame();
//        Card kreuz10 = new Card(Card.COLOR.CLUBS, 10);
//        Card kreuz7 = new Card(Card.COLOR.CLUBS, 7);
//        game.putCardOnStapel(kreuz10);
//        List<Player> players = game.getPlayers();
//        Player player0 = players.get(0);
//        Player player1 = players.get(1);
//        player0.addCardToHand(kreuz7);
//        player0.playCard(kreuz7);
//        int sizeBefore = player1.getHandCards().size();
//        player1.pass();
//        int sizeAfter = player1.getHandCards().size();
//        assertEquals(2, sizeAfter - sizeBefore);
//    }
//
//    @Test
//    public void playerPassedBecauseHeHasNoOptionsTest() {
//        Game game = createGameWithNoPlayerHavingCards();
//        Card pik10 = new Card(Card.COLOR.SPADES, 10);
//        game.putCardOnStapel(pik10);
//        List<Player> players = game.getPlayers();
//        Player player0 = players.get(0);
//        Player player1 = players.get(1);
//        Player player2 = players.get(2);
//
//        Card pik9 = new Card(Card.COLOR.SPADES, 9);
//        Card kreuz9 = new Card(Card.COLOR.CLUBS, 9);
//        Card herz10 = new Card(Card.COLOR.HEARTS, 10);
//        Card pikAce = new Card(Card.COLOR.SPADES, Card.ACE);
//
//        //Hands
//        player0.addCardToHand(pik9);
//        player0.addCardToHand(kreuz9);
//        player1.addCardToHand(herz10);
//        player2.addCardToHand(pikAce);
//
//        player0.playCard(pik9);
//
//        assertEquals(2, game.getCurrentPlayerId());
//
//    }
//
//
//
//    @Test
//    public void playerFinishedTest() {
//        Game game = createGameWithNoPlayerHavingCards();
//        List<Player> players = game.getPlayers();
//        Player player0 = players.get(0);
//        Player player1 = players.get(1);
//        Player player2 = players.get(2);
//
//        Card herz9 = new Card(Card.COLOR.HEARTS, 9);
//        Card herz10 = new Card(Card.COLOR.HEARTS, 10);
//        Card herzKing = new Card(Card.COLOR.HEARTS, Card.KING);
//        Card herzQueen = new Card(Card.COLOR.HEARTS, Card.QUEEN);
//        Card herzAce = new Card(Card.COLOR.HEARTS, Card.ACE);
//
//        game.putCardOnStapel(herzAce);
//        player0.addCardToHand(herz9);
//        player0.addCardToHand(herzKing);
//        player0.addCardToHand(herzKing);
//        player1.addCardToHand(herz10);
//        player2.addCardToHand(herzQueen);
//        player2.addCardToHand(herzQueen);
//
//        player0.playCard(herzAce);
//        player1.playCard(herz10);
//        assertEquals(1, player1.getPlace());
//        player2.playCard(herzQueen);
//        player0.playCard(herzKing);
//        assertTrue(game.getCurrentPlayerId() == player2.getPlayerId());
//        player2.playCard(herzQueen);
//        assertEquals(2, player2.getPlace());
//        assertEquals(3, player0.getPlace());
//    }
//
//    private Game createGameWithNoPlayerHavingCards(){
//        Game game = new Game(3);
//        game.initGame();
//        List<Player> players = game.getPlayers();
//        Player player0 = players.get(0);
//        Player player1 = players.get(1);
//        Player player2 = players.get(2);
//
//        player0.getHandCards().clear();
//        player1.getHandCards().clear();
//        player2.getHandCards().clear();
//        return game;
//    }
//
//    @Test
//    public void firstCardIsSevenUserPasses() {
//        Game game = new Game(3);
//        game.initGame();
//        List<Card> deck = game.getDeck();
//        Card pikSeven = new Card(Card.COLOR.SPADES, 7);
//        deck.set(0, pikSeven);
//        game.resetSevenMultiplier();
//        game.drawFirstStapelCard();
//        Player player0 = game.getPlayers().get(0);
//        int sizeBefore = player0.getHandCards().size();
//        player0.pass();
//        int sizeAfter = player0.getHandCards().size();
//        assertEquals(2, sizeAfter - sizeBefore);
//    }
//
//    @Test
//    public void firstCardIsSevenUserPlaysSeven() {
//        Game game = new Game(3);
//        game.initGame();
//        List<Card> deck = game.getDeck();
//        Card pikSeven = new Card(Card.COLOR.SPADES, 7);
//        Card caroSeven = new Card(Card.COLOR.DIAMONDS, 7);
//        deck.set(0, pikSeven);
//        game.resetSevenMultiplier();
//        game.drawFirstStapelCard();
//        Player player0 = game.getPlayers().get(0);
//        Player player1 = game.getPlayers().get(1);
//        player0.addCardToHand(caroSeven);
//        player0.playCard(caroSeven);
//        int sizeBefore = player1.getHandCards().size();
//        player1.pass();
//        int sizeAfter = player1.getHandCards().size();
//        assertEquals(4, sizeAfter - sizeBefore);
//    }
//
//    @Test
//    public void firstCardIsJackPlayerCanPlayAnything() {
//        Game game = new Game(3);
//        game.initGame();
//        List<Card> deck = game.getDeck();
//        Card pikJack = new Card(Card.COLOR.SPADES, Card.JACK);
//        Card caroSeven = new Card(Card.COLOR.DIAMONDS, 7);
//        deck.set(0, pikJack);
//        game.resetSevenMultiplier();
//        game.drawFirstStapelCard();
//        Player player0 = game.getPlayers().get(0);
//        player0.addCardToHand(caroSeven);
//        player0.playCard(caroSeven);
//    }
//
//    @Test
//    public void firstCardIsEightFirstUserIsSkipped() {
//        Game game = new Game(3);
//        game.initGame();
//        List<Card> deck = game.getDeck();
//        Card caro8 = new Card(Card.COLOR.DIAMONDS, 8);
//        int nextPlayerBefore = game.getCurrentPlayerId();
//        deck.set(0, caro8);
//        game.resetSevenMultiplier();
//        game.drawFirstStapelCard();
//        assertEquals(1, game.getCurrentPlayerId() - nextPlayerBefore);
//    }
//
//}