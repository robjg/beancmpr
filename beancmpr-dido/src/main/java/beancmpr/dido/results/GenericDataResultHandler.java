package beancmpr.dido.results;

import dido.data.*;
import org.oddjob.beancmpr.Comparison;
import org.oddjob.beancmpr.matchables.*;
import org.oddjob.beancmpr.results.MatchResultType;

import java.lang.reflect.Type;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A Result Handler that Creates Generic Data.
 *
 * @author rob
 */
public class GenericDataResultHandler
        implements CompareResultsHandler {

    public static final String MATCH_TYPE_FIELD = "MatchType";

    private final Consumer<? super DidoData> to;

    private final boolean ignoreMatches;

    private final String xFieldFormat;

    private final String yFieldFormat;

    private final String comparisonFieldFormat;

    private PerResultDelegate delegate;

    private GenericDataResultHandler(Settings settings) {
        this.to = Objects.requireNonNullElse(settings.to, ignore -> {
        });
        this.ignoreMatches = settings.ignoreMatches;
        this.xFieldFormat = Objects.requireNonNullElse(settings.xFieldFormat, "X_%s");
        this.yFieldFormat = Objects.requireNonNullElse(settings.yFieldFormat, "Y_%s");
        this.comparisonFieldFormat = Objects.requireNonNullElse(settings.comparisonFieldFormat, "%s_");
    }


    public static class Settings {

        private Consumer<? super DidoData> to;

        private boolean ignoreMatches;

        private String xFieldFormat;

        private String yFieldFormat;

        private String comparisonFieldFormat;

        public Settings setTo(Consumer<? super DidoData> to) {
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

    PerResultDelegate delegate(MatchableMetaData matchableMetaData) {
        if (delegate == null) {
            DataFactoryProvider factoryProvider = DataFactoryProvider.newInstance();
            SchemaFactory schemaFactory = factoryProvider.getSchemaFactory();
            DataSchema schema = schemaFromMatchableMeta(schemaFactory, matchableMetaData);
            DataFactory factory = factoryProvider.factoryFor(schemaFactory.toSchema());

            delegate = new PerResultDelegate(factory);
        }
        return delegate;
    }

    @Override
    public void compared(MultiValueComparison<Matchable> comparison) {

        if (ignoreMatches && comparison.getResult() == 0) {
            return;
        }

        delegate(comparison.getX().getMetaData()).compared(comparison);
    }

    @Override
    public void xMissing(MatchableGroup ys) {

        delegate(ys.getMetaData()).xMissing(ys);
    }

    @Override
    public void yMissing(MatchableGroup xs) {

        delegate(xs.getMetaData()).yMissing(xs);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
                ", ignoreMatches=" + ignoreMatches;
    }

    class PerResultDelegate implements CompareResultsHandler {

        private final DataFactory dataFactory;
        private final WritableData writableData;

        PerResultDelegate(DataFactory dataFactory) {
            this.dataFactory = dataFactory;
            this.writableData = dataFactory.getWritableData();
        }

        @Override
        public void compared(MultiValueComparison<Matchable> comparison) {

            to.accept(createComparisonResult(comparison));
        }

        @Override
        public void xMissing(MatchableGroup ys) {

            for (Matchable y : ys.getGroup()) {
                to.accept(createXMissingResult(y));
            }
        }

        @Override
        public void yMissing(MatchableGroup xs) {

            for (Matchable x : xs.getGroup()) {
                to.accept(createYMissingResult(x));
            }
        }

        protected DidoData createXMissingResult(Matchable y) {

            writableData.setNamed(MATCH_TYPE_FIELD, MatchResultType.X_MISSING);

            populateKeys(y);

            ValueIterable<Object> comparisons =
                    new ValueIterable<>(
                            y.getMetaData().getValueProperties(),
                            y.getValues());

            for (ValueIterable.Value<Object> set : comparisons) {

                Object value = set.getValue();

                String name = set.getPropertyName();
                writableData.setNamed(String.format(yFieldFormat, name), value);
            }

            populateOthers(y,
                    name -> String.format(yFieldFormat, name));

            return dataFactory.toData();
        }

        protected DidoData createYMissingResult(Matchable x) {

            writableData.setNamed(MATCH_TYPE_FIELD, MatchResultType.Y_MISSING);

            populateKeys(x);

            ValueIterable<Object> comparisons =
                    new ValueIterable<>(
                            x.getMetaData().getValueProperties(),
                            x.getValues());

            for (ValueIterable.Value<Object> set : comparisons) {

                Object value = set.getValue();

                String name = set.getPropertyName();
                writableData.setNamed(String.format(xFieldFormat, name), value);
            }

            populateOthers(x,
                    name -> String.format(xFieldFormat, name));

            return dataFactory.toData();
        }

        protected DidoData createComparisonResult(
                MultiValueComparison<Matchable> matchableComparison) {

            writableData.setNamed(MATCH_TYPE_FIELD,
                    matchableComparison.getResult() == 0 ?
                            MatchResultType.EQUAL : MatchResultType.NOT_EQUAL);


            populateKeys(matchableComparison.getX());

            populateValues(matchableComparison.getX().getMetaData().getValueProperties(),
                    matchableComparison.getValueComparisons());

            populateOthers(matchableComparison.getX(),
                    name -> String.format(xFieldFormat, name));

            populateOthers(matchableComparison.getY(),
                    name -> String.format(yFieldFormat, name));

            return dataFactory.toData();
        }

        private void populateKeys(Matchable matchable) {

            ValueIterable<Object> keys =
                    new ValueIterable<>(
                            matchable.getMetaData().getKeyProperties(),
                            matchable.getKeys());

            for (ValueIterable.Value<Object> set : keys) {

                Object value = set.getValue();

                writableData.setNamed(set.getPropertyName(), value);
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
                writableData.setNamed(String.format(xFieldFormat, name), comparison.getX());
                writableData.setNamed(String.format(yFieldFormat, name), comparison.getY());
                writableData.setNamed(String.format(comparisonFieldFormat, name), comparison.getSummaryText());
            }
        }

        private void populateOthers(Matchable matchable, Function<? super String, ? extends String> format) {

            ValueIterable<Object> xOthers =
                    new ValueIterable<>(
                            matchable.getMetaData().getOtherProperties(),
                            matchable.getOthers());

            for (ValueIterable.Value<Object> set : xOthers) {

                Object value = set.getValue();

                writableData.setNamed(format.apply(set.getPropertyName()), value);
            }
        }
    }

    public DataSchema schemaFromMatchableMeta(SchemaFactory schemaFactory,
                                              MatchableMetaData matchableMetaData) {

        SchemaBuilder schemaBuilder = SchemaBuilder.builderFor(schemaFactory);

        schemaBuilder.addNamed(MATCH_TYPE_FIELD, MatchResultType.class);

        for (String key : matchableMetaData.getKeyProperties()) {
            Type type = matchableMetaData.getPropertyType(key);
            schemaBuilder.addNamed(key, type);
        }

        for (String value : matchableMetaData.getValueProperties()) {
            Type type = matchableMetaData.getPropertyType(value);
            schemaBuilder.addNamed(String.format(xFieldFormat, value), type);
            schemaBuilder.addNamed(String.format(yFieldFormat, value), type);
            schemaBuilder.addNamed(String.format(comparisonFieldFormat, value), String.class);
        }

        for (String other : matchableMetaData.getOtherProperties()) {
            Type type = matchableMetaData.getPropertyType(other);
            schemaBuilder.addNamed(String.format(xFieldFormat, other), type);
            schemaBuilder.addNamed(String.format(yFieldFormat, other), type);
        }

        return schemaBuilder.build();
    }
}
