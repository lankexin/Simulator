package simulate;

import common.DataStore;
import lmf.Component;
import lmf.Data;
import safety.FaultInjection;

import java.util.List;
import java.util.Map;

public abstract class ComponentManage implements DataStore, FaultInjection {
    public synchronized void update(Component component, String dataName, String newValue) {
        Map<String, Data> dataMap=component.getDataMap();
        Data data = dataMap.get(dataName);
        boolean isShared = data.isShared();
        if (!isShared)
            data.setValue(newValue);
        else {
            data.setValue(newValue);
            Simulator.update(dataName, newValue);
        }
    }

    public String get(Component component, String dataName) {
        Map<String, Data> dataMap=component.getDataMap();
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

    public boolean isShared(Component component, String dataName) {
        Map<String, Data> dataMap=component.getDataMap();
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
