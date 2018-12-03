package util;

import java.util.concurrent.ThreadLocalRandom;

public class IdUtil {

    public static int generateID(int length) {
        int number = 1;
        number = number * 10 * (length - 1);

        return ThreadLocalRandom.current().nextInt(number, number*10);
    }
}
