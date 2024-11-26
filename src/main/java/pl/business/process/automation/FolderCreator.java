package pl.business.process.automation;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
public class FolderCreator {

    private static final String FILE_PATH = "src/main/resources/folderCreator.txt";
    private static final String NETWORK_PATH = "C:/Users/FolderCreator/";

    public static void main(String[] args) {
        Path path = Paths.get(FILE_PATH);
        if (!Files.exists(path)) {
            log.error("File does not exist: {}", FILE_PATH);
            return;
        }
        try {
            List<String> folderNames = Files.readAllLines(path);
            createFolders(folderNames);
        } catch (IOException e) {
            log.error("An error occurred while creating folders: ", e);
        }
    }

    private static void createFolders(List<String> folderNames) throws IOException {
        for (String folderName : folderNames) {
            String cleanedFolderName = folderName.trim().replace("\t", "_");
            Path folderPath = Paths.get(NETWORK_PATH, cleanedFolderName);
            if (!Files.exists(folderPath)) {
                Files.createDirectories(folderPath);
                log.info("Folder created: {}", folderPath);
            } else {
                log.info("Folder already exists: {}", folderPath);
            }
        }
    }
}