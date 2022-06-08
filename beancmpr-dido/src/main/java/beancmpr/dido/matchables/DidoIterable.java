package beancmpr.dido.matchables;

import dido.data.GenericData;
import dido.data.IndexedData;

import java.util.Iterator;

abstract public class DidoIterable implements Iterable<Object> {

    private DidoIterable() {}


    public static <F> Iterable<Object> iterableForFields(GenericData<F> genericData,
                                                         Iterable<F> fields) {

        return new FieldIterable<>(genericData, fields);
    }

    static class IndexedIterable extends DidoIterable {

        private final IndexedData<?> indexedData;

        private final Iterable<Integer> indexes;

        public IndexedIterable(IndexedData<?> indexedData, Iterable<Integer> indexes) {
            this.indexedData = indexedData;
            this.indexes = indexes;
        }

        @Override
        public Iterator<Object> iterator() {

            return new Iterator<>() {

                final Iterator<Integer> it = indexes.iterator();

                @Override
                public boolean hasNext() {
                    return it.hasNext();
                }

                @Override
                public Object next() {
                    return indexedData.getAt(it.next());
                }
            };
        }
    }

    static class FieldIterable<F> extends DidoIterable {

        private final GenericData<F> genericData;

        private final Iterable<F> fields;

        public FieldIterable(GenericData<F> genericData, Iterable<F> fields) {
            this.genericData = genericData;
            this.fields = fields;
        }

        @Override
        public Iterator<Object> iterator() {

            return new Iterator<>() {

                final Iterator<F> it = fields.iterator();

                @Override
                public boolean hasNext() {
                    return it.hasNext();
                }

                @Override
                public Object next() {
                    return genericData.get(it.next());
                }
            };
        }
    }

}
