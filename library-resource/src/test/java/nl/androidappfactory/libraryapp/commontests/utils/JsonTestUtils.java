package nl.androidappfactory.libraryapp.commontests.utils;


import org.json.JSONException;
import org.junit.Ignore;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.io.InputStream;
import java.util.Scanner;

@Ignore
public class JsonTestUtils {
    public static final String BASE_JSON_DIR = "json/";

    private JsonTestUtils() {
    }

    public static String readJsonFile(final String relativePath) {
        final InputStream is = JsonTestUtils.class.getClassLoader().getResourceAsStream(BASE_JSON_DIR + relativePath);
        try (Scanner scanner = new Scanner(is)) {
            return scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
        }
    }

    public static void assertJsonMatchesExpectedJson(final String actualJson, final String expectedJson) {
        try {
            JSONAssert.assertEquals(expectedJson, actualJson, JSONCompareMode.NON_EXTENSIBLE);
        } catch (final JSONException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
