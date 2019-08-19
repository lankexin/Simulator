package simulate;

import lmf.*;
import manager.ComponentManage;
import manager.TaskInstanceManage;
import safety.FaultInject;
import safety.FaultSet;
import util.EventProcess;
import java.util.List;
import java.util.Map;

import static safety.FaultSet.getFaultInjectMap;

public class TaskExcute implements FaultInject {

    //timePieceMap  时间片--任务Id
    public void taskExcute(int currentTimePiece,Map<String, Component> componentMap,Map<String, TaskInstance> taskInstanceMap,
                                  List<TaskInstance> blockQueue,Map<Integer,String> timePieceMap, Map<String,Task> taskMap) {

        /**
         * 在队列里找到当前需要执行的task并使其开始执行
         * 即 更改该g任务的 execute time
         */
        if (timePieceMap!=null && !timePieceMap.isEmpty()) {
            //当前执行的任务实例id
            String taskInsaneId=timePieceMap.get(currentTimePiece);

            TaskInstance currentTaskInstance = taskInstanceMap.get(taskInsaneId);

            //任务剩余几个时间片
            float leftTaskPiece = currentTaskInstance.getLeftExcuteTime();
            //任务的当前状态
            State currentState = currentTaskInstance.getCurrentState();

            //状态剩余几个时间片
            float leftStatePiece = currentState.getLeftExcuteTime();

            String taskId=currentTaskInstance.getTaskId();
            Task task=taskMap.get(taskId);

            String componentId=task.getComponentId();
            Component component=componentMap.get(componentId);

            if (!currentTaskInstance.getTaskState().equals("运行")) {
                currentTaskInstance.setTaskState("运行");
            }

            if(leftStatePiece==0){
                StateOperate.stateTransition(currentState,component,currentTaskInstance,task,blockQueue,taskInstanceMap);
            }

            if (leftStatePiece == currentState.getWcet()) {
                String entryEvent = currentState.getEntryEvent();
                if (entryEvent != null) {
                    // TODO: 做状态内的数据更新--状态的记录
                }
            }

            if (leftStatePiece == 2) {
                String doEvent = currentState.getDoEvent();
                if (doEvent != null) {
                    // TODO: 做状态内的数据更新--状态的记录
                }
            }

            if (leftStatePiece == 1) {
                String exitEvent = currentState.getExitEvent();
                if (exitEvent != null) {
                    // TODO: 做状态内的数据更新--状态的记录
                }
                faultInjection(currentTaskInstance,component,currentState);

                StateOperate.stateTransition(currentState,component,currentTaskInstance,task,blockQueue,taskInstanceMap);
            }
            currentState.setLeftExcuteTime(leftStatePiece - 1);
            currentTaskInstance.setLeftExcuteTime(leftTaskPiece - 1);
        }
    }

    @Override
    public void faultInjection(TaskInstance taskInstance,Component component,State lastState) {
        Map<String,Fault> faultSet= getFaultInjectMap();
        Fault fault= faultSet.get(lastState.getId());
        ComponentManage componentManage=new ComponentManage();
        TaskInstanceManage taskInstanceManage=new TaskInstanceManage();

        if (fault != null) {
            String conditionType = fault.getConditionType();
            String operateorMethod = fault.getOperateorMethod();
            //根据注入条件类型的不同来分别检查
            if (conditionType.equals("relatedData")) {
                String condition = fault.getCondition();
                String relatedDataName = condition.split(";")[0];
                //判定当前这个环境数据值是否满足注入条件的范围
                boolean isInRange = componentManage.get(component,relatedDataName).isInRange(condition);
                if (isInRange) {
                    List<Data> dataList = fault.getDataName_type_value();
                    componentManage.updateData(component,operateorMethod,dataList);
                }
            }

            if (conditionType.equals("transitionPath")) {
                String condition = fault.getCondition();
                String path = taskInstanceManage.getTransitionPath(taskInstance);
                boolean istransition = taskInstanceManage.isTransition(taskInstance, condition);
                if (istransition) {
                    List<Data> dataList = fault.getDataName_type_value();
                    componentManage.updateData(component,operateorMethod,dataList);
                }
            }
        }
    }
}