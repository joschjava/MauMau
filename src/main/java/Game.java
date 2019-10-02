import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {

    private List<Card> deck = new ArrayList<>();
    private List<Card> stapel = new ArrayList<>();
    private int numPlayers;
    private int CARDS_PER_PLAYER = 6;
    private int playerTurn = 0;
    private Card.COLOR wishedColor = null;

    Game(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    void initGame() {
        stapel.clear();
        deck.clear();
        generateAllCards();
        shuffleDeck();
        generatePlayers();
        drawFirstStapelCard();
        System.out.println("Deck: ");
        deck.forEach(System.out::println);
    }

    public void setWishedColor(Card.COLOR color){
        wishedColor = color;
    }

    public void setNextPlayer() {
        playerTurn = calculateNextPlayer();

        final Card topStapelCard = getTopStapelCard();
        if (topStapelCard == null) {
            throw new RuntimeException("Stapel is null but shouldn't be");
        }
        if (topStapelCard.getValue() == 8) {
            playerTurn = calculateNextPlayer();
        }
    }

    public Card getTopStapelCard() {
        int size = stapel.size();
        if (size == 0) {
            return null;
        } else {
            return stapel.get(size - 1);
        }
    }

    public int getCurrentPlayer() {
        return playerTurn;
    }

    private int calculateNextPlayer() {
        return (playerTurn + 1) % numPlayers;
    }

    private void drawFirstStapelCard() {
        final Card firstStapelCard = drawCardFromDeck();
        //TODO: Wish when Jack
        requestPutCardOnStapel(firstStapelCard);
    }

    private void shuffleDeck() {
        Collections.shuffle(deck);
    }

    private void generateAllCards() {
        for (int i = 7; i <= 14; i++) {
            Card herz = new Card(Card.COLOR.HERZ, i);
            Card pik = new Card(Card.COLOR.PIK, i);
            Card kreuz = new Card(Card.COLOR.KREUZ, i);
            Card caro = new Card(Card.COLOR.CARO, i);
            deck.add(herz);
            deck.add(pik);
            deck.add(kreuz);
            deck.add(caro);
        }
    }

    public void requestPutCardOnStapel(Card card) {
        requestPutCardOnStapel(card, false);
    }

    /**
     * Adds card to top of the stapel
     * @param card Card to lay on top of the stapel
     * @param force if true, skip checking if move is valid
     */
    public void requestPutCardOnStapel(Card card, boolean force) {
        final Card topStapelCard = getTopStapelCard();
        if(topStapelCard.getValue() == Card.JACK && card.getColor() == wishedColor){
            putCardOnStapel(card);
        } else if (card.canBeLayedOn(topStapelCard) || force) {
            putCardOnStapel(card);
        } else {
            throw new RuntimeException("Card " + card + " can't be layed on " + topStapelCard);
        }
    }

    private void putCardOnStapel(Card card) {
        stapel.add(card);
        wishedColor = null;
    }

    public Card drawCardFromDeck() {
        if (deck.size() == 0) {
            List<Card> stapelCards = stapel.subList(0, stapel.size() - 1);//keep one card
            deck.addAll(stapelCards);
            stapel.removeAll(stapelCards);
        }
        Card drawnCard = deck.get(0);
        deck.remove(0);
        return drawnCard;
    }

    public List<Card> drawCardsFromDeck(int number) {
        List<Card> drawnCards = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            drawnCards.add(drawCardFromDeck());
        }
        return drawnCards;
    }

    private void generatePlayers() {
        if (numPlayers * CARDS_PER_PLAYER > deck.size()) {
            throw new RuntimeException("Not enough cards (" + deck.size() + ") for " + numPlayers + " Players");
        }
        for (int i = 0; i < numPlayers; i++) {
            Player player = new Player(this);
            List<Card> playerCards = drawCardsFromDeck(CARDS_PER_PLAYER);
            player.addCardsToHand(playerCards);
            System.out.println("Player1: \n" + player);
        }
    }


}
