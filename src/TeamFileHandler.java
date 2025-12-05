import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;


public class TeamFileHandler extends AbstractFileHandler<Team> {

    public TeamFileHandler(String fileName, String path) {
        this.setFileName(fileName);
        this.setFileSaveLocation(path);
    }

    @Override
    public ArrayList<Team> loadFromFile(String path) {
        TeamDataManager teamDataManager = new TeamDataManager();
        boolean noError=true;
        File file = new File(path);

        //Check if the file exists
        if (!file.exists()) {
            System.out.println("Error: Team file not found at path: " + path);
            return teamDataManager.getTeamArray(); // Return empty list
        }

        // Check if the file is readable
        if (!file.canRead()) {
            System.out.println("Error: Cannot read team file. Check file permissions: " + path);
            return teamDataManager.getTeamArray();
        }

        try (Scanner scanner = new Scanner(file)) {

            //skip header if present
            if (scanner.hasNextLine()) scanner.nextLine();

            int lineNumber = 1;

            while (scanner.hasNextLine()) {
                lineNumber++;
                String line = scanner.nextLine();
                String[] values = line.split(",");

                final int fieldsPerPlayer = 8;
                // Minimum expected fields: TeamSize (1) + TeamID (1)
                final int minFields = 2;

                //Basic Data Integrity (TeamID and TeamSize)
                if (values.length < minFields) {
                    System.out.println("Skipping corrupted line " + lineNumber + " (insufficient Team ID/Size data): " + line);
                    noError=false;
                }

                int teamSize;
                int teamId;

                //Number Format for TeamSize and TeamID
                try {
                    teamSize = Integer.parseInt(values[0]);
                    teamId = Integer.parseInt(values[1]);
                } catch (NumberFormatException nfe) {
                    System.out.println("Skipping line " + lineNumber + " due to invalid number format for TeamSize or TeamID: " + line);
                    noError=false;
                    continue;
                }

                //Check if total field count matches expected team size
                int expectedTotalFields = minFields + (teamSize * fieldsPerPlayer);
                if (values.length < expectedTotalFields) {
                    //System.out.println("Skipping line " + lineNumber + ": Insufficient player data for Team ID " + teamId + ". Expected " + teamSize + " players, found incomplete data.");
                    continue;
                }

                Team t = new Team(teamSize, teamId);

                for (int i = 0; i < teamSize; i++) {
                    int startIndex = 2 + (i * fieldsPerPlayer);

                    if (startIndex + fieldsPerPlayer > values.length) {
                        System.out.println("Error on line " + lineNumber + ": Insufficient player data for Team ID " + teamId + ". Stopping processing this team.");
                        noError=false;
                        break;
                    }

                    //Number Format for Player data
                    try {
                        Player p = new Player(
                                values[startIndex].toLowerCase(),      // ID(index 0)
                                values[startIndex + 1].toLowerCase(),  // Name(index 1)
                                values[startIndex + 2].toLowerCase(),  // Email(index 2)
                                values[startIndex + 3].toLowerCase(),  // PreferredGame(index 3)
                                Integer.parseInt(values[startIndex + 4]), // SkillLevel(index 4)
                                values[startIndex + 5].toLowerCase(),  // PreferredRole(index 5)
                                Integer.parseInt(values[startIndex + 6]), // PersonalityScore(index 6)
                                values[startIndex + 7].toLowerCase()   // PersonalityType(index 7)
                        );
                        t.addPlayer(p);
                    } catch (NumberFormatException nfe) {
                        System.err.println("Skipping player data in line " + lineNumber + " due to invalid number format (SkillLevel/Score).");
                        noError=false;
                        // If a single player is corrupted, we skip the player but keep the rest of the team data.
                    }

                }
                teamDataManager.addTeam(t);
            }

        } catch (FileNotFoundException e) {
            System.out.println("Error: Team file not found or access denied during loading: " + path);
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("An unexpected error occurred while reading team CSV file: " + path);
            e.printStackTrace();
        }
        if (noError) {
            System.out.println("File Successfully Uploaded!");
        }

        return teamDataManager.getTeamArray();
    }

    @Override
    public synchronized void saveToFile(ArrayList<Team> Teams) {
        Teams.sort(Comparator.comparingInt(Team::getId));
        String path = this.getFileSaveLocation();
        File file = new File(path);

        // Checks if the file is writable
        if (file.exists() && !file.canWrite()) {
            System.err.println("Error: Cannot write to file. Check file permissions or if the file is open elsewhere: " + path);
            return;
        }

        try (FileWriter writer = new FileWriter(path, true)) { // append mode


            for (Team t : Teams) {
                StringBuilder line = new StringBuilder();

                line.append(t.getTeamSize()).append(",");
                line.append(t.getId());

                for (Player player : t.getTeamList()) {
                    line.append(","); // separator before the next player's data

                    line.append(player.getId()).append(",");
                    line.append(player.getName()).append(",");
                    line.append(player.getEmail()).append(",");
                    line.append(player.getPreferredGame()).append(",");
                    line.append(player.getSkillLevel()).append(",");
                    line.append(player.getPreferredRole()).append(",");
                    line.append(player.getPersonalityScore()).append(",");
                    line.append(player.getPersonalityType());
                }

                writer.write(line + "\n");
            }

            //Force data to be written to disk immediately at  once
            writer.flush();
            System.out.println("Teams Saved to file");

        } catch (IOException e) {
            System.out.println("Error writing file in Chosen Folder. Please ensure the file is not open or locked by another application/process: " + path);
        }
    }

    @Override
    public synchronized void saveToFolder(ArrayList<Team> Teams) {
        Teams.sort(Comparator.comparingInt(Team::getId));
        String folderPath = this.getFileSaveLocation();
        String fileName = this.getFileName();
        File folder = new File(folderPath);

        // handle folder creation errors
        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                System.out.println("Error: Failed to create necessary directories: " + folderPath);
                return;
            }
        }

        //Check if folder is writable
        if (!folder.canWrite()) {
            System.out.println("Error: Cannot write to the folder/directory. Check folder permissions: " + folderPath);
            return;
        }

        String path = folderPath + File.separator + fileName + ".csv";

        try (FileWriter writer = new FileWriter(path, false)) { // Overwrite mode

            writer.write("TeamSize,TeamID,Player1_ID,Player1_Name,...,PlayerN_Type\n");

            for (Team t : Teams) {
                StringBuilder line = new StringBuilder();

                line.append(t.getTeamSize()).append(",");
                line.append(t.getId());

                for (Player player : t.getTeamList()) {
                    line.append(",");

                    line.append(player.getId().toUpperCase()).append(",");
                    line.append(player.getName().toUpperCase()).append(",");
                    line.append(player.getEmail().toUpperCase()).append(",");
                    line.append(player.getPreferredGame().toUpperCase()).append(",");
                    line.append(player.getSkillLevel()).append(",");
                    line.append(player.getPreferredRole().toUpperCase()).append(",");
                    line.append(player.getPersonalityScore()).append(",");
                    line.append(player.getPersonalityType().toUpperCase());
                }

                writer.write(line + "\n");
            }

            writer.flush();
            System.out.println("Saved Teams to Folder ");

        } catch (IOException e) {


            System.out.println("Error writing file in Chosen Folder. Please ensure the file is not open or locked by another application/process: " + path);

        }
    }
}