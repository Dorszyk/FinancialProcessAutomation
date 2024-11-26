package pl.business.process.automation.fileDownloader.loanAgreement;

import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
public class FileRename {

    private static final String FOLDER_PATH = "C:\\Users\\Downloads";

    public static void main(String[] args) {
        File folder = new File(FOLDER_PATH);
        if (isFolderValid(folder)) {
            File[] files = folder.listFiles();
            assert files != null;
            for (File file : files) {
                processFile(file);
            }
        } else {
            log.info("Folder does not exist or it's not a directory.");
        }
    }

    private static void processFile(File file) {
        if (file.isFile()) {
            String fileName = file.getName();
            String newFileName = getNewName(fileName);
            File newFile = new File(FOLDER_PATH + File.separator + newFileName);
            if (renameFile(file, newFile)) {
                log.info("File {} changed to {}", fileName, newFileName);
            } else {
                log.info("Failed to rename file {}", fileName);
            }
        }
    }

    private static String getNewName(String fileName) {
        if (fileName.contains("contract")) {
            return extractNameFromContract(fileName) + ".pdf";
        } else if (fileName.startsWith("L")) {
            return extractLastCharacters(fileName);
        } else {
            return fileName;
        }
    }

    private static String extractNameFromContract(String fileName) {
        int startIndex = fileName.indexOf("contract") + "contract".length() + 1;
        int endIndex = fileName.indexOf('_', startIndex);
        return fileName.substring(startIndex, endIndex);
    }

    private static String extractLastCharacters(String fileName) {
        return fileName.substring(fileName.length() - 15);
    }

    private static boolean renameFile(File oldFile, File newFile) {
        return !newFile.exists() && oldFile.renameTo(newFile);
    }

    private static boolean isFolderValid(File folder) {
        return folder.exists() && folder.isDirectory();
    }
}
