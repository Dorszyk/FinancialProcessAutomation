package pl.business.process.automation.ow.company1;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class FileRename {

    private static final String COMPANY_ONE_REFNO_TXT = "companyOne/refno.txt";
    private static final String DIRECTORY_PATH = "C:\\Users\\CompanyOne\\";

    public static void main(String[] args) {
        List<Long> refNumbers = loadReferenceNumbers();
        File[] listOfFiles = findFilesAt();
        if (listOfFiles == null) {
            log.error("No files found in the directory, exiting.");
            return;
        }
        renameFiles(refNumbers, listOfFiles);
    }

    private static File[] findFilesAt() {
        File folder = new File(DIRECTORY_PATH);
        File[] files = folder.listFiles();
        if (files != null) {
            Arrays.sort(files, Comparator.comparingInt(FileRename::extractPartNumber));
        }
        return files;
    }

    private static int extractPartNumber(File file) {
        String fileName = file.getName();
        Pattern pattern = Pattern.compile("Part(\\d+)");
        Matcher matcher = pattern.matcher(fileName);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return Integer.MAX_VALUE;
    }

    private static void renameFiles(List<Long> refNumbers, File[] listOfFiles) {
        if (refNumbers.size() < listOfFiles.length) {
            log.error("Not enough reference numbers for the number of files. Exiting.");
            return;
        }
        for (int i = 0; i < listOfFiles.length; i++) {
            File file = listOfFiles[i];
            String newFileName = DIRECTORY_PATH + refNumbers.get(i) + ".pdf";
            renameFile(file, newFileName);
        }
        log.info("File rename completed.");
    }

    private static void renameFile(File file, String newFileName) {
        File renamedFile = new File(newFileName);
        if (file.renameTo(renamedFile)) {
            log.info("Successfully renamed: {} -> {}", file.getName(), renamedFile.getName());
        } else {
            log.error("Failed to rename: {}", file.getName());
        }
    }

    private static List<Long> loadReferenceNumbers() {
        List<Long> refNumbers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(FileRename.class.getClassLoader().getResourceAsStream(COMPANY_ONE_REFNO_TXT))))) {
            String line;
            while ((line = reader.readLine()) != null) {
                refNumbers.add(Long.parseLong(line));
            }
        } catch (IOException e) {
            log.error("Error loading reference numbers: {}", e.getMessage());
        } catch (NumberFormatException e) {
            log.error("Error parsing reference numbers: {}", e.getMessage());
        }
        return refNumbers;
    }
}
