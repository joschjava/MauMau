import java.util.ArrayList;
import java.util.List;

/**
 * Plays lots of games to evaluate strength of ai
 */
public class AiAnalyzer {

    public static void main(String[] args){
        int winsRandom = 0;
        int winsAdvRandom = 0;


        for(int i=0;i<10000;i++){
            int winner = simulateGame();
            if(winner == 0){
                winsRandom++;
            } else {
                winsAdvRandom++;
            }
        }

        System.out.println("Wins random: "+winsRandom);
        System.out.println("Wins advanced random: "+winsAdvRandom);
    }

    public static int simulateGame() {
        Game game = new Game(5);
        List<AI> ais = new ArrayList<>();
        ais.add(new RandomAI(game));
        ais.add(new RandomAI(game));
        ais.add(new RandomAI(game));
        ais.add(new RandomAI(game));
        ais.add(new AdvancedRandomAI(game));
        game.initGame(ais);
        while(!game.isGameFinished()){
            game.triggerNextPlayerAction();
        }
        Player winner = game.getPlayers().stream().filter(player -> player.getPlace() == 1).findFirst().get();
        return winner.getPlayerId();
    }
}
