package jhyun.jh.controllers;

import jhyun.jh.controllers.exceptions.SavedUrlNotFoundException;
import jhyun.jh.services.SavedUrlListingService;
import jhyun.jh.services.UrlShorteningService;
import lombok.val;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;

import static jhyun.jh.testing_support.TestingFixtures.generateRandomShortenedCode;
import static jhyun.jh.testing_support.TestingFixtures.generateRandomUrl;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class IndexControllerTests {
    @Mock
    private UrlShorteningService urlShorteningService;

    @Mock
    private SavedUrlListingService savedUrlListingService;  // NOTE: never used

    @InjectMocks
    private IndexController testSubject;

    @Test
    public void shortenUrlOk() {
        val sampleShortenCode = generateRandomShortenedCode();
        given(urlShorteningService.shorten(anyString())).willReturn(sampleShortenCode);
        //
        val randomUrl = generateRandomUrl();
        val result = testSubject.shortenUrl(randomUrl);
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(sampleShortenCode);
        //
        verify(urlShorteningService, times(1)).shorten(eq(randomUrl));
    }

    @Test
    public void redirectToUrlOk() {
        val sampleUrl = generateRandomUrl();
        given(urlShorteningService.expand(anyString())).willReturn(Optional.of(sampleUrl));
        //
        val randomShortenedCode = generateRandomShortenedCode();
        val result = testSubject.redirectToUrl(randomShortenedCode);
        assertThat(result).isNotNull().isInstanceOf(RedirectView.class);
        assertThat(result.getUrl()).isEqualTo(sampleUrl);
        //
        verify(urlShorteningService, times(1)).expand(eq(randomShortenedCode));
    }

    @Test
    public void redirectToUrlNotFound() {
        given(urlShorteningService.expand(anyString())).willReturn(Optional.empty());
        assertThatThrownBy(() -> testSubject.redirectToUrl(generateRandomShortenedCode()))
                .isInstanceOf(SavedUrlNotFoundException.class);
    }
}