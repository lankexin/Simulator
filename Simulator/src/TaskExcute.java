import common.DataStore;
import common.Log;
import lmf.State;
import lmf.Task;
import lmf.Transition;
import util.EventProcess;
import java.util.List;
import java.util.Map;

public class TaskExcute implements DataStore, Log {
<<<<<<< HEAD
    //timePieceMap  时间片--任务Id
    public static void taskExcute(int currentTimePiece,Map<String, Task> taskMap, List<String> taskQueue,
                                  Map<Integer,String> timePieceMap) {
=======

    public static void taskExcute(int currentTimePiece, List<Task> taskQueue ,) {
        boolean hasExecutingTask = false;
>>>>>>> 8f83462d344c1435d3c76c735aeb5f407f8169fb
        /**
         * 在队列里找到当前需要执行的task并使其开始执行
         * 即 更改该g任务的 execute time
         */
        if (timePieceMap!=null && !timePieceMap.isEmpty()) {

            //当前执行的任务id
            String taskId=timePieceMap.get(currentTimePiece);

            Task currentTask = taskMap.get(taskId);

            //任务剩余几个时间片
            float leftTaskPiece = currentTask.getLeftExcuteTime();

<<<<<<< HEAD
            String currentStateId = taskMap.get(taskId).getCurrentStateId();
            State currentState = currentTask.getStateMap().get(currentStateId);
=======
            String currentStateId = taskMap.get(taskKey).getCurrentStateId();
            State currentState = componentMap.get(taskMap.get(taskKey).getComponentId()).getStateMap().get(currentStateId);
>>>>>>> 8f83462d344c1435d3c76c735aeb5f407f8169fb

            //状态剩余几个时间片
            float leftStatePiece = currentState.getLeftExcuteTime();

            if (leftTaskPiece == currentTask.getWcet()) {
                currentTask.setTaskState("运行");
            }
            if (leftStatePiece == currentState.getWcet()) {
                String entry = currentState.getEntryEvent();
                if (entry != null) {
                    // TODO: 做状态内的数据更新--状态的记录

                }
            }
            if (leftStatePiece == 1) {
                String exit = currentState.getExitEvent();
                if (exit != null) {
                    // TODO: 做状态内的数据更新--状态的记录
                }
                //判断是否满足迁移条件，如果满足，把当前状态设置为下一个要迁移的状态
                List<Transition> transitions = currentTask.getTransitionMap().get(currentStateId);

                boolean isTransition = false;
                for (Transition transition : transitions) {
                    if (EventProcess.eventProcess(transition.getEvent, dataMap)) {
                        currentTask.setCurrentStateId(newStateId);
                        isTransition = true;
                    }
                }
                if (! isTransition)
                    taskQueue.remove(taskId);
            }

            currentState.setLeftExcuteTime(leftStatePiece - 1);
            currentTask.setLeftExcuteTime(leftTaskPiece - 1);

            if (leftTaskPiece == 1) {
                taskQueue.remove(taskId);
            }
//            if (currentTime - taskQueue.get(0).getExecuteTimestamp() >= taskQueue.get(0).getWcet()) {
//                // todo: 检查是否会迁移，如果不迁移了，删除第一个任务，并执行第二个任务
//            }
//            // todo: 当找到一个满足迁移条件和wcet的任务，设置hasExcutingTask为true；并在statePath中记录当前的状态
        }

    }
}
}
