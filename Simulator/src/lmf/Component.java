package lmf;

import common.DataStore;
import safety.FaultInjection;
import simulate.Simulator;

import java.util.List;
import java.util.Map;

public abstract class Component implements DataStore, FaultInjection {

    private String id;
    private String name;

    /**
     * 该组件下的所有data
     * key: data name
     */
    private Map<String, Data> dataMap;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Data> getDataMap() {
        return dataMap;
    }

    public void setDataMap(Map<String, Data> dataMap) {
        this.dataMap = dataMap;
    }

    public synchronized void update(String dataName, String newValue) {
        Data data = dataMap.get(dataName);
        boolean isShared = data.isShared();
        if (!isShared)
            data.setValue(newValue);
        else {
            data.setValue(newValue);
            Simulator.update(dataName, newValue);
        }
    }

    public String get(String dataName) {
        Data data = dataMap.get(dataName);
        boolean isShared = data.isShared();
        String value = null;
        if (!isShared)
            value = data.getValue();
        else {
            value = Simulator.get(dataName);
            data.setValue(value);
            dataMap.put(dataName, data);
        }
        return value;
    }

    public boolean isShared(String dataName) {
        boolean isShared = dataMap.get(dataName).isShared();
        return isShared;
    }

    public void updateData(String operateorMethod, List<Data> dataList) {
        if (("assignValue").equals(operateorMethod))
            for (Data data : dataList) {
                String dataName = data.getName();
                update(dataName, data.getValue());
            }
        else if (("operateData").equals(operateorMethod))
            for (Data data : dataList) {
                String dataName = data.getName();
                String oldValue = get(dataName);
                String operate = data.getValue();
                String operatorExcute = operate.substring(3);
                String dataType = data.getValueType();
                //对要更改的数据进行计算
                String dataValue = calculate(oldValue, operatorExcute);
                update(dataName,dataValue);
            }
    }
}
