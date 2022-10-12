package beancmpr.dido.results;

import org.oddjob.beancmpr.matchables.CompareResultsHandler;
import org.oddjob.beancmpr.matchables.CompareResultsHandlerFactory;

import java.util.function.Consumer;

/**
 * @oddjob.description A handler that will generate results in format
 * used by Dido. Dido (Data In Data Out) is currently experimental.
 * It can be found at <a href="http://sourceforge.net/projects/di-do>
 * http://sourceforge.net/projects/di-do</a>.
 * 
 * @author rob
 *
 */
public class GenericDataResultHandlerFactory
implements CompareResultsHandlerFactory {

	/**
	 * @oddjob.property
	 * @oddjob.description The format of X fields. Must contain one and only one %s which will be replaced
	 * with the underlying field name.
	 * @oddjob.required No. Defaults to X_%s.
	 */
	private String xFieldFormat;

	/**
	 * @oddjob.property
	 * @oddjob.description The format of Y fields. Must contain one and only one %s which will be replaced
	 * with the underlying field name.
	 * @oddjob.required No. Defaults to Y_%s.
	 */
	private String yFieldFormat;

	/**
	 * @oddjob.property
	 * @oddjob.description The format of the comparison fields. Must contain one and only one %s which will be replaced
	 * with the underlying field name.
	 * @oddjob.required No. Defaults to %s_.
	 */
	private String comparisonFieldFormat;

	/**
	 * @oddjob.property
	 * @oddjob.description If true then result beans will not be created
	 * when a comparison results in a match. If false result beans
	 * for all comparisons will be created.
	 * @oddjob.required No. Defaults to false.
	 */
	private boolean ignoreMatches;

	@Override
	public CompareResultsHandler createResultsHandlerTo(Consumer<Object> resultsConsumer) {
		return GenericDataResultHandler.withSettings()
				.setxFieldFormat(xFieldFormat)
				.setyFieldFormat(yFieldFormat)
				.setComparisonFieldFormat(comparisonFieldFormat)
				.setIgnoreMatches(ignoreMatches)
				.setTo(resultsConsumer)
				.make();
	}


	public String getxFieldFormat() {
		return xFieldFormat;
	}

	public void setxFieldFormat(String xFieldFormat) {
		this.xFieldFormat = xFieldFormat;
	}

	public String getyFieldFormat() {
		return yFieldFormat;
	}

	public void setyFieldFormat(String yFieldFormat) {
		this.yFieldFormat = yFieldFormat;
	}

	public String getComparisonFieldFormat() {
		return comparisonFieldFormat;
	}

	public void setComparisonFieldFormat(String comparisonFieldFormat) {
		this.comparisonFieldFormat = comparisonFieldFormat;
	}

	public boolean isIgnoreMatches() {
		return ignoreMatches;
	}

	public void setIgnoreMatches(boolean ignoreMatches) {
		this.ignoreMatches = ignoreMatches;
	}

	@Override
	public String toString() {
		return "GenericDataResultHandlerFactory{" +
				"xFieldFormat='" + xFieldFormat + '\'' +
				", yFieldFormat='" + yFieldFormat + '\'' +
				", comparisonFieldFormat='" + comparisonFieldFormat + '\'' +
				", ignoreMatches=" + ignoreMatches +
				'}';
	}
}
