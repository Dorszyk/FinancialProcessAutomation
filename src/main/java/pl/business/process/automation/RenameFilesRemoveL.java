package pl.business.process.automation;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

@Slf4j
public class RenameFilesRemoveL {

    public static final String SOURCE_FOLDER_PATH = "C:/Users/RenameFilesRemoveL/";
    public static final String OUTPUT_FILE_PATH = "src/main/resources/renameFilesRemoveL.txt";

    public static void main(String[] args) {
        log.info("Starting the renaming process.");
        renameFiles(SOURCE_FOLDER_PATH);
        try {
            String[] fileNamesWithoutExtensions = getFileNamesWithoutExtensions(SOURCE_FOLDER_PATH);
            writeToTextFile(fileNamesWithoutExtensions);
            log.info("The file names have been saved: {}", OUTPUT_FILE_PATH);
        } catch (IOException e) {
            log.error("Error while saving files: {}", e.getMessage());
        }
    }

    public static void renameFiles(String directoryPath) {
        try (Stream<Path> paths = Files.list(Paths.get(directoryPath))) {
            paths.filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .filter(file -> file.getName().endsWith(".pdf"))
                    .peek(file -> log.info("Found file: {}", file.getName()))
                    .forEach(RenameFilesRemoveL::renameFile);
        } catch (Exception e) {
            log.error("Error while listing files in directory {}", directoryPath, e);
        }
    }

    private static void renameFile(File file) {
        String newFileName = file.getName().replace("L", "");
        log.info("Attempting to rename file {} to {}", file.getName(), newFileName);
        if (!newFileName.equals(file.getName())) {
            File newFile = new File(file.getParent(), newFileName);
            if (!file.renameTo(newFile)) {
                log.error("Failed to rename file: {}", file.getName());
            } else {
                log.info("The file name has been changed from {} to {}", file.getName(), newFileName);
            }
        } else {
            log.info("No 'L' found in the file name: {}", file.getName());
        }
    }

    private static String[] getFileNamesWithoutExtensions(String folderPath) throws IOException {
        return removeExtensions(getFileNames(folderPath));
    }

    private static String[] getFileNames(String folderPath) {
        File folder = new File(folderPath);
        if (folder.isDirectory()) {
            return folder.list();
        }
        return new String[0];
    }

    private static String[] removeExtensions(String[] fileNames) {
        return Arrays.stream(fileNames)
                .map(RenameFilesRemoveL::removeExtension)
                .toArray(String[]::new);
    }

    private static String removeExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex != -1 && lastDotIndex != 0) {
            return fileName.substring(0, lastDotIndex);
        }
        return fileName;
    }

    private static void writeToTextFile(String[] fileNames) throws IOException {
        File outputFile = new File(RenameFilesRemoveL.OUTPUT_FILE_PATH);
        outputFile.getParentFile().mkdirs();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            for (String fileName : fileNames) {
                writer.write(fileName);
                writer.newLine();
            }
        }
    }
}