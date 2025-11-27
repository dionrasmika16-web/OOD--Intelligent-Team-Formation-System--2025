import java.util.ArrayList;
import java.util.HashMap;

public class Team {
    int id;
    int teamSize;
    int maxCap;
    int skillOverall = 0;

    String[] Roles = {"attacker", "defender", "coordinator", "supporter", "strategist"};
    String[] PersonalityTypes = {"leader", "thinker", "balanced", "Average"};
    String[] games = {"chess", "valorant", "basketball", "dota2", "fifa", "cs:go"};

    ArrayList<Player> teamList;
    HashMap<String, Integer> preferredGames;
    HashMap<String, Integer> personalityType;
    public HashMap<String, Integer> teamRoles;

    public Team(int teamSize,int id) {
        this.id = id;
        this.teamSize = teamSize;
        maxCap = teamSize / 5;

        teamList = new ArrayList<>();
        preferredGames = new HashMap<>();
        personalityType = new HashMap<>();
        teamRoles = new HashMap<>();
    }
    public Team(){};

    public void addPlayer(Player player) {
        teamList.add(player);

        // Update preferredGames
        preferredGames.put(player.getPreferredGame(),
                preferredGames.getOrDefault(player.getPreferredGame(), 0) + 1);

        // Update personalityType
        personalityType.put(player.getPersonalityType(),
                personalityType.getOrDefault(player.getPersonalityType(), 0) + 1);

        // Update teamRoles
        teamRoles.put(player.getPreferredRole(),
                teamRoles.getOrDefault(player.getPreferredRole(), 0) + 1);

        skillOverall += player.getSkillLevel();
        //System.out.println(preferredGames+"---"+personalityType+"---"+teamRoles);
    }

    public HashMap<String, ArrayList<String>> idealFeatures() {
        //System.out.println(preferredGames+"---"+personalityType+"---"+teamRoles);
        HashMap<String, ArrayList<String>> idealTypes = new HashMap<>();
        ArrayList<String> gameArray = new ArrayList<>();
        ArrayList<String> personalityTypeArray = new ArrayList<>();
        ArrayList<String> roleArray = new ArrayList<>();

        // Games
        for (String game : games) {
            int count = preferredGames.getOrDefault(game, 0);
            if (count < maxCap) {
                gameArray.add(game);
            }
        }

        // Roles
        for (String role : Roles) {
            int count = teamRoles.getOrDefault(role, 0);
            //System.out.println(count+"---"+maxCap);
            if (count < maxCap) {
                roleArray.add(role);
            }
        }

        // Personality Types
        for (String type : PersonalityTypes) {
            //System.out.println("-------------"+personalityType.getOrDefault(type,0)+"---"+personalityType);
            int count = personalityType.getOrDefault(type, 0);

            if (type.equals("leader") && count !=1) {
               // System.out.println("test leader---"+count);
                personalityTypeArray.add(type);
            }
            if (type.equals("thinker") && count < 2) {
                personalityTypeArray.add(type);
                //break;
            }
            if (type.equals("balanced") || type.equals("Average")) {
                personalityTypeArray.add(type);
            }
        }

        idealTypes.put("roles", roleArray);
        idealTypes.put("personality", personalityTypeArray);
        idealTypes.put("games", gameArray);

        return idealTypes;
    }


    public ArrayList<Player> getTeamList() {
        return teamList;
    }

    public void setTeamList(ArrayList<Player> teamList) {
        this.teamList = teamList;
    }

    public int getSkillLevel() {
        return skillOverall;
    }

    public HashMap<String, Integer> getTeamPreferredGames() {
        return preferredGames;
    }

    public HashMap<String, Integer> getTeamPersonality() {
        return personalityType;
    }

    public HashMap<String, Integer> getTeamRoles() {
        return teamRoles;
    }

    public int getId() {
        return id;
    }
    public void setid(int id) {
        this.id = id;
    }
    public  int getTeamSize() {
        return teamSize;
    }

    public void viewTeam() {
        for (Player player : teamList) {
            System.out.println(this.id+"------------"+player.getId() + " ----- " + player.getName() + " ----- " + player.getPersonalityType() + " ----- " + player.getPreferredRole() + " ----- " + player.getPreferredGame()+ " ----- " + skillOverall);
        }
    }
}
