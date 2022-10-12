package beancmpr.dido.beans;

import beancmpr.dido.results.GenericDataResultHandler;
import dido.data.DataSchema;
import dido.data.GenericData;
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

        List<GenericData<String>> dataX = List.of(
                MapData.of("Fruit", "Apple", "Quantity", 5, "Price", 53.2),
                MapData.of("Fruit", "Pear", "Quantity", 7, "Price", 42.1)
        );

        List<GenericData<String>> dataY = List.of(
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

        List<GenericData<String>> results = new ArrayList<>();

        GenericDataResultHandler resultHandler = GenericDataResultHandler.withSettings()
                .setTo(results::add)
                .make();

        IterableBeansComparer<GenericData<String>> comparer
            = test.createComparerWith(new DefaultComparersByType(), resultHandler);

        MultiItemComparison<Iterable<GenericData<String>>> comparison = comparer.compare(dataX, dataY);

        MatcherAssert.assertThat(comparison.getResult(), is(3));

        DataSchema<String> expectedSchema = SchemaBuilder.forStringFields()
                .addField("MatchType", MatchResultType.class)
                .addField("Fruit", String.class)
                .addField("X_Quantity", Integer.class)
                .addField("Y_Quantity", Integer.class)
                .addField("Quantity_", String.class)
                .addField("X_Price", Double.class)
                .addField("Y_Price", Double.class)
                .addField("Price_", String.class)
                .build();

        assertThat(results.get(0).getSchema(), is(expectedSchema));

        assertThat(results.get(0), is(MapData.valuesFor(expectedSchema).of(
                MatchResultType.Y_MISSING, "Apple", 5, null, null, 53.2, null, null)));

        assertThat(results.get(1), is(MapData.valuesFor(expectedSchema).of(
                MatchResultType.X_MISSING, "Orange", null, 3, null, null, 98.2, null)));

        assertThat(results.get(2), is(MapData.valuesFor(expectedSchema).of(
                MatchResultType.NOT_EQUAL, "Pear", 7, 7, "", 42.1, 63.7, "21.60 (51.3%)")));
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