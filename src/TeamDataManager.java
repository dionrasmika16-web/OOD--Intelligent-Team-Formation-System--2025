import java.util.ArrayList;
import java.util.Comparator;

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

    public void viewTeam(){
        teamArray.sort(Comparator.comparingInt(Team::getId));

        //-----------------Team View-----------------

        for (Team team : teamArray) {
            System.out.println('┌'+"─".repeat(51)+'┬'+"─".repeat(61)+"┬"+"─".repeat(51)+'┐');
            System.out.println('│'+center("Team ID: "+team.getId(),51)+'│'+center("Team Size: "+ team.teamSize,61)+'│'+center("Team Skill: "+ team.skillOverall,51)+'│');
            System.out.println('├'+"─".repeat(20)+'┬'+"─".repeat(30)+'┼'+"─".repeat(30)+"┬"+"─".repeat(30)+"┼"+"─".repeat(30)+"┬"+"─".repeat(20)+'┤');
            System.out.println('│'+center("Player ID : ",20)+'│'+center("Player Name: ",30)+'│'+center("Player Personality Type : ",30)+'│'+center("Player Preferred Game: ",30)+'│'+center("Player Preferred Role : ",30)+'│'+center("Player Skill: ",20)+'│');
            for(Player player : team.getTeamList()){
                System.out.println('│'+"─".repeat(20)+'┼'+"─".repeat(30)+'┼'+"─".repeat(30)+'┼'+"─".repeat(30)+'┼'+"─".repeat(30)+"─".repeat(21)+'│');
                System.out.println('│'+center(player.getId(),20)+'│'+center(player.getName(),30)+"│"+center(player.getPersonalityType(),30)+'│'+center(player.getPreferredGame(),30)+"│"+center(player.getPreferredRole(),30)+'│'+center(String.valueOf(player.getSkillLevel()),20)+'│');
            }
            System.out.println('└'+"─".repeat(20)+'┴'+"─".repeat(30)+'┴'+"─".repeat(30)+'┴'+"─".repeat(30)+'┴'+"─".repeat(30)+"─".repeat(21)+'┘');
            System.out.println();
            System.out.println();


        }




    }
    public  String center(String text, int width) {
        if (text == null || width <= text.length()) {
            return text;
        }
        int padding = width - text.length();
        int left = padding / 2;
        int right = padding - left;
        return " ".repeat(left) + text + " ".repeat(right);
    }
}
