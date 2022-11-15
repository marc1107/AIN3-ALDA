package Aufgabenblatt1.dictionary;

import java.util.Iterator;

public class SortedArrayDictionary<K extends Comparable<? super K>, V> implements Dictionary<K, V> {
    private Entry<K, V>[] data;
    private int size = 0;

    public SortedArrayDictionary() {
        data = new Entry[10];
    }

    private int binarySearch(K key) {
        int li = 0;
        int re = size - 1;

        while (re >= li) {
            int m = (li + re) / 2;
            if (key.equals(data[m].getKey()))
                return m;
            else if (key.compareTo(data[m].getKey()) < 0) // Stimmt das so mit toString? key.compareTo geht nicht
                re = m - 1;
            else
                li = m + 1;
        }
        return -1;
    }

    @Override
    public V insert(K key, V value) {
        int search = binarySearch(key);
        if (search == -1) {
            if (data[data.length - 1] != null) {
                // Wenn data voll, dann um 10 vergrößern
                Entry<K, V>[] tmp = new Entry[data.length + 10];
                System.arraycopy(data, 0, tmp, 0, data.length);
                data = tmp;
            }
            // An richtiger Stelle einfügen
            int i;
            for (i = size - 1; (i >= 0 && data[i].getKey().compareTo(key) > 0); i--)
                data[i + 1] = data[i];

            data[i + 1] = new Entry<>(key, value);

            size++;
            return null;
        }

        V v = data[search].getValue();
        data[search].setValue(value);

        return v;
    }

    @Override
    public V search(K key) {
        int search = binarySearch(key);

        if (search >= 0)
            return data[search].getValue();

        return null;
    }

    @Override
    public V remove(K key) {
        int search = binarySearch(key);
        if (search >= 0) {
            V v = data[search].getValue();

            if (size - 1 - search >= 0)
                System.arraycopy(data, search + 1, data, search, size - 1 - search);

            size--;
            return v;
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new Iterator<>() {
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            public Entry<K, V> next() {
                return data[index++];
            }
        };
    }
}
