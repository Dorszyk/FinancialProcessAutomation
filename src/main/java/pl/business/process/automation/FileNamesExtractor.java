package pl.business.process.automation;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Slf4j
public class FileNamesExtractor {

    public static void main(String[] args) {

        String sourceFolderPath = "C:/Users/FileNamesExtractor/";
        String outputFilePath = "src/main/resources/fileNamesExtractor.txt";
        try {
            String[] fileNamesWithoutExtensions = removeExtensions(getFileNames(sourceFolderPath));
            writeToTextFile(fileNamesWithoutExtensions, outputFilePath);
            log.info("File names (without extensions) saved in the file {} ", outputFilePath);
        } catch (IOException e) {
            log.error("An error occurred", e);
        }
    }

    private static String[] getFileNames(String folderPath) {
        File folder = new File(folderPath);
        if (folder.isDirectory()) {
            return folder.list();
        }
        return new String[0];
    }

    private static String[] removeExtensions(String[] fileNames) {
        String[] fileNamesWithoutExtensions = new String[fileNames.length];
        for (int i = 0; i < fileNames.length; i++) {
            fileNamesWithoutExtensions[i] = removeExtension(fileNames[i]);
        }
        return fileNamesWithoutExtensions;
    }

    private static String removeExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex != -1 && lastDotIndex != 0) {
            return fileName.substring(0, lastDotIndex);
        }
        return fileName;
    }

    private static void writeToTextFile(String[] fileNames, String outputPath) throws IOException {
        File outputFile = new File(outputPath);
        outputFile.getParentFile().mkdirs();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            for (String fileName : fileNames) {
                writer.write(fileName);
                writer.newLine();
            }
        }
    }
}
