import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
abstract class AI {

    Game game;
    String aiName = "Unnamed AI";

    AI(Game game){
        this.game = game;
    }

    List<Card> getPossiblePlayableCards(List<Card> handCards){
        List<Card> possibleCards = new ArrayList<>();
        for(int i=0;i< handCards.size();i++){
            Card curCard = handCards.get(i);
            if(game.isValidMove(curCard)){
                possibleCards.add(curCard);
            }
        }
        return possibleCards;
    }

    abstract CardAction makeMove(List<Card> handCards);

    public String getAiName(){
        return aiName;
    }
}
