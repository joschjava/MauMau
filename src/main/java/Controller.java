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

import java.util.List;

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

    Game game;

    @FXML
    public void initialize() {
        btSubmit.setOnAction(event -> {
            submitAction();
            updateGui();
        });
        btPass.setOnAction(event -> {
            passAction();
            updateGui();
        });
        tfInput.setOnKeyPressed(ae -> {
            if(ae.getCode().equals(KeyCode.ENTER)){
                submitAction();
                updateGui();
            }
        });
        setJackChooserButtonListener(btPik, Card.COLOR.PIK);
        setJackChooserButtonListener(btHerz, Card.COLOR.HERZ);
        setJackChooserButtonListener(btCaro, Card.COLOR.CARO);
        setJackChooserButtonListener(btKreuz, Card.COLOR.KREUZ);

        game = new Game(3);
        game.initGame();
        hbJackPickerBox.setVisible(false);
        updateGui();
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
            Text cardText = createColoredText(cards.get(i), i);
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
                    Text cardText = createColoredText(card, i);
                    if (plId == currentPlayerId) {
                        tFlPlayer.setBackground(
                                new Background(
                                        new BackgroundFill(
                                                Color.web("#9281ED"),
                                                CornerRadii.EMPTY,
                                                Insets.EMPTY)
                                ));
                    }
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
    }

    private Text createColoredText(Card card, int i) {
        Text cardText = new Text(i + ": " + card.toPrettyString() + "    ");
        if (card.getColor() == Card.COLOR.HERZ || card.getColor() == Card.COLOR.CARO) {
            cardText.setFill(Color.ORANGE);
        }
        return cardText;
    }

    public void submitAction() {
        String text = tfInput.getText();
        tfInput.setText("");
        tfInput.requestFocus();
        int cardId = Integer.valueOf(text);
        Player currentPlayer = game.getCurrentPlayer();
        boolean jackLaid = currentPlayer.playCardId(cardId);
        if (jackLaid) {
            hbJackPickerBox.setVisible(true);
        }
    }

    public void wishColor(Card.COLOR color) {
        game.setWishedColor(color);
        hbJackPickerBox.setVisible(false);
    }

    public void passAction() {
        Player currentPlayer = game.getCurrentPlayer();
        currentPlayer.pass();
    }

}
