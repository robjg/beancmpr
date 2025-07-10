package beancmpr.dido.beans;

import beancmpr.dido.results.GenericDataResultHandler;
import dido.data.DataSchema;
import dido.data.DidoData;
import dido.data.MapData;
import dido.data.SchemaBuilder;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.oddjob.arooa.ArooaAnnotations;
import org.oddjob.arooa.ArooaBeanDescriptor;
import org.oddjob.arooa.ArooaDescriptor;
import org.oddjob.arooa.ConfiguredHow;
import org.oddjob.arooa.beanutils.BeanUtilsPropertyAccessor;
import org.oddjob.arooa.deploy.ArooaAnnotation;
import org.oddjob.arooa.deploy.URLDescriptorFactory;
import org.oddjob.arooa.deploy.annotations.ArooaAttribute;
import org.oddjob.arooa.life.SimpleArooaClass;
import org.oddjob.beancmpr.beans.IterableBeansComparer;
import org.oddjob.beancmpr.comparers.NumericComparer;
import org.oddjob.beancmpr.composite.ComparersByNameType;
import org.oddjob.beancmpr.composite.DefaultComparersByType;
import org.oddjob.beancmpr.multiitem.MultiItemComparison;
import org.oddjob.beancmpr.results.MatchResultType;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class IterableDataComparerTypeTest {

    @Test
    void testSimpleComparison() {

        List<DidoData> dataX = List.of(
                MapData.of("Fruit", "Apple", "Quantity", 5, "Price", 53.2),
                MapData.of("Fruit", "Pear", "Quantity", 7, "Price", 42.1)
        );

        List<DidoData> dataY = List.of(
                MapData.of("Fruit", "Orange", "Quantity", 3, "Price", 98.2),
                MapData.of("Fruit", "Pear", "Quantity", 7, "Price", 63.7)
        );


        IterableDataComparerType test = new IterableDataComparerType();
        test.setKeys(new String[] { "Fruit"});
        test.setValues(new String[] { "Quantity", "Price" });

        ComparersByNameType comparersByName = new ComparersByNameType();
        comparersByName.setComparers("Price",
                NumericComparer.with()
                        .deltaFormat("0.00")
                        .toFactory());

        test.setComparersByName(comparersByName);

        List<DidoData> results = new ArrayList<>();

        GenericDataResultHandler resultHandler = GenericDataResultHandler.withSettings()
                .setTo(results::add)
                .make();

        IterableBeansComparer<DidoData> comparer
            = test.createComparerWith(new DefaultComparersByType(), resultHandler);

        MultiItemComparison<Iterable<DidoData>> comparison = comparer.compare(dataX, dataY);

        MatcherAssert.assertThat(comparison.getResult(), is(3));

        DataSchema expectedSchema = SchemaBuilder.newInstance()
                .addNamed("MatchType", MatchResultType.class)
                .addNamed("Fruit", String.class)
                .addNamed("X_Quantity", Integer.class)
                .addNamed("Y_Quantity", Integer.class)
                .addNamed("Quantity_", String.class)
                .addNamed("X_Price", Double.class)
                .addNamed("Y_Price", Double.class)
                .addNamed("Price_", String.class)
                .build();

        assertThat(results.get(0).getSchema(), is(expectedSchema));

        assertThat(results.get(0), is(MapData.valuesWithSchema(expectedSchema).of(
                MatchResultType.Y_MISSING, "Apple", 5, null, null, 53.2, null, null)));

        assertThat(results.get(1), is(MapData.valuesWithSchema(expectedSchema).of(
                MatchResultType.X_MISSING, "Orange", null, 3, null, null, 98.2, null)));

        assertThat(results.get(2), is(MapData.valuesWithSchema(expectedSchema).of(
                MatchResultType.NOT_EQUAL, "Pear", 7, 7, "", 42.1, 63.7, "21.60 (51.3%)")));
    }

    @Test
    void whenNoKeysOrValuesThenComparisonIsEqual() {

        List<DidoData> dataX = List.of(
                MapData.of("Fruit", "Apple", "Quantity", 5, "Price", 53.2),
                MapData.of("Fruit", "Pear", "Quantity", 7, "Price", 42.1)
        );

        List<DidoData> dataY = List.of(
                MapData.of("Fruit", "Orange", "Quantity", 3, "Price", 98.2),
                MapData.of("Fruit", "Pear", "Quantity", 7, "Price", 63.7)
        );

        IterableDataComparerType test = new IterableDataComparerType();

        ComparersByNameType comparersByName = new ComparersByNameType();

        test.setComparersByName(comparersByName);

        List<DidoData> results = new ArrayList<>();

        GenericDataResultHandler resultHandler = GenericDataResultHandler.withSettings()
                .setTo(results::add)
                .make();

        IterableBeansComparer<DidoData> comparer
                = test.createComparerWith(new DefaultComparersByType(), resultHandler);

        MultiItemComparison<Iterable<DidoData>> comparison = comparer.compare(dataX, dataY);

        MatcherAssert.assertThat(comparison.getResult(), is(0));

        DataSchema expectedSchema = SchemaBuilder.newInstance()
                .addNamed("MatchType", MatchResultType.class)
                .build();

        assertThat(results.get(0).getSchema(), is(expectedSchema));

        assertThat(results.get(0).getNamed("MatchType"), is(MatchResultType.EQUAL));
        assertThat(results.get(1).getNamed("MatchType"), is(MatchResultType.EQUAL));
    }

    // A bug in Arooa that is now fixed.
    @Test
    void testAnnotationInXmlNotRecognised() {

        URL url = Objects.requireNonNull(
                getClass().getResource("/META-INF/arooa.xml"));

        URLDescriptorFactory df = new URLDescriptorFactory(url);

        ArooaDescriptor ad = df.createDescriptor(getClass().getClassLoader());

        ArooaBeanDescriptor abd = ad.getBeanDescriptor(new SimpleArooaClass(IterableDataComparerType.class),
                new BeanUtilsPropertyAccessor());

        ArooaAnnotations annotations = abd.getAnnotations();

        ArooaAnnotation annotation =
                annotations.annotationForProperty("keys", ArooaAttribute.class.getName());

        assertThat(annotation.getName(), is(ArooaAttribute.class.getName()));

        assertThat(abd.getConfiguredHow("keys"), is(ConfiguredHow.ATTRIBUTE));
    }
}