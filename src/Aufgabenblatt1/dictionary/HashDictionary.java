package Aufgabenblatt1.dictionary;

import java.util.Iterator;
import java.util.LinkedList;

public class HashDictionary <K, V> implements Dictionary<K, V> {
    private LinkedList<Entry<K, V>>[] data;

    public HashDictionary(int m) {
        if (!isPrim(m)) {
            do {
                m++;
            } while (!isPrim(m));
        }
        data = new LinkedList[m];
    }

    private boolean isPrim(int n) {
        if (n == 2 || n == 3)
            return true;
        if (n <= 1 || n % 2 == 0 || n % 3 == 0)
            return false;
        for (int i = 5; i * i <= n; i += 6)
        {
            if (n % i == 0 || n % (i + 2) == 0)
                return false;
        }
        return true;
    }

    private int h(K key) {
        int m = data.length;
        int adr = key.hashCode();
        /*int adr = 0;
        for (int i = 0; i < key.length(); i++)
            adr = 31*adr + key.charAt(i);*/

        if (adr < 0)
            adr = -adr;
        return adr % m;
    }

    @Override
    public V insert(K key, V value) {
        if (data[h(key)] != null) {
            for (Entry<K, V> entry : data[h(key)]) {
                if (entry.getKey().equals(key)) {
                    V v = entry.getValue();
                    entry.setValue(value);
                    return v;
                }
            }
        } else {
            data[h(key)] = new LinkedList<>();
        }
        data[h(key)].add(new Entry<>(key, value));
        return null;
    }

    @Override
    public V search(K key) {
        if (data[h(key)] != null) {
            for (Entry<K, V> entry : data[h(key)]) {
                if (entry.getKey().equals(key))
                    return entry.getValue();
            }
        }
        return null;
    }

    @Override
    public V remove(K key) {
        if (data[h(key)] != null) {
            for (Entry<K, V> entry : data[h(key)]) {
                if (entry.getKey().equals(key)) {
                    V v = entry.getValue();
                    data[h(key)].remove(entry);
                    return v;
                }
            }
        }
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return null;
    }
}
