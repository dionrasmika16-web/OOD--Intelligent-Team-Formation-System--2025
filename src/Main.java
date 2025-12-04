import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Main main = new Main();
        final PlayerDataManager playerDataManager = new PlayerDataManager();

        //Enters default Participant file path Save Location and File Name;
        final PlayerFileHandler playerFileHandler = new PlayerFileHandler("PlayerFile-"+main.DateTimeStringExample(), "C:\\Users\\Extra\\Desktop\\lastjavatut");
        final TeamFileHandler teamFileHandler=new TeamFileHandler("Team-File-"+main.DateTimeStringExample(), "C:\\Users\\Extra\\Desktop\\lastjavatut");


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

                    // ---Execute Survey Concurrently-----
                    // 1 Create task
                    Runnable surveyTask = new SurveyProcessor(
                            playerSurveyHandler,//Takes survey
                            playerDataManager,//Saves player data
                            playerFileHandler//Saves player to file
                    );

                    // 2 Submit the task
                    executor.submit(surveyTask);


                    // 3 Shut down the executor
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

                    break;

                case "UP":
                    //Opens a gui to take file address
                    String uploadFileLocation=playerFileHandler.getFileAddress();
                    if (uploadFileLocation ==null){
                        System.out.println("Error occured opening file");
                    }else {
                        //loads the data in the file to playerDataManager(ArrayList)
                        playerDataManager.addFilePlayers(playerFileHandler.loadFromFile(uploadFileLocation));
                        System.out.println("File Successfully Uploaded!");
                    }
                    break;

                case "FT":
                    //playerDataManager.addFilePlayers(playerFileHandler.loadFromFile("C:\\Users\\Extra\\Desktop\\CM1601 VIVA\\Starter pack\\Starter pack\\participants_sample.csv"));
                    //System.out.println("players---"+playerDataManager.getAllPlayers().size());
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
                        ArrayList<Player> playerListForSetupHandler = new ArrayList<>(playerDataManager.getAllPlayers());

                        GenerateTeamSetupHandler setupHandler = new GenerateTeamSetupHandler(playerListForSetupHandler,intTeamSize,numberOfTeamsToMake);


                        // Execute Concurrent
                        setupHandler.startWorkFlow(teamFileHandler);


                        //Access the Data Manager and Retrieve Teams
                        TeamDataManager dataManager = setupHandler.getTeamDataManager();

                        boolean clearPlayerFlag=false;
                        while (!clearPlayerFlag) {
                            System.out.println("Would you like to clear the players from the system memory for the next team formation(Y/N): ");
                            String clearPlayers= main.getNonEmptyString("Enter your choice: ");
                            if(clearPlayers.toUpperCase().equals("Y")){
                                playerDataManager.clearAllPlayers();
                                System.out.println("Clearing all players from the system memory...");
                                clearPlayerFlag=true;
                            } else if(clearPlayers.toUpperCase().equals("N")){
                                clearPlayerFlag=true;
                            } else{
                                System.out.println("Enter Y or N: ");
                            }
                        }


                    }else{
                        System.out.println("There are no players added to create Teams.");
                        System.out.println("Upload an existing player file or complete a survey!");
                        System.out.println();
                    }
                    break;

                case "VT":
                    System.out.println("-----------------Team View-----------------");
                    TeamDataManager viewTeamDataManager = new TeamDataManager();
                    //Gets Teams File Address
                    String uploadTeamFileLocation=teamFileHandler.getFileAddress();
                    if (uploadTeamFileLocation ==null){
                        System.out.println("Error occured opening file");
                    }else {
                        //Loads the teams to teamDataManager
                        viewTeamDataManager.setTeamArray(teamFileHandler.loadFromFile(uploadTeamFileLocation));
                        System.out.println("File Successfully Uploaded!");
                    }
                    viewTeamDataManager.viewTeam();
                    break;


                case "SFS":
                    //CHanges file save setting in either teams or players to a preferred folder(directory) or file
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

                                    String uploadfileLocation=playerFileHandler.getFileAddress();
                                    if (uploadfileLocation ==null){
                                        System.out.println("Error occured opening file");
                                    }else {
                                        playerFileHandler.setFileSaveLocation(uploadfileLocation);
                                        playerFileHandler.setSaveFile(true);
                                        System.out.println("File Successfully Uploaded!");
                                    }
                                    break;
                                case "FOLDER":

                                    String uploadfolderLocation=playerFileHandler.getFolderAddress();
                                    if (uploadfolderLocation ==null){
                                        System.out.println("Error occured opening file");
                                    }else {
                                        playerFileHandler.setFileSaveLocation(uploadfolderLocation);
                                        System.out.println("Folder location Successfully Uploaded!");
                                    }
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
                                    String uploadteamfileLocation=teamFileHandler.getFileAddress();
                                    if (uploadteamfileLocation ==null){
                                        System.out.println("Error occured opening file");
                                    }else {
                                        teamFileHandler.setFileSaveLocation(uploadteamfileLocation);
                                        teamFileHandler.setSaveFile(true);
                                        System.out.println("File Successfully Uploaded!");
                                    }
                                    break;
                                case "FOLDER":
                                    String uploadteamfolderLocation=teamFileHandler.getFolderAddress();
                                    if (uploadteamfolderLocation ==null){
                                        System.out.println("Error occured opening file");
                                    }else {
                                        teamFileHandler.setFileSaveLocation(uploadteamfolderLocation);
                                        System.out.println("Folder location Successfully Uploaded!");
                                    }

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

    private String DateTimeStringExample() {
            // Get current date and time
            LocalDateTime now = LocalDateTime.now();

            // Define a formatter for the date-time string
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

            // Format the date-time
            String dateTimeString = now.format(formatter);

            return dateTimeString;


    }
}
