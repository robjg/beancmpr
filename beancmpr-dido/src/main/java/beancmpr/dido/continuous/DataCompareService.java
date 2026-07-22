package beancmpr.dido.continuous;

import beancmpr.dido.matchables.SchemaMatchableFactory;
import beancmpr.dido.results.GenericDataResultHandler;
import dido.data.DataSchema;
import dido.data.DidoData;
import org.oddjob.beancmpr.MatchDefinition;
import org.oddjob.beancmpr.SimpleMatchDefinition;
import org.oddjob.beancmpr.composite.*;
import org.oddjob.beancmpr.continuous.CloseableDuelConsumer;
import org.oddjob.beancmpr.continuous.SourceStrategyFactory;
import org.oddjob.beancmpr.continuous.TimedMatchableComparer;
import org.oddjob.beancmpr.matchables.CompareResultsHandler;
import org.oddjob.beancmpr.matchables.CompareResultsHandlerFactory;
import org.oddjob.beancmpr.matchables.ComparisonGatheringProcessor;
import org.oddjob.beancmpr.matchables.Matchable;
import org.oddjob.beancmpr.multiitem.MultiItemComparisonCounts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.ExceptionListener;
import java.time.Duration;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @oddjob.description Creates a service that continuously compares two
 * streams of data.
 *
 * @oddjob.example Compare two streams of data.
 * {@oddjob.xml.resource examples/ContinuousCompare.xml}
 * The output is:
 * {@oddjob.text.resource examples/ContinuousCompareOut.txt}
 *
 */
public class DataCompareService implements MultiItemComparisonCounts {

    private static final Logger logger = LoggerFactory.getLogger(DataCompareService.class);

    /**
     * @oddjob.property
     * @oddjob.description The name of the job as seen in Oddjob.
     * @oddjob.required No.
     */
    private String name;

    /**
     * @oddjob.property
     * @oddjob.description The key field names. The key
     * properties decided if another data item is missing or not.
     * @oddjob.required No.
     */
    private String[] keys;

    /**
     * @oddjob.property
     * @oddjob.description The value field names. The value properties
     * decide if two data items match when their keys match.
     * @oddjob.required No.
     */
    private String[] values;

    /**
     * @oddjob.property
     * @oddjob.description Other field names. Other fields
     * may be of interest on reports but take no part in the matching
     * process.
     * @oddjob.required No.
     */
    private String[] others;

    /**
     * @oddjob.property
     * @oddjob.description Comparers for comparing the field data.
     * These comparers will override any other comparers defined
     * in the comparer hierarchy for their type. This property is most
     * often set with a {@link ComparersByTypeList}.
     * @oddjob.required No.
     */
    private ComparersByTypeFactory comparersByType;

    /**
     * @oddjob.property
     * @oddjob.description Comparers for comparing the field data of the
     * Generic Data defined by the name of the field. This property is most
     * often set with a {@link ComparersByNameType}.
     * @oddjob.required No.
     */
    private ComparersByNameFactory comparersByName;

    /**
     * @oddjob.property
     * @oddjob.description The schema, if known in advance.
     * @oddjob.required No, it will be taken from the first item of data.
     */
    private DataSchema schema;

    /**
     * @oddjob.property
     * @oddjob.description The strategy. See {@link org.oddjob.beancmpr.continuous.SourceStrategies} for a
     * means of configuration.
     * @oddjob.required No. Defaults to ONE_FOR_ONE
     */
    private SourceStrategyFactory<Matchable> sourceStrategy;

    /** Hidden */
    private ExceptionListener exceptionListener;

    /**
     * @oddjob.property
     * @oddjob.description The tolerance to be used. This is a duration, Oddjob
     * provides a conversion from java's {@link Duration} text format.
     * How it is used depends on the
     * strategy, but missing data is generally reported only after the tolerance period
     * has expired.
     * @oddjob.required No. Defaults to PT01S (1 second).
     */
    private Duration tolerance;

    /**
     * @oddjob.property
     * @oddjob.description Something to handle results. Typically, a
     * {@link beancmpr.dido.results.GenericDataResultHandler}.
     * @oddjob.required No.
     */
    private CompareResultsHandlerFactory results;

    /**
     * @oddjob.property
     * @oddjob.description A destination for results that create beans. This
     * allows this job to play with Oddjob's Bean Bus Framework.
     * @oddjob.required No.
     */
    private Consumer<? super Object> to;

    /** Counts. */
    private MultiItemComparisonCounts counts;

    /** Internal. */
    private volatile CloseableDuelConsumer<DidoData> consumers;

    /**
     * @oddjob.property
     * @oddjob.description Provides a consumer to accept X data.
     * @oddjob.required Read only.
     */
    private final Consumer<DidoData> x = new Consumer<>() {

        @Override
        public void accept(DidoData x) {

            if (consumers == null) {
                synchronized (DataCompareService.this) {
                    if (consumers == null) {
                        logger.info("Creating match from schema of X: {}",
                                x.getSchema());
                        consumers = schemaKnown(x.getSchema());
                    }
                }
            }
            consumers.acceptX(x);
        }

        @Override
        public String toString() {
            return "Consumer for X";
        }
    };

    /**
     * @oddjob.property
     * @oddjob.description Provides a consumer to accept Y data.
     * @oddjob.required Read only.
     */
    private final Consumer<DidoData> y = new Consumer<>() {

        @Override
        public void accept(DidoData y) {

            if (consumers == null) {
                synchronized (DataCompareService.this) {
                    if (consumers == null) {
                        logger.info("Creating match from schema of Y: {}",
                                y.getSchema());
                        consumers = schemaKnown(y.getSchema());
                    }
                }
            }
            consumers.acceptY(y);
        }

        @Override
        public String toString() {
            return "Consumer for Y";
        }
    };

    public void start() {

        if (schema == null) {
            logger.info("Starting without a schema.");
        }
        else {
            logger.info("Starting with schema: {}",
                    schema);
            consumers = schemaKnown(schema);
        }
    }

    public void stop() {
        if (consumers != null) {
            consumers.close();
            consumers = null;
        }
        logger.info("Stopped with counts: Xs Missing {}, Ys Missing {}, Different {}, Same {}",
                getXMissingCount(), getYMissingCount(), getDifferentCount(), getMatchedCount());
    }

    public void reset() {
        counts = null;
    }

    public Consumer<DidoData> getX() {

        return x;
    }

    public Consumer<DidoData> getY() {

        return y;
    }

    protected CloseableDuelConsumer<DidoData> schemaKnown(DataSchema schema) {

        ComparersByNameOrTypeFactory comparerProviderFactory =
                new ComparersByNameOrTypeFactory(
                        comparersByName, comparersByType);

        MatchDefinition matchDefinition = SimpleMatchDefinition.of(
                keys, values, others);

        SchemaMatchableFactory matchableFactory =
                SchemaMatchableFactory.from(schema, matchDefinition);

        CompareResultsHandler resultsHandler;
        if (to == null ) {
            if (results == null) {
                resultsHandler = null;
            }
            else {
                resultsHandler = this.results.createResultsHandlerTo(null);
            }
        }
        else {
            if (results == null) {
                resultsHandler = GenericDataResultHandler.withSettings()
                        .setTo(to)
                        .make();
            }
            else {
                resultsHandler = results.createResultsHandlerTo(to);
            }
        }

        ComparisonGatheringProcessor statsResultsHandler = new ComparisonGatheringProcessor(resultsHandler);
        this.counts = statsResultsHandler;

        @SuppressWarnings("resource")
        CloseableDuelConsumer<Matchable> comparer = TimedMatchableComparer.with()
                .comparerProvider(comparerProviderFactory
                        .createWith(null))
                .resultsHandler(statsResultsHandler)
                .sourceStrategy(sourceStrategy)
                .tolerance(tolerance)
                .exceptionListener(exceptionListener)
                .createFor(matchableFactory.getMetaData());

        return new CloseableDuelConsumer<>() {

            @Override
            public void acceptX(DidoData x) {
                comparer.acceptX(matchableFactory.createMatchable(x));
            }

            @Override
            public void acceptY(DidoData y) {
                comparer.acceptY(matchableFactory.createMatchable(y));
            }

            @Override
            public void close() {
                comparer.close();
            }
        };

    }

    public DataSchema getSchema() {
        return schema;
    }

    public void setSchema(DataSchema schema) {
        this.schema = schema;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getKeys() {
        return keys;
    }

    public void setKeys(String[] keys) {
        this.keys = keys;
    }

    public String[] getValues() {
        return values;
    }

    public void setValues(String[] values) {
        this.values = values;
    }

    public String[] getOthers() {
        return others;
    }

    public void setOthers(String[] others) {
        this.others = others;
    }

    public ComparersByTypeFactory getComparersByType() {
        return comparersByType;
    }

    public void setComparersByType(ComparersByTypeFactory comparersByType) {
        this.comparersByType = comparersByType;
    }

    public ComparersByNameFactory getComparersByName() {
        return comparersByName;
    }

    public void setComparersByName(ComparersByNameFactory comparersByName) {
        this.comparersByName = comparersByName;
    }

    public SourceStrategyFactory<Matchable> getSourceStrategy() {
        return sourceStrategy;
    }

    public void setSourceStrategy(SourceStrategyFactory<Matchable> sourceStrategy) {
        this.sourceStrategy = sourceStrategy;
    }

    public Duration getTolerance() {
        return tolerance;
    }

    public void setTolerance(Duration tolerance) {
        this.tolerance = tolerance;
    }

    public CompareResultsHandlerFactory getResults() {
        return results;
    }

    public void setResults(CompareResultsHandlerFactory results) {
        this.results = results;
    }

    public Consumer<? super Object> getTo() {
        return to;
    }

    public void setTo(Consumer<? super Object> to) {
        this.to = to;
    }

    public void exceptionListener(ExceptionListener exceptionListener) {
        this.exceptionListener = exceptionListener;
    }

    /**
     * @oddjob.property
     * @oddjob.description The number of items missing from the first source.
     * @oddjob.required Read only.
     */
    @Override
    synchronized public int getXMissingCount() {
        return counts == null ? 0 : counts.getXMissingCount();
    }

    /**
     * @oddjob.property
     * @oddjob.description The number of items missing from the second source.
     * @oddjob.required Read only.
     */
    @Override
    synchronized public int getYMissingCount() {
        return counts == null ? 0 : counts.getYMissingCount();
    }

    /**
     * @oddjob.property
     * @oddjob.description The number of items matched.
     * @oddjob.required Read only.
     */
    @Override
    synchronized public int getMatchedCount() {
        return counts == null ? 0 : counts.getMatchedCount();
    }

    /**
     * @oddjob.property
     * @oddjob.description The number of items different.
     * @oddjob.required Read only.
     */
    @Override
    synchronized public int getDifferentCount() {
        return counts == null ? 0 : counts.getDifferentCount();
    }

    /**
     * @oddjob.property
     * @oddjob.description The number of differences. This is the sum of the
     * missing and the different.
     * @oddjob.required Read only.
     */
    @Override
    synchronized public int getBreaksCount() {
        return counts == null ? 0 : counts.getBreaksCount();
    }

    /**
     * @oddjob.property
     * @oddjob.description The total number of items compared.
     * @oddjob.required Read only.
     */
    @Override
    synchronized public int getComparedCount() {
        return counts == null ? 0 : counts.getComparedCount();
    }

    @Override
    public String toString() {
        return Objects.requireNonNullElseGet(name, () -> getClass().getSimpleName());
    }
}
