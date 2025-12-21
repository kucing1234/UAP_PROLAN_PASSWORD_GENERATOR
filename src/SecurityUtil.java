import java.security.SecureRandom;

public class SecurityUtil {

    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMBER = "0123456789";
    private static final String SYMBOL = "!@#$%^&*()-_=+[]{}<>?/";

    public static String generate(int length, boolean upper, boolean lower, boolean number, boolean symbol) {
        String chars = "";
        if (upper) chars += UPPER;
        if (lower) chars += LOWER;
        if (number) chars += NUMBER;
        if (symbol) chars += SYMBOL;

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
