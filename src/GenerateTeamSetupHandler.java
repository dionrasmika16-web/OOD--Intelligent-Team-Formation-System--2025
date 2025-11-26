import java.util.*;

public class GenerateTeamSetupHandler {
    ArrayList<Player> playerArray;
    int teamSize;
    TeamDataManager teamDataManager;

    //-------------------------------Features-------------------------------
    Map<String, Set<Player>> roleSets;
    Map<String, Set<Player>> personalitySets;
    Map<String, Set<Player>> gameSets;

    //-------------------------------Specific Features-----------------------
    Set<Player> attacker = new HashSet<>();
    Set<Player> defender = new HashSet<>();
    Set<Player> supporter = new HashSet<>();
    Set<Player> coordinator = new HashSet<>();
    Set<Player> strategist = new HashSet<>();

    Set<Player> leader = new HashSet<>();
    Set<Player> thinker = new HashSet<>();
    Set<Player> balanced = new HashSet<>();
    Set<Player> average = new HashSet<>();

    Set<Player> chess = new HashSet<>();
    Set<Player> fifa = new HashSet<>();
    Set<Player> basketball = new HashSet<>();
    Set<Player> dota2 = new HashSet<>();
    Set<Player> csgo = new HashSet<>();
    Set<Player> valorant = new HashSet<>();

    public GenerateTeamSetupHandler(ArrayList<Player> playerArray, int teamSize) {
        this.playerArray = playerArray;
        this.teamSize = teamSize;
        this.teamDataManager = new TeamDataManager();

        addPlayersToFeatureSets();
    }

    public void addPlayersToFeatureSets(){
        for (Player p : playerArray) {

            switch (p.getPreferredRole().toLowerCase()) {
                case "attacker": attacker.add(p); break;
                case "defender": defender.add(p); break;
                case "supporter": supporter.add(p); break;
                case "coordinator": coordinator.add(p); break;
                case "strategist": strategist.add(p); break;
            }

            switch (p.getPersonalityType().toLowerCase()) {
                case "leader": leader.add(p); break;
                case "thinker": thinker.add(p); break;
                case "balanced": balanced.add(p); break;
                case "average": average.add(p); break;
            }

            switch (p.getPreferredGame().toLowerCase()) {
                case "fifa": fifa.add(p); break;
                case "basketball": basketball.add(p); break;
                case "chess": chess.add(p); break;
                case "valorant": valorant.add(p); break;
                case "dota2": dota2.add(p); break;
                case "cs:go": csgo.add(p); break;
            }
        }

        roleSets = Map.of("attacker", attacker, "defender", defender, "supporter", supporter,
                "coordinator", coordinator, "strategist", strategist);

        personalitySets = Map.of("leader", leader, "thinker", thinker, "balanced", balanced, "average", average);

        gameSets = Map.of("fifa", fifa, "basketball", basketball, "chess", chess,
                "valorant", valorant, "dota2", dota2, "cs:go", csgo);
    }

    public void formTeam(){
        GenerateTeamHandler generateTeamHandler = new GenerateTeamHandler(playerArray, roleSets, personalitySets, gameSets, teamSize);
        generateTeamHandler.addFirstTeamPlayer();
        teamDataManager.addTeam(generateTeamHandler.fillTeam());
        teamDataManager.getTeamArray().get(0).viewTeam();
    }
}