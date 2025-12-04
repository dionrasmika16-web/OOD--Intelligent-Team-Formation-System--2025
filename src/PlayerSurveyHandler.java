import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class PlayerSurveyHandler extends AbstractWorkFlow<Player>{

    private Scanner input = new Scanner(System.in);
    private int nextId=001;


    //WorkFlow Template Methods

    @Override
    protected  void process(){
        this.result= conductSurvey();

    }

    @Override
    public void save(PlayerFileHandler playerFileHandler) {
        ArrayList<Player> surveylist = new ArrayList<>();
        surveylist.add(result);
        playerFileHandler.useSetFileOption(surveylist);
    }

    public void save(TeamFileHandler teamFileHandler) {
        //Empty
    }




    // --- Validation Methods---
    private int getValidatedInt(String question, int min, int max) {
        //Checks whether in Range
        while (true) {
            try {
                System.out.print(question);
                int value = Integer.parseInt(input.nextLine());

                if (value < min || value > max) {
                    System.out.println("Input must be between " + min + " and " + max);
                } else {
                    return value;
                }

            } catch (NumberFormatException e) {
                System.out.println("Enter a number!: ");
            }
        }
    }

    private String getNonEmptyString(String question) {
        //Checks for empty inputs
        while (true) {
            System.out.print(question);
            String value = input.nextLine().trim();
            if (!value.isEmpty()) return value;

            System.out.println("Input cannot be empty!");
        }
    }

    private String getEmail(String question) {
        while (true) {
            System.out.print(question);
            String value = input.nextLine().trim();

            //Checks whether @ and . present in string
            if (value.contains("@") && value.contains(".")) return value;

            System.out.println("Invalid email format!");
        }
    }

    private String getValidatedChoice(String question, List<String> validOptions) {
        while (true) {
            System.out.print(question);
            String value = input.nextLine().trim();

            if (validOptions.contains(value.toUpperCase())) return value;

            System.out.println("Invalid option! Choose: " + validOptions);
        }
    }

    // --------Survey------
    public Player conductSurvey() {
        //Questions validated with earlier validation Methods

        System.out.println("----- Player Registration -----");

        //General user Profile Question
        String name = getNonEmptyString("Enter your Name:");
        String email = getEmail("Enter your Email:");
        int skillLevel = getValidatedInt("Enter your Skill Level (1–10):", 1, 10);

        System.out.println("Take the Personality Test!");

        //Personality Question
        int q1 = getValidatedInt("1. I enjoy taking leadership: ", 1, 5);
        int q2 = getValidatedInt("2. I prefer strategy thinking: ", 1, 5);
        int q3 = getValidatedInt("3. I enjoy teamwork: ", 1, 5);
        int q4 = getValidatedInt("4. I stay calm under pressure: ", 1, 5);
        int q5 = getValidatedInt("5. I decide quickly in dynamic situations: ", 1, 5);

        int total = (q1 + q2 + q3 + q4 + q5) * 4;

        //Personality Classification
        String personalityType;
        if (total >= 90) personalityType = "Leader";
        else if (total >= 70) personalityType = "Balanced";
        else if (total >= 50) personalityType = "Thinker";
        else personalityType = "Average";

        //Preferred Roles and Games
        List<String> roles = Arrays.asList("STRATEGIST", "ATTACKER", "DEFENDER", "SUPPORTER", "COORDINATOR");
        String preferredRole = getValidatedChoice("Enter Preferred Role: " + roles +" ", roles);

        List<String> games = Arrays.asList("VALORANT", "DOTA2", "FIFA", "BASKETBALL", "BADMINTON", "CHESS");
        String preferredGame = getValidatedChoice("Enter Preferred Game: " + games +" ", games);

        // create player object
        return new Player("p"+ ++nextId, name, email, preferredGame, skillLevel, preferredRole, total, personalityType);
    }

}
