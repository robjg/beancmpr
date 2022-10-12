package beancmpr.dido.results;

import dido.data.*;
import org.oddjob.beancmpr.Comparison;
import org.oddjob.beancmpr.matchables.*;
import org.oddjob.beancmpr.results.MatchResultType;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 *
 * @author rob
 *
 */
public class GenericDataResultHandler
implements CompareResultsHandler {

	public static final String MATCH_TYPE_FIELD = "MatchType";

	private final Consumer<? super GenericData<String>> to;
		
	private final boolean ignoreMatches;

	private final String xFieldFormat;

	private final String yFieldFormat;

	private final String comparisonFieldFormat;

	private DataSchema<String> schema;

	private GenericDataBuilder<String> dataBuilder;

	private GenericDataResultHandler(Settings settings) {
		this.to = Objects.requireNonNullElse(settings.to, ignore -> {});
		this.ignoreMatches = settings.ignoreMatches;
		this.xFieldFormat = Objects.requireNonNullElse(settings.xFieldFormat, "X_%s");
		this.yFieldFormat = Objects.requireNonNullElse(settings.yFieldFormat, "Y_%s");
		this.comparisonFieldFormat = Objects.requireNonNullElse(settings.comparisonFieldFormat, "%s_");
	}


	public static class Settings {

		private Consumer<? super GenericData<String>> to;

		private boolean ignoreMatches;

		private String xFieldFormat;

		private String yFieldFormat;

		private String comparisonFieldFormat;

		public Settings setTo(Consumer<? super GenericData<String>> to) {
			this.to = to;
			return this;
		}

		public Settings setIgnoreMatches(boolean ignoreMatches) {
			this.ignoreMatches = ignoreMatches;
			return this;
		}

		public Settings setxFieldFormat(String x_titleFormat) {
			this.xFieldFormat = x_titleFormat;
			return this;
		}

		public Settings setyFieldFormat(String y_Name) {
			this.yFieldFormat = y_Name;
			return this;
		}

		public Settings setComparisonFieldFormat(String comparisonFieldFormat) {
			this.comparisonFieldFormat = comparisonFieldFormat;
			return this;
		}

		public GenericDataResultHandler make() {
			return new GenericDataResultHandler(this);
		}
	}

	public static Settings withSettings() {
		return new Settings();
	}
	
	@Override
	public void compared(MultiValueComparison<Matchable> comparison) {

		if (ignoreMatches && comparison.getResult() == 0) {
			return;
		}

		if (schema == null) {
			this.schema = schemaFromMatchableMeta(comparison.getX().getMetaData());
			this.dataBuilder = MapData.newBuilder(schema);
		}

		to.accept(createComparisonResult(comparison));
	}
	
	@Override
	public void xMissing(MatchableGroup ys) {
		if (schema == null) {
			this.schema = schemaFromMatchableMeta((ys.getMetaData()));
			this.dataBuilder = MapData.newBuilder(schema);
		}

		for (Matchable y : ys.getGroup()) {
			to.accept(createXMissingResult(y));
		}
	}
	
	@Override
	public void yMissing(MatchableGroup xs) {
		if (schema == null) {
			this.schema = schemaFromMatchableMeta((xs.getMetaData()));
			this.dataBuilder = MapData.newBuilder(schema);
		}

		for (Matchable x : xs.getGroup()) {
			to.accept(createYMissingResult(x));
		}
	}	

	@Override
	public String toString() {
		return getClass().getSimpleName() + 
				", ignoreMatches=" + ignoreMatches;
	}
	
	protected GenericData<String> createXMissingResult(Matchable y) {

		dataBuilder.set(MATCH_TYPE_FIELD, MatchResultType.X_MISSING);

		populateKeys(y);

		ValueIterable<Object> comparisons =
				new ValueIterable<>(
						y.getMetaData().getValueProperties(),
						y.getValues());

		for (ValueIterable.Value<Object> set : comparisons) {

			Object value = set.getValue();

			String name = set.getPropertyName();
			dataBuilder.set(String.format(yFieldFormat, name), value);
		}

		populateOthers(y,
				name ->  String.format(yFieldFormat, name));

		return dataBuilder.build();
	}
	
	protected GenericData<String> createYMissingResult(Matchable x) {

		dataBuilder.set(MATCH_TYPE_FIELD, MatchResultType.Y_MISSING);

		populateKeys(x);

		ValueIterable<Object> comparisons =
				new ValueIterable<>(
						x.getMetaData().getValueProperties(),
						x.getValues());

		for (ValueIterable.Value<Object> set : comparisons) {

			Object value = set.getValue();

			String name = set.getPropertyName();
			dataBuilder.set(String.format(xFieldFormat, name), value);
		}

		populateOthers(x,
				name ->  String.format(xFieldFormat, name));

		return dataBuilder.build();
	}
	
	
	protected GenericData<String> createComparisonResult(
			MultiValueComparison<Matchable> matchableComparison) {

		dataBuilder.set(MATCH_TYPE_FIELD,
				matchableComparison.getResult() == 0 ?
						MatchResultType.EQUAL : MatchResultType.NOT_EQUAL);


		populateKeys(matchableComparison.getX());

		populateValues(matchableComparison.getX().getMetaData().getValueProperties(),
				matchableComparison.getValueComparisons());

		populateOthers(matchableComparison.getX(),
				name ->  String.format(xFieldFormat, name));

		populateOthers(matchableComparison.getY(),
				name -> String.format(yFieldFormat, name));

		return dataBuilder.build();
	}

	private void populateKeys(Matchable matchable) {
		
		ValueIterable<Object> keys =
				new ValueIterable<>(
						matchable.getMetaData().getKeyProperties(),
						matchable.getKeys());
		
		for (ValueIterable.Value<Object> set : keys) {

			Object value = set.getValue();
			
			dataBuilder.set(set.getPropertyName(), value);
		}
	}

	private void populateValues(
			Iterable<String> comparisonProperties, 
			Iterable<? extends Comparison<?>> comparisonValues) {
		
		ValueIterable<Comparison<?>> comparisons =
				new ValueIterable<>(
						comparisonProperties,
						comparisonValues);

		for (ValueIterable.Value<Comparison<?>> set : comparisons) {

			Comparison<?> comparison = set.getValue();

			String name = set.getPropertyName();
			dataBuilder.set(String.format(xFieldFormat, name), comparison.getX());
			dataBuilder.set(String.format(yFieldFormat, name), comparison.getY());
			dataBuilder.set(String.format(comparisonFieldFormat, name), comparison.getSummaryText());
		}
	}

	private void populateOthers(Matchable matchable, Function<? super String, ? extends String> format) {

		ValueIterable<Object> xOthers =
				new ValueIterable<>(
						matchable.getMetaData().getOtherProperties(),
						matchable.getOthers());

		for (ValueIterable.Value<Object> set : xOthers) {

			Object value = set.getValue();

			dataBuilder.set(format.apply(set.getPropertyName()), value);
		}
	}
	public DataSchema<String> schemaFromMatchableMeta(MatchableMetaData matchableMetaData) {

		SchemaBuilder<String> schemaBuilder = SchemaBuilder.forStringFields();

		schemaBuilder.addField(MATCH_TYPE_FIELD, MatchResultType.class);

		for (String key : matchableMetaData.getKeyProperties()) {
			Class<?> type = matchableMetaData.getPropertyType(key);
			schemaBuilder.addField(key, type);
		}

		for (String value : matchableMetaData.getValueProperties()) {
			Class<?> type = matchableMetaData.getPropertyType(value);
			schemaBuilder.addField(String.format(xFieldFormat, value), type);
			schemaBuilder.addField(String.format(yFieldFormat, value), type);
			schemaBuilder.addField(String.format(comparisonFieldFormat, value), String.class);
		}

		for (String other : matchableMetaData.getOtherProperties()) {
			Class<?> type = matchableMetaData.getPropertyType(other);
			schemaBuilder.addField(String.format(xFieldFormat, other), type);
			schemaBuilder.addField(String.format(yFieldFormat, other), type);
		}

		DataSchema<String> schema = schemaBuilder.build();
		dataBuilder = ArrayData.builderForSchema(schema);

		return schema;
	}
}
