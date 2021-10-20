package integration;

public class IntegrationTestUtil {

    public static String buildUrl(final int port, final String path) {
        return "http://localhost:" + port + "/" + path;
    }

    private IntegrationTestUtil() {
        // do nothing
    }
}
