package manager;

import common.DataStore;
import lmf.Component;
import lmf.Data;
import lmf.TaskInstance;
import safety.FaultInjectUpdate;
import simulate.Simulator;
import simulate.StateOperate;

import java.util.List;
import java.util.Map;

import static simulate.ExpressCalculate.getResultData;
import static simulate.Simulator.channelDataMap;
import static util.ParseStr.getAssignedData;

public class ComponentManage implements DataStore, FaultInjectUpdate {

    @Override
    public synchronized void update(Component component, String dataName, String newValue) {
        Map<String, Data> dataMap = component.getDataMap();
        Data data = dataMap.get(dataName);
        boolean isShared = data.isShared();
        if (!isShared)
            data.setValue(newValue);
        else {
            data.setValue(newValue);
            Simulator simulator = new Simulator();
            simulator.update(component,dataName, newValue);
        }
    }

    @Override
    public String get(TaskInstance taskInstance, Component component, String dataName) {
        if(channelDataMap.get(dataName)!=null){

        }
        else{
        Map<String, Data> dataMap = component.getDataMap();
        Data data = dataMap.get(dataName);
        boolean isShared = data.isShared();
        String value = null;
        if (!isShared)
            value = data.getValue();
        else {
            Simulator simulator = new Simulator();
            value = simulator.get(component,dataName);
            data.setValue(value);
            dataMap.put(dataName, data);
        }
        }
        return value;
    }

//    @Override
//    public boolean isShared(Component component, String dataName) {
//        Map<String, Data> dataMap = component.getDataMap();
//        boolean isShared = dataMap.get(dataName).isShared();
//        return isShared;
//    }

    @Override
    public void updateData(Component component, List<String> dataList) {
        for (String dataStr : dataList) {
            StateOperate.updateDataInState(dataStr, component);
        }
    }

}
