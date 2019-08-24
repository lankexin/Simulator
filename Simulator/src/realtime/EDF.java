package realtime;

import lmf.Task;
import lmf.TaskInstance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static simulate.Simulator.timePiece;

/**
 * 截止时间越短，优先级越高
 */

public class EDF {

    public static Map<Integer, String> EDFSchedule(int currentTimePiece,
                                                         Map<String, TaskInstance> waitingTaskList) {
        Map<Integer, String> timePieceMap = new HashMap<>();

        List<TaskInstance> taskInstanceList = new ArrayList<>();

        System.out.println("waiting list size " + waitingTaskList.size());
        for (String key : waitingTaskList.keySet()) {

            TaskInstance curTaskInstance = waitingTaskList.get(key);
            int insertIndex = 0;
            for (TaskInstance taskInstance : taskInstanceList) {
                if (taskInstance.getDeadline() > curTaskInstance.getDeadline()) {
                    break;
                }
                insertIndex++;
            }
            if (insertIndex == taskInstanceList.size()) taskInstanceList.add(curTaskInstance);
            else {
                taskInstanceList.add(insertIndex, curTaskInstance);
            }
        }

        int timePieceCounter = currentTimePiece;
//        System.out.println("start  " + taskInstanceList.size());
        for (TaskInstance taskInstance : taskInstanceList) {
//            System.out.println(taskInstance.getInstanceId() + " " +
//                    taskInstance.getLeftExcuteTime() + " " + timePieceCounter);
            if (taskInstance.getLeftExcuteTime() > 0) {
                for (int i = timePieceCounter; i < timePieceCounter + taskInstance.getLeftExcuteTime() / timePiece; i++) {
                    timePieceMap.put(i, taskInstance.getInstanceId());
                }
                timePieceCounter += taskInstance.getLeftExcuteTime()/timePiece;
            }
        }

        return timePieceMap;
    }
}
