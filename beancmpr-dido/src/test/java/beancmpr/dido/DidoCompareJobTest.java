package beancmpr.dido;

import dido.data.ArrayData;
import dido.data.DataSchema;
import dido.data.GenericData;
import dido.data.SchemaBuilder;
import org.junit.jupiter.api.Test;
import org.oddjob.Oddjob;
import org.oddjob.OddjobLookup;
import org.oddjob.arooa.convert.ArooaConversionException;
import org.oddjob.beancmpr.results.MatchResultType;
import org.oddjob.state.ParentState;

import java.io.File;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

class DidoCompareJobTest {

    @Test
    void testDidoCompareInOddjob() throws ArooaConversionException {

        File file = new File(Objects.requireNonNull(
                getClass().getResource("/examples/DidoCompare.xml")).getFile());

        Oddjob oddjob = new Oddjob();
        oddjob.setFile(file);

        oddjob.run();

        assertThat(oddjob.lastStateEvent().getState(), is(ParentState.COMPLETE));

        DataSchema<String> expectedSchema = SchemaBuilder.forStringFields()
                .addField("MatchType", MatchResultType.class)
                .addField("Fruit", String.class)
                .addField("Colour", String.class)
                .addField("X_Qty", Integer.class)
                .addField("Y_Qty", Integer.class)
                .addField("Qty_", String.class)
                .addField("X_Price", Double.class)
                .addField("Y_Price", Double.class)
                .addField("Price_", String.class)
                .addField("X_Comments", String.class)
                .addField("Y_Comments", String.class)
                .build();

        List<GenericData<String>> results = new OddjobLookup(oddjob).lookup("bus.to", List.class);

        assertThat(results.get(0).getSchema(), is(expectedSchema));

        assertThat(results, contains(
                ArrayData.valuesFor(expectedSchema)
                        .of(MatchResultType.NOT_EQUAL, "apple", "green", 6, 7, "6<>7", 23.2, 23.2, "", "crisp", "crisp"),
                ArrayData.valuesFor(expectedSchema)
                        .of(MatchResultType.EQUAL, "apple", "red", 5, 5, "", 22.4, 22.4, "", "crunchy", "crunchy"),
                ArrayData.valuesFor(expectedSchema)
                        .of(MatchResultType.EQUAL, "banana", "yellow", 3, 3, "", 46.4, 46.4, "", "bent", "bent"),
                ArrayData.valuesFor(expectedSchema)
                        .of(MatchResultType.NOT_EQUAL, "orange", "orange", 2, 2, "", 23.5, 57.2, "33.7 (143.4%)", "healthy", "healthy"),
                ArrayData.valuesFor(expectedSchema)
                        .of(MatchResultType.X_MISSING, "pear", "gr", null, 8, null, null, 37.0, null, null, "shapely"),
                ArrayData.valuesFor(expectedSchema)
                        .of(MatchResultType.Y_MISSING, "pear", "green", 8, null, null, 37.0, null, null, "shapely")

        ));
    }

}
