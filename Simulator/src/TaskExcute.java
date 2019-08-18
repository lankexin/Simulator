import common.DataStore;
import common.Log;
import lmf.State;
import lmf.Task;
import lmf.TaskInstance;
import lmf.Transition;
import util.EventProcess;
import java.util.List;
import java.util.Map;

public class TaskExcute implements DataStore, Log {

    //timePieceMap  时间片--任务Id
    public static void taskExcute(int currentTimePiece,Map<String, TaskInstance> taskInstanceMap, List<String> taskQueue,
                                  Map<Integer,String> timePieceMap, Map<String,Task> taskMap) {

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
                List<Transition> transitions = taskMap.get(taskId).getTransitionMap().get(currentState.getId());

                boolean isTransition = false;
                for (Transition transition : transitions) {
                    if (EventProcess.eventProcess(transition.getEvent, dataMap)) {
                        currentTaskInstance.setCurrentState(newState);
                        isTransition = true;
                        if(newState.getName.trim().toLowerCase().equals("idle")){
                            taskQueue.remove(currentTaskInstance);
                        }
                    }
                }
                if (! isTransition)
                    taskQueue.remove(taskId);
            }

            currentState.setLeftExcuteTime(leftStatePiece - 1);
            currentTaskInstance.setLeftExcuteTime(leftTaskPiece - 1);
        }

    }
}
