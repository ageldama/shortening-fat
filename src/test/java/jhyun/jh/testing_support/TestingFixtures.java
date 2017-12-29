package jhyun.jh.testing_support;

import com.github.javafaker.Faker;
import org.apache.commons.lang3.RandomUtils;

public final class TestingFixtures {

    public static int randomInt() {
        return RandomUtils.nextInt(0, Integer.MAX_VALUE);
    }

    private static final Faker faker = new Faker();

    public static String generateRandomUrl() {
        return faker.internet().url();
    }

}
