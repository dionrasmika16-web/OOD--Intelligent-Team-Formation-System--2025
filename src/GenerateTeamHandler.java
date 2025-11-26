import java.util.*;

public class GenerateTeamHandler {
    Map<String, Set<Player>> roleSets;
    Map<String, Set<Player>> personalitySets;
    Map<String, Set<Player>> gameSets;
    ArrayList<Player> playerArray;
    int teamSize;
    Team team;
    Random rand = new Random();
    //int thinkerCount = 0; //

    public GenerateTeamHandler(ArrayList<Player> playerArray, Map<String, Set<Player>> roleSets,Map<String, Set<Player>> personalitySets,Map<String, Set<Player>> gameSets, int teamSize) {
        this.playerArray = playerArray;
        this.roleSets = roleSets;
        this.personalitySets = personalitySets;
        this.gameSets = gameSets;
        this.teamSize = teamSize;
        team = new Team(teamSize);

    }

    public void addFirstTeamPlayer(){
        // Prioritize Leader, then Thinker, Balanced, Average
        Player firstPlayer = null;

        if (!personalitySets.get("leader").isEmpty()) {
            firstPlayer = personalitySets.get("leader").iterator().next();
            personalitySets.get("leader").remove(firstPlayer);
        }
        else if (!personalitySets.get("thinker").isEmpty()) {
            firstPlayer = personalitySets.get("thinker").iterator().next();
            personalitySets.get("thinker").remove(firstPlayer);
        }
        else if (!personalitySets.get("balanced").isEmpty()) {
            firstPlayer = personalitySets.get("balanced").iterator().next();
            personalitySets.get("balanced").remove(firstPlayer);
        }
        else if (!personalitySets.get("average").isEmpty()) {
            firstPlayer = personalitySets.get("average").iterator().next();
            personalitySets.get("average").remove(firstPlayer);
        }

        if (firstPlayer != null) {
            team.addPlayer(firstPlayer);
            // Increment count if the first player is a thinker
            if ("thinker".equalsIgnoreCase(firstPlayer.getPersonalityType()));
            removeFromAllSets(firstPlayer);
        }
    }

    public Team fillTeam(){
        // Identify if a Leader was selected as the first player to enforce a one-leader rule
        boolean hasLeader = team.getTeamList().stream()
                .anyMatch(p -> "leader".equalsIgnoreCase(p.getPersonalityType()));

        while (team.getTeamList().size() < teamSize) {
            HashMap<String, ArrayList<String>> ideal = team.idealFeatures();
            ArrayList<String> personalityFeatures = new ArrayList<>(ideal.get("personality"));

            // If the team already has a leader, remove 'leader' from the list of desired personality features.
            if (hasLeader) {
                personalityFeatures.remove("leader");
            }




            Set<Player> rolesSet = getUnionOfSets2(ideal.get("roles"),  roleSets );
            Set<Player> personalitySet = getUnionOfSets2(personalityFeatures,  personalitySets );
            Set<Player> gameSet = getUnionOfSets2(ideal.get("games"), gameSets);

            // --- Intersection  3 Selection Logic ---
            Set<Player> candidates = new HashSet<>(rolesSet);
            candidates.retainAll(personalitySet);
            candidates.retainAll(gameSet);

            // Intersection 2 Selection logic
            if (candidates.isEmpty()) {
                // two-way intersection
                Set<Player> temp;
                temp = new HashSet<>(rolesSet); temp.retainAll(personalitySet); if (!temp.isEmpty()) candidates.addAll(temp);
                temp = new HashSet<>(rolesSet); temp.retainAll(gameSet); if (!temp.isEmpty()) candidates.addAll(temp);
                temp = new HashSet<>(personalitySet); temp.retainAll(gameSet); if (!temp.isEmpty()) candidates.addAll(temp);
            }
            //intersection 2 Selection logic
            if (candidates.isEmpty()) {
                candidates.addAll(rolesSet); candidates.addAll(personalitySet); candidates.addAll(gameSet);
            }

            // --- Skill Balancing Filter ---
            double teamAverage = team.getTeamList().isEmpty()
                    ? 0 : team.getSkillLevel() / (double) team.getTeamList().size();

            double globalAverage = playerArray.stream()
                    .mapToInt(Player::getSkillLevel)
                    .average()
                    .orElse(0);

            List<Player> candidateList = new ArrayList<>(candidates);

            // If team is already stronger than global average by more than 2,
            // remove players who are above global average.
            boolean teamTooStrong = teamAverage > globalAverage + 2;
            candidateList.removeIf(p -> teamTooStrong && p.getSkillLevel() > globalAverage);

            if (candidateList.isEmpty()) candidateList = new ArrayList<>(candidates);

            if (candidateList.isEmpty()) {
                break;
            }

            Player selected = candidateList.get(rand.nextInt(candidateList.size()));

            team.addPlayer(selected);
            //if ("thinker".equalsIgnoreCase(selected.getPersonalityType())) thinkerCount++;
            removeFromAllSets(selected);
        }
        team.viewTeam();
        return team;
    }

    // FIX: Removes the player from their specific set in each category (Role, Personality, Game)
    public void removeFromAllSets(Player player) {

        // Remove from Role Sets
        String role = player.getPreferredRole().toLowerCase();
        Set<Player> roleSet = roleSets.get(role);
        if (roleSet != null){
            roleSet.remove(player);
        }

        // Remove from Personality Sets
        String personality = player.getPersonalityType().toLowerCase();
        Set<Player> personalitySet = personalitySets.get(personality);
        if (personalitySet != null){
            personalitySet.remove(player);
        }

        // Remove from Game Sets
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
            s = CategoryPlayerSets.get("cs:go");
            if (s != null) result.addAll(s);
        }
        return result;
    }
}