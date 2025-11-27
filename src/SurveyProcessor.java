import java.util.ArrayList;

public class SurveyProcessor implements Runnable {

    private final PlayerSurveyHandler surveyHandler;
    private final PlayerFileHandler playerFileHandler;
    private final PlayerDataManager playerDataManager;

    public SurveyProcessor(PlayerSurveyHandler surveyHandler, PlayerFileHandler fileHandler, PlayerDataManager dataManager) {
        this.surveyHandler = surveyHandler;
        this.playerFileHandler = fileHandler;
        this.playerDataManager = dataManager;
    }

    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        System.out.println("\n[Thread: " + threadName + "] Starting player survey...");

        //process survey (blocks the current thread until completed)
        Player surveyPlayer = surveyHandler.conductSurvey();

        ArrayList<Player> surveylist = new ArrayList<>();
        surveylist.add(surveyPlayer);

        //save to File (uses the synchronized method)
        playerFileHandler.useSetFileOption(surveylist);

        playerDataManager.addSurveyPlayer(surveyPlayer);

        //System.out.println("[Thread: " + threadName + "] survey completed and data saved successfully!");
    }
}