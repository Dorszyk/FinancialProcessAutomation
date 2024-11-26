package pl.business.process.automation.fileDownloader.schedule;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

import static pl.business.process.automation.appPaths.AppPaths.SOURCE_PATH;
import static pl.business.process.automation.fileDownloader.service.FileMover.copyFile;
import static pl.business.process.automation.fileDownloader.service.FileMover.extractSourceRefNumber;
import static pl.business.process.automation.fileDownloader.service.FileMover.fetchSourceFiles;
import static pl.business.process.automation.fileDownloader.service.FileMover.isValidDirectory;

@Slf4j
public class FileMoverSchedule {

    private static final String HARMONOGRAM_PREFIX = "/2. harmonogram ";
    public static final String TARGET_PATH = "C:\\Users\\Harmonogramy";

    public static void main(String[] args) throws IOException {
        File targetFolder = new File(TARGET_PATH);
        File sourceFolder = new File(SOURCE_PATH);
        validateAndMoveFiles(targetFolder, sourceFolder);
    }

    private static void moveMatchingFiles(File targetFile, File[] sourceFiles) throws IOException {
        moveMatchingFiles(targetFile, sourceFiles, HARMONOGRAM_PREFIX);
    }

    public static void moveMatchingFiles(File targetFile, File[] sourceFiles, String harmonogramPrefix) throws IOException {
        String targetRefNumber = targetFile.getName().substring(0, 11);

        for (File sourceFile : sourceFiles) {
            String sourceRefNumber = extractSourceRefNumber(sourceFile);
            if (targetRefNumber.equals(sourceRefNumber)) {
                String destination = sourceFile.getPath() + harmonogramPrefix + targetFile.getName();
                copyFile(targetFile.getPath(), destination);
            }
        }
    }

    public static void validateAndMoveFiles(File targetFolder, File sourceFolder) throws IOException {
        if (isValidDirectory(targetFolder)) {
            File[] targetFiles = targetFolder.listFiles();

            assert targetFiles != null;
            for (File targetFile : targetFiles) {
                moveMatchingFiles(targetFile, fetchSourceFiles(sourceFolder));
            }
        } else {
            log.info("Target directory does not exist or is not a directory");
        }
    }

}