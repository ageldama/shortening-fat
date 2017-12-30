package jhyun.jh.controllers;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static jhyun.jh.testing_support.TestingFixtures.generateRandomUrl;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IndexControllerIntegrationTests {

    @LocalServerPort
    private int port;

    private RestTemplate restTemplate;

    @Before
    public void setUp() {
        val httpClient = HttpClientBuilder.create().disableRedirectHandling().build();
        restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient));
    }

    private String apiBase() {
        return String.format("http://localhost:%s", port);
    }

    private ResponseEntity<String> postUrlsShorten(final String url) {
        val headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        val form = new LinkedMultiValueMap<String, String>();
        form.add("url", url);
        val request = new HttpEntity<MultiValueMap<String, String>>(form, headers);
        return restTemplate.exchange(apiBase() + "/urls/shorten", HttpMethod.POST, request, String.class);
    }

    private ResponseEntity<String> getUrlFromShortenedCode(final String shortenedCode) {
        return restTemplate.exchange(apiBase() + "/{shortenedCode}", HttpMethod.GET, HttpEntity.EMPTY, String.class, shortenedCode);
    }

    @Test
    public void ok() {
        // save url
        val generatedUrl = generateRandomUrl();
        val shortenUrlResponse = postUrlsShorten(generatedUrl);
        assertThat(shortenUrlResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        val shortenedCode = shortenUrlResponse.getBody();
        assertThat(shortenedCode).isNotEmpty();
        // expand url
        val expandUrlResponse = getUrlFromShortenedCode(shortenedCode);
        assertThat(expandUrlResponse.getStatusCode().is3xxRedirection()).isTrue();
        val loc = expandUrlResponse.getHeaders().getLocation();
        assertThat(loc.toString()).isEqualTo(generatedUrl);
    }

    @Test
    public void notFound() {
        // save url
        val generatedUrl = generateRandomUrl();
        val shortenUrlResponse = postUrlsShorten(generatedUrl);
        assertThat(shortenUrlResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        val shortenedCode = shortenUrlResponse.getBody();
        assertThat(shortenedCode).isNotEmpty();
        //
        val notFoundShortenedCode = shortenedCode + shortenedCode;
        assertThatThrownBy(()->getUrlFromShortenedCode(notFoundShortenedCode))
                .isInstanceOf(HttpClientErrorException.class)
                .extracting("statusCode").containsOnly(HttpStatus.NOT_FOUND);
    }
}
