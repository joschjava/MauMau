import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Game {

    private List<Card> deck = new ArrayList<>();
    private List<Card> stapel = new ArrayList<>();
    private int numPlayers;
    // If 7 is put on top of other 7s cards to draw increase
    public int sevenMultiplier = 0;


    private int CARDS_PER_PLAYER = 6;
    public List<Player> players = new ArrayList<>();
    private int playerTurn = 0;
    private int leftPlayers;

    // If the first card on stapel is jack at game beginning
    private boolean firstCardIsJack = false;


    private Card.COLOR wishedColor = null;

    Game(int numPlayers) {
        this.numPlayers = numPlayers;
        this.leftPlayers = numPlayers;
    }

    public int getSevenMultiplier() {
        return sevenMultiplier;
    }

    public void increaseSevenMultiplier() {
        sevenMultiplier++;
    }

    public void resetSevenMultiplier() {
        sevenMultiplier = 0;
    }

    public int getLeftPlayers() {
        return leftPlayers;
    }

    public List<Card> getDeck(){
        return deck;
    }

    /**
     * Decreases counter for left players
     *
     * @return true if only one player is left
     */
    public boolean decreaseLeftPlayers() {
        leftPlayers--;
        if (leftPlayers == 1) {
            return true;
        } else {
            return false;
        }
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void finishGame() {
        List<Player> leftPlayer = players.stream().
                filter(player -> player.getPlace() == -1).
                collect(Collectors.toList());
        if (leftPlayer.size() > 1) {
            throw new RuntimeException("Finish game has been called even though more than one player is still active");
        } else {
            leftPlayer.get(0).setPlayerFinished();
        }
    }

    void initGame() {
        stapel.clear();
        deck.clear();
        generateAllCards();
        shuffleDeck();
        generatePlayers();
        drawFirstStapelCard();
    }

    public Player getCurrentPlayer() {
        return players.get(playerTurn);
    }

    public void setWishedColor(Card.COLOR color) {
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
        int nextPlayer = playerTurn;
        int initialPlayer = playerTurn;
        do {
            nextPlayer = (nextPlayer + 1) % numPlayers;
            if (nextPlayer == initialPlayer) {
                throw new RuntimeException("Couldn't find next player");
            }
        } while (getPlayers().get(nextPlayer).isPlayerFinished());
        return nextPlayer;
    }

    public void drawFirstStapelCard() {
        final Card firstStapelCard = drawCardFromDeck();

        if(firstStapelCard.getValue() == 7){
            increaseSevenMultiplier();
        } else if (firstStapelCard.getValue() == 8){
            playerTurn = calculateNextPlayer();
        } else if(firstStapelCard.getValue() == Card.JACK){
            firstCardIsJack = true;
        }
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
     *
     * @param card Card to lay on top of the stapel
     */
    public void requestPutCardOnStapel(Card card) {
        final Card topStapelCard = getTopStapelCard();
        boolean validCard = true;
        if(firstCardIsJack){
            putCardOnStapel(card);
            firstCardIsJack = false;
        } else {
            //TODO: If 7 is on top, only 7 allowed
            if (topStapelCard.getValue() == Card.JACK) {
                if (card.getColor() == wishedColor) {
                    putCardOnStapel(card);
                } else {
                    validCard = false;
                }
            } else if (card.canBeLayedOn(topStapelCard)) {
                putCardOnStapel(card);
            } else {
                validCard = false;
            }
            if (!validCard) {
                printGameInformation();
                throw new RuntimeException("Card " + card + " can't be layed on " + topStapelCard);
            }
        }
    }

    private void printGameInformation(){
        StringBuilder sb = new StringBuilder();
        getPlayers().forEach(player -> {
            String currentPlayerDisplay = "";
            if(playerTurn == player.getPlayerId()){
                currentPlayerDisplay = "(current)";
            }
            sb.append("Player " + player.getPlayerId() +" "+ currentPlayerDisplay + "\n");
            sb.append("Handcards:" + player.getHandCards() + "\n");
            sb.append("Place: " + player.getPlace() + "\n");
            sb.append("\n");
        });
        sb.append("Wished Color: " + wishedColor + "\n");
        sb.append("Oberste Stapelkarte: " + getTopStapelCard() + "\n");
        sb.append("Stapel: " + "\n");
        stapel.forEach(card1 -> sb.append(card1 + "\n"));
        sb.append("Deck: ");
        deck.forEach(card2 -> sb.append(card2 + "\n"));
        System.out.println(sb.toString());
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
            shuffleDeck();
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
        }
    }


}
