package pl.business.process.automation.fileDownloader.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileMover {

    public static boolean isValidDirectory(File folder) {
        return folder.exists() && folder.isDirectory();
    }

    public static File[] fetchSourceFiles(File sourceFolder) {
        return sourceFolder.listFiles();
    }

    public static String extractSourceRefNumber(File sourceFile) {
        String fileName = sourceFile.getName();
        int fileNameLength = fileName.length();

        if (fileName.endsWith("r") && fileNameLength > 18) {
            return fileName.substring(fileNameLength - 18, fileNameLength - 7);
        } else if (fileNameLength > 11) {
            return fileName.substring(fileNameLength - 11);
        }
        return null;
    }

    public static void copyFile(String source, String destination) throws IOException {
        Files.copy(Paths.get(source), Paths.get(destination), StandardCopyOption.REPLACE_EXISTING);
    }
}
