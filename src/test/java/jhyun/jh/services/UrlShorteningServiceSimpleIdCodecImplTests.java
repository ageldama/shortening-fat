package jhyun.jh.services;

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

import static org.mockito.ArgumentMatchers.*;
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

    private String generateRandomUrl() {
        Faker faker;
        return "";
    }

    @Test
    public void shortenNewOk() throws Exception {
        // record mocks
        given(urlRepository.findOneByUrl(anyString())).willReturn(null);
        given(urlRepository.save(argThat(new ArgumentMatcher<Url>() {
            @Override
            public boolean matches(Url url) {
                return url != null && url.getId() != null;
            }
        }))).will(invocationOnMock -> Url.builder()
                .id(randomInt())
                .url(((Url) invocationOnMock.getArgument(0)).getUrl())
                .build());
        //
        val shortenedCode = testSubject.shorten(generateRandomUrl());
    }

    // TODO: shortenFoundOk

    // TODO: shortenNewButNoId

    @Test
    public void expand() throws Exception {
    }

}