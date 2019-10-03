import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
        setJackChooserButtonListener(btPik, Card.COLOR.PIK);
        setJackChooserButtonListener(btHerz, Card.COLOR.HERZ);
        setJackChooserButtonListener(btCaro, Card.COLOR.CARO);
        setJackChooserButtonListener(btKreuz, Card.COLOR.KREUZ);

        game = new Game(3);
        game.initGame();
        Card card = new Card(Card.COLOR.HERZ, Card.JACK);
        game.getPlayers().get(0).addCardToHand(card);
        hbJackPickerBox.setVisible(false);
        updateGui();
    }

    public void setJackChooserButtonListener(Button button, Card.COLOR color){
        button.setOnAction(event -> {
            wishColor(color);
            updateGui();
        });
    }

    public void updateGui() {
        int currentPlayerId = game.getCurrentPlayerId();
        vBplayerDisplay.getChildren().clear();
        List<Player> players = game.getPlayers();
        for (int plId = 0; plId < players.size(); plId++) {
            List<Card> handCards = players.get(plId).getHandCards();
            TextFlow tFlPlayer = new TextFlow();
            Text playerIdText = new Text("Player " + plId + "\n");
            tFlPlayer.getChildren().add(playerIdText);
            for (int i = 0; i < handCards.size(); i++) {
                Card card = handCards.get(i);
                Text cardText = new Text(i + ": " + card.toPrettyString() + "  ");
                if (card.getColor() == Card.COLOR.HERZ || card.getColor() == Card.COLOR.CARO) {
                    cardText.setFill(Color.RED);
                }
                if (plId == currentPlayerId) {
                    tFlPlayer.setBackground(
                            new Background(
                                    new BackgroundFill(
                                            Color.web("#9281ED"),
                                            CornerRadii.EMPTY,
                                            Insets.EMPTY)
                            ));
                } else {

                }
                tFlPlayer.getChildren().add(cardText);
            }
            vBplayerDisplay.getChildren().add(tFlPlayer);
        }
        Card stapelCard = game.getTopStapelCard();
        if (stapelCard.getColor() == Card.COLOR.HERZ || stapelCard.getColor() == Card.COLOR.CARO) {
            lbStapel.setTextFill(Color.RED);
        } else {
            lbStapel.setTextFill(Color.BLACK);
        }
        lbStapel.setText(stapelCard.toPrettyString());
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
