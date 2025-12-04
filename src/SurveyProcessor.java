import java.util.ArrayList;

public class SurveyProcessor implements Runnable {

    private final PlayerSurveyHandler surveyHandler;
    private final PlayerDataManager playerDataManager;
    private final PlayerFileHandler playerFileHandler;

    public SurveyProcessor(PlayerSurveyHandler surveyHandler, PlayerDataManager dataManager,PlayerFileHandler playerFileHandler) {
        this.surveyHandler = surveyHandler;
        this.playerDataManager = dataManager;
        this.playerFileHandler = playerFileHandler;


    }

    @Override
    public void run() {
        //process survey
        Player surveyPlayer = surveyHandler.startWorkFlow(playerFileHandler);
        //save to File using the synchronized method
        playerDataManager.addSurveyPlayer(surveyPlayer);


    }
}