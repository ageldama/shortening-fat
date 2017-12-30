package jhyun.jh.services;

import jhyun.jh.IdCodec;
import jhyun.jh.storage.entities.Url;
import jhyun.jh.storage.repositories.UrlRepository;
import lombok.val;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static jhyun.jh.IdCodec.encode;
import static jhyun.jh.testing_support.TestingFixtures.generateRandomUrl;
import static jhyun.jh.testing_support.TestingFixtures.randomInt;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class UrlShorteningServiceSimpleIdCodecImplTests {
    @Mock
    private UrlRepository urlRepository;

    @InjectMocks
    private UrlShorteningServiceSimpleIdCodecImpl testSubject;

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
        given(urlRepository.findOneByUrl(anyString())).will(invocation -> {
            val id = randomInt();
            return Url.builder()
                    .id(id)
                    .url(invocation.getArgument(0))
                    .shortenedCode(encode(id))
                    .build();
        });
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