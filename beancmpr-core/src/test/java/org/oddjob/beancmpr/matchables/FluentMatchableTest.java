package org.oddjob.beancmpr.matchables;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

class FluentMatchableTest {

    @Test
    void keysOnly() {

        Matchable m1 = FluentMatchable.withKeys(42)
                .make();

        assertThat(m1.getKeys(), contains(42));
        assertThat(m1.getValues().isEmpty(), is(true));
        assertThat(m1.getOthers().isEmpty(), is(true));
    }

    @Test
    void keysAndValueOnly() {

        Matchable m1 = FluentMatchable.withKeys(42)
                .andValues("Apple")
                .make();

        assertThat(m1.getKeys(), contains(42));
        assertThat(m1.getValues(), contains("Apple"));
        assertThat(m1.getOthers().isEmpty(), is(true));
    }

    @Test
    void keysAndValuesAndOthers() {

        Matchable m1 = FluentMatchable.withKeys(42)
                .andValues("Apple")
                .andOthers("Crunchy")
                .make();

        assertThat(m1.getKeys(), contains(42));
        assertThat(m1.getValues(), contains("Apple"));
        assertThat(m1.getOthers(), contains("Crunchy"));

        MatchableMetaData meta = m1.getMetaData();
        assertThat(meta.getKeyProperties(), is(ImmutableCollection.of("k1")));
        assertThat(meta.getValueProperties(), is(ImmutableCollection.of("v1")));
        assertThat(meta.getOtherProperties(), is(ImmutableCollection.of("o1")));
        assertThat(meta.getPropertyType("k1"), is(Integer.class));
        assertThat(meta.getPropertyType("v1"), is(String.class));
        assertThat(meta.getPropertyType("o1"), is(String.class));
    }
}