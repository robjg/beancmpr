package beancmpr.examples;

import org.junit.jupiter.api.Test;
import org.oddjob.Oddjob;
import org.oddjob.state.StateConditions;
import org.oddjob.tools.ConsoleCapture;
import org.oddjob.tools.StateSteps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class GettingStartedTest {

    private static final Logger logger = LoggerFactory.getLogger(GettingStartedTest.class);

    @Test
    void csvCompare() throws InterruptedException, IOException {

        Path workDir = Path.of("target/work");
        Files.createDirectories(workDir);

        Properties properties = new Properties();
        properties.setProperty("work.dir", workDir.toString());

        File file = new File(Objects.requireNonNull(
                getClass().getResource("/examples/CsvCompare.xml")).getFile());

        Oddjob oddjob = new Oddjob();
        oddjob.setFile(file);
        oddjob.setProperties(properties);

        StateSteps oddjobStates = new StateSteps(oddjob);
        oddjobStates.startCheck(StateConditions.READY, StateConditions.RUNNING,
                StateConditions.ACTIVE, StateConditions.COMPLETE);

        ConsoleCapture console = new ConsoleCapture();
        try (ConsoleCapture.Close ignored = console.captureConsole()) {

            oddjob.run();

            oddjobStates.checkWait();
        }

        console.dump(logger);

        List<String> lines = console.getAsList()
                .stream()
                .map(String::stripTrailing)
                .toList();

        List<String> expected = new BufferedReader(
                new InputStreamReader(Objects.requireNonNull(
                        getClass().getResourceAsStream("/expected/CsvCompare.txt"))))
                .lines().map(String::stripTrailing).toList();

        assertThat(lines, is(expected));

    }
}
