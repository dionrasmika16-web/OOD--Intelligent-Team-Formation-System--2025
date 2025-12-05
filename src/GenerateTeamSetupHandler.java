import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class GenerateTeamSetupHandler extends AbstractWorkFlow<TeamDataManager>{

    // ------------------- Team ID Management ----------------------
    private static final Object ID_LOCK = new Object();
    private static int nextTeamId = 1;
    // -------------------------------------------------------------

    private ArrayList<Player> playerArray;
    private int teamSize;
    private final TeamDataManager teamDataManager;
    private int numberOfTeams;

    //-------------------------------Features-------------------------------
    private Map<String, Set<Player>> roleSets;
    private Map<String, Set<Player>> personalitySets;
    private Map<String, Set<Player>> gameSets;

    //-------------------------------Specific Features(Sets that are going to be used for feature selection using Intersection Function )-----------------------
    private Set<Player> attacker = new HashSet<>();
    private Set<Player> defender = new HashSet<>();
    private Set<Player> supporter = new HashSet<>();
    private Set<Player> coordinator = new HashSet<>();
    private Set<Player> strategist = new HashSet<>();

    private Set<Player> leader = new HashSet<>();
    private Set<Player> thinker = new HashSet<>();
    private Set<Player> balanced = new HashSet<>();
    private Set<Player> average = new HashSet<>();

    private Set<Player> chess = new HashSet<>();
    private Set<Player> fifa = new HashSet<>();
    private  Set<Player> basketball = new HashSet<>();
    private Set<Player> dota2 = new HashSet<>();
    private Set<Player> csgo = new HashSet<>();
    private Set<Player> valorant = new HashSet<>();



    public static int getNextTeamId() {
        synchronized (ID_LOCK) {
            return nextTeamId++;
        }
    }

    @Override
    protected void process(){
        this.result= formMultipleTeams();

    }

    @Override
    public void save(TeamFileHandler teamFileHandler) {
        teamFileHandler.useSetFileOption(teamDataManager.getTeamArray());

    }

    public void save(PlayerFileHandler playerFileHandler) {
        //empty
    }


    public GenerateTeamSetupHandler(ArrayList<Player> playerArray,int teamSize,int numberOfTeams) {
        this.playerArray = playerArray;
        this.teamSize = teamSize;
        this.teamDataManager = new TeamDataManager();
        this.numberOfTeams = numberOfTeams;
        nextTeamId=1;

        addPlayersToFeatureSets();
    }

    public TeamDataManager getTeamDataManager(){
        return teamDataManager;
    }

    public void addPlayersToFeatureSets(){
        // adds players to their respective features
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


    public TeamDataManager formMultipleTeams() {
        // Create a fixed size thread pool
        int poolSize = Math.min(numberOfTeams, Runtime.getRuntime().availableProcessors());
        ExecutorService executor = Executors.newFixedThreadPool(poolSize);
        List<Future<?>> futures = new ArrayList<>();

        for (int i = 0; i < numberOfTeams; i++) {
            // Get the unique id before creating the task
            final int teamId = GenerateTeamSetupHandler.getNextTeamId();

            // Create a task for each team
            Runnable task = new TeamGeneratorTask(
                    this.playerArray,
                    this.roleSets,
                    this.personalitySets,
                    this.gameSets,
                    this.teamSize,
                    this.teamDataManager,
                    teamId
            );

            // Submit the task to the executor
            Future<?> future = executor.submit(task);
            futures.add(future);
        }

        // waits for all tasks to complete
        for (Future<?> future : futures) {
            try {
                future.get(); // blocks until the task is complete
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        // closes the executor service
        executor.shutdown();
        return teamDataManager;
    }
}
