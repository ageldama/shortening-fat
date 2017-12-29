package jhyun.jh.services;

import com.github.javafaker.Faker;
import jhyun.jh.storage.entities.Url;
import jhyun.jh.storage.repositories.UrlRepository;
import lombok.val;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class UrlShorteningServiceSimpleIdCodecImplTests {
    @Mock
    private UrlRepository urlRepository;

    @InjectMocks
    private UrlShorteningServiceSimpleIdCodecImpl testSubject;

    private int randomInt() {
        return RandomUtils.nextInt(0, Integer.MAX_VALUE);
    }

    private Faker faker = new Faker();

    private String generateRandomUrl() {
        return faker.internet().url();
    }

    @Test
    public void shortenNewOk() {
        // record mocks
        given(urlRepository.findOneByUrl(anyString())).willReturn(null);
        given(urlRepository.save(argThat(new ArgumentMatcher<Url>() {
            @Override
            public boolean matches(Url url) {
                return url != null && url.getId() == null;
            }
        }))).will(invocationOnMock -> Url.builder()
                .id(randomInt())
                .url(((Url) invocationOnMock.getArgument(0)).getUrl())
                .build());
        //
        val shortenedCode = testSubject.shorten(generateRandomUrl());
        assertThat(shortenedCode).isNotEmpty();
    }

    @Test
    public void shortenFoundAndOk() {
        given(urlRepository.findOneByUrl(anyString())).will(invocation -> Url.builder()
                .id(randomInt())
                .url(invocation.getArgument(0))
                .build());
        //
        val givenUrl = generateRandomUrl();
        val shortenedCode = testSubject.shorten(givenUrl);
        assertThat(shortenedCode).isNotEmpty();
    }

    @Test
    public void expandOk() {
        val savedUrl = generateRandomUrl();
        given(urlRepository.findOne(anyInt())).will(invocation -> Url.builder()
                .id(invocation.getArgument(0))
                .url(savedUrl)
                .build());
        //
        val result = testSubject.expand("foobar");
        assertThat(result).isEqualTo(Optional.of(savedUrl));
    }

    @Test
    public void expandNotFound() {
        val result = testSubject.expand("foobar");
        assertThat(result).isNotPresent();
    }
}