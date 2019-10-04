import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Controller {

    @FXML
    Button btSubmit;

    @FXML
    VBox vBplayerDisplay;

    @FXML
    TextField tfInput;

    @FXML
    Label lbStapel;

    @FXML
    Button btPass;

    @FXML
    Button btPik;

    @FXML
    Button btHerz;

    @FXML
    Button btCaro;

    @FXML
    Button btKreuz;

    @FXML
    HBox hbJackPickerBox;

    @FXML
    HBox hbDeck;

    @FXML
    Button btSetNextPlayer;

    @FXML
    Label lbWishedColor;

    Game game;

    private static int aiThinkTimeMs = 1000;

    private CardAction cardAction;

    Color caroHerzColor = Color.ORANGE;
    Color pikKreuzColor = Color.BLACK;

    @FXML
    public void initialize() {
        btSubmit.setOnAction(event -> {
            setNextPlayerButtonAction();
        });
        btPass.setOnAction(event -> {
            buttonPassAction();
        });
        tfInput.setOnKeyPressed(ae -> {
            if (ae.getCode().equals(KeyCode.ENTER)) {
                String text = tfInput.getText();
                if (text.equals("d")) {
                    game.hasPlayerPlayableCards(true);

                } else if (text.equals("+")) {
                    buttonPassAction();
                } else {
                    if (text.equals("")) {
                        game.setNextPlayer();
                        updateGui();
                    } else {
                        setNextPlayerButtonAction();
                    }
                }
                tfInput.setText("");
            }
        });
        btSetNextPlayer.setOnAction(event -> {
            game.setNextPlayer();
            updateGui();
        });

        setJackChooserButtonListener(btPik, Card.COLOR.PIK);
        setJackChooserButtonListener(btHerz, Card.COLOR.HERZ);
        setJackChooserButtonListener(btCaro, Card.COLOR.CARO);
        setJackChooserButtonListener(btKreuz, Card.COLOR.KREUZ);

        game = new Game(3);
        List<AI> ais = new ArrayList<>();
        ais.add(new RandomAI(game));
        ais.add(new AdvancedRandomAI(game));
        ais.add(new AdvancedRandomAI(game));
        game.initGame(ais);
        hbJackPickerBox.setVisible(false);
        updateGui();
        if (game.getCurrentPlayer().isAi()) {
            setDelayedNextPlayerExceptGameIsFinished();
        }
    }

    private void buttonPassAction() {
        System.out.println("buttonPassAction");
        passAction();
        updateGui();
        setDelayedNextPlayerExceptGameIsFinished();
    }

    private void setNextPlayerButtonAction() {
        System.out.println("setNextPlayerButtonAction");
        boolean jackPlayed = submitAction();
        updateGui();
        if (!jackPlayed) {
            setDelayedNextPlayerExceptGameIsFinished();
        }
    }

    public void setDelayedNextPlayerExceptGameIsFinished() {
        System.out.println("setDelayedNextPlayerExceptGameIsFinished called");
        if (!game.isGameFinished()) {
            System.out.println("Setting delayed next player");
            Timeline timeline = new Timeline(new KeyFrame(
                    Duration.millis(aiThinkTimeMs),
                    ae -> {
                        System.out.println("Delayed next player action triggered");
                        game.triggerNextPlayerAction();
                        updateGui();
                        if (game.getCurrentPlayer().isAi()) {
                            setDelayedNextPlayerExceptGameIsFinished();
                        } else {
                            System.out.println("Next player not ai");
                        }
                    }));
            timeline.play();
        } else {
            System.out.println("Game is finished");
        }
    }

    public void setJackChooserButtonListener(Button button, Card.COLOR color) {
        button.setOnAction(event -> {
            wishColor(color);
            updateGui();
        });
    }

    public void updateGui() {
        int currentPlayerId = game.getCurrentPlayerId();
        vBplayerDisplay.getChildren().clear();
        List<Player> players = game.getPlayers();
        updateDeckUi();
        updatePlayerUi(currentPlayerId, players);
        updateStapelCard();
        updateWishedColorUi();
        tfInput.requestFocus();
    }

    private void updateWishedColorUi() {
        Card.COLOR wishedColor = game.getWishedColor();
        if (wishedColor == null) {
            lbWishedColor.setText("");
        } else {
            Color labelColor = getColorFromCardColor(wishedColor);
            lbWishedColor.setText(Card.colorToAsciiSymbol(wishedColor));
            lbWishedColor.setTextFill(labelColor);
        }
    }

    private void updateDeckUi() {
        List<Card> deck = game.getDeck();
        int shownCards = 6;
        if (deck.size() < shownCards) {
            shownCards = deck.size();
        }
        List<Card> cards = deck.subList(0, shownCards);
        hbDeck.getChildren().clear();
        for (int i = 0; i < cards.size(); i++) {
            Text cardText = createColoredText(cards.get(i), i, true);
            hbDeck.getChildren().add(cardText);
        }
    }

    private void updatePlayerUi(int currentPlayerId, List<Player> players) {
        for (int plId = 0; plId < players.size(); plId++) {
            Player player = players.get(plId);
            List<Card> handCards = player.getHandCards();
            TextFlow tFlPlayer = new TextFlow();
            String aiText = "";
            if (player.isAi()) {
                aiText = "(" + player.getAi().getAiName() + ")";
                if (player == game.getCurrentPlayer()) {
                    aiText += " thinking...";
                }
            }
            Text playerIdText = new Text("Player " + plId + " " + aiText + " (" + handCards.size() + ")\n");
            tFlPlayer.getChildren().add(playerIdText);
            if (!player.isPlayerFinished()) {
                for (int i = 0; i < handCards.size(); i++) {
                    Card card = handCards.get(i);
                    boolean validMove = true;
                    if (plId == currentPlayerId) {
                        String colorString = "#9281ED";
                        tFlPlayer.setBackground(
                                new Background(
                                        new BackgroundFill(
                                                Color.web(colorString),
                                                CornerRadii.EMPTY,
                                                Insets.EMPTY)
                                ));
                        validMove = game.isValidMove(card);
                    }
                    Text cardText = createColoredText(card, i, validMove);
                    tFlPlayer.getChildren().add(cardText);
                }
            } else {
                int place = player.getPlace();
                Text placeText = new Text(place + ". place");
                tFlPlayer.getChildren().add(placeText);
            }
            vBplayerDisplay.getChildren().add(tFlPlayer);
        }
    }

    private void updateStapelCard() {
        Card stapelCard = game.getTopStapelCard();
        Color stapelColor = getColorFromCardColor(stapelCard.getColor());
        lbStapel.setTextFill(stapelColor);
        lbStapel.setText(stapelCard.toPrettyString());
    }

    private Text createColoredText(Card card, int i, boolean validMove) {
        Text cardText = new Text(i + ": " + card.toPrettyString() + "    ");
        if (validMove) {
            Color cardColor = getColorFromCardColor(card.getColor());
            cardText.setFill(cardColor);
        } else {
            cardText.setFill(Color.GRAY);
        }
        return cardText;
    }

    /**
     * Plays selected card
     *
     * @return true if played card is jack, false otherwise
     */
    public boolean submitAction() {
        String text = tfInput.getText();
        tfInput.setText("");
        int cardId = Integer.valueOf(text);
        Player currentPlayer = game.getCurrentPlayer();
        Card card = currentPlayer.getCardFromId(cardId);
        cardAction = new CardAction(card);
        if (cardAction.isJack()) {
            hbJackPickerBox.setVisible(true);
            return true;
        } else {
            currentPlayer.playCard(cardAction);
            cardAction = null;
            return false;
        }
    }

    public void wishColor(Card.COLOR color) {
        if (cardAction != null) {
            cardAction.setJackWishColor(color);
            game.getCurrentPlayer().playCard(cardAction);
        } else {
            throw new RuntimeException("Couldn't find any cardAction for my Jack");
        }
        setDelayedNextPlayerExceptGameIsFinished();
        hbJackPickerBox.setVisible(false);
    }

    private Color getColorFromCardColor(Card.COLOR color) {
        Color guiColor;
        if (color == Card.COLOR.HERZ || color == Card.COLOR.CARO) {
            guiColor = caroHerzColor;
        } else {
            guiColor = pikKreuzColor;
        }
        return guiColor;
    }

    public void passAction() {
        Player currentPlayer = game.getCurrentPlayer();
        currentPlayer.pass();
    }

}
