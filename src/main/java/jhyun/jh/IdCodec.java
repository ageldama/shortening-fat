package jhyun.jh;

import lombok.val;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * https://stackoverflow.com/questions/742013/how-to-code-a-url-shortener
 */
public final class IdCodec {
    private static final String DIGITS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int BASE = DIGITS.length();

    public static String encode(final int num) {
        checkArgument(num >= 0);
        if (num == 0) return "a";
        //
        int n = num;
        val sb = new StringBuilder();
        while (n > 0) {
            sb.append(DIGITS.charAt(n % BASE));
            n /= BASE;
        }
        return sb.reverse().toString();
    }

    public static int decode(final String str) {
        checkArgument(str != null);
        //
        int num = 0;
        for (int i = 0; i < str.length(); i++)
            num = num * BASE + DIGITS.indexOf(str.charAt(i));
        return num;
    }
}
