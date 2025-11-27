import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Main main = new Main();
        final PlayerDataManager playerDataManager = new PlayerDataManager();

        //Enters default Participant file path Save Location and File Name;
        final PlayerFileHandler playerFileHandler = new PlayerFileHandler("Test", "C:\\Users\\Extra\\Desktop\\lastjavatut");
        final TeamFileHandler teamFileHandler=new TeamFileHandler("TeamSave", "C:\\Users\\Extra\\Desktop\\lastjavatut");
        TeamDataManager teamDataManager = new TeamDataManager();

        while (true) {
            System.out.println("--------------Intelligent Team Formation System-----------------------");
            System.out.println("│ " + "Complete Survey(CS)"
                    + " │ " + "Upload Players(UP)" + " │ "
                    + " │ " + "Form Teams(FT)" + " │ "
                    + " │ " + "View Teams(VT)" + " │ "
                    + " │ " + "Save File Settings(SFS)" + " │ "
                    + " │ " + "Exit Program(EXIT)" + " │ ");

            Scanner input = new Scanner(System.in);

            //Takes Validated Users Input
            String choice1= main.getNonEmptyString("Enter your choice: ");

            //Switch Cases for Options
            switch (choice1.toUpperCase()) {
                case "CS":
                    PlayerSurveyHandler playerSurveyHandler = new PlayerSurveyHandler();
                    ExecutorService executor = Executors.newSingleThreadExecutor();

                    // --- Execute Survey Concurrently ---
                    // 1. Create the task
                    Runnable surveyTask = new SurveyProcessor(
                            playerSurveyHandler,
                            playerFileHandler,
                            playerDataManager // Use the method you want to call for saving
                    );

                    // 2. Submit the task
                    executor.submit(surveyTask);

                    System.out.println("-- Main Thread ---");
                    System.out.println("Main thread is NOT blocked. It continues while user fills out survey.");


                    // 3. Shut down the executor
                    executor.shutdown();
                    try {
                        // Wait for the survey task to finish execution (up to 5 minutes)
                        if (!executor.awaitTermination(5, TimeUnit.MINUTES)) {
                            System.err.println("Executor timeout. Forcing shutdown.");
                            executor.shutdownNow();
                        }
                    } catch (InterruptedException e) {
                        executor.shutdownNow();
                        Thread.currentThread().interrupt();
                    }

                    System.out.println("\n--- Main Thread Finished ---");
                    break;

                case "UP":
                    String uploadFileLocation=playerFileHandler.getFileAddress();
                    playerDataManager.addFilePlayers(playerFileHandler.loadFromFile(uploadFileLocation));
                    System.out.println("File Successfully Uploaded!");
                    break;

                case "FT":
                    playerDataManager.addFilePlayers(playerFileHandler.loadFromFile("C:\\Users\\Extra\\Desktop\\CM1601 VIVA\\Starter pack\\Starter pack\\participants_sample.csv"));
                    if(!playerDataManager.getAllPlayers().isEmpty()){

                        boolean gotTeamSize=false;
                        int intTeamSize = 0;
                        int numberOfTeamsToMake=0;
                        while (!gotTeamSize) {
                            ArrayList<Integer> possibleTeamSizes=new ArrayList<>();
                            String teamSize = main.getNonEmptyString("Enter a Team Size:");
                            intTeamSize=Integer.parseInt(teamSize);
                            int numPlayers = playerDataManager.getAllPlayers().size();

                            if(intTeamSize<=0 ){
                                System.out.println("Team Size must be a positive integer!");
                            }
                            else if(numPlayers%intTeamSize!=0){
                                for (int i = 1; i <= numPlayers; i++) {
                                    if (numPlayers % i == 0) {
                                        possibleTeamSizes.add(i);
                                    }
                                }
                                System.out.println("Equal Size Teams Cannot be created!");
                                System.out.println("Here's a list of possible equal sized teams that can be created!: ");
                                for (Integer teamSize1 : possibleTeamSizes) {
                                    System.out.print(teamSize1+" ");
                                }
                                System.out.println();

                            }
                            else if(numPlayers%intTeamSize==0) {
                                numberOfTeamsToMake=playerDataManager.getAllPlayers().size() / intTeamSize;
                                gotTeamSize=true;

                            }
                        }

                        GenerateTeamSetupHandler setupHandler = new GenerateTeamSetupHandler(playerDataManager.getAllPlayers(), intTeamSize);

                        // 2. Execute Concurrent Generation
                        System.out.println("Starting concurrent team generation...");
                        setupHandler.formMultipleTeams(numberOfTeamsToMake);
                        System.out.println("Concurrent generation finished.");

                        // 3. Access the Data Manager and Retrieve Teams
                        TeamDataManager dataManager = setupHandler.teamDataManager;
                        teamDataManager=setupHandler.teamDataManager;
                        teamFileHandler.useSetFileOption(dataManager.getTeamArray());

                        //dataManager.viewTeam();

                    }else{
                        System.out.println("There are no players added to create Teams.");
                        System.out.println("Upload an existing player file or complete a survey!");
                        System.out.println();
                    }
                    break;

                case "VT":
                    System.out.println("-----------------Team View-----------------");
                    TeamDataManager viewTeamDataManager = new TeamDataManager();
                    viewTeamDataManager.setTeamArray(teamFileHandler.loadFromFile(teamFileHandler.getFileAddress()));
                    viewTeamDataManager.viewTeam();
                    break;


                case "SFS":
                    System.out.println("--------------Save File Settings-----------------------");
                    System.out.println("│ " + "Change Participant File Save(CPFS)" + " │ " + "Change Team File Save(CTFS)" + " │ ");

                    boolean saveFileChoice=false;
                    String sfsChoice="";
                    while (!saveFileChoice) {
                        sfsChoice = main.getNonEmptyString("Enter your Save File Setting choice: ");
                        if (sfsChoice.equalsIgnoreCase("CPFS") || sfsChoice.equalsIgnoreCase("CTFS")) {
                            saveFileChoice=true;
                        }else{
                            System.out.println("Invalid choice. Try again.");
                        }
                    }

                    switch (sfsChoice.toUpperCase()) {
                        case "CPFS":
                            System.out.println("--------------Change Participant File Save Settings-----------------------");
                            System.out.println("│ " + "Save To File(File)" + " │ " + "Save To Folder(Folder)" + " │ ");

                            boolean saveParticipantFileChoice=false;
                            String cpfssChoice="";
                            while (!saveParticipantFileChoice) {
                                cpfssChoice = main.getNonEmptyString("Enter your  Change Participant File Save Settings choice: ");
                                if (cpfssChoice.equalsIgnoreCase("FILE") || cpfssChoice.equalsIgnoreCase("FOLDER")) {
                                    saveParticipantFileChoice=true;
                                }else{
                                    System.out.println("Invalid choice. Try again.");
                                }
                            }


                            switch (cpfssChoice.toUpperCase()) {
                                case "FILE":
                                    playerFileHandler.setFileSaveLocation(playerFileHandler.getFileAddress());
                                    playerFileHandler.setSaveFile(true);
                                    System.out.println("File Save Address Successfully Changed!");
                                    break;
                                case "FOLDER":
                                    playerFileHandler.setFileSaveLocation(playerFileHandler.getFolderAddress());
                                    //System.out.println("Enter the name of the File to be saved as: ");

                                    String fileName1 = main.getNonEmptyString("Enter the name of the File to be saved as: ");

                                    playerFileHandler.setFileName(fileName1);
                                    System.out.println("File Save Address and File Name Successfully Changed!");
                                    break;
                                default:
                                    System.out.println();
                                    System.out.println("Enter a Given Option Only");
                                    System.out.println();
                                    break;
                            }
                             break;

                        case "CTFS":
                            System.out.println("--------------Change Team File Save Settings-----------------------");
                            System.out.println("│ " + "Save To File(File)" + " │ " + "Save To Folder(Folder)" + " │ ");

                            boolean saveTeamFileChoice=false;
                            String ctfssChoice="";
                            while (!saveTeamFileChoice) {
                                ctfssChoice = main.getNonEmptyString("Enter your  Change Participant File Save Settings choice: ");
                                if (ctfssChoice.equalsIgnoreCase("FILE") || ctfssChoice.equalsIgnoreCase("FOLDER")) {
                                    saveTeamFileChoice=true;
                                }else{
                                    System.out.println("Invalid choice. Try again.");
                                }
                            }

                            switch (ctfssChoice.toUpperCase()) {
                                case "FILE":
                                    teamFileHandler.setFileSaveLocation(playerFileHandler.getFileAddress());
                                    teamFileHandler.setSaveFile(true);
                                    System.out.println("File Save Address Successfully Changed!");
                                    break;
                                case "FOLDER":
                                    teamFileHandler.setFileSaveLocation(playerFileHandler.getFolderAddress());
                                    //System.out.println("Enter the name of the File to be saved as: ");

                                    String teamFileName = main.getNonEmptyString("Enter the name of the File to be saved as: ");

                                    teamFileHandler.setFileName(teamFileName);
                                    System.out.println("File Save Address and File Name Successfully Changed!");
                                    break;
                                default:
                                    System.out.println();
                                    System.out.println("Enter a Given Option Only");
                                    System.out.println();
                                    break;
                            }
                            break;
                    }
                    break;

                case "EXIT":
                    System.out.println();
                    System.out.println("----------Exiting Program-------------");
                    System.exit(0);
                    break;


                default:
                    System.out.println();
                    System.out.println("Enter a Given Option Only");
                    System.out.println();

                    break;
            }


        }
    }

    private String getNonEmptyString(String question) {
        //Checks for empty inputs
        Scanner input = new Scanner(System.in);
        while (true) {
            System.out.print(question);
            String value = input.nextLine().trim();
            if (!value.isEmpty()) return value;

            System.out.println("Choice cannot be empty!");
        }
    }
}
