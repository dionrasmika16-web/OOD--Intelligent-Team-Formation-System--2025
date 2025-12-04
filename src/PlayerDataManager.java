import java.util.*;

public class PlayerDataManager {

    private ArrayList<Player> Surveyplayers = new ArrayList<>();
    private ArrayList<Player> Fileplayers = new ArrayList<>();
    private ArrayList<Player> AllPlayers = new ArrayList<>();
    private int surveyIdCounter = 1;

    public void addSurveyPlayer(Player player) {
        Surveyplayers.add(player);
        AllPlayers.add(player);
        surveyIdCounter++;
    }

    public void addFilePlayers(ArrayList<Player> player) {
        Fileplayers.addAll(player);
        AllPlayers.addAll(player);
        surveyIdCounter++;
    }

    public ArrayList<Player> getSurveyplayers() {
        return Surveyplayers;
    }

    public ArrayList<Player> getFileplayers() {
        return Fileplayers;
    }

    public ArrayList<Player> getAllPlayers() {
        return AllPlayers;
    }

    public int getNextId() {
        return surveyIdCounter;
    }

    public void clearAllPlayers() {
        AllPlayers.clear();
        Fileplayers.clear();
        Surveyplayers.clear();
    }
}
