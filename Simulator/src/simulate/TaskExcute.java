package simulate;

import lmf.*;
import manager.ComponentManage;
import manager.TaskInstanceManage;
import realtime.Schedule;
import safety.FaultInject;
import util.LogicCaculator;
import util.ParseStr;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static safety.FaultSet.getFaultInjectMap;
import static simulate.Simulator.*;
import static simulate.Simulator.currentTimePiece;

public class TaskExcute implements FaultInject {

    //timePieceMap  时间片--任务Id
    public void taskExcute() {

        /**
         * 在队列里找到当前需要执行的task并使其开始执行
         * 即 更改该g任务的 execute time
         */
        if (Simulator.timePieceMap != null && !Simulator.timePieceMap.isEmpty()) {
            //当前执行的任务实例id
            String taskInsaneId = timePieceMap.get(currentTimePiece);

            TaskInstance currentTaskInstance = waitingTaskInstanceList.get(taskInsaneId);

            //任务剩余几个时间片
//            float leftTaskPiece = currentTaskInstance.getLeftExcuteTime()/timePiece;
            //任务的当前状态
            State currentState = currentTaskInstance.getCurrentState();
            //状态剩余几个时间片
            float leftStatePiece = currentTaskInstance.getStateLeftExcuteTime()/timePiece;

            String taskId = currentTaskInstance.getTaskId();
            Task task = taskMap.get(taskId);

            String componentId = task.getComponentId();
            Component component = componentMap.get(componentId);

            if (!currentTaskInstance.getTaskState().equals("运行")) {
                currentTaskInstance.setTaskState("运行");
            }

            if (leftStatePiece <= 0) {
                String statePath=currentTaskInstance.getStatePath();
                List<Transition> transitions = task.getTransitionMap().get(currentState.getId());
//        Map<String, Data> dataMap = component.getDataMap();
//        String taskInsaneId = currentTaskInstance.getTaskId();
//        boolean isTransition = false;
                State newState=null;
                String transitionEvent=null;
                String parsedEvent=null;
                for (Transition transition : transitions) {
                    String express= ParseStr.parseStr(transition.getEvent(),component);
                    if (LogicCaculator.eventProcess(express)) {
                        transitionEvent=transition.getEvent();
                        parsedEvent=express;
                        String destId=transition.getDest();
                        newState=task.getStateMap().get(destId);
                    }
                }
//                boolean trueTransition=StateOperate.stateTransition(currentState, component, currentTaskInstance,
//                        task, blockQueue, taskInstanceMap);
                if(newState==null || newState.getName().trim().toLowerCase().equals("idle")){
                    if(newState==null){
                        blockQueue.add(currentTaskInstance);
                        String appendMessage="当前阻塞在状态"+currentState.getName();
                        List<String> pathBuffer=statePathBuffer.get(taskInsaneId);
                        pathBuffer.add(appendMessage);
                        statePathBuffer.put(taskInsaneId,pathBuffer);
                    }
                    else{
                        waitingTaskInstanceList.remove(taskInsaneId);
                        String appendMessage="当前任务实例状态迁移到idle,运行结束,时间"+currentTimePiece+
                                ",迁移事件"+transitionEvent+",解析事件"+parsedEvent;
                        StringBuilder temproStatePath=new StringBuilder(statePath);
                        temproStatePath.append("->"+newState.getName());
                        List<String> pathBuffer=statePathBuffer.get(taskInsaneId);
                        pathBuffer.add(appendMessage);
                        statePathBuffer.put(taskInsaneId,pathBuffer);
                    }
                    Schedule schedule = new Schedule();
                    timePieceMap = schedule.schedule(currentTimePiece, waitingTaskInstanceList,
                            taskMap);
//
//                    taskInsaneId = newTimePieceMap.get(currentTimePiece);
//
//                    currentTaskInstance = taskInstanceMap.get(taskInsaneId);
//
//                    //任务剩余几个时间片
//                    leftTaskPiece = currentTaskInstance.getLeftExcuteTime();
//                    //任务的当前状态
//                    currentState = currentTaskInstance.getCurrentState();
//
//                    //状态剩余几个时间片
//                    leftStatePiece = currentState.getLeftExcuteTime();
//
//                    taskId = currentTaskInstance.getTaskId();
//                    task = taskMap.get(taskId);
//
//                    componentId = task.getComponentId();
//                    component = componentMap.get(componentId);
//
//                    if (!currentTaskInstance.getTaskState().equals("运行")) {
//                        currentTaskInstance.setTaskState("运行");
//                    }
                    taskExcute();
                    return;
                }
                else{
                    String lastStateName=currentState.getName();
                    currentTaskInstance.setCurrentState(newState);
                    String appendMessage=lastStateName+"状态迁移到"+currentState.getName()+",时间"+currentTimePiece+",迁移事件"
                            +transitionEvent+",解析事件"+parsedEvent;
                    currentTaskInstance.setStateLeftExcuteTime(currentState.getWcet());

                    //记录迁移到故障状态的信息
                    if(newState.isFaultState()){
                        //todo:
                        String appendFault=lastStateName+"状态迁移到故障状态"+currentState.getName()+",时间"
                                +currentTimePiece+",迁移事件"+transitionEvent+",解析事件"+parsedEvent;
                        List<String> faults=faultBuffer.get(taskInsaneId);
                        faults.add(appendFault);
                        faultBuffer.put(taskInsaneId,faults);
                    }

                    List<String> pathBuffer=statePathBuffer.get(taskInsaneId);
                    pathBuffer.add(appendMessage);
                    statePathBuffer.put(taskInsaneId,pathBuffer);
                }
            }

            else if (leftStatePiece == currentState.getWcet()) {
                String entryEvent = currentState.getEntryEvent();
                if (entryEvent != null) {
                    StateOperate.updateDataInState(entryEvent,component);
                }
            }

            else if (leftStatePiece == 2) {
                String doEvent = currentState.getDoEvent();
                if (doEvent != null) {
                    StateOperate.updateDataInState(doEvent,component);
                }
            }

            else if (leftStatePiece == 1) {
                String exitEvent = currentState.getExitEvent();
                if (exitEvent != null) {
                    StateOperate.updateDataInState(exitEvent,component);
                }
                faultInjection(currentTaskInstance, component, currentState);
                List<Transition> transitions = task.getTransitionMap().get(currentState.getId());

                State possibleNewState=null;
                String transitionEvent=null;
                String parsedEvent=null;
                for (Transition transition : transitions) {
                    String express= ParseStr.parseStr(transition.getEvent(),component);
                    if (LogicCaculator.eventProcess(express)) {
                        transitionEvent=transition.getEvent();
                        parsedEvent=express;
                        String destId=transition.getDest();
                        possibleNewState=task.getStateMap().get(destId);
                    }
                }

                if(possibleNewState==null){
                    blockQueue.add(currentTaskInstance);
                    /** key：任务实例id
                     * value：状态-event-data-timestamp*/
                    String appendMessage="当前阻塞在状态"+currentState.getName();
                    List<String> pathBuffer=statePathBuffer.get(taskInsaneId);
                    pathBuffer.add(appendMessage);
                    statePathBuffer.put(taskInsaneId,pathBuffer);
                }
                else if(possibleNewState.getName().trim().toLowerCase().equals("idle")){
                    waitingTaskInstanceList.remove(taskInsaneId);
                    String appendMessage="当前任务实例状态迁移到idle，运行结束,时间"+currentTimePiece+
                    ",迁移事件"+transitionEvent+",解析事件"+parsedEvent;
                    List<String> pathBuffer=statePathBuffer.get(taskInsaneId);
                    pathBuffer.add(appendMessage);
                    statePathBuffer.put(taskInsaneId,pathBuffer);
                }
            }
            currentTaskInstance.setStateLeftExcuteTime((leftStatePiece-1)*timePiece);
//            currentState.setLeftExcuteTime(leftStatePiece - 1);
//            currentTaskInstance.setLeftExcuteTime(leftTaskPiece - 1);
        }
    }

    @Override
    public void faultInjection(TaskInstance taskInstance, Component component, State lastState) {
        Map<String, Fault> faultSet = getFaultInjectMap();
        Fault fault = faultSet.get(lastState.getId());
        ComponentManage componentManage = new ComponentManage();
        TaskInstanceManage taskInstanceManage = new TaskInstanceManage();

        if (fault != null) {
            String conditionType = fault.getConditionType();
            String operateorMethod = fault.getOperateorMethod();
            //根据注入条件类型的不同来分别检查
            if (conditionType.equals("relatedData")) {
                String condition = fault.getCondition();
//                String relatedDataName = condition.split(";")[0];
                //判定当前这个环境数据值是否满足注入条件的范围
//                boolean isInRange = componentManage.get(component, relatedDataName).isInRange(condition);
                String parsedStr=ParseStr.parseStr(condition,component);
                boolean isInRange=LogicCaculator.eventProcess(parsedStr);
                //满足条件
                if (isInRange) {
                    List<String> dataList = fault.getDataList();
                    componentManage.updateData(component, dataList);
                }
            }
            if (conditionType.equals("transitionPath")) {
                String condition = fault.getCondition();
                boolean istransition = taskInstanceManage.isTransition(taskInstance, condition);
                if (istransition) {
                    List<String> dataList = fault.getDataList();
                    componentManage.updateData(component, dataList);
                }
            }
        }
    }

}
