package simulate;

import lmf.*;
import safety.FaultInjection;
import util.EventProcess;
import java.util.List;
import java.util.Map;

public abstract class TaskExcute implements FaultInjection {

    //timePieceMap  时间片--任务Id
    public static void taskExcute(int currentTimePiece,Map<String, Component> componentMap,Map<String, TaskInstance> taskInstanceMap,
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

            if (!currentTaskInstance.getTaskState().equals("运行")) {
                currentTaskInstance.setTaskState("运行");
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
                //判断是否满足迁移条件，如果满足，把当前状态设置为下一个要迁移的状态
                List<Transition> transitions = task.getTransitionMap().get(currentState.getId());
                String componentId=currentState.getComponent();

                Map<String, Data> dataMap=componentMap.get(componentId).getDataMap();

                boolean isTransition = false;
                for (Transition transition : transitions) {
                    if (EventProcess.eventProcess(transition.getEvent, dataMap)) {
                        String destId=transition.getDest();
                        State newState=task.getStateMap().get(destId);
                        currentTaskInstance.setCurrentState(newState);
                        isTransition = true;
                        if(newState.getName().trim().toLowerCase().equals("idle")){
                            taskInstanceMap.remove(taskInsaneId);
                        }
                    }
                }
                if (! isTransition)
                    blockQueue.add(currentTaskInstance);
            }

            currentState.setLeftExcuteTime(leftStatePiece - 1);
            currentTaskInstance.setLeftExcuteTime(leftTaskPiece - 1);
        }
    }
}