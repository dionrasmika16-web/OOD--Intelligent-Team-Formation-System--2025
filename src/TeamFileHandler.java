import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

// Assuming AbstractFileHandler<Team> is defined elsewhere
public class TeamFileHandler extends AbstractFileHandler<Team> {

    public TeamFileHandler(String fileName, String path) {
        this.setFileName(fileName);
        this.setFileSaveLocation(path);
    }

    @Override
    public ArrayList<Team> loadFromFile(String path) {
        TeamDataManager teamDataManager = new TeamDataManager();

        try {
            File file = new File(path);
            Scanner scanner = new Scanner(file);

            //skip header if present
            if (scanner.hasNextLine()) scanner.nextLine();

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] values = line.split(",");

                //if (values.length < 2) continue; checks whether valid by checking TeamSize and TeamID

                int teamSize = Integer.parseInt(values[0]);
                int teamId = Integer.parseInt(values[1]);
                Team t = new Team(teamSize, teamId);

                final int fieldsPerPlayer = 8;

                for (int i = 0; i < teamSize; i++) {

                    int startIndex = 2 + (i * fieldsPerPlayer);

                    if (startIndex + fieldsPerPlayer > values.length) {
                        System.err.println("Error: Insufficient player data for Team ID " + teamId + ". Expected " + teamSize + " players.");
                        break;
                    }


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
                }
                teamDataManager.addTeam(t);
            }

            scanner.close();

        } catch (Exception e) {
            System.out.println("Error reading CSV file!");
            e.printStackTrace();
        }

        return teamDataManager.getTeamArray();
    }

    @Override
    public void saveToFile(ArrayList<Team> Teams) {
        Teams.sort(Comparator.comparingInt(Team::getId));
        System.out.println("Saving Teams to file...");
        String path = this.getFileSaveLocation();
        try {

            FileWriter writer = new FileWriter(path, true);


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

            writer.close();
            //System.out.println("Saved to CSV at: " + path);
            System.out.println("Saved Teams to file!");

        } catch (IOException e) {
            System.out.println("Error writing file.");
            e.printStackTrace();
        }
    }

    @Override
    public void saveToFolder(ArrayList<Team> Teams) {
        Teams.sort(Comparator.comparingInt(Team::getId));
        System.out.println("Saving Teams to folder...");

        // Creates a address by adding file location and filename
        String folderPath = this.getFileSaveLocation();
        String fileName = this.getFileName();
        File folder = new File(folderPath);

        // Create folder if it doesn't exist
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String path = folderPath + File.separator + fileName+".csv";

        try (FileWriter writer = new FileWriter(path, false)) { // Overwrite mode


            for (Team t : Teams) {
                StringBuilder line = new StringBuilder();


                line.append(t.getTeamSize()).append(",");
                line.append(t.getId());


                for (Player player : t.getTeamList()) {
                    line.append(",");

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

            System.out.println("✔ Saved to CSV at: " + path);

        } catch (IOException e) {
            System.out.println("Error writing file.");
            e.printStackTrace();
        }
    }
}