public class Player extends Person {     //User specific class
    private String preferredGame;
    private int skillLevel;
    private String preferredRole;
    private int personalityScore;
    private String personalityType;

    public Player(String id, String name, String email, String preferredGame, int skillLevel, String preferredRole, int personalityScore, String personalityType) {
        super(id,name,email);
        this.preferredGame = preferredGame;
        this.skillLevel = skillLevel;
        this.preferredRole = preferredRole;
        this.personalityScore = personalityScore;
        this.personalityType = personalityType;
    }

    public String getPreferredGame() {
        return preferredGame;
    }
    public void setPreferredGame(String preferredGame) {
        this.preferredGame = preferredGame;
    }
    public int getSkillLevel() {
        return skillLevel;
    }
    public void setSkillLevel(int skillLevel) {
        this.skillLevel = skillLevel;
    }
    public String getPreferredRole() {
        return preferredRole;
    }
    public void setPreferredRole(String preferredRole) {
        this.preferredRole = preferredRole;
    }
    public int getPersonalityScore() {
        return personalityScore;
    }
    public void setPersonalityScore(int personalityScore) {
        this.personalityScore = personalityScore;
    }
    public String getPersonalityType() {
        return personalityType;
    }

}
