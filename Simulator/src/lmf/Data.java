package lmf;

import java.util.HashMap;
import java.util.Map;

public class Data {
    private String name;
    private String valueType;
    private String value;
    private boolean isShared;

    private Map<String, String> attrs;

    public Data() {
        attrs = new HashMap<>();
    }

    public Data(String name, String valueType, String value) {
        this.name = name;
        this.valueType = valueType;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValueType() {
        return valueType;
    }

    public String getValue() {
        return value;
    }

    public boolean isShared() {
        return isShared;
    }

    public void setShared(boolean shared) {
        isShared = shared;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setAttr(String key, String value) {
        attrs.put(key, value);
    }

    public String getAttr(String key) {
        return attrs.get(key);
    }
}
