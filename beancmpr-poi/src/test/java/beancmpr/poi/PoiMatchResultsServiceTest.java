package beancmpr.poi;

import beancmpr.test.OurDirs;
import dido.data.GenericData;
import dido.how.DataIn;
import org.junit.jupiter.api.Test;
import org.oddjob.Oddjob;
import org.oddjob.OddjobLookup;
import org.oddjob.beancmpr.results.MatchResult;
import org.oddjob.dido.poi.data.PoiWorkbook;
import org.oddjob.dido.poi.layouts.DataRows;
import org.oddjob.state.ParentState;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class PoiMatchResultsServiceTest {

    @Test
    void testDidoCompareInOddjob() throws Exception {

        Path workDir = OurDirs.workPathDir(PoiMatchResultsServiceTest.class, true);

        Properties properties = new Properties();
        properties.setProperty("work.dir", workDir.toString());

        File file = new File(Objects.requireNonNull(
                getClass().getResource("/examples/DidoCompare.xml")).getFile());

        Oddjob oddjob = new Oddjob();
        oddjob.setFile(file);
        oddjob.setProperties(properties);

        oddjob.run();

        assertThat(oddjob.lastStateEvent().getState(), is(ParentState.COMPLETE));

        List<MatchResult> results = new OddjobLookup(oddjob).lookup("bus.to", List.class);

        assertThat(results.size(), is(6));

        PoiWorkbook workbook = new PoiWorkbook();
        workbook.setInput(Files.newInputStream(workDir.resolve("MatchResults.xlsx")));

        DataRows dataRows = new DataRows();
        dataRows.setWithHeader(true);

        DataIn<String> dataIn = dataRows.inFrom(workbook);

        List<GenericData<String>> list = new ArrayList<>();
        for (GenericData<String> data = dataIn.get(); data != null; data = dataIn.get()) {
            list.add(data);

        }

        assertThat(list.size(), is(6));

        assertThat(list.get(5).getString("ResultType"), is("Y_MISSING"));
        assertThat(list.get(4).getDouble("Y_Qty"), is(8.0));
        assertThat(list.get(2).getString("Colour"), is("yellow"));
        assertThat(list.get(0).getString("X_Comments"), is("Crisp"));

        oddjob.destroy();
    }

}
