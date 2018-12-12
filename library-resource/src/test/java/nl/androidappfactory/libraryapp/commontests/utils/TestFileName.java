package nl.androidappfactory.libraryapp.commontests.utils;

import org.junit.Ignore;

    @Ignore
    public class TestFileName {
        private static final String PATH_REQUEST = "/request/";
        private static final String PATH_RESPONSE = "/response/";

        private TestFileName() {
        }

        public static String getPathFileRequest(final String mainFolder, final String fileName) {
            return mainFolder + PATH_REQUEST + fileName;
        }

        public static String getPathFileResponse(final String mainFolder, final String fileName) {
            return mainFolder + PATH_RESPONSE + fileName;
        }
}
