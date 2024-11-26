package pl.business.process.automation.fileDownloader.schedule;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static pl.business.process.automation.fileDownloader.chromeDriver.WebDriverInitializer.initializeWebDriver;

@Slf4j
public class DownloadSchedule {

    private static final String DOWNLOAD_DIRECTORY_PATH = "C:\\Users\\Downloads\\";
    private static final String HARMONOGRAMY_DIRECTORY_PATH = DOWNLOAD_DIRECTORY_PATH + "harmonogramy\\";

    public static void main(String[] args) throws IOException, URISyntaxException {
        WebDriver driver = initializeWebDriver();
        try {
            processUrls(driver);
        } finally {
            driver.quit();
        }
    }

    private static void processUrls(WebDriver driver) throws IOException, URISyntaxException {
        List<String> urls = readLinesFromResourceFile("schedule/urls.txt");
        String[][] ref = readRefData();

        for (String url : urls) {
            processSingleUrl(driver, url, ref);
        }
    }

    private static void processSingleUrl(WebDriver driver, String url, String[][] ref) {
        driver.get(url);
        String refno = getRefNoFromUrl(url, ref);
        try {
            Thread.sleep(1000);
            copyAndRenameDownloadedFile(refno, url);
        } catch (Exception e) {
            handleException(refno, url, e);
        }
    }

    private static String[][] readRefData() throws IOException, URISyntaxException {
        String[][] ref = new String[2][];
        ref[0] = readLinesFromResourceFile("schedule/id.txt").toArray(new String[0]);
        ref[1] = readLinesFromResourceFile("schedule/refno.txt").toArray(new String[0]);
        return ref;
    }

    private static void copyAndRenameDownloadedFile(String refno, String url) throws IOException, InterruptedException {
        File source = waitForDownloadToComplete();
        if (source == null || !source.getName().startsWith("Payment schedule")) {
            printError(url, refno);
            return;
        }
        File dest = new File(HARMONOGRAMY_DIRECTORY_PATH + refno + ".pdf");
        Files.copy(source.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
        log.info("SUCCESS file downloaded and copied: {} for {}", url, refno);
    }

    private static File waitForDownloadToComplete() throws InterruptedException {
        File[] files;
        File latestFile = null;
        long lastMod = Long.MIN_VALUE;

        while (latestFile == null) {
            files = new File(DOWNLOAD_DIRECTORY_PATH).listFiles(File::isFile);

            for (File file : Objects.requireNonNull(files)) {
                if (file.getName().endsWith(".crdownload")) continue;
                if (file.lastModified() > lastMod && file.getName().matches("Payment schedule( \\(\\d+\\))?\\.pdf")) {
                    latestFile = file;
                    lastMod = file.lastModified();
                }
            }
            if (latestFile == null) {
                log.info("No completed download found yet, waiting...");
                Thread.sleep(1000);
            }
        }
        return latestFile;
    }

    private static void printError(String url, String refno) {
        log.error("Error: Failed to download file from URL: {} for refno: {}", url, refno);
    }

    private static void handleException(String refno, String url, Exception e) {
        log.error("Exception occurred for URL: {} for refno: {}", url, refno, e);
    }

    private static List<String> readLinesFromResourceFile(String fileName) throws IOException, URISyntaxException {
        ClassLoader classLoader = DownloadSchedule.class.getClassLoader();
        URL fileUrl = Objects.requireNonNull(classLoader.getResource(fileName));
        Path filePath = Paths.get(fileUrl.toURI());
        return Files.lines(filePath).collect(Collectors.toList());
    }

    private static String getRefNoFromUrl(String url, String[][] ref) {
        String refno = null;
        String id = extractIdFromUrl(url);
        if (id != null) {
            refno = compareIndexWithRefId(id, ref);
        }
        if (refno == null)
            log.info("No match found for URL: {}", url);
        return refno;
    }

    private static String extractIdFromUrl(String url) {
        try {
            int start = url.indexOf("/contracts/") + "/contracts/".length();
            int end = url.indexOf("/pdf_print_document");
            return url.substring(start, end).trim();
        } catch (Exception e) {
            log.error("Unable to extract ID from URL: {}", url, e);
            return null;
        }
    }

    private static String compareIndexWithRefId(String id, String[][] ref) {
        log.info("Checking ID: {}", id);
        for (int ii = 0; ii < ref[0].length; ii++) {
            String refId = ref[0][ii].trim();
            if (refId.equals(id)) {
                log.info(" Match found: {} -> {}", id, ref[1][ii]);
                return ref[1][ii];
            }
        }
        return null;
    }
}
