package org.oddjob.beancmpr;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.notNullValue;

public class TestCase {


    public static void assertSame(Object expected, Object actual) {

        assertThat(actual, sameInstance(expected));
    }

    public static void assertSame(String message, Object expected, Object actual) {

        assertThat(message, actual, sameInstance(expected));
    }

    public static void assertEquals(Object expected, Object actual) {

        assertThat(actual, is(expected));
    }

    public static void assertEquals(String message, Object expected, Object actual) {

        assertThat(message, actual, is(expected));
    }

    public static void assertEquals(long expected, long actual) {

        assertThat(actual, is(expected));
    }

    public static void assertEquals(String message, long expected, long actual) {

        assertThat(message, actual, is(expected));
    }
    public static void assertEquals(double expected, double actual, double tolerance) {

        assertThat(actual, closeTo(expected, tolerance));
    }

    public static void assertTrue(boolean actual) {

        assertThat("Expected true", actual);
    }

    public static void assertTrue(String message, boolean actual) {

        assertThat(message, actual);
    }
    public static void assertFalse(boolean actual) {

        assertThat("Expected false", !actual);
    }

    public static void assertFalse(String message, boolean actual) {

        assertThat(message, !actual);
    }

    public static void assertNull(Object actual) {
        assertThat(actual, nullValue());
    }

    public static void assertNull(String message, Object actual) {
        assertThat(message, actual, nullValue());
    }

    public static void assertNotNull(Object actual) {
        assertThat(actual, notNullValue());
    }

    public static void assertNotNull(String message, Object actual) {
        assertThat(message, actual, notNullValue());
    }

    public static void fail() {

        //noinspection ConstantConditions
        assertThat("Fail!", false);
    }

    public static void fail(String message) {

        //noinspection ConstantConditions
        assertThat(message, false);
    }
}
