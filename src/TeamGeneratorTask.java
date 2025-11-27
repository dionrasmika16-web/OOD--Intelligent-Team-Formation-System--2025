import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class TeamGeneratorTask implements Runnable {
    private final ArrayList<Player> playerArray;
    private final Map<String, Set<Player>> roleSets;
    private final Map<String, Set<Player>> personalitySets;
    private final Map<String, Set<Player>> gameSets;
    private final int teamSize;
    private final int teamId;
    private final TeamDataManager teamDataManager;


    public TeamGeneratorTask(ArrayList<Player> playerArray,
                             Map<String, Set<Player>> roleSets,
                             Map<String, Set<Player>> personalitySets,
                             Map<String, Set<Player>> gameSets,
                             int teamSize,
                             TeamDataManager teamDataManager,
                             int teamId) {
        this.playerArray = playerArray;
        this.roleSets = roleSets;
        this.personalitySets = personalitySets;
        this.gameSets = gameSets;
        this.teamSize = teamSize;
        this.teamDataManager = teamDataManager;
        this.teamId = teamId;
    }

    @Override
    public void run() {
        GenerateTeamHandler generateTeamHandler = new GenerateTeamHandler(
                playerArray, roleSets, personalitySets, gameSets, teamSize, teamId
        );
        synchronized (roleSets) {
            Team newTeam = generateTeamHandler.formTeam();

            synchronized (teamDataManager) {
                teamDataManager.addTeam(newTeam);
            }
        }
    }
}