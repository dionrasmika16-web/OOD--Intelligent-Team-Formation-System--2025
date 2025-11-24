import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Main main = new Main();
        PlayerDataManager playerDataManager = new PlayerDataManager();
        //Enters default Participant file path Save Location and File Name;
        PlayerFileHandler playerFileHandler = new PlayerFileHandler("Test", "C:\\Users\\Extra\\Desktop\\lastjavatut");
        boolean programRunning = true;
        while (programRunning) {
            System.out.println("--------------Intelligent Team Formation System-----------------------");
            System.out.println("│ " + "Complete Survey(CS)"
                    + " │ " + "Upload Players(UP)" + " │ "
                    + " │ " + "Form Teams(FT)" + " │ "
                    + " │ " + "View Teams(VT)" + " │ "
                    + " │ " + "Save File Settings(SFS)" + " │ "
                    + " │ " + "Exit Program(EXIT)" + " │ ");

            Scanner input = new Scanner(System.in);

            //Takes Users Input
            String choice1 = "";
            boolean isChoice1 = false;
            //Validates for empty spaces
            while (!isChoice1) {
                System.out.print("Enter your choice: ");
                choice1 = input.nextLine();
                if (!choice1.trim().isEmpty()) {
                    isChoice1 = true;
                } else {
                    System.out.println("Choice Cannot be left blank");
                }
            }
            switch (choice1.toUpperCase()) {
                case "CS":
                    PlayerSurveyHandler playerSurveyHandler = new PlayerSurveyHandler();
                    //Calls to Execute survey--->Stores the survey details as player object--->Saves in playerDataManager(Player Array for players from surveys)
                    playerDataManager.addSurveyPlayer(playerSurveyHandler.conductSurvey());
                    System.out.println("Survey Completed Successfully!");
                    break;

                case "UP":
                    //Gets File Location--->File is loaded and processed--->Players are saved in PlayerDataManager PlayerFileArray(Players loaded from a file)
                    playerDataManager.addFilePlayers(playerFileHandler.loadFromFile(playerFileHandler.getFileAddress()));
                    System.out.println("File Successfully Uploaded!");
                    break;

                case "SFS":
                    System.out.println("--------------Save File Settings-----------------------");
                    System.out.println("│ " + "Change Participant File Save(CPFS)" + " │ " + "Change Team File Save(CTFS)" + " │ ");
                    String choice2 = input.nextLine();
                    boolean isChoice2 = false;
                    //Validates for empty spaces
                    while (!isChoice2) {
                        System.out.print("Enter your choice: ");
                        choice2 = input.nextLine();
                        if (!choice2.trim().isEmpty()) {
                            isChoice2 = true;
                        } else {
                            System.out.println("Choice Cannot be left blank");
                        }
                    }
                    switch (choice2) {
                        case "CPFS":
                            System.out.println("--------------Change Participant File Save Settings-----------------------");
                            System.out.println("│ " + "Save To File(File)" + " │ " + "Save To Folder(Folder)" + " │ ");
                            String choice3 = input.nextLine();
                            boolean isChoice3 = false;
                            //Validates for empty spaces
                            while (!isChoice3) {
                                System.out.print("Enter your choice: ");
                                choice3 = input.nextLine();
                                if (!choice3.trim().isEmpty()) {
                                    isChoice3 = true;
                                } else {
                                    System.out.println("Choice Cannot be left blank");
                                }
                            }
                            switch (choice3.toUpperCase()) {
                                case "FILE":
                                    playerFileHandler.setFileSaveLocation(playerFileHandler.getFileAddress());
                                    System.out.println("File Save Address Successfully Changed!");
                                    break;
                                case "FOLDER":
                                    playerFileHandler.setFileSaveLocation(playerFileHandler.getFolderAddress());
                                    System.out.println("Enter the name of the File to be saved as: ");
                                    String fileName1 = input.nextLine();
                                    boolean isChoice5 = false;
                                    while (!isChoice5) {
                                        System.out.print("Enter your choice: ");
                                        fileName1 = input.nextLine();
                                        if (!fileName1.trim().isEmpty()) {
                                            isChoice5 = true;
                                        } else {
                                            System.out.println("Choice Cannot be left blank");
                                        }
                                    }
                                    playerFileHandler.setFileName(fileName1);
                                    System.out.println("File Save Address and File Name Successfully Changed!");
                                    break;
                                default:
                                    System.out.println();
                                    System.out.println("Enter a Given Option Only");
                                    System.out.println();
                                    break;
                            }

                    }


                case "EXIT":
                    System.out.println();
                    System.out.println("----------Exiting Program-------------");
                    System.exit(0);


                default:
                    System.out.println();
                    System.out.println("Enter a Given Option Only");
                    System.out.println();

                    break;
            }


        }
    }
}