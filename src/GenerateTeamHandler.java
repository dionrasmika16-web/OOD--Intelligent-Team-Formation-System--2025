import java.util.*;

public class GenerateTeamHandler {
    Map<String, Set<Player>> roleSets;
    Map<String, Set<Player>> personalitySets;
    Map<String, Set<Player>> gameSets;
    ArrayList<Player> playerArray;
    int teamSize;
    Team team;
    Random rand = new Random();

    public GenerateTeamHandler(ArrayList<Player> playerArray,
                               Map<String, Set<Player>> roleSets,
                               Map<String, Set<Player>> personalitySets,
                               Map<String, Set<Player>> gameSets,
                               int teamSize,
                               int teamId) {
        this.playerArray = playerArray;
        this.roleSets = roleSets;
        this.personalitySets = personalitySets;
        this.gameSets = gameSets;
        this.teamSize = teamSize;

        team = new Team(teamSize, teamId);
    }


    public Team formTeam(){
        //Add First Team Player (extracted from old addFirstTeamPlayer logic)
        Player firstPlayer = null;

        if (!personalitySets.get("leader").isEmpty()) {
            List<Player> leaderList = new ArrayList<>(personalitySets.get("leader"));
            firstPlayer = leaderList.get(new Random().nextInt(leaderList.size()));
            personalitySets.get("leader").remove(firstPlayer);
        }
        else if (!personalitySets.get("thinker").isEmpty()) {
            List<Player> thinkerList = new ArrayList<>(personalitySets.get("thinker"));
            firstPlayer = thinkerList.get(new Random().nextInt(thinkerList.size()));
            personalitySets.get("thinker").remove(firstPlayer);
        }
        else if (!personalitySets.get("balanced").isEmpty()) {
            List<Player> balancedList = new ArrayList<>(personalitySets.get("balanced"));
            firstPlayer = balancedList.get(new Random().nextInt(balancedList.size()));
            personalitySets.get("balanced").remove(firstPlayer);
        }
        else if (!personalitySets.get("average").isEmpty()) {
            List<Player> averageList = new ArrayList<>(personalitySets.get("average"));
            firstPlayer = averageList.get(new Random().nextInt(averageList.size()));
            personalitySets.get("average").remove(firstPlayer);
        }

        if (firstPlayer != null) {
            team.addPlayer(firstPlayer);
            if ("thinker".equalsIgnoreCase(firstPlayer.getPersonalityType()));
            removeFromAllSets(firstPlayer);
            playerArray.remove(firstPlayer);
        }

        //Fill the rest of the Team

        int thinkerCount;
        boolean hasLeader = team.getTeamList().stream()
                .anyMatch(p -> "leader".equalsIgnoreCase(p.getPersonalityType()));

        while (team.getTeamList().size() < teamSize) {
            HashMap<String, ArrayList<String>> ideal = team.idealFeatures();
            thinkerCount = Collections.frequency(ideal.get("personality"), "thinker");

            Set<Player> rolesSet = getUnionOfSets2(ideal.get("roles"),  roleSets );
            Set<Player> personalitySet = getUnionOfSets2(ideal.get("personality"),  personalitySets );
            Set<Player> gameSet = getUnionOfSets2(ideal.get("games"), gameSets);

            // --- Intersection Logic (3-way, 2-way, 1-way) ---
            Set<Player> candidates = new HashSet<>(rolesSet);
            candidates.retainAll(personalitySet);
            candidates.retainAll(gameSet);

            if (candidates.isEmpty()) {
                // 2-way intersection
                Set<Player> temp;
                temp = new HashSet<>(rolesSet); temp.retainAll(personalitySet);
                if (!temp.isEmpty()) candidates.addAll(temp);
                temp = new HashSet<>(rolesSet); temp.retainAll(gameSet);
                if (!temp.isEmpty()) candidates.addAll(temp);
                temp = new HashSet<>(personalitySet); temp.retainAll(gameSet);
                if (!temp.isEmpty()) candidates.addAll(temp);
            }

            if (candidates.isEmpty()) {
                // 1-way union
                candidates.addAll(rolesSet); candidates.addAll(personalitySet); candidates.addAll(gameSet);
            }

            // --- Personality Filters (Leader/Thinker) ---
            if (hasLeader) {
                Set<Player> nonLeaders = new HashSet<>();
                for (Player p : candidates) {
                    if (!"leader".equalsIgnoreCase(p.getPersonalityType())) {
                        nonLeaders.add(p);
                    }
                }
                if (!nonLeaders.isEmpty()) { candidates = nonLeaders; }
            } else {
                if (thinkerCount > 2) {
                    Set<Player> nonThinkers = new HashSet<>();
                    for (Player p : candidates) {
                        if (!"thinker".equalsIgnoreCase(p.getPersonalityType())) {
                            nonThinkers.add(p);
                        }
                    }
                    if (!nonThinkers.isEmpty()) { candidates = nonThinkers; }
                } else {
                    Set<Player> nonAverages = new HashSet<>();
                    for (Player p : candidates) {
                        if (!"average".equalsIgnoreCase(p.getPersonalityType()) || !"balanced".equalsIgnoreCase(p.getPersonalityType())) {
                            nonAverages.add(p);
                        }
                    }
                    if (!nonAverages.isEmpty()) { candidates = nonAverages; }
                }
            }

            // --- Skill Balancing Filter ---
            double teamAverage = team.getTeamList().isEmpty()
                    ? 0 : team.getSkillLevel() / (double) team.getTeamList().size();

            double globalAverage;

            globalAverage = playerArray.stream()
                    .mapToInt(Player::getSkillLevel)
                    .average()
                    .orElse(0);

            List<Player> candidateList = new ArrayList<>(candidates);

            boolean teamTooStrong = teamAverage > globalAverage + 2;
            candidateList.removeIf(p -> teamTooStrong && p.getSkillLevel() > globalAverage);

            if (candidateList.isEmpty()) candidateList = new ArrayList<>(candidates);

            if (candidateList.isEmpty()) {
                break;
            }

            Player selected = candidateList.get(rand.nextInt(candidateList.size()));

            team.addPlayer(selected);
            if ("thinker".equalsIgnoreCase(selected.getPersonalityType())) thinkerCount++;

            removeFromAllSets(selected);
            playerArray.remove(selected);
        }
        team.viewTeam();
        return team;
    }

    public void removeFromAllSets(Player player) {
        //Remove from Role Sets
        String role = player.getPreferredRole().toLowerCase();
        Set<Player> roleSet = roleSets.get(role);
        if (roleSet != null){
            roleSet.remove(player);
        }

        //Remove from Personality Sets
        String personality = player.getPersonalityType().toLowerCase();
        Set<Player> personalitySet = personalitySets.get(personality);
        if (personalitySet != null){
            personalitySet.remove(player);
        }

        //Remove from Game Sets
        String game = player.getPreferredGame().toLowerCase();
        String gameKey = game.equals("csgo") ? "cs:go" : game;
        Set<Player> gameSet = gameSets.get(gameKey);
        if (gameSet != null){
            gameSet.remove(player);
        }
    }

    public Set<Player> getUnionOfSets2(ArrayList<String> features, Map<String, Set<Player>> CategoryPlayerSets) {
        Set<Player> result = new HashSet<>();
        for (String f : features) {
            String key = f.toLowerCase();
            Set<Player> s;
            s = CategoryPlayerSets.get(f);
            if (s != null) result.addAll(s);
        }
        return result;
    }
}