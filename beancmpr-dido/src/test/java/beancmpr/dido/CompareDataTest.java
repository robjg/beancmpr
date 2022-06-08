package beancmpr.dido;

import dido.data.GenericData;
import dido.data.MapData;
import org.junit.jupiter.api.Test;
import org.oddjob.beancmpr.BeanCompareJob;

import java.util.List;

class CompareDataTest {

    @Test
    void testCompareGenericData() {

        List<GenericData> dataX = List.of(
                MapData.of("Fruit", "Apple", "Quantity", 5, "Price", 53.2),
                MapData.of("Fruit", "Pear", "Quantity", 7, "Price", 42.1)
        );

        List<GenericData> dataY = List.of(
                MapData.of("Fruit", "Orange", "Quantity", 3, "Price", 98.2),
                MapData.of("Fruit", "Pear", "Quantity", 7, "Price", 67.1)
        );

        BeanCompareJob compareJob = new BeanCompareJob();
//        compareJob.setComparer();

    }
}
