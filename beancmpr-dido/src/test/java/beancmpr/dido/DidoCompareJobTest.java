package beancmpr.dido;

import dido.data.DataSchema;
import dido.data.DidoData;
import dido.data.schema.SchemaBuilder;
import org.junit.jupiter.api.Test;
import org.oddjob.Oddjob;
import org.oddjob.OddjobLookup;
import org.oddjob.arooa.convert.ArooaConversionException;
import org.oddjob.beancmpr.results.MatchResultType;
import org.oddjob.state.ParentState;
import org.oddjob.tools.ConsoleCapture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

class DidoCompareJobTest {

    private static final Logger logger = LoggerFactory.getLogger(DidoCompareJobTest.class);

    @Test
    void testDidoCompareInOddjob() throws ArooaConversionException {

        File file = new File(Objects.requireNonNull(
                getClass().getResource("/examples/DidoCompare.xml")).getFile());

        Oddjob oddjob = new Oddjob();
        oddjob.setFile(file);

        ConsoleCapture console = new ConsoleCapture();
        try (ConsoleCapture.Close ignored = console.captureConsole()) {

            oddjob.run();
        }

        console.dump(logger);

        List<String> lines = console.getAsList()
                .stream()
                .map(String::stripTrailing)
                .toList();

        List<String> expected = new BufferedReader(
                new InputStreamReader(Objects.requireNonNull(
                        getClass().getResourceAsStream("/examples/DidoCompareOut.txt"))))
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
                        .of(MatchResultType.NOT_EQUAL, "apple", "green", 6, 7, "6<>7", 23.2, 23.2, "", "crisp", "crisp"),
                DidoData.withSchema(expectedSchema)
                        .of(MatchResultType.EQUAL, "apple", "red", 5, 5, "", 22.4, 22.4, "", "crunchy", "crunchy"),
                DidoData.withSchema(expectedSchema)
                        .of(MatchResultType.EQUAL, "banana", "yellow", 3, 3, "", 46.4, 46.4, "", "bent", "bent"),
                DidoData.withSchema(expectedSchema)
                        .of(MatchResultType.NOT_EQUAL, "orange", "orange", 2, 2, "", 23.5, 57.2, "33.7 (143.4%)", "healthy", "healthy"),
                DidoData.withSchema(expectedSchema)
                        .of(MatchResultType.X_MISSING, "pear", "gr", null, 8, null, null, 37.0, null, null, "shapely"),
                DidoData.withSchema(expectedSchema)
                        .of(MatchResultType.Y_MISSING, "pear", "green", 8, null, null, 37.0, null, null, "shapely")

        ));
    }

    @Test
    void didoComparersByType() throws ArooaConversionException {

        File file = new File(Objects.requireNonNull(
                getClass().getResource("/examples/DidoComparersByType.xml")).getFile());

        Oddjob oddjob = new Oddjob();
        oddjob.setFile(file);

        ConsoleCapture console = new ConsoleCapture();
        try (ConsoleCapture.Close ignored = console.captureConsole()) {

            oddjob.run();
        }

        console.dump(logger);

        List<String> lines = console.getAsList()
                .stream()
                .map(String::stripTrailing)
                .toList();

        List<String> expected = new BufferedReader(
                new InputStreamReader(Objects.requireNonNull(
                        getClass().getResourceAsStream("/examples/DidoComparersByTypeOut.txt"))))
                .lines().map(String::stripTrailing).toList();

        assertThat(lines, is(expected));

        assertThat(oddjob.lastStateEvent().getState(), is(ParentState.COMPLETE));

    }

    @Test
    void didoComparersByName() throws ArooaConversionException {

        File file = new File(Objects.requireNonNull(
                getClass().getResource("/examples/DidoComparersByName.xml")).getFile());

        Oddjob oddjob = new Oddjob();
        oddjob.setFile(file);

        ConsoleCapture console = new ConsoleCapture();
        try (ConsoleCapture.Close ignored = console.captureConsole()) {

            oddjob.run();
        }

        console.dump(logger);

        List<String> lines = console.getAsList()
                .stream()
                .map(String::stripTrailing)
                .toList();

        List<String> expected = new BufferedReader(
                new InputStreamReader(Objects.requireNonNull(
                        getClass().getResourceAsStream("/examples/DidoComparersByNameOut.txt"))))
                .lines().map(String::stripTrailing).toList();

        assertThat(lines, is(expected));

        assertThat(oddjob.lastStateEvent().getState(), is(ParentState.COMPLETE));

    }

}
