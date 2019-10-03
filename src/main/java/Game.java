import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {

    private List<Card> deck = new ArrayList<>();
    private List<Card> stapel = new ArrayList<>();
    private int numPlayers;
    // If 7 is put on top of other 7s cards to draw increase
    public int sevenMultiplyer = 0;


    private int CARDS_PER_PLAYER = 6;
    public List<Player> players = new ArrayList<>();
    private int playerTurn = 0;
    private Card.COLOR wishedColor = null;

    Game(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    public int getSevenMultiplyer(){
        return sevenMultiplyer;
    }

    public void increaseSevenMultiplier(){
        sevenMultiplyer++;
    }

    public void resetSevenMultiplier(){
        sevenMultiplyer = 0;
    }

    public List<Player> getPlayers(){
        return players;
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

    public Player getCurrentPlayer(){
        return players.get(playerTurn);
    }

    public void setWishedColor(Card.COLOR color){
        wishedColor = color;
        setNextPlayer();
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

    public int getCurrentPlayerId() {
        return playerTurn;
    }

    private int calculateNextPlayer() {
        return (playerTurn + 1) % numPlayers;
    }

    private void drawFirstStapelCard() {
        final Card firstStapelCard = drawCardFromDeck();
        //TODO: Wish when Jack
        putCardOnStapel(firstStapelCard);
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

    //TODO: Later refactor this function so it returns if card is valid to put on or not
    /**
     * Adds card to top of the stapel
     * @param card Card to lay on top of the stapel
     */
    public void requestPutCardOnStapel(Card card) {
        final Card topStapelCard = getTopStapelCard();
        boolean validCard = true;
        if(topStapelCard.getValue() == Card.JACK){
            if(card.getColor() == wishedColor) {
                putCardOnStapel(card);
            } else {
                validCard = false;
            }
        } else if (card.canBeLayedOn(topStapelCard)) {
            putCardOnStapel(card);
        } else {
            validCard = false;
        }
        if(!validCard){
            throw new RuntimeException("Card " + card + " can't be layed on " + topStapelCard);
        }
    }

    public void putCardOnStapel(Card card) {
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
            Player player = new Player(this, i);
            List<Card> playerCards = drawCardsFromDeck(CARDS_PER_PLAYER);
            player.addCardsToHand(playerCards);
            players.add(player);
            System.out.println("Player1: \n" + player);
        }
    }


}
