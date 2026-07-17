package beancmpr.dido.matchables;

import dido.data.DidoData;
import org.junit.jupiter.api.Test;
import org.oddjob.beancmpr.matchables.ImmutableCollection;

import java.util.function.Function;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;

class DidoImmutableCollectionTest {

    @Test
    void factory() {

        DidoData data = DidoData.builder()
                .with("Fruit", "Apple")
                .with("Qty", 5)
                .with("Price", 52.5)
                .with("Colour", "Red")
                .build();


        Function<DidoData, ImmutableCollection<Object>>
                iterable = DidoImmutableCollection.factoryFrom(data.getSchema(),
                "Qty", "Price");

        ImmutableCollection<Object> result = iterable.apply(data);

        assertThat(result, hasItems(5, 52.5));

    }
}