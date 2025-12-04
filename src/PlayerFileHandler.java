import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.nio.file.AccessDeniedException;

public class PlayerFileHandler extends AbstractFileHandler<Player> {

    public PlayerFileHandler(String fileName, String path) {
        this.setFileName(fileName);
        this.setFileSaveLocation(path);
    }

    @Override
    public ArrayList<Player> loadFromFile(String path) {
        ArrayList<Player> players = new ArrayList<>();
        File file;
        try{
            file = new File(path);
        }catch(Exception e){
            System.out.println("Error occurred choosing file");
            return null;
        }

        // 1. Check if the file exists before attempting to read
        if (!file.exists()) {
            System.out.println("Error: File not found at path: " + path);
            return null; // Return empty list
        }

        // 2. Check if the file is readable
        if (!file.canRead()) {
            System.out.println("Error: Cannot read file. Check file permissions: " + path);
            return null;
        }

        try (Scanner scanner = new Scanner(file)) {

            // Skip header if present
            if (scanner.hasNextLine()) scanner.nextLine();

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] v = line.split(",");

                // Add error handling for corrupted data (checking array length)
                if (v.length < 8) {
                    System.out.println("Skipping corrupted line (insufficient data): " + line);
                    continue; // Skip this line and move to the next
                }

                // Add try-catch for NumberFormatException during parsing
                try {
                    Player p = new Player(
                            v[0].toLowerCase(),                  // iD
                            v[1].toLowerCase(),                  // name
                            v[2].toLowerCase(),                  // email
                            v[3].toLowerCase(),                  // preferred game
                            Integer.parseInt(v[4]),              // Skill level
                            v[5].toLowerCase(),                  // preferred role
                            Integer.parseInt(v[6]),              // personality score
                            v[7].toLowerCase()                   // personality type
                    );
                    players.add(p);
                } catch (NumberFormatException nfe) {
                    System.out.println("Skipping line due to invalid number format (SkillLevel or TotalScore): " + line);
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found or access denied during loading: " + path);
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("An unexpected error occurred while reading CSV file: " + path);
            e.printStackTrace();
        }

        return players;
    }

    @Override
    public synchronized void saveToFile(ArrayList<Player> players) {
        String path = this.getFileSaveLocation();
        File file = new File(path);

        //Checks if the file is writable

        if (file.exists() && !file.canWrite()) {
            System.out.println("Error: Cannot write to file. Check file permissions or if the file is open elsewhere: " + path);
            return;
        }

        try (FileWriter writer = new FileWriter(path, true)) { // append mode

            // Check file length
            if (file.length() == 0) {
                writer.write("ID,Name,Email,PreferredGame,SkillLevel,PreferredRole,TotalScore,PersonalityType\n");
            }
            for (Player p : players) {
                writer.write(
                        p.getId().toUpperCase() + "," +
                                p.getName().toUpperCase() + "," +
                                p.getEmail().toUpperCase() + "," +
                                p.getPreferredGame().toUpperCase() + "," +
                                p.getSkillLevel() + "," +
                                p.getPreferredRole().toUpperCase() + "," +
                                p.getPersonalityScore() + "," +
                                p.getPersonalityType().toUpperCase() + "\n"
                );
            }

            //Force data to be written to disk immediately at once
            writer.flush();

            System.out.println("Players Saved to file");

        } catch (IOException e) {
            System.out.println("Error writing file. Please ensure the file is not open or locked by another application/process");
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void saveToFolder(ArrayList<Player> players) {
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

        // Check if folder is writable
        if (!folder.canWrite()) {
            System.out.println("Error: Cannot write to the folder/directory. Check folder permissions: " + folderPath);
            return;
        }

        String path = folderPath + File.separator + fileName + ".csv";
        File file = new File(path);

        try (FileWriter writer = new FileWriter(path, false)) { // overwrite mode

            if (file.length() == 0) {
                writer.write("SurveyID,Name,Email,PreferredGame,SkillLevel,PreferredRole,TotalScore,PersonalityType\n");
            }
            for (Player p : players) {
                // ... (Writing player data)
                writer.write(
                        p.getId().toUpperCase() + "," +
                                p.getName().toUpperCase() + "," +
                                p.getEmail().toUpperCase() + "," +
                                p.getPreferredGame().toUpperCase() + "," +
                                p.getSkillLevel() + "," +
                                p.getPreferredRole().toUpperCase() + "," +
                                p.getPersonalityScore() + "," +
                                p.getPersonalityType().toUpperCase() + "\n"
                );
            }

            writer.flush();

            System.out.println("Players Saved to folder");

        } catch (IOException e) {
            System.out.println("Error writing file in Chosen Folder. Please ensure the file is not open or locked by another application/process");
        }
    }
}