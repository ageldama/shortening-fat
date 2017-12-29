package jhyun.jh.services;

import lombok.val;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static jhyun.jh.testing_support.TestingFixtures.generateRandomUrl;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UrlShorteningServiceIntegrationTests {

    @Autowired
    private UrlShorteningService testSubject;

    @Test
    public void shortenNewAndExpandBack() {
        val originalUrl_1 = generateRandomUrl();
        val originalUrl_2 = generateRandomUrl();
        assertThat(originalUrl_1).isNotEqualTo(originalUrl_2);
        //
        val shortened_1 = testSubject.shorten(originalUrl_1);
        val shortened_2 = testSubject.shorten(originalUrl_2);
        assertThat(shortened_1).isNotEqualTo(shortened_2);
        val shortened_1_again = testSubject.shorten(originalUrl_1);
        assertThat(shortened_1_again).isEqualTo(shortened_1);
        //
        val expanded_1 = testSubject.expand(shortened_1);
        val expanded_2 = testSubject.expand(shortened_2);
        assertThat(expanded_1).isNotEqualTo(expanded_2);
        assertThat(expanded_1).isEqualTo(originalUrl_1);
        assertThat(expanded_1).isEqualTo(originalUrl_2);
    }

}
