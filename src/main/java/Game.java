import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class Game {

    private List<Card> deck = new ArrayList<>();
    private List<Card> stapel = new ArrayList<>();
    private int numPlayers;
    /**
     * If 7 is put on top of other 7s cards to draw increase
     */
    public int sevenMultiplier = 0;

    /**
     * If eight that is on top of stapel was already skipped
     */
    private boolean eightIsPaid = false;

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

    public List<Card> getDeck() {
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
        initGame(null);
    }

    void initGame(List<AI> ais) {
        stapel.clear();
        deck.clear();
        generateAllCards();
        shuffleDeck();
        if (ais == null) {
            generatePlayers();
        } else {
            generatePlayers(ais);
        }
        Card card = new Card(Card.COLOR.PIK, Card.JACK);
        deck.set(0, card);
        drawFirstStapelCard();
    }

    public Player getCurrentPlayer() {
        return players.get(playerTurn);
    }

    public void setWishedColor(Card.COLOR color) {
        wishedColor = color;
    }

    public void setNextPlayer() {
        System.out.println("Going to next player from " + getCurrentPlayerId());
        playerTurn = calculateNextPlayer();
        final Card topStapelCard = getTopStapelCard();
        if (topStapelCard == null) {
            throw new RuntimeException("Stapel is null but shouldn't be");
        }
        if (topStapelCard.getValue() == 8 && !eightIsPaid) {
            eightIsPaid = true;
            playerTurn = calculateNextPlayer();
        }
        if (hasPlayerPlayableCards()) {
            Player currentPlayer = getCurrentPlayer();
            if (currentPlayer.isAi()) {
                AI ai = currentPlayer.getAi();
                CardAction cardAction = ai.makeMove(currentPlayer.getHandCards(), getTopStapelCard());
                if (cardAction.getCard() != null) {
                    currentPlayer.playCard(cardAction);
                } else {
                    throw new RuntimeException("Player(AI) "+currentPlayer.getPlayerId()+" has no playable card but should have");
                }
            }
        } else {
            if (getCurrentPlayerId() == 0) {
                System.out.println("Human Player has no playable cards");
            } else {
                System.out.println("Skipped player " + getCurrentPlayerId());
                getCurrentPlayer().pass();
            }
        }
    }

    public boolean hasPlayerPlayableCards(){
        return hasPlayerPlayableCards(false);
    }

    public boolean hasPlayerPlayableCards(boolean debug) {
        List<Card> handCards = getCurrentPlayer().getHandCards();
        boolean hasValidCards = false;
        for (int i = 0; i < handCards.size(); i++) {
            Card playableCard = handCards.get(i);
            if (isValidMove(playableCard, debug)) {
                if(debug) {
                    System.out.println("Playable card found: " + playableCard);
                }
                hasValidCards = true;
                //TODO: Put back in for performance
//                break;
            }
        }
        return hasValidCards;
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
        switch (firstStapelCard.getValue()) {
            case 7:
                increaseSevenMultiplier();
                break;

            case 8:
                playerTurn = calculateNextPlayer();
                break;

            case Card.JACK:
                firstCardIsJack = true;
                break;
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

    /**
     * Adds card to top of the stapel
     *
     * @param card Card to lay on top of the stapel
     */
    public void requestPutCardOnStapel(Card card) {
        boolean validMove = isValidMove(card);
        if (validMove) {
            putCardOnStapel(card);
            firstCardIsJack = false;
        } else {
            printGameInformation();
            throw new RuntimeException("Card " + card + " can't be layed on '" + getTopStapelCard() + "'");
        }
    }

    public boolean isValidMove(Card card){
       return isValidMove(card, false);
    }

    public boolean isValidMove(Card card, boolean debug) {
        final Card topStapelCard = getTopStapelCard();
        boolean validCard;
        if (firstCardIsJack) {
            validCard = true;
        } else {
            if (topStapelCard.getValue() == Card.JACK) {
                if (card.getColor() == wishedColor) {
                    validCard = true;
                } else {
                    validCard = false;
                    if(debug){
                        System.out.println(card + " not playable: stapel is jack, wished color is not the same: ");
                        System.out.println("Game.wishedColor: "+wishedColor+", Card Color: "+card.getValue());
                    }
                }
            } else if (topStapelCard.getValue() == 7 && sevenMultiplier != 0) {
                if (card.getValue() == 7) {
                    validCard = true;
                } else {
                    if(debug) {
                        System.out.println(card + " is not playable: Must be 7");
                    }
                    validCard = false;
                }
            } else if (card.canBeLayedOn(topStapelCard, debug)) {
                validCard = true;
            } else {
                validCard = false;
            }
        }
        return validCard;
    }

    private void printGameInformation() {
        StringBuilder sb = new StringBuilder();
        sb.append("firstCardIsJack: " + firstCardIsJack);
        getPlayers().forEach(player -> {
            String currentPlayerDisplay = "";
            if (playerTurn == player.getPlayerId()) {
                currentPlayerDisplay = "(current)";
            }
            sb.append("Player " + player.getPlayerId() + " " + currentPlayerDisplay + "\n");
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
        if (card.getValue() == 8) {
            // Set that 8 penalty will be given for next player
            eightIsPaid = false;
        }
        stapel.add(card);
        if (card.getValue() != Card.JACK) {
            log.debug("Reset wished color");
            wishedColor = null;
        }
    }

    public Card drawCardFromDeck() {
        if (deck.size() == 0) {
            List<Card> stapelCards = stapel.subList(0, stapel.size() - 1);//keep one card
            deck.addAll(stapelCards);
            stapel.removeAll(stapelCards);
            shuffleDeck();
        }
        if (deck.size() == 0) {
            throw new RuntimeException("Deck shouldn't be empty");
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
        generatePlayers(null);
    }

    private void generatePlayers(List<AI> ais) {
        if (ais != null) {
            if (ais.size() != numPlayers) {
                throw new RuntimeException("AI length is not the same as possible players");
            }
        }

        if (numPlayers * CARDS_PER_PLAYER > deck.size()) {
            throw new RuntimeException("Not enough cards (" + deck.size() + ") for " + numPlayers + " Players");
        }
        for (int i = 0; i < numPlayers; i++) {
            Player player;
            if (ais == null) {
                player = new Player(this, i, null);
            } else {
                player = new Player(this, i, ais.get(i));
            }
            List<Card> playerCards = drawCardsFromDeck(CARDS_PER_PLAYER);
            player.addCardsToHand(playerCards);
            players.add(player);
        }
    }


}
