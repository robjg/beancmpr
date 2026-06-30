package org.oddjob.beancmpr.matchables;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * Create a matchable using a fluent builder. Mainly for tests.
 */
public class FluentMatchable {

    public static FluentMatchable.Keys withKeys(Object... keys) {
        return new Keys(List.of(keys));
    }

    public FluentMatchable.Keys withKey(Object key) {
        return withKeys(key);
    }

    public FluentMatchable.Keys noKey() {
        return withKeys();
    }

    public static class Keys {

        private final List<Object> keys;

        private List<String> names;

        private Keys(List<Object> keys) {
            this.keys = keys;
        }

        public Keys ofNames(String... names) {
            this.names = List.of(names);
            return this;
        }

        public Keys ofName(String name) {
            return ofNames(name);
        }

        public Values andValues(Object... values) {
            if (this.names == null) {
                this.names = IntStream.range(1, keys.size())
                        .mapToObj(i -> "k" + i)
                        .toList();
            }
            Map<String, Type> types = new HashMap<>();
            for (int i = 0; i < this.names.size(); i++) {
                types.put(names.get(i), keys.get(i).getClass());
            }

            return new Values(keys, names, types,
                    List.of(values));
        }

        public Matchable make() {

            return andValues().make();
        }
    }

    public static class Values {

        private final List<Object> keys;

        private final List<String> keyNames;

        private final Map<String, Type> types;

        private final List<Object> values;

        private List<String> names;

        private Values(List<Object> keys,
                       List<String> KeyNames,
                       Map<String, Type> types,
                       List<Object> values) {
            this.keys = keys;
            this.values = values;
            this.keyNames = KeyNames;
            this.types = types;
        }

        public Values ofNames(String names) {
            this.names = List.of(names);
            return this;
        }

        public Others andOthers(Object... others) {
            if (this.names == null) {
                this.names = IntStream.range(1, values.size())
                        .mapToObj(i -> "v" + i)
                        .toList();
            }
            for (int i = 0; i < this.names.size(); i++) {
                types.put(names.get(i), keys.get(i).getClass());
            }

            return new Others(keys, keyNames, values, names, types,
                    List.of(others));
        }

        public Matchable make() {
            return andOthers().make();
        }
    }

    public static class Others {

        private final List<Object> keys;

        private final List<Object> values;

        private final List<String> keyNames;

        private final List<String> valueNames;

        private final Map<String, Type> types;

        private final List<Object> others;

        private List<String> names;

        public Others(List<Object> keys,
                      List<String> keyNames,
                      List<Object> values,
                      List<String> valueNames,
                      Map<String, Type> types,
                      List<Object> others) {
            this.keys = keys;
            this.values = values;
            this.keyNames = keyNames;
            this.valueNames = valueNames;
            this.types = types;
            this.others = others;
        }

        public Others ofNames(String... names) {
            this.names = List.of(names);
            return this;
        }

        public Matchable make() {

            if (this.names == null) {
                this.names = IntStream.range(1, others.size())
                        .mapToObj(i -> "o" + i)
                        .toList();
            }

            return new SimpleMatchable(keys, values, others,
                    SimpleMatchableMeta.of(
                            ImmutableCollection.of(keyNames),
                            ImmutableCollection.of(valueNames),
                            ImmutableCollection.of(names),
                            types));
        }
    }
}
