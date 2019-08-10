package lmf;

import java.util.List;

public class Fault {
    private String lastState;
    private String conditionType;
    private String condition;
    private String operateorMethod;
    private String dataSize;
    private List<Data> dataName_type_value;

    public Fault(String lastState, String conditionType, String condition,
                 String operateorMethod, String dataSize, List<Data> dataName_type_value) {
        this.lastState = lastState;
        this.conditionType = conditionType;
        this.condition = condition;
        this.operateorMethod = operateorMethod;
        this.dataSize = dataSize;
        this.dataName_type_value = dataName_type_value;
    }

    public void setLastState(String lastState) {
        this.lastState = lastState;
    }

    public void setConditionType(String conditionType) {
        this.conditionType = conditionType;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public void setOperateorMethod(String operateorMethod) {
        this.operateorMethod = operateorMethod;
    }

    public void setDataSize(String dataSize) {
        this.dataSize = dataSize;
    }

    public void setDataName_type_value(List<Data> dataName_type_value) {
        this.dataName_type_value = dataName_type_value;
    }

    public String getLastState() {
        return lastState;
    }

    public String getConditionType() {
        return conditionType;
    }

    public String getCondition() {
        return condition;
    }

    public String getOperateorMethod() {
        return operateorMethod;
    }

    public String getDataSize() {
        return dataSize;
    }

    public List<Data> getDataName_type_value() {
        return dataName_type_value;
    }
}
