package manager;

import common.DataStore;
import lmf.Component;
import lmf.Data;
import safety.FaultInjectUpdate;
import simulate.Simulator;
import util.NumericCaculator;

import java.util.List;
import java.util.Map;

public class ComponentManage implements DataStore, FaultInjectUpdate {

    @Override
    public synchronized void update(Component component,String dataName, String newValue) {
        Map<String, Data> dataMap=component.getDataMap();
        Data data = dataMap.get(dataName);
        boolean isShared = data.isShared();
        if (!isShared)
            data.setValue(newValue);
        else {
            data.setValue(newValue);
            Simulator simulator=new Simulator();
            simulator.update(dataName, newValue);
        }
    }

    @Override
    public String get(Component component,String dataName) {
        Map<String, Data> dataMap=component.getDataMap();
        Data data = dataMap.get(dataName);
        boolean isShared = data.isShared();
        String value = null;
        if (!isShared)
            value = data.getValue();
        else {
            Simulator simulator=new Simulator();
            value = simulator.get(dataName);
            data.setValue(value);
            dataMap.put(dataName, data);
        }
        return value;
    }

    @Override
    public boolean isShared(Component component,String dataName) {
        Map<String, Data> dataMap=component.getDataMap();
        boolean isShared = dataMap.get(dataName).isShared();
        return isShared;
    }

    @Override
    public void updateData(Component component, String operateorMethod, List<String> dataList ) {
        if (("assignValue").equals(operateorMethod))
            for (Data data : dataList) {
                String dataName = data.getName();
                update(component,dataName, data.getValue());
            }
        else if (("operateData").equals(operateorMethod))
            for (Data data : dataList) {
                String dataName = data.getName();
                String oldValue = get(component,dataName);
                String operate = data.getValue();
                String operatorExcute = operate.substring(3);
                String dataType = data.getValueType();
                NumericCaculator numericCaculator=new NumericCaculator();
                //对要更改的数据进行计算
                String dataValue = numericCaculator.getEventuate(operatorExcute);
                update(component,dataName,dataValue);
            }
    }
}
