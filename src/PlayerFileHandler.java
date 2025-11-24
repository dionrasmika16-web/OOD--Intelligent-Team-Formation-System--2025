import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PlayerFileHandler extends AbstractFileHandler {

    public PlayerFileHandler(String fileName,String path) {
        this.setFileName(fileName);
        this.setFileSaveLocation(path);
    }

    @Override
    public List<Player> loadFromFile(String path) {
        List<Player> players = new ArrayList<>();

        try {
            File file = new File(path);
            Scanner scanner = new Scanner(file);

            // skip header if present
            if (scanner.hasNextLine()) scanner.nextLine();

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] v = line.split(",");

                Player p = new Player(
                        v[0],                  //iD
                        v[1],                  //name
                        v[2],                  //email
                        v[3],                  //preferred game
                        Integer.parseInt(v[4]),//Skill level
                        v[5],                  //preferred role
                        Integer.parseInt(v[6]),//personality score
                        v[7]                  //personality type
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
    public void saveToFile(String path, List<Player> players) {
        try {
            FileWriter writer = new FileWriter(path, true);//Appends to File

            //writer.write("SurveyID,Name,Email,PreferredGame,SkillLevel,PreferredRole,TotalScore,PersonalityType\n");

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

            writer.close();
            System.out.println("✔ Saved to CSV!");

        } catch (IOException e) {
            System.out.println("Error writing file.");
            e.printStackTrace();
        }
    }

    @Override
    public void saveToFolder(String folderPath, List<Player> players) {
        String fileName = this.getFileName();
        File folder = new File(folderPath);

        // Create folder if it doesn't exist
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String path = folderPath + File.separator + fileName;

        try (FileWriter writer = new FileWriter(path, true)) { // append mode
            File file = new File(path);
            // Write header only if file is new
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

            System.out.println("✔ Saved to CSV at: " + path);

        } catch (IOException e) {
            System.out.println("Error writing file.");
            e.printStackTrace();
        }

    }



}
