package pl.business.process.automation.fileDownloader.loanAgreement;

import lombok.extern.slf4j.Slf4j;
import pl.business.process.automation.fileDownloader.chromeDriver.WebDriverInitializer;
import org.openqa.selenium.WebDriver;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
public class DownloadLoanAgreement {

    private static final String DOWNLOAD_PATH = "C:\\Users\\Downloads";
    private static final String URLS_FILE_RESOURCE = "loanAgreement/urls.txt";

    public static void main(String[] args) throws IOException, URISyntaxException {
        WebDriver webDriver = WebDriverInitializer.initializeWebDriver();
        List<String> links = readLinesFromResourceFile();
        processLinks(webDriver, links);
        webDriver.quit();
    }

    public static void processLinks(WebDriver webDriver, List<String> links) {
        int counter = 0;
        for (String link : links) {
            counter = processLink(webDriver, link, counter);
        }
    }

    public static int processLink(WebDriver webDriver, String link, int counter) {
        try {
            webDriver.get(link);
            waitForDownloadToComplete(new File(DownloadLoanAgreement.DOWNLOAD_PATH));
            log.info("SUCCESS: Downloaded: {} Nr :{}", link, ++counter);
        } catch (Exception e) {
            log.error("ERROR for {} Nr :{}", link, ++counter);
        }
        return counter;
    }

    public static List<String> readLinesFromResourceFile() {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(DownloadLoanAgreement.class.getClassLoader()
                        .getResourceAsStream(DownloadLoanAgreement.URLS_FILE_RESOURCE))))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            log.error("Error reading file: {}", e.getMessage());
        }
        return lines;
    }

    public static void waitForDownloadToComplete(File folder) throws InterruptedException {
        File[] files;
        boolean downloading;
        do {
            Thread.sleep(1000);
            files = folder.listFiles((dir, name) -> name.endsWith(".crdownload"));
            downloading = files != null && files.length > 0;
        } while (downloading);
    }
}

