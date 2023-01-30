package beancmpr.poi;

import beancmpr.test.OurDirs;
import org.junit.jupiter.api.Test;
import org.oddjob.Oddjob;
import org.oddjob.OddjobLookup;
import org.oddjob.arooa.convert.ArooaConversionException;
import org.oddjob.beancmpr.results.MatchResult;
import org.oddjob.state.ParentState;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class PoiMatchResultsServiceTest {

    @Test
    void testDidoCompareInOddjob() throws ArooaConversionException, IOException {

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

        oddjob.destroy();
    }

}
