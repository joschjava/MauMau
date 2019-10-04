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

    Game game;

    private CardAction cardAction;

    @FXML
    public void initialize() {
        btSubmit.setOnAction(event -> {
            setNextPlayerButtonAction();
        });
        btPass.setOnAction(event -> {
            passAction();
            updateGui();
            setDelayedNextPlayer();
        });
        tfInput.setOnKeyPressed(ae -> {
            if (ae.getCode().equals(KeyCode.ENTER)) {
                String text = tfInput.getText();
                if(text.equals("d")){
                    game.hasPlayerPlayableCards(true);
                    tfInput.setText("");
                } else {
                    if(text.equals("")){
                        game.setNextPlayer();
                        updateGui();
                    } else {
                        setNextPlayerButtonAction();
                    }
                }
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
        ais.add(null);
        ais.add(new RandomAI(game));
        ais.add(new RandomAI(game));
        game.initGame(ais);
        hbJackPickerBox.setVisible(false);
        updateGui();
    }

    private void setNextPlayerButtonAction() {
        submitAction();
        updateGui();
        setDelayedNextPlayer();
    }

    public void setDelayedNextPlayer(){
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(2500),
                ae -> {
                    game.triggerNextPlayerAction();
                    updateGui();
                    if(game.getCurrentPlayer().isAi()){
                        setDelayedNextPlayer();
                    }
                }));
        timeline.play();
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

        for (int plId = 0; plId < players.size(); plId++) {
            Player player = players.get(plId);
            List<Card> handCards = player.getHandCards();
            TextFlow tFlPlayer = new TextFlow();
            Text playerIdText = new Text("Player " + plId + " (" + handCards.size() + ")\n");
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
        Card stapelCard = game.getTopStapelCard();
        if (stapelCard.getColor() == Card.COLOR.HERZ || stapelCard.getColor() == Card.COLOR.CARO) {
            lbStapel.setTextFill(Color.ORANGE);
        } else {
            lbStapel.setTextFill(Color.BLACK);
        }
        lbStapel.setText(stapelCard.toPrettyString());
        tfInput.requestFocus();
    }

    private Text createColoredText(Card card, int i, boolean validMove) {
        Text cardText = new Text(i + ": " + card.toPrettyString() + "    ");
        if (validMove) {
            if (card.getColor() == Card.COLOR.HERZ || card.getColor() == Card.COLOR.CARO) {
                cardText.setFill(Color.ORANGE);
            }
        } else {
            cardText.setFill(Color.GRAY);
        }
        return cardText;
    }

    public void submitAction() {
        String text = tfInput.getText();
        tfInput.setText("");
        int cardId = Integer.valueOf(text);
        Player currentPlayer = game.getCurrentPlayer();
        Card card = currentPlayer.getCardFromId(cardId);

        cardAction = new CardAction(card);
        if (cardAction.isJack()) {
            hbJackPickerBox.setVisible(true);
        } else {
            currentPlayer.playCard(cardAction);
            cardAction = null;
        }
    }

    public void wishColor(Card.COLOR color) {
        if (cardAction != null) {
            cardAction.setJackWishColor(color);
            game.getCurrentPlayer().playCard(cardAction);
        } else {
            throw new RuntimeException("Couldn't find any cardAction for my Jack");
        }
        hbJackPickerBox.setVisible(false);
    }

    public void passAction() {
        Player currentPlayer = game.getCurrentPlayer();
        currentPlayer.pass();
    }

}
