package pl.business.process.automation.ow.company4;

import lombok.extern.slf4j.Slf4j;
import pl.business.process.automation.ow.service.GenerateRefnoOW;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static pl.business.process.automation.appPaths.AppPaths.SOURCE_PATH;

@Slf4j
public class OwCompanyFour {

    private static final String TARGET_PATH =
            "C:\\Users\\CompanyFour\\Pliki";

    public static void main(String[] args) throws IOException {
        try (Stream<Path> sourceFiles = Files.list(Paths.get(SOURCE_PATH))) {
            sourceFiles.forEach(sourceFile -> GenerateRefnoOW.copyMatchingFilesOW(sourceFile, TARGET_PATH));
        }
    }
}
