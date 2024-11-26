package pl.business.process.automation;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class AllFileNameFolder {

    private static final Set<String> EXCLUDED_FOLDERS =
            new HashSet<>(Arrays.asList("folder1", "folder2", "folder3", "folder4"));

    private static final String SOURCE_FOLDER_PATH = "C:\\Users\\folderMain";
    private static final String OUTPUT_FILE_PATH = "src/main/resources/allFileNameFolder.txt";

    public static void main(String[] args) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(OUTPUT_FILE_PATH))) {
            processFolder(SOURCE_FOLDER_PATH, writer);
            log.info("The file names have been saved {}", OUTPUT_FILE_PATH);
        } catch (IOException e) {
            log.error("Error while saving folder paths", e);
        }
    }

    private static void processFolder(String sourceFolderPath, BufferedWriter writer) throws IOException {
        Path rootPath = Paths.get(sourceFolderPath);
        if (EXCLUDED_FOLDERS.contains(rootPath.getFileName().toString())) {
            return;
        }
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(rootPath)) {
            for (Path path : stream) {
                if (Files.isDirectory(path)) {
                    if (!EXCLUDED_FOLDERS.contains(path.getFileName().toString())) {
                        writeDirectory(writer, path);
                        processFolder(path.toString(), writer);
                    }
                } else if (Files.isRegularFile(path)) {
                    writeFile(writer, path);
                }
            }
        }
    }

    private static void writeDirectory(BufferedWriter writer, Path path) throws IOException {
        writer.write("Sciezka: " + path);
        writer.newLine();
    }

    private static void writeFile(BufferedWriter writer, Path path) throws IOException {
        writer.write("plik: " + path.getFileName());
        writer.newLine();
    }
}
