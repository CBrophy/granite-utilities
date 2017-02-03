/*
 * Copyright (C) 2016 Charles Brophy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.granite.base;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Comparator;

public class KeyValue<K, V> {

    private K key;
    private V value;

    public KeyValue() {
    }

    public KeyValue(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public void setValue(V value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof KeyValue
            && getKey().equals(((KeyValue) obj).getKey());
    }

    @Override
    public int hashCode() {
        return getKey().hashCode();
    }

    public static class ValueComparator<K, V> implements Comparator<KeyValue<K, V>> {

        private final Comparator<V> valueComparator;

        public ValueComparator(final Comparator<V> valueComparator) {
            this.valueComparator = checkNotNull(valueComparator, "valueComparator");
        }

        @Override
        public int compare(KeyValue<K, V> o1, KeyValue<K, V> o2) {
            return valueComparator.compare(o1.getValue(), o2.getValue());
        }
    }

    public static class KeyComparator<K, V> implements Comparator<KeyValue<K, V>> {

        private Comparator<K> keyComparator;

        public KeyComparator(final Comparator<K> keyComparator) {
            this.keyComparator = checkNotNull(keyComparator, "keyComparator");
        }

        @Override
        public int compare(KeyValue<K, V> o1, KeyValue<K, V> o2) {
            return keyComparator.compare(o1.getKey(), o2.getKey());
        }
    }
}
