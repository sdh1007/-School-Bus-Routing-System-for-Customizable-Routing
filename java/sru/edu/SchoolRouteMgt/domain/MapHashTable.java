package sru.edu.SchoolRouteMgt.domain;

// map hash table to track coordinates from Google Maps

import java.util.ArrayList;
import java.util.List;

public class MapHashTable <K, V> {
    private final int INITIAL_SIZE = 16;
    private List<Entry<K, V>>[] entries;

    public void HashTable() {
        entries = new ArrayList[INITIAL_SIZE];
    }

    public void put(K key, V value) {
        int index = getIndex(key);
        if (entries[index] == null) {
            entries[index] = new ArrayList<>();
        }
        entries[index].add(new Entry<>(key, value));
    }

    public V get(K key) {
        int index = getIndex(key);
        List<Entry<K, V>> bucket = entries[index];
        if (bucket != null) {
            for (Entry<K, V> entry : bucket) {
                if (entry.key.equals(key)) {
                    return entry.value;
                }
            }
        }
        return null;
    }

    private int getIndex(K key) {
        return key.hashCode() % INITIAL_SIZE;
    }

    static class Entry<K, V> {
        K key;
        V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}