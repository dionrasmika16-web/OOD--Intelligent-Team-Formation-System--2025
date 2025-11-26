import java.util.ArrayList;

public class TeamDataManager {
    private ArrayList<Team> teamArray= new ArrayList<>();


    public void addTeam(Team team){
        teamArray.add(team);
    }

    public void setTeamArray(ArrayList<Team>  teamArray){
        this.teamArray=teamArray;
    }

    public ArrayList<Team>  getTeamArray(){
        return teamArray;
    }
}
