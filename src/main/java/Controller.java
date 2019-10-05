import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Controller {

    @FXML
    VBox vBplayerDisplay;

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
    Label lbWishedColor;

    Game game;

    private static int aiThinkTimeMs = 1000;

    private CardAction cardAction;

    Color caroHerzColor = Color.ORANGE;
    Color pikKreuzColor = Color.BLACK;

    @FXML
    public void initialize() {
        Card card = new Card(Card.COLOR.CLUBS, Card.ACE);
        GuiCard guiCard = new GuiCard(card);
        ImageView imageView = guiCard.getImageView();
        vBplayerDisplay.getChildren().add(imageView);

//        btPass.setOnAction(event -> buttonPassAction());
//
//        setJackChooserButtonListener(btPik, Card.COLOR.SPADES);
//        setJackChooserButtonListener(btHerz, Card.COLOR.HEARTS);
//        setJackChooserButtonListener(btCaro, Card.COLOR.DIAMONDS);
//        setJackChooserButtonListener(btKreuz, Card.COLOR.CLUBS);
//
//        game = new Game(3);
//        List<AI> ais = new ArrayList<>();
//        ais.add(null);
//        ais.add(new RandomAI(game));
//        ais.add(new AdvancedRandomAI(game));
//        game.initGame(ais);
//        hbJackPickerBox.setVisible(false);
//        updateGui();
//        if (game.getCurrentPlayer().isAi()) {
//            setDelayedNextPlayerExceptGameIsFinished();
//        }
    }

    private void buttonPassAction() {
        System.out.println("buttonPassAction");
        passAction();
        updateGui();
        setDelayedNextPlayerExceptGameIsFinished();
    }

    private void setNextPlayerButtonAction(int cardId) {
        boolean jackPlayed = submitAction(cardId);
        updateGui();
        if (!jackPlayed) {
            setDelayedNextPlayerExceptGameIsFinished();
        }
    }

    public void setDelayedNextPlayerExceptGameIsFinished() {
        btPass.setDisable(true);
        if (!game.isGameFinished()) {
            Timeline timeline = new Timeline(new KeyFrame(
                    Duration.millis(aiThinkTimeMs),
                    ae -> {
                        game.triggerNextPlayerAction();
                        updateGui();
                        Player currentPlayer = game.getCurrentPlayer();
                        if (currentPlayer.isAi()) {
                            setDelayedNextPlayerExceptGameIsFinished();
                        } else if(!game.hasPlayerPlayableCards()) {
                            setDelayedNextPlayerExceptGameIsFinished();
                        } else
                            btPass.setDisable(false);
                            System.out.println("Next player not ai");
                        }
                    ));
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
//        updateDeckUi();
        updatePlayerUi();
        updateStapelCard();
        updateWishedColorUi();
//        tfInput.requestFocus();
    }

    private void updateWishedColorUi() {
        Card.COLOR wishedColor = game.getWishedColor();
        if (wishedColor == null) {
            lbWishedColor.setText(game.getPlayedRounds()+"");
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
            Text cardText = createColoredText(cards.get(i), true);
            hbDeck.getChildren().add(cardText);
        }
    }

    private void updatePlayerUi() {
        vBplayerDisplay.getChildren().clear();
        Player currentPlayer = game.getCurrentPlayer();
        int currentPlayerId = currentPlayer.getPlayerId();
        List<Player> players = game.getPlayers();
        for (int plId = 0; plId < players.size(); plId++) {
            Player player = players.get(plId);
            List<Card> handCards = player.getHandCards();
            HBox hBPlayer = new HBox();
            String aiText = "";
            if (player.isAi()) {
                aiText = "(" + player.getAi().getAiName() + ")";
                if (player == game.getCurrentPlayer()) {
                    aiText += " thinking...";
                }
            }
            Text playerIdText = new Text("Player " + plId + " " + aiText + " (" + handCards.size() + ")\n");
            vBplayerDisplay.getChildren().add(playerIdText);
            if (!player.isPlayerFinished()) {
                for (int i = 0; i < handCards.size(); i++) {
                    Card card = handCards.get(i);
                    boolean validMove = true;
                    if (plId == currentPlayerId) {
                        String colorString = "#9281ED";
                        hBPlayer.setBackground(
                                new Background(
                                        new BackgroundFill(
                                                Color.web(colorString),
                                                CornerRadii.EMPTY,
                                                Insets.EMPTY)
                                ));
                        validMove = game.isValidMove(card);
                    }

                    boolean leadingColon = !player.isAi();
                    if(leadingColon){
                        if(currentPlayerId != plId){
                            validMove = false;
                        }
                        Button cardButton = createColoredButton(card, i, validMove);
                        hBPlayer.getChildren().add(cardButton);
                    } else {
                        Text cardText = createColoredText(card, validMove);
                        hBPlayer.getChildren().add(cardText);
                    }
                    hBPlayer.setSpacing(20);
                }
            } else {
                int place = player.getPlace();
                Text placeText = new Text(place + ". place");
                hBPlayer.getChildren().add(placeText);
            }
            vBplayerDisplay.getChildren().add(hBPlayer);
        }
    }

    private void updateStapelCard() {
        Card stapelCard = game.getTopStapelCard();
        Color stapelColor = getColorFromCardColor(stapelCard.getColor());
        lbStapel.setTextFill(stapelColor);
        lbStapel.setText(stapelCard.toPrettyString());
    }

    private Button createColoredButton(Card card, int i, boolean validMove) {
        Button cardButton = new Button(card.toPrettyString());

        if (validMove) {
            Color cardColor = getColorFromCardColor(card.getColor());
            String hexString = Util.getHexRepresentation(cardColor);
            cardButton.setStyle("-fx-text-fill: "+hexString);
            cardButton.setDisable(false);
        } else {
            Color gray = Color.GRAY;
            String hexString = Util.getHexRepresentation(gray);
            cardButton.setStyle("-fx-text-fill: "+hexString);
            cardButton.setDisable(true);
        }
        cardButton.setFont(new Font(20.0));
        cardButton.setOnAction(
                ae -> setNextPlayerButtonAction(i)
        );
        cardButton.setPadding(new Insets(2));
        return cardButton;
    }

    private Text createColoredText(Card card, boolean validMove) {
        Text cardText = new Text(card.toPrettyString());
        if (validMove) {
            Color cardColor = getColorFromCardColor(card.getColor());
            cardText.setFill(cardColor);
        } else {
            cardText.setFill(Color.GRAY);
        }
        cardText.setFont(new Font(20.0));
        return cardText;
    }

    /**
     * Plays selected card
     *
     * @return true if played card is jack, false otherwise
     * @param cardId
     */
    public boolean submitAction(int cardId) {
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
        if (color == Card.COLOR.HEARTS || color == Card.COLOR.DIAMONDS) {
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
