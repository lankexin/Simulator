package lmf;

public class Data {
    private String name;
    private String valueType;
    private String value;
    private boolean isShared;

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
}
