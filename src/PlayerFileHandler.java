import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PlayerFileHandler extends AbstractFileHandler<Player> {

    public PlayerFileHandler(String fileName,String path) {
        this.setFileName(fileName);
        this.setFileSaveLocation(path);
    }

    @Override
    public ArrayList<Player> loadFromFile(String path) {
        ArrayList<Player> players = new ArrayList<>();

        try {
            File file = new File(path);
            Scanner scanner = new Scanner(file);

            // skip header if present
            if (scanner.hasNextLine()) scanner.nextLine();

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] v = line.split(",");

                Player p = new Player(
                        v[0].toLowerCase(),                  //iD
                        v[1].toLowerCase(),                  //name
                        v[2].toLowerCase(),                  //email
                        v[3].toLowerCase(),                  //preferred game
                        Integer.parseInt(v[4]),//Skill level
                        v[5].toLowerCase(),                  //preferred role
                        Integer.parseInt(v[6]),//personality score
                        v[7].toLowerCase()                  //personality type
                );

                players.add(p);
            }

            scanner.close();

        } catch (Exception e) {
            System.out.println("Error reading CSV file!");
            e.printStackTrace();
        }

        return players;
    }

    @Override
    public synchronized void saveToFile(ArrayList<Player> players) {
        String path=this.getFileSaveLocation();
        try (FileWriter writer = new FileWriter(path, true)) { // append mode
            File file = new File(path);
            // Write header only if file is new
            if (file.length() == 0) {
                writer.write("ID,Name,Email,PreferredGame,SkillLevel,PreferredRole,TotalScore,PersonalityType\n");
            }
            for (Player p : players) {
                writer.write(
                        p.getId() + "," +
                                p.getName() + "," +
                                p.getEmail() + "," +
                                p.getPreferredGame() + "," +
                                p.getSkillLevel() + "," +
                                p.getPreferredRole() + "," +
                                p.getPersonalityScore() + "," +
                                p.getPersonalityType() + "\n"
                );
            }

            System.out.println("[File Handler] ✔ Saved to CSV at: " + path);

        } catch (IOException e) {
            System.out.println("Error writing file.");
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void saveToFolder(ArrayList<Player> players) {
        // Creates a address by adding file location and filename
        String folderPath =this.getFileSaveLocation();
        String fileName = this.getFileName();
        File folder = new File(folderPath);

        // creates folder if it doesn't exist
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String path = folderPath + File.separator + fileName+".csv";

        // The entire block is synchronized, ensuring only one thread writes to the file at a time
        try (FileWriter writer = new FileWriter(path, false)) { // append mode
            File file = new File(path);
            // writes header only if file is new, another check just in case another file with the same name exists in the directory
            if (file.length() == 0) {
                writer.write("SurveyID,Name,Email,PreferredGame,SkillLevel,PreferredRole,TotalScore,PersonalityType\n");
            }
            for (Player p : players) {
                writer.write(
                        p.getId() + "," +
                                p.getName() + "," +
                                p.getEmail() + "," +
                                p.getPreferredGame() + "," +
                                p.getSkillLevel() + "," +
                                p.getPreferredRole() + "," +
                                p.getPersonalityScore() + "," +
                                p.getPersonalityType() + "\n"
                );
            }

            System.out.println("[File Handler] ✔ Saved to CSV at: " + path);

        } catch (IOException e) {
            System.out.println("Error writing file.");
            e.printStackTrace();
        }
    }




}
