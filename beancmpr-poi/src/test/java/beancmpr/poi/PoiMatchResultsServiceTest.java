package beancmpr.poi;

import beancmpr.test.OurDirs;
import dido.data.DidoData;
import dido.how.DataIn;
import dido.poi.data.PoiWorkbook;
import dido.poi.layouts.DataRows;
import org.junit.jupiter.api.Test;
import org.oddjob.Oddjob;
import org.oddjob.state.ParentState;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
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
                getClass().getResource("/examples/PoiMatchResultExample.xml")).getFile());

        Oddjob oddjob = new Oddjob();
        oddjob.setFile(file);
        oddjob.setProperties(properties);

        oddjob.run();

        assertThat(oddjob.lastStateEvent().getState(), is(ParentState.COMPLETE));

        PoiWorkbook workbook = new PoiWorkbook();
        workbook.setInput(Files.newInputStream(workDir.resolve("MatchResults.xlsx")));

        DataRows dataRows = new DataRows();
        dataRows.setWithHeader(true);
        dataRows.setFirstRow(3);
        dataRows.setFirstColumn(2);

        List<DidoData> list;

        try (DataIn dataIn = dataRows.inFrom(workbook)) {
            list = dataIn.stream().toList();
        }

        assertThat(list.size(), is(6));

        assertThat(list.get(5).getStringNamed("ResultType"), is("Y_MISSING"));
        assertThat(list.get(4).getDoubleNamed("Y_Qty"), is(8.0));
        assertThat(list.get(2).getStringNamed("Colour"), is("yellow"));
        assertThat(list.get(0).getStringNamed("X_Comments"), is("Crisp"));

        oddjob.destroy();
    }

}
