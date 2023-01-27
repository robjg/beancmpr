package org.oddjob.beancmpr.matchables;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

public class ImmutableCollectionImplTest {

    @Test
    public void testFromList() {

        ImmutableCollection<Integer> ints = ImmutableCollection.of(List.of(1, 2, 3));

        assertThat(ints.size(), is(3));

        List<Integer> back = new ArrayList<>(3);

        for (Integer i : ints) {
            back.add(i);
        }

        assertThat(back, contains(1, 2, 3));
    }

    @Test
    public void testFromNulls() {

        ImmutableCollection<?> nulls = ImmutableCollection.of(null, null, null);

        assertThat(nulls.size(), is(3));

        List<Object> back = new ArrayList<>(3);

        for (Object o : nulls) {
            back.add(o);
        }

        assertThat(back, contains(null, null, null));
    }

    @Test
    public void testFromListWithNulls() {

        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(null);
        list.add(3);

        ImmutableCollection<Integer> ints = ImmutableCollection.of(list);

        assertThat(ints.size(), is(3));

        List<Object> back = new ArrayList<>(3);

        for (Object o : ints) {
            back.add(o);
        }

        assertThat(back, contains(1, null, 3));
    }
}