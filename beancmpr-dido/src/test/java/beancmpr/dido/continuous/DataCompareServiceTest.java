package beancmpr.dido.continuous;

import dido.data.DataSchema;
import dido.data.DidoData;
import dido.data.immutable.MapData;
import dido.data.schema.SchemaBuilder;
import org.junit.jupiter.api.Test;
import org.oddjob.Oddjob;
import org.oddjob.OddjobLookup;
import org.oddjob.arooa.convert.ArooaConversionException;
import org.oddjob.beancmpr.comparers.NumericComparer;
import org.oddjob.beancmpr.composite.ComparersByNameType;
import org.oddjob.beancmpr.continuous.SourceStrategies;
import org.oddjob.beancmpr.results.MatchResultType;
import org.oddjob.state.ParentState;
import org.oddjob.tools.ConsoleCapture;
import org.oddjob.tools.StateSteps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.oddjob.tools.StateSteps.definitely;

class DataCompareServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(DataCompareServiceTest.class);

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
        test.exceptionListener(System.err::println);
        test.setSourceStrategy(SourceStrategies.Strategy.ONE_FOR_ONE);
        test.setKeys(new String[]{"Fruit"});
        test.setValues(new String[]{"Quantity", "Price"});

        ComparersByNameType comparersByName = new ComparersByNameType();
        comparersByName.setComparers("Price",
                NumericComparer.with()
                        .deltaFormat("0.00")
                        .toFactory());

        test.setComparersByName(comparersByName);

        BlockingQueue<DidoData> results = new LinkedBlockingQueue<>();

        test.setTo(o -> results.add((DidoData) o));

        test.start();

        test.getX().accept(dataX.get(0));
        test.getY().accept(dataY.get(0));
        test.getX().accept(dataX.get(1));
        test.getY().accept(dataY.get(1));

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

    // This test may fail on slow cpus as it
    @Test
    void oddjobExample() throws ArooaConversionException, InterruptedException {

        File file = new File(Objects.requireNonNull(
                getClass().getResource("/examples/ContinuousCompare.xml")).getFile());

        Oddjob oddjob = new Oddjob();
        oddjob.setFile(file);

        StateSteps states = new StateSteps(oddjob);
        states.startCheck(definitely(ParentState.READY),
                definitely(ParentState.EXECUTING),
                definitely(ParentState.ACTIVE),
                definitely(ParentState.STARTED),
                definitely(ParentState.ACTIVE),
                definitely(ParentState.COMPLETE));

        ConsoleCapture console = new ConsoleCapture();
        try (ConsoleCapture.Close ignored = console.captureConsole()) {

            oddjob.run();

            states.checkWait();
        }

        console.dump(logger);

        List<String> lines = console.getAsList()
                .stream()
                .map(String::stripTrailing)
                .toList();

        List<String> expected = new BufferedReader(
                new InputStreamReader(Objects.requireNonNull(
                        getClass().getResourceAsStream("/examples/ContinuousCompareOut.txt"))))
                .lines().map(String::stripTrailing).toList();

        assertThat(lines, is(expected));

        assertThat(oddjob.lastStateEvent().getState(), is(ParentState.COMPLETE));

        DataSchema expectedSchema = SchemaBuilder.newInstance()
                .addNamed("MatchType", MatchResultType.class)
                .addNamed("Fruit", String.class)
                .addNamed("Colour", String.class)
                .addNamed("X_Qty", Integer.class)
                .addNamed("Y_Qty", Integer.class)
                .addNamed("Qty_", String.class)
                .addNamed("X_Price", Double.class)
                .addNamed("Y_Price", Double.class)
                .addNamed("Price_", String.class)
                .addNamed("X_Comments", String.class)
                .addNamed("Y_Comments", String.class)
                .build();

        @SuppressWarnings("unchecked")
        List<DidoData> results = new OddjobLookup(oddjob).lookup("results.list", List.class);

        assertThat(results.getFirst().getSchema(), is(expectedSchema));

        assertThat(results, contains(
                DidoData.withSchema(expectedSchema)
                        .of(MatchResultType.EQUAL, "apple", "red", 5, 5, "", 22.4, 22.4, "", "crunchy", "crunchy"),
                DidoData.withSchema(expectedSchema)
                        .of(MatchResultType.NOT_EQUAL, "apple", "green", 6, 7, "6<>7", 23.2, 23.2, "", "crisp", "crisp"),
                DidoData.withSchema(expectedSchema)
                        .of(MatchResultType.EQUAL, "banana", "yellow", 3, 3, "", 46.4, 46.4, "", "bent", "bent"),
                DidoData.withSchema(expectedSchema)
                        .of(MatchResultType.NOT_EQUAL, "orange", "orange", 2, 2, "", 23.5, 57.2, "33.7 (143.4%)", "healthy", "healthy"),
                DidoData.withSchema(expectedSchema)
                        .of(MatchResultType.Y_MISSING, "pear", "green", 8, null, null, 37.0, null, null, "shapely"),
                DidoData.withSchema(expectedSchema)
                        .of(MatchResultType.X_MISSING, "pear", "gr", null, 8, null, null, 37.0, null, null, "shapely")
        ));

    }

}