package manager;

import common.DataStore;
import lmf.Component;
import lmf.Data;
import lmf.TaskInstance;
import safety.FaultInjectUpdate;
import simulate.Simulator;
import simulate.StateOperate;
import util.ParseStr;

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
            simulator.update(component, dataName, newValue);
        }
    }

    @Override
    public String get(TaskInstance taskInstance, Component component, String dataName) {
        String value=null;
        if (channelDataMap.get(dataName) != null && taskInstance!=null) {
            Map<String, String> dataMap = taskInstance.getDataMap();
            value=dataMap.get(dataName);
        } else {
            Map<String, Data> dataMap = component.getDataMap();
            System.out.println(dataMap.keySet());
            Data data = dataMap.get(dataName);
            boolean isShared = data.isShared();
            if (!isShared) {
                value = data.getValue();
                System.out.println("yyyyyy"+dataMap.entrySet().iterator().next().getValue().getValue());
            }
            else {
                Simulator simulator = new Simulator();
                value = simulator.get(taskInstance,component, dataName);
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
    public void updateData(TaskInstance taskInstance,Component component, List<String> dataList) {
        for (String dataStr : dataList) {
            String parsedStr = ParseStr.parseStr(taskInstance, dataStr, component);
            Map<String, String> dataStrs = getAssignedData(parsedStr);
            ComponentManage componentManage = new ComponentManage();
            String dataName = dataStrs.entrySet().iterator().next().getKey();
            String express = dataStrs.entrySet().iterator().next().getValue();
            String value = getResultData(express);
            System.out.println(dataName+" "+"value:"+value);
            componentManage.update(component, dataName, value);
        }
    }

}
