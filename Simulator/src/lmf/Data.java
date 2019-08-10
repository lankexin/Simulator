package lmf;

public class Data {
    private String name;
    private String valueType;
    private String value;

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
}
