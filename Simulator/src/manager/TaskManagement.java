package manager;

import lmf.Component;
import lmf.Task;
import lmf.TaskInstance;
import lmf.Transition;
import realtime.Schedule;
import util.ParseStr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskManagement {

    private Schedule mSchedule;

    public TaskManagement() {
        mSchedule = new Schedule();
    }

    public Map<Integer, TaskInstance> timePieceMapManagement(int currentSystemTime,
                                                             Map<String, Task> taskMap,
                                                             Map<String, TaskInstance> waitingTaskList,
                                                             List<TaskInstance> blockTaskList,
                                                             Map<String, Component> componentMap) {
        Map<Integer, TaskInstance> timePieceMap = new HashMap<>();

        Map<Integer, TaskInstance> tempTimePieceMap;
        tempTimePieceMap = waitingQueueManagement(currentSystemTime, taskMap, waitingTaskList, componentMap);
        if (tempTimePieceMap.size() > 0) {
            timePieceMap = tempTimePieceMap;
        }
        tempTimePieceMap = blockQueueManageMent(currentSystemTime, blockTaskList, taskMap, componentMap, waitingTaskList);
        if (tempTimePieceMap.size() > 0) {
            timePieceMap = tempTimePieceMap;
        }

        return timePieceMap;
    }


    private Map<Integer, TaskInstance> waitingQueueManagement(int currentSystemTime,
                                                              Map<String, Task> taskMap,
                                                              Map<String, TaskInstance> waitingTaskList,
                                                              Map<String, Component> componentMap) {
        Map<Integer, TaskInstance> timePieceMap = new HashMap<>();

        for (String taskKey : taskMap.keySet()) {
            // todo：遍历task map，看是否有task满足触发条件，满足则生成对应的task instance，并加入等待队列中。

            Task currentTask = taskMap.get(taskKey);

            //获取当前状态的所有迁移列表
            List<Transition> transitions = currentTask.getTransitionMap().get(currentTask.getFirstStateId());

            /** 故障注入
             if (faultMap.get(currentStateId) != null) {
             //更改数据
             FaultInjection.inject();
             } */

            if (currentSystemTime % currentTask.getPeriod() == 0) {
                TaskInstance newTaskInstance = new TaskInstance(currentTask.getId()+"_"+currentSystemTime,
                        currentTask.getId(), currentTask.getFirstStateId());
                waitingTaskList.put(newTaskInstance.getInstanceId(), newTaskInstance);
                timePieceMap = mSchedule.schedule(currentSystemTime, waitingTaskList, taskMap);
            }
            else {

                Component targetComponent = componentMap
                        .get(currentTask.getComponentId());
                TaskInstance newTaskInstance = isTransitted(currentSystemTime, transitions, targetComponent, currentTask);

                if (newTaskInstance != null) {
                    waitingTaskList.put(newTaskInstance.getInstanceId(), newTaskInstance);
                    timePieceMap = mSchedule.schedule(currentSystemTime, waitingTaskList, taskMap);
                }
            }
        }

        return timePieceMap;

    }


    /**
     * 遍历阻塞队列，看是否满足触发条件
     */
    private Map<Integer, TaskInstance> blockQueueManageMent(int currentSystemTime,
                                                           List<TaskInstance> blockTaskList,
                                                           Map<String, Task> taskMap,
                                                           Map<String, Component> componentMap,
                                                           Map<String, TaskInstance> waitingTaskList) {

        Map<Integer, TaskInstance> timePieceMap = new HashMap<>();

        for (TaskInstance taskInstance : blockTaskList) {
            List<Transition> transitions = taskMap.get(taskInstance
                    .getTaskId())
                    .getTransitionMap()
                    .get(taskInstance
                            .getCurrentState()
                            .getId());

            Component targetComponent = componentMap.get(taskMap
                            .get(taskInstance
                                    .getTaskId())
                            .getComponentId());
            TaskInstance newTaskInstance = isTransitted(currentSystemTime, transitions,
                    targetComponent, taskMap.get(taskInstance.getTaskId()));
            waitingTaskList.put(newTaskInstance.getInstanceId(), newTaskInstance);
            timePieceMap = mSchedule.schedule(currentSystemTime, waitingTaskList, taskMap);
        }

        return timePieceMap;
    }

    private TaskInstance isTransitted(int currentSystemTime,
                                      List<Transition> transitions,
                                      Component targetComponent,
                                      Task currentTask) {
        TaskInstance newTaskInstance = null;
        for (Transition transition : transitions) {
            // todo：调用parseStr方法的流程
            if (ParseStr.parseStr(transition.getEvent(), targetComponent)) {
                newTaskInstance = new TaskInstance(currentTask.getId() + "_" + currentSystemTime,
                        currentTask.getId(), currentTask.getFirstStateId());
                break;
            }
        }

        return newTaskInstance;
    }
}
