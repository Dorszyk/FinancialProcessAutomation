package pl.business.process.automation.pn.service;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Slf4j
public class CheckAndCopyFilePN {

    private static final int REFNO_SUFFIX_LENGTH = 11;

    public static void checkAndCopyFilePN(Path sourceFile, Path targetFile, String refno2) {
        String refno = targetFile.getFileName().toString().substring(0, REFNO_SUFFIX_LENGTH);
        if (refno.equals(refno2)) {
            Path targetPath = Paths.get(sourceFile + "/4. OW PN Company " + refno + ".pdf");
            try {
                Files.copy(targetFile, targetPath, StandardCopyOption.REPLACE_EXISTING);
                log.info("File copied from {} to {}", targetFile, targetPath);
            } catch (IOException e) {
                log.error("An error occurred", e);
            }
        }
    }
}
