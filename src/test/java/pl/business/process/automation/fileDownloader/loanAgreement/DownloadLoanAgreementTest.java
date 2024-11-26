package pl.business.process.automation.fileDownloader.loanAgreement;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openqa.selenium.WebDriver;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

class DownloadLoanAgreementTest {

    private final WebDriver webDriver = Mockito.mock(WebDriver.class);

    @Test
    public void shouldReadLinesFromResourceFile() {
        List<String> lines = DownloadLoanAgreement.readLinesFromResourceFile();
        assert lines != null : "Lines should not be null";
        assert lines.size() > 0 : "Lines should not be empty"; 
    }

    @Test
    public void shouldProcessLink() throws Exception {
        int counter = 5;
        String link = "http://www.test.com";
        Mockito.doNothing().when(webDriver).get(any(String.class));

        counter = DownloadLoanAgreement.processLink(webDriver, link, counter);

        assert (counter == 6);
        Mockito.verify(webDriver, times(1)).get(link);
    }

    @Test
    public void shouldProcessLinks() {
        List<String> links = List.of("http://www.test1.com", "http://www.test2.com", "http://www.test3.com");
        Mockito.doNothing().when(webDriver).get(any(String.class));

        DownloadLoanAgreement.processLinks(webDriver, links);

        Mockito.verify(webDriver, times(links.size())).get(any(String.class));
    }

}