package beancmpr.dido;

import org.junit.jupiter.api.Test;
import org.oddjob.Oddjob;
import org.oddjob.state.ParentState;

import java.io.File;
import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class DidoCompareJobTest {

    @Test
    void testDidoCompareInOddjob() {

        File file = new File(Objects.requireNonNull(
                getClass().getResource("/examples/DidoCompare.xml")).getFile());

        Oddjob oddjob = new Oddjob();
        oddjob.setFile(file);

        oddjob.run();

        assertThat(oddjob.lastStateEvent().getState(), is(ParentState.COMPLETE));

        // Todo Check results.
    }

}
