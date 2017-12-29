package jhyun.jh;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.Test;

import static jhyun.jh.IdCodec.decode;
import static jhyun.jh.IdCodec.encode;
import static org.junit.Assert.assertEquals;

@Slf4j
public class ShortenerHelloTests {

    @Test
    public void encodeAndDecode() {
        for (int n = 0 ; n < 999999 ; n ++) {
            val s = encode(n);
            val m = decode(s);
            //log.warn("f({}) = {} -> {}", n, s, m);
            assertEquals(n, m);
            if (s.length() > 8) {
                log.warn("f({}) = {} -> {}", n, s, m);
                break;
            }
        }
    }

    @Test(expected = RuntimeException.class)
    public void encodeLessThanZero() {
        encode(-1);
    }

    @Test(expected = RuntimeException.class)
    public void decodeNull() {
        decode(null);
    }

    @Test
    public void encodeZero() {
        assertEquals(encode(0), "a");
    }

    @Test
    public void decodeEmptyString() {
        assertEquals(decode(""), 0);
    }

}
