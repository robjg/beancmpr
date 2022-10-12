package org.oddjob.beancmpr.results;

public enum MatchResultType {

    /**
     * Two things matched by key and their values were equal.
     */
    EQUAL,

    /**
     * No X data was found matching the Y key.
     */
    X_MISSING,

    /**
     * No Y data was found matching the X key.
     */
    Y_MISSING,

    /**
     * Two things matched by key but one or more of there values
     * were not equal.
     */
    NOT_EQUAL,
}
