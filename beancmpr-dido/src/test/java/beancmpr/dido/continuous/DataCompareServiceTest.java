package beancmpr.dido.continuous;

import dido.data.DataSchema;
import dido.data.DidoData;
import dido.data.immutable.MapData;
import dido.data.schema.SchemaBuilder;
import org.junit.jupiter.api.Test;
import org.oddjob.beancmpr.comparers.NumericComparer;
import org.oddjob.beancmpr.composite.ComparersByNameType;
import org.oddjob.beancmpr.continuous.SourceStrategies;
import org.oddjob.beancmpr.results.MatchResultType;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

class DataCompareServiceTest {

    @Test
    void simpleComparison() throws InterruptedException {

        List<DidoData> dataX = List.of(
                MapData.of("Fruit", "Apple", "Quantity", 5, "Price", 53.2),
                MapData.of("Fruit", "Pear", "Quantity", 7, "Price", 42.1)
        );

        List<DidoData> dataY = List.of(
                MapData.of("Fruit", "Orange", "Quantity", 3, "Price", 98.2),
                MapData.of("Fruit", "Pear", "Quantity", 7, "Price", 63.7)
        );

        DataCompareService test = new DataCompareService();
        test.setExceptionListener(System.err::println);
        test.setSourceStrategy(SourceStrategies.Strategy.ONE_FOR_ONE);
        test.setKeys(new String[] { "Fruit"});
        test.setValues(new String[] { "Quantity", "Price" });

        ComparersByNameType comparersByName = new ComparersByNameType();
        comparersByName.setComparers("Price",
                NumericComparer.with()
                        .deltaFormat("0.00")
                        .toFactory());

        test.setComparersByName(comparersByName);

        BlockingQueue<DidoData> results = new LinkedBlockingQueue<>();

        test.setTo(o -> results.add((DidoData) o));

        test.start();

        test.acceptX(dataX.get(0));
        test.acceptY(dataY.get(0));
        test.acceptX(dataX.get(1));
        test.acceptY(dataY.get(1));

        DataSchema expectedSchema = SchemaBuilder.newInstance()
                .addNamed("MatchType", MatchResultType.class)
                .addNamed("Fruit", String.class)
                .addNamed("X_Quantity", Integer.class)
                .addNamed("Y_Quantity", Integer.class)
                .addNamed("Quantity_", String.class)
                .addNamed("X_Price", Double.class)
                .addNamed("Y_Price", Double.class)
                .addNamed("Price_", String.class)
                .build();

        DidoData first = results.poll(3, TimeUnit.SECONDS);

        assertThat(first, notNullValue());
        assertThat(first.getSchema(), is(expectedSchema));

        assertThat(first, is(DidoData.withSchema(expectedSchema).of(
                MatchResultType.NOT_EQUAL, "Pear", 7, 7, "", 42.1, 63.7, "21.60 (51.3%)")));

        assertThat(results.poll(3, TimeUnit.SECONDS), is(DidoData.withSchema(expectedSchema).of(
                MatchResultType.Y_MISSING, "Apple", 5, null, null, 53.2, null, null)));

        assertThat(results.poll(3, TimeUnit.SECONDS), is(DidoData.withSchema(expectedSchema).of(
                MatchResultType.X_MISSING, "Orange", null, 3, null, null, 98.2, null)));

        test.stop();

    }


}