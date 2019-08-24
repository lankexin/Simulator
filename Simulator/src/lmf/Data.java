package lmf;

import java.util.HashMap;
import java.util.Map;

public class Data {
    private String name;
    private String valueType;
    private String value;
    private boolean isShared;
    private boolean isChannel;

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
        if (attrs.get("name") != null) {
            name = attrs.get("name");
        } else {
            name = "";
        }
        return name;
    }

    public String getValueType() {
        return valueType;
    }

    public String getValue() {
        if (value == null) value = "null";
        return value;
    }

    public boolean isShared() {
        isShared = (attrs.get("shared")!=null);
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

    public boolean isChannel() {
        isChannel = (attrs.get("ischannel") != null);
        return isChannel;
    }
}
