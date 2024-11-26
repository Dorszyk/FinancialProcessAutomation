package pl.business.process.automation.ow.service;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

import static pl.business.process.automation.ow.service.CheckAndCopyFileOW.checkAndCopyFileOW;

@Slf4j
public class GenerateRefnoOW {

    private static final int REFNO_SUFFIX_LENGTH = 11;
    private static final int REFNO2_SUFFIX_LENGTH = 18;
    private static final int REFNO2_SUFFIX_OFFSET = 7;


    public static void copyMatchingFilesOW(Path sourceFile, String targetPath) {
        Optional<String> optRefno2 = generateRefnoOW(sourceFile.getFileName().toString()).describeConstable();
        if (optRefno2.isPresent()) {
            String refno2 = optRefno2.get();
            try (Stream<Path> targetFiles = Files.list(Paths.get(targetPath))) {
                targetFiles.forEach(targetFile -> checkAndCopyFileOW(sourceFile, targetFile, refno2));
            } catch (IOException e) {
                log.error("Error copying files from {} to {}", sourceFile, targetPath, e);
            }
        }
    }

    private static String generateRefnoOW(String filename) {
        String output = "";
        if (filename.endsWith("r")) {
            if (filename.length() >= REFNO2_SUFFIX_LENGTH) {
                output = filename.substring(filename.length() - REFNO2_SUFFIX_LENGTH, filename.length() - REFNO2_SUFFIX_OFFSET);
            }
        } else {
            if (filename.length() >= REFNO_SUFFIX_LENGTH) {
                output = filename.substring(filename.length() - REFNO_SUFFIX_LENGTH);
            }
        }
        return output;
    }
}
