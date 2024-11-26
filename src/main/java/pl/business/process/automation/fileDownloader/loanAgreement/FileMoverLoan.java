package pl.business.process.automation.fileDownloader.loanAgreement;

import lombok.extern.slf4j.Slf4j;
import pl.business.process.automation.fileDownloader.schedule.FileMoverSchedule;

import java.io.File;
import java.io.IOException;

import static pl.business.process.automation.appPaths.AppPaths.SOURCE_PATH;
import static pl.business.process.automation.fileDownloader.service.FileMover.fetchSourceFiles;
import static pl.business.process.automation.fileDownloader.service.FileMover.isValidDirectory;

@Slf4j
public class FileMoverLoan {

    private static final String LOAN = "/1. umowa ";
    public static final String TARGET_PATH = "C:\\Users\\Umowy";

    public static void main(String[] args) throws IOException {
        File targetFolder = new File(TARGET_PATH);
        File sourceFolder = new File(SOURCE_PATH);
        validateAndMoveFiles(targetFolder, sourceFolder);
    }

    private static void moveMatchingFiles(File targetFile, File[] sourceFiles) throws IOException {
        FileMoverSchedule.moveMatchingFiles(targetFile, sourceFiles, LOAN);
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