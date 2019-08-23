package simulate;

import lmf.*;

import util.LogicCaculator;
import manager.ComponentManage;
import util.ParseStr;

import java.util.List;
import java.util.Map;

import static simulate.ExpressCalculate.getResultData;
import static util.ParseStr.getAssignedData;

public class StateOperate {

//    public void faultInjection(String taskKey, Fault fault, Map<String, Data> dataMap,
//                               Map<String, List<String>> statePath) {
//        if (fault != null) {
//            String conditionType = fault.getConditionType();
//            String operateorMethod = fault.getOperateorMethod();
//            //根据注入条件类型的不同来分别检查
//            if (conditionType.equals("relatedData")) {
//                String condition = fault.getCondition();
//                String relatedDataName = condition.split(";")[0];
//                //判定当前这个环境数据值是否满足注入条件的范围
//                boolean isInRange = dataMap.get(relatedDataName).isInRange(condition);
//                if (isInRange) {
//                    List<Data> dataList = fault.getDataName_type_value();
//                    updateData(operateorMethod,dataMap , dataList);
//                }
//            }
//
//            if (conditionType.equals("transitionPath")) {
//                String condition = fault.getCondition();
//                List<String> transitionPath = statePath.get(taskKey);
//                String path = getPath(transitionPath);
//                boolean istransition = isTransition(path, condition);
//                if (istransition) {
//                    List<Data> dataList = fault.getDataName_type_value();
//                    updateData(operateorMethod,dataMap , dataList);
//                }
//            }
//        }
//    }
//
//    /**
//     * 得到当前任务的迁移路径
//     */
//    public String getPath(List<String> transitionPath) {
//        String path = null;
//        if(transitionPath!=null) {
//            StringBuilder sb = new StringBuilder();
//            for (String transition : transitionPath) {
//                String[] strs = transition.split("-");
//                String state = strs[0];
//                sb.append(state + "->");
//            }
//            path = sb.toString();
//        }
//        return path;
//    }
//
//    /**判定是不是配置文件中指定的迁移路径*/
//    public boolean isTransition(String path,String condition){
//        boolean isTransition=false;
//        if(path.contains(condition))
//            isTransition=true;
//        return isTransition;
//    }
//
//    /**根据配置文件中指定的方式更新数据*/
//    public void updateData(String operateorMethod, Map<String, Data> dataMap , List<Data> dataList) {
//        if(("assignValue").equals(operateorMethod))
//            for(Data data:dataList){
//                String dataName=data.getName();
//                dataMap.put(dataName,data);
//            }
//        else if(("operateData").equals(operateorMethod))
//            for(Data data:dataList) {
//                String dataName = data.getName();
//                String oldValue=dataMap.get(dataName).getValue();
//                String operate = data.getValue();
//                String operatorExcute=operate.substring(3);
//                String dataType=data.getValueType();
//                //对要更改的数据进行计算
//                String dataValue=calculate(oldValue,operatorExcute);
//                Data newData=new Data(dataName,dataType,dataValue);
//                dataMap.put(dataName,newData);
//            }
//    }
//
//    /**
//     * 记录哪个注入的故障备注入进去了，哪个没有被注入进去，
//     * 即上下文的条件不具备，就是说最初的数据输入不满足会让这个故障输入起效
//     */
//    public List<String> report(String faultState) {
//        List<String> faultInjectLog = new ArrayList<>();
//        faultInjectLog.add(faultState);
//        return faultInjectLog;
//    }

//    public static String stateTransition(State currentState, Component component, TaskInstance currentTaskInstance,
//                                          Task task, List<TaskInstance> blockQueue, Map<String, TaskInstance> taskInstanceMap) {
        public static State stateTransition(State currentState, Component component, Task task) {
//        boolean trueTransition = false;
        List<Transition> transitions = task.getTransitionMap().get(currentState.getId());
//        Map<String, Data> dataMap = component.getDataMap();
//        String taskInsaneId = currentTaskInstance.getTaskId();
//        boolean isTransition = false;
        for (Transition transition : transitions) {
            String express=ParseStr.parseStr(transition.getEvent(),component);
            if (LogicCaculator.eventProcess(express)) {
                String destId=transition.getDest();
                State newState=task.getStateMap().get(destId);
//                String stateName=newState.getName();
                return newState;
//                currentTaskInstance.setCurrentState(newState);
//                isTransition = true;
//                if (newState.getName().trim().toLowerCase().equals("idle")) {
//                    taskInstanceMap.remove(taskInsaneId);
//                    trueTransition = false;
//                } else
//                    trueTransition = true;
            }
        }
        return null;
//        if (!isTransition) {
//            blockQueue.add(currentTaskInstance);
//            trueTransition = false;
//        }
//        return trueTransition;
    }

    public static void updateDataInState(String event, Component component) {
        String parsedStr = ParseStr.parseStr(event, component);
        Map<String, String> dataStr = getAssignedData(parsedStr);
        ComponentManage componentManage=new ComponentManage();
        String dataName = dataStr.entrySet().iterator().next().getKey();
        String express = dataStr.entrySet().iterator().next().getValue();
        String value=getResultData(express);
        componentManage.update(component, dataName, value);

        List<String> componentIds=channelDataMap.get(dataName);
        if(componentIds!=null){
            for(String componentId:componentIds){
                if(component.getId().equals(componentId)){
                    componentManage.update(component,dataName,"null");
                }
                else{
                    componentManage.update(component,dataName,value);
                }
            }
        }

    }

}
