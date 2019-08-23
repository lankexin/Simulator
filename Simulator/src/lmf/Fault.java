package lmf;

import java.util.List;

public class Fault {
    private String lastState;
    //故障注入的模式--一定注入，以条件注入
    private String injectionMode;
    //条件类型，环境数据或者迁移路径
    private String conditionType;
    //环境条件，相关数据的取值范围
    private String condition;
    //操作方式，改变值或者指定值
    private String operateorMethod;
    //涉及到触发事件的数据有几个
    private String dataSize;
    //对触发事件相关的数据操作的配置
    private List<String> dataList;

    public Fault(String lastState, String injectionMode,String conditionType, String condition,
                 String operateorMethod, String dataSize, List<String> dataList) {
        this.lastState = lastState;
        this.injectionMode=injectionMode;
        this.conditionType = conditionType;
        this.condition = condition;
        this.operateorMethod = operateorMethod;
        this.dataSize = dataSize;
        this.dataList = dataList;
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

    public void setDataName_type_value(List<String> dataList) {
        this.dataList = dataList;
    }

    public String getLastState() {
        return lastState;
    }

    public String getInjectionMode() {
        return injectionMode;
    }

    public void setInjectionMode(String injectionMode) {
        this.injectionMode = injectionMode;
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

    public List<String> getDataList() {
        return dataList;
    }
}
