package org.oddjob.beancmpr.comparers;

import org.junit.jupiter.api.Test;
import org.oddjob.Oddjob;
import org.oddjob.tools.ConsoleCapture;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NumericComparerTest  {

	@Test
	void testNoTolerances() {
		
		NumericComparer test = new NumericComparer();
		
		NumericComparison comparison = test.compare(2.0, 2.0);
		
		assertEquals(0, comparison.getResult());
		
		comparison = test.compare(200.0, 190);
		
		assertTrue(comparison.getResult() > 0);
		assertEquals(-10, comparison.getDelta(), 0.001);
		assertEquals(-5, comparison.getPercentage(), 0.001);
		assertEquals("-10.0 (-5.0%)", comparison.getSummaryText());
		
		comparison = test.compare(200.0, 250);
		
		assertTrue(comparison.getResult() < 0);
		assertEquals(50, comparison.getDelta(), 0.001);
		assertEquals(25, comparison.getPercentage(), 0.001);
		assertEquals("50.0 (25.0%)", comparison.getSummaryText());

	}

	@Test
	void testOutsideNumericTolerance() {
		
		NumericComparer test = new NumericComparer();
		test.setDeltaTolerance(0.1);
		
		NumericComparison comparison = test.compare(2.0, 2.11);
		
		assertTrue(comparison.getResult() < 0);
		assertEquals(0.11, comparison.getDelta(), 0.0001);
		assertEquals(5.5, comparison.getPercentage(), 0.0001);
		
		test.setDeltaTolerance(2);
		
		comparison = test.compare(200.0, 190.0);
		
		assertTrue(comparison.getResult() > 0);		
		assertEquals(-10.0, comparison.getDelta(), 0.0001);
		assertEquals(-5.0, comparison.getPercentage(), 0.0001);
	}
	
	@Test
	void testOutsidePercentageTolerence() {
		
		NumericComparer test = new NumericComparer();
		test.setPercentageTolerance(5);
		test.setPercentageFormat("###");
		test.setDeltaFormat("##");
		
		NumericComparison comparison;
		
		comparison = test.compare(200, 190);
		
		assertTrue(comparison.getResult() > 0);
		assertEquals(-10.0, comparison.getDelta(), 0.0001);
		assertEquals(-5.0, comparison.getPercentage(), 0.0001);
		assertEquals("-10 (-5%)", comparison.getSummaryText());
		
		test.setDeltaTolerance(20);
		
		comparison = test.compare(200, 190);
		
		assertEquals(0, comparison.getResult());
	}

	@Test
	void testIntegerDoubleComparison() {
		
		NumericComparer test = new NumericComparer();
		
		NumericComparison comparison = test.compare(2, 2.0);
		
		assertEquals(0, comparison.getResult());
		
		test.setDeltaTolerance(0.05);
		
		comparison = test.compare(2, 2.01);
		
		assertEquals(0, comparison.getResult());
	}

	@Test
	void testNullValuesComparison() {
		
		NumericComparer test = new NumericComparer();

		try {
			test.compare(null, null);
		}
		catch (NullPointerException e) {
			// expected
		}		
	}

	@Test
	void testZerosWithPercentageTolerance() {
		
		NumericComparer test = new NumericComparer();
		test.setPercentageTolerance(1.0);
		
		NumericComparison comparison = test.compare(0.0, 1000.0);
		
		assertEquals(-1, comparison.getResult());
		assertEquals("1000.0", comparison.getSummaryText());
        assertTrue(Double.isInfinite(comparison.getPercentage()));
		
		comparison = test.compare(1000.0, 0.0);
		
		assertEquals(1, comparison.getResult());
		assertEquals("-1000.0 (-100.0%)", comparison.getSummaryText());
		
		comparison = test.compare(0.0, 0.0);
		
		assertEquals(0, comparison.getResult());
		assertEquals("", comparison.getSummaryText());
	}

    @Test
    void example() {

        File file = new File(Objects.requireNonNull(getClass().getResource(
                "NumericComparerExample.xml")).getFile());

        Oddjob oddjob = new Oddjob();
        oddjob.setFile(file);

        ConsoleCapture console = new ConsoleCapture();
        try (ConsoleCapture.Close ignored = console.captureConsole()) {

            oddjob.run();
        }

        assertTrue(oddjob.lastStateEvent().getState().isComplete());

        List<String> lines = console.getAsList();

        List<String> expected = new BufferedReader(
                new InputStreamReader(Objects.requireNonNull(
                        getClass().getResourceAsStream("NumericComparerExampleOut.txt"))))
                .lines().toList();

        assertThat(lines, is(expected));

        oddjob.destroy();

    }

}
