package realtime;

import lmf.*;
import util.XmlParse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static simulate.Simulator.timePiece;

/**
 * 周期短任务的优先
 */

public class RMS {

    public static Map<Integer, String> RMSSchedule(int currentTimePiece,
                                                         Map<String, TaskInstance> waitingTaskList,
                                                         Map<String, Task> taskMap) {

        Map<Integer, String> timePieceMap = new HashMap<>();

        List<TaskInstance> taskInstanceList = new ArrayList<>();

        for (String key : waitingTaskList.keySet()) {

            TaskInstance curTaskInstance = waitingTaskList.get(key);
            int insertIndex = 0;
            for (TaskInstance taskInstance : taskInstanceList) {
                if (taskInstance.getPeriod() > curTaskInstance.getPeriod()) {
                    break;
                }
                insertIndex++;
            }
            if (insertIndex == taskInstanceList.size()) taskInstanceList.add(curTaskInstance);
            else {
                taskInstanceList.add(insertIndex, curTaskInstance);
            }
        }

        int timePieceCounter = currentTimePiece+1;
        System.out.println("start  " + taskInstanceList.size());
        for (TaskInstance taskInstance : taskInstanceList) {
            System.out.println(taskInstance.getInstanceId() + " " +
                    taskInstance.getLeftExcuteTime() + " " + timePieceCounter);
            if (taskInstance.getLeftExcuteTime() > 0) {
                for (int i = timePieceCounter; i < timePieceCounter + taskInstance.getLeftExcuteTime() / timePiece; i++) {
                    timePieceMap.put(i, taskInstance.getInstanceId());
                }
                timePieceCounter += taskInstance.getWcet()/timePiece;
            }
        }

        return timePieceMap;
    }

    public static void main(String[] args) {
        TaskExtraction mTaskExtraction = new TaskExtraction();

        Map<String, Component> componentMap = new HashMap<>();
        List<Channel> channelList = new ArrayList<>();
        Map<String, Data> sharedDataMap = new HashMap<>();
        XmlParse.parseXML("simulink0822.xml", componentMap, sharedDataMap, channelList);

        Map<String, Task> taskMap;
        taskMap = mTaskExtraction.taskExtraction(componentMap, channelList);

        Map<String, TaskInstance> waitingTaskInstanceMap = new HashMap<>();
        System.out.println(timePiece);

        int counter = 0;
        for (String key : taskMap.keySet()) {
            System.out.println(key + " " + taskMap.get(key).getWcet() + " "
                    + taskMap.get(key).getComponentId());

            TaskInstance newTaskInstance = new TaskInstance(0, String.valueOf(counter),
                    taskMap.get(key).getFirstState(), "test",
                    -1, taskMap.get(key));
            counter++;

            waitingTaskInstanceMap.put(newTaskInstance.getInstanceId(), newTaskInstance);
        }

        System.out.println("waiting list size is " + waitingTaskInstanceMap.size());

        Map<Integer, String> timePieceMap = RMSSchedule(0, waitingTaskInstanceMap, taskMap);

        System.out.println("result");
        for (int key : timePieceMap.keySet()) {
            System.out.println(key + " " + timePieceMap.get(key) + " " +
                    waitingTaskInstanceMap.get(timePieceMap.get(key)).getPeriod());
        }
    }
}
