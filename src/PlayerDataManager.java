import java.util.*;

public class PlayerDataManager {

    private List<Player> Surveyplayers = new ArrayList<>();
    private List<Player> Fileplayers = new ArrayList<>();
    private int surveyIdCounter = 1;

    public void addSurveyPlayer(Player player) {
        Surveyplayers.add(player);
        surveyIdCounter++;
    }

    public void addFilePlayers(List<Player> player) {
        Fileplayers.addAll(player);
        surveyIdCounter++;
    }

    public List<Player> getSurveyplayers() {
        return Surveyplayers;
    }

    public List<Player> getFileplayers() {
        return Fileplayers;
    }

    //public List<Player> getAllPlayers() {
        //return players;
    //}

    public int getNextId() {
        return surveyIdCounter;
    }
}
