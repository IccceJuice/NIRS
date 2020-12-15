package sample.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class HistogramEntry implements Serializable {

    private int key;
    private int value;
    private static final long serialVersionUID = 46573410293483L;

    public HistogramEntry() {

    }

    public HistogramEntry(int key, int value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
