package beancmpr.dido.matchables;

import dido.data.GenericData;
import dido.data.MapData;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.List.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class DidoIterableTest {

    @Test
    void testIterateOverSeveralFields() {

        GenericData<String> data = MapData.of(
                "type", "apple",
                "qty", 2,
                "price", 26.3F,
                "offer", true);

        Iterable<Object> iterable = DidoIterable.iterableForFields(data,
                of("qty", "price"));

        List<Object> results = StreamSupport.stream(iterable.spliterator(), false)
                .collect(Collectors.toList());

        assertThat(results, is(of(2, 26.3F)));
    }
}