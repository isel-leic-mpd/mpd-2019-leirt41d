package utils;

public class AsyncUtils {
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        }
        catch(InterruptedException e) {

        }
    }
}
