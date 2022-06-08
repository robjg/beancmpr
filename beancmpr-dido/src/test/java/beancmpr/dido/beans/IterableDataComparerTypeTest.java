package beancmpr.dido.beans;

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

import java.net.URL;
import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class IterableDataComparerTypeTest {

    @Test
    void wtf() {

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