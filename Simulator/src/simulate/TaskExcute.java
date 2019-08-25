package simulate;

import lmf.*;
import manager.ComponentManage;
import manager.TaskInstanceManage;
import realtime.Schedule;
import safety.FaultInject;
import safety.FaultInjectMust;
import util.LogicCaculator;
import util.ParseStr;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static safety.FaultSet.getFaultInjectMap;
import static simulate.Simulator.*;

public class TaskExcute implements FaultInject, FaultInjectMust {

    //timePieceMap  时间片--任务Id
    public void taskExcute() {
        System.out.println("|||---///////"+statePathBuffer);

        System.out.println("@@@@" + currentTimePiece + ":" + waitingTaskInstanceList.keySet());

        /** 在队列里找到当前需要执行的task并使其开始执行
         * 即 更改该g任务的 execute time*/
        if (Simulator.timePieceMap != null && !Simulator.timePieceMap.isEmpty()) {
            System.out.println("---///////"+statePathBuffer);

            System.out.println("timePieceMap:" + timePieceMap);
            //当前执行的任务实例id
            String taskInsaneId = timePieceMap.get(currentTimePiece);
            System.out.println("taskInstanceId" + taskInsaneId);

            TaskInstance currentTaskInstance = waitingTaskInstanceList.get(taskInsaneId);

            //任务剩余几个时间片
//            float leftExcuteTime = currentTaskInstance.getLeftExcuteTime();
            //任务的当前状态
//            State currentState = currentTaskInstance.getCurrentState();
            //状态剩余几个时间片
//            float leftStatePiece = currentTaskInstance.getStateLeftExcuteTime() / timePiece;

            String taskId = currentTaskInstance.getTaskId();
            Task task = taskMap.get(taskId);

            String componentId = task.getComponentId();
            Component component = componentMap.get(componentId);

            if (!currentTaskInstance.getTaskState().equals("运行")) {
                currentTaskInstance.setTaskState("运行");
            }
            System.out.println(component.getName() + ":" + currentTaskInstance.getCurrentState().getName());
            System.out.println(currentTaskInstance.getStateLeftExcuteTime());
            System.out.println(currentTaskInstance.getCurrentState().getWcet() / timePiece);
            System.out.println(timePiece);

            //任务实例超过deadline的判断
            if (currentTimePiece > currentTaskInstance.getDeadline()) {
                String statePath = currentTaskInstance.getStatePath();
                waitingTaskInstanceList.remove(taskInsaneId);
                System.out.println("任务实例" + taskInsaneId + "超出deadline,时间" + currentTimePiece);
                String appendMessage = "组件" + component.getName() + " " + "当前任务实例超出deadline,时间" + currentTimePiece + ",deadline" +
                        currentTaskInstance.getDeadline();
                System.out.println("------"+statePathBuffer.get(taskInsaneId));

                setStateBuffer(component,taskInsaneId,appendMessage);
                System.out.println("///////"+statePathBuffer);


                String appendFault = "组件" + component.getName() + " " + "当前任务实例超出deadline,发生超时故障,时间"
                        + currentTimePiece + ",deadline" + currentTaskInstance.getDeadline();
                setFaultBuffer(component,taskInsaneId,appendFault);

                Schedule schedule = new Schedule();
                timePieceMap = schedule.schedule(currentTimePiece - 1, waitingTaskInstanceList);

                taskExcute();
                return;
            }

            System.out.println("left state piece " + (int) (currentTaskInstance.getStateLeftExcuteTime() / timePiece));
            System.out.println("state name " + currentTaskInstance.getCurrentState().getName());

            if ((int) (currentTaskInstance.getStateLeftExcuteTime() / timePiece) <= 0) {
                String statePath = currentTaskInstance.getStatePath();
                List<Transition> transitions = task.getTransitionMap().get(currentTaskInstance.getCurrentState().getId());
                System.out.println("transitions" + transitions);
                State newState = null;
                String transitionEvent = null;
                String parsedEvent = null;
                for (Transition transition : transitions) {
                    System.out.println("event" + transition.getEvent());
                    if (transition.getEvent().equals("")) {
                        transitionEvent = transition.getEvent();
                        parsedEvent = "";
                        String destId = transition.getDest();
                        newState = task.getStateMap().get(destId);
                        break;
                    }
                    String express = ParseStr.parseStr(currentTaskInstance, transition.getEvent(), component);
                    System.out.println("express" + express);
                    if (LogicCaculator.eventProcess(express)) {
                        transitionEvent = transition.getEvent();
                        parsedEvent = express;
                        String destId = transition.getDest();
                        newState = task.getStateMap().get(destId);
                        break;
                    }
                }
                if (newState == null || newState.getName().trim().toLowerCase().equals("idle")) {
                    if (newState == null) {
                        blockQueue.add(currentTaskInstance);
                        waitingTaskInstanceList.remove(taskInsaneId);
                        String appendMessage = "组件" + component.getName() + " " + "当前阻塞在状态" + currentTaskInstance.getCurrentState().getName();
                        System.out.println("------"+statePathBuffer.get(taskInsaneId));

                        setStateBuffer(component,taskInsaneId,appendMessage);
                        System.out.println("///////"+statePathBuffer);
                    } else {
                        waitingTaskInstanceList.remove(taskInsaneId);
                        String appendMessage = "组件" + component.getName() + " " + "当前任务实例状态从" + currentTaskInstance.getCurrentState().getName() + "迁移到idle,运行结束,时间" + currentTimePiece +
                                ",迁移事件" + transitionEvent + ",解析事件" + parsedEvent;
                        StringBuilder temproStatePath = new StringBuilder(statePath);
                        temproStatePath.append("->" + newState.getName());
                        System.out.println("------"+statePathBuffer.get(taskInsaneId));

                        setStateBuffer(component,taskInsaneId,appendMessage);
                        System.out.println("///////"+statePathBuffer);


                    }
                    Schedule schedule = new Schedule();
                    timePieceMap = schedule.schedule(currentTimePiece, waitingTaskInstanceList);

                    taskExcute();
                    return;

                } else {
                    String lastStateName = currentTaskInstance.getCurrentState().getName();
                    currentTaskInstance.setCurrentState(newState);
                    currentTaskInstance.setStateLeftExcuteTime(newState.getWcet());
                    String appendMessage = "组件" + component.getName() + " " + lastStateName + "状态迁移到" + newState.getName() + ",时间" +
                            currentTimePiece + ",迁移事件" + transitionEvent + ",解析事件" + parsedEvent;
                    currentTaskInstance.setStateLeftExcuteTime(newState.getWcet());
                    System.out.println("------"+statePathBuffer.get(taskInsaneId));

                    setStateBuffer(component,taskInsaneId,appendMessage);
                    System.out.println("///////"+statePathBuffer);


                    String entryEvent = currentTaskInstance.getCurrentState().getEntryEvent();
                    if (entryEvent != null && !entryEvent.isEmpty()) {
                        StateOperate.updateDataInState(currentTaskInstance, entryEvent, component);
                    }

                    //记录迁移到故障状态的信息
                    if (newState.isFaultState()) {
                        String appendFault = "组件" + component.getName() + " " + lastStateName + "状态迁移到故障状态" +
                                currentTaskInstance.getCurrentState().getName() + ",时间"
                                + currentTimePiece + ",迁移事件" + transitionEvent + ",解析事件" + parsedEvent;
                        setFaultBuffer(component,taskInsaneId,appendFault);
                        System.out.println("///////"+statePathBuffer);

                    }
                }
            }
            if ((int) (currentTaskInstance.getStateLeftExcuteTime() / timePiece) == currentTaskInstance.getCurrentState().getWcet() / timePiece) {
                System.err.println("start entry");
                String entryEvent = currentTaskInstance.getCurrentState().getEntryEvent();
                if (entryEvent != null && !entryEvent.isEmpty()) {
                    StateOperate.updateDataInState(currentTaskInstance, entryEvent, component);
                }
                String appendMessage = "组件" + component.getName() + "当前仍在运行,时间" + currentTimePiece;
                System.out.println("------"+statePathBuffer.get(taskInsaneId));

                setStateBuffer(component,taskInsaneId,appendMessage);
                System.out.println("///////"+statePathBuffer);

            }
            if ((int) (currentTaskInstance.getStateLeftExcuteTime() / timePiece) == 2) {
                String doEvent = currentTaskInstance.getCurrentState().getDoEvent();
                if (doEvent != null && !doEvent.isEmpty()) {
                    StateOperate.updateDataInState(currentTaskInstance, doEvent, component);
                }
                String appendMessage = "组件" + component.getName() + "当前仍在运行,时间" + currentTimePiece;
                System.out.println("------"+statePathBuffer.get(taskInsaneId));

                setStateBuffer(component,taskInsaneId,appendMessage);
                System.out.println("///////"+statePathBuffer);

            }
            if ((int) (currentTaskInstance.getStateLeftExcuteTime() / timePiece) == 1) {
                String exitEvent = currentTaskInstance.getCurrentState().getExitEvent();
                if (exitEvent != null && !exitEvent.isEmpty()) {
                    if (!exitEvent.contains("report")) {
                        StateOperate.updateDataInState(currentTaskInstance, exitEvent, component);
                        System.out.println("hahaha");
                    }
                }
                /**故障注入，通过改变当前触发事件相关的数据值来注入故障*/
                Map<String, Fault> faultSet = getFaultInjectMap();
                //故障集中是否存在当前的fault
                Fault fault = faultSet.get(component.getName() + "-" + currentTaskInstance.getCurrentState().getName());

                /**不管什么条件下都强行注入故障*/
                if (fault != null && fault.getInjectionMode().equals("must")) {
                    String faultType = faultInjectMust(currentTaskInstance, component, fault);
                    if (faultType != null) {
                        String appendFault = "组件" + component.getName() + "注入故障，时间" + currentTimePiece +
                                "故障类型为" + faultType;
                        setFaultBuffer(component,taskInsaneId,appendFault);
                    }
                }

                /**判断满足配置文件中指定的条件才注入故障*/
                else if (fault != null && fault.getInjectionMode().equals("not_must")) {
                    String faultType = faultInject(currentTaskInstance, component, fault);
                    if (faultType != null) {
                        String appendFault = "组件" + component.getName() + "注入故障，时间" + currentTimePiece +
                                "故障类型为" + faultType;
                        setFaultBuffer(component,taskInsaneId,appendFault);
                    }
                    if (faultType == null) {
                        String appendFault = "组件" + component.getName() + "注入故障失败，不满足注入条件，时间" + currentTimePiece;
                        setFaultBuffer(component,taskInsaneId,appendFault);
                    }
                }

                List<Transition> transitions = task.getTransitionMap()
                        .get(currentTaskInstance.getCurrentState().getId());
                System.out.println("transitionMap" + task.getTransitionMap());
                System.out.println("transitions" + transitions);
                State possibleNewState = null;
                String transitionEvent = null;
                String parsedEvent = null;
                for (Transition transition : transitions) {
                    String express = ParseStr.parseStr(currentTaskInstance, transition.getEvent(), component);
                    if (LogicCaculator.eventProcess(express)) {
                        transitionEvent = transition.getEvent();
                        parsedEvent = express;
                        String destId = transition.getDest();
                        possibleNewState = task.getStateMap().get(destId);
                    }
                }

                if (possibleNewState == null) {
                    blockQueue.add(currentTaskInstance);
                    waitingTaskInstanceList.remove(taskInsaneId);
                    /** key：任务实例id
                     * value：状态-event-data-timestamp*/
                    String appendMessage = "当前阻塞在状态" + currentTaskInstance.getCurrentState().getName();
                    System.out.println("------"+statePathBuffer.get(taskInsaneId));

                    setStateBuffer(component,taskInsaneId,appendMessage);
                    System.out.println("///////"+statePathBuffer);


                } else if (possibleNewState.getName().trim().toLowerCase().equals("idle")) {
                    waitingTaskInstanceList.remove(taskInsaneId);
                    String appendMessage = "当前任务实例状态迁移到idle，运行结束,时间" + currentTimePiece +
                            ",迁移事件" + transitionEvent + ",解析事件" + parsedEvent;
                    System.out.println("------"+statePathBuffer.get(taskInsaneId));

                    setStateBuffer(component,taskInsaneId,appendMessage);
                    System.out.println("///////"+statePathBuffer);

                } else {
                    String appendMessage = "组件" + component.getName() + "当前仍在运行,时间" + currentTimePiece;
                    System.out.println("------"+statePathBuffer.get(taskInsaneId));
                    setStateBuffer(component,taskInsaneId,appendMessage);
                    System.out.println("///////"+statePathBuffer);

                }
            }

            System.out.println("!!!!!" + currentTimePiece + "-" + component.getName() + ":" + taskInsaneId + ":" +
                    currentTaskInstance.getCurrentState().getName());
            currentTaskInstance.setStateLeftExcuteTime(currentTaskInstance.getStateLeftExcuteTime() - timePiece);
            currentTaskInstance.setLeftExcuteTime(currentTaskInstance.getLeftExcuteTime() - 1 * timePiece);
            System.out.println("---///////"+statePathBuffer);

        }
    }

    @Override
    public String faultInject(TaskInstance taskInstance, Component component, Fault fault) {
        String type = null;
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
                String parsedStr = ParseStr.parseStr(taskInstance, condition, component);
                boolean isInRange = LogicCaculator.eventProcess(parsedStr);
                //满足条件
                if (isInRange) {
                    List<String> dataList = fault.getDataList();
                    componentManage.updateData(taskInstance, component, dataList);
                    type = fault.getFaultType();
                }
            }
            if (conditionType.equals("transitionPath")) {
                String condition = fault.getCondition();
                boolean istransition = taskInstanceManage.isTransition(taskInstance, condition);
                if (istransition) {
                    List<String> dataList = fault.getDataList();
                    componentManage.updateData(taskInstance, component, dataList);
                    type = fault.getFaultType();

                }
            }
        }
        return type;
    }

    @Override
    public String faultInjectMust(TaskInstance taskInstance, Component component, Fault fault) {
        String type = null;
        ComponentManage componentManage = new ComponentManage();
        TaskInstanceManage taskInstanceManage = new TaskInstanceManage();
        String operateorMethod = fault.getOperateorMethod();
        //不检查条件是否满足，直接注入
        List<String> dataList = fault.getDataList();
        componentManage.updateData(taskInstance, component, dataList);
        type = fault.getFaultType();
        return type;
    }

    public void setStateBuffer(Component component,String taskInsaneId,String appendMessage){
        System.out.println("appendMessage"+appendMessage);
        List<String> pathBuffer = statePathBuffer.get("任务实例编号----"+taskInsaneId);
        System.out.println("原来的"+pathBuffer);
        if (pathBuffer == null) {
            pathBuffer = new ArrayList<>();
        }
        if (!pathBuffer.contains(appendMessage))
            pathBuffer.add(appendMessage);
        System.out.println("现在的"+pathBuffer);
        statePathBuffer.put("任务实例编号----"+taskInsaneId, pathBuffer);
        System.out.println(pathBuffer+"pathbbbb");
    }

    public void setFaultBuffer(Component component,String taskInsaneId,String appendFault){
        List<String> faults = faultBuffer.get("任务实例编号----"+taskInsaneId);
        if (faults == null)
            faults = new ArrayList<>();
        faults.add(appendFault);
        faultBuffer.put("任务实例编号----"+taskInsaneId, faults);
    }

}
