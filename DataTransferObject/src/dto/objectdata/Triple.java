package dto.objectdata;

import javafx.util.Pair;

public class Triple<K,E,V> {
    private K key;
    private E extraData;
    private V value;

    public Triple() {

    }

    public Triple(K key, E extraData, V value) {
        this.key = key;
        this.extraData = extraData;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public E getExtraData() {
        return extraData;
    }

    public void setExtraData(E extraData) {
        this.extraData = extraData;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }
}
