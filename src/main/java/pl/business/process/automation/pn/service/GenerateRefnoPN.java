package pl.business.process.automation.pn.service;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

import static pl.business.process.automation.pn.service.CheckAndCopyFilePN.checkAndCopyFilePN;


@Slf4j
public class GenerateRefnoPN {

    private static final int REFNO_SUFFIX_LENGTH = 11;
    private static final int REFNO2_SUFFIX_LENGTH = 18;
    private static final int REFNO2_SUFFIX_OFFSET = 7;

    public static void copyMatchingFilesPN(Path sourceFile, String targetPath) {
        Optional<String> optRefno2 = generateRefnoPN(sourceFile.getFileName().toString());
        if (optRefno2.isPresent()) {
            String refno2 = optRefno2.get();
            try (Stream<Path> targetFiles = Files.list(Paths.get(targetPath))) {
                targetFiles.forEach(targetFile -> checkAndCopyFilePN(sourceFile, targetFile, refno2));
            } catch (IOException e) {
                log.error("Error copying files from {} to {}", sourceFile, targetPath, e);
            }
        }
    }

    public static Optional<String> generateRefnoPN(String filename) {
        if (filename.length() < REFNO_SUFFIX_LENGTH) {
            return Optional.empty();
        }
        String refno = filename.endsWith("r")
                ? filename.substring(Math.max(0, filename.length() - REFNO2_SUFFIX_LENGTH), Math.max(0, filename.length() - REFNO2_SUFFIX_OFFSET))
                : filename.substring(Math.max(0, filename.length() - REFNO_SUFFIX_LENGTH));
        return Optional.of(refno);
    }
}
