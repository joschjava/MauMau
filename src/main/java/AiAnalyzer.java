import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Plays lots of games to evaluate strength of ai
 */
public class AiAnalyzer {

    public static void main(String[] args){
        AiAnalyzer analyzer = new AiAnalyzer();
        int winsRandom = 0;
        int winsAdvRandom = 0;

        int totalRoundsPlayed = 0;
        int numSimulatedGames = 500;
        int quickestGame = Integer.MAX_VALUE;
        int longestGame = Integer.MIN_VALUE;

        List<Integer> roundLengths = new ArrayList<>();

        for(int i=0;i<numSimulatedGames;i++){
            GameStats gameStats = analyzer.simulateGame();
            int numberRounds = gameStats.getNumberRounds();
            roundLengths.add(numberRounds);
            totalRoundsPlayed += numberRounds;
            if(numberRounds > longestGame){
                longestGame = numberRounds;
            }
            if(numberRounds < quickestGame){
                quickestGame = numberRounds;
            }
            int winner = gameStats.getWinner();
            if(winner == 0){
                winsRandom++;
            } else {
                winsAdvRandom++;
            }
        }

        System.out.println("Wins random: "+winsRandom);
        System.out.println("Wins advanced random: "+winsAdvRandom);
        double averageGameLength = (double)totalRoundsPlayed/(double)numSimulatedGames;
        System.out.println(String.format("Average game length: %.2f", averageGameLength));
        System.out.println("Quickest Game: "+quickestGame);
        System.out.println("Longest Game: "+longestGame);
        StringBuilder sb = new StringBuilder();
        roundLengths.forEach(integer -> sb.append(integer).append(","));
        try {
            File file = new File("results.txt");
            FileUtils.writeStringToFile(file, sb.toString(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public GameStats simulateGame() {
        Game game = new Game(2);
        List<AI> ais = new ArrayList<>();
        ais.add(new RandomAI(game));
        ais.add(new AdvancedRandomAI(game));
        game.initGame(ais);
        while(!game.isGameFinished()){
            game.triggerNextPlayerAction();
        }
        Player winner = game.getPlayers().stream().filter(player -> player.getPlace() == 1).findFirst().get();
        return new GameStats(winner.getPlayerId(), game.getPlayedRounds());
    }

    @Data
    @AllArgsConstructor
    class GameStats {
        private int winner;
        private int numberRounds;
    }

}
