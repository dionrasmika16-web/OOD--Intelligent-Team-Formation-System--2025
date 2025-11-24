import java.util.List;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public abstract class AbstractFileHandler {

    private String fileSaveLocation;
    private String fileName;

    // Method to open file chooser and return selected CSV file path
    public String getFileAddress() {
        try {
            // Set system look and feel (makes it look like Windows Explorer)
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Failed to set system look and feel: " + e.getMessage());
        }

        JFrame frame = new JFrame();
        frame.setAlwaysOnTop(true); // Bring dialog to front

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select a CSV File");

        // Filter for CSV files only
        FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV Files", "csv");
        fileChooser.setFileFilter(filter);

        // Only allow single file selection
        fileChooser.setMultiSelectionEnabled(false);

        int result = fileChooser.showOpenDialog(frame);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (selectedFile.getName().toLowerCase().endsWith(".csv")) {
                return selectedFile.getAbsolutePath();
            } else {
                JOptionPane.showMessageDialog(frame,
                        "Please select a valid CSV file.",
                        "Invalid File",
                        JOptionPane.ERROR_MESSAGE);
                return null;
            }
        } else {
            return null; // No file selected
        }
    }

    // Method to open a folder chooser and return selected folder path
    public String getFolderAddress() {
        try {
            // Set system look and feel (modern Windows style)
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Failed to set system look and feel: " + e.getMessage());
        }

        JFrame frame = new JFrame();
        frame.setAlwaysOnTop(true); // Bring dialog to front

        JFileChooser folderChooser = new JFileChooser();
        folderChooser.setDialogTitle("Select a Folder to Save");
        folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); // Only folders
        folderChooser.setAcceptAllFileFilterUsed(false); // Disable "All files" option

        int result = folderChooser.showSaveDialog(frame);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFolder = folderChooser.getSelectedFile();
            return selectedFolder.getAbsolutePath();
        } else {
            return null; // No folder selected
        }
    }

    public void setFileSaveLocation(String fileSaveLocation) {
        this.fileSaveLocation = fileSaveLocation;
    }
    public String getFileSaveLocation() {
        return fileSaveLocation;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public String getFileName() {
        return fileName;
    }

    public abstract List<Player> loadFromFile(String path);

    public abstract void saveToFile(String path, List<Player> players);

    public abstract void saveToFolder(String path, List<Player> players);



}
