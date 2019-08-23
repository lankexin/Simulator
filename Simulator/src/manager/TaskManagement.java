package manager;

import lmf.*;
import realtime.Schedule;
import simulate.ExpressCalculate;

import java.util.*;

public class TaskManagement {

    private Schedule mSchedule;

    public TaskManagement() {
        mSchedule = new Schedule();
    }

    public Map<Integer, String> timePieceMapManagement(int currentSystemTime,
                                                             Map<String, Task> taskMap,
                                                             Map<String, TaskInstance> waitingTaskList,
                                                             List<TaskInstance> blockTaskList,
                                                             Map<String, Component> componentMap) {
        Map<Integer, String> timePieceMap = new HashMap<>();

        Map<Integer, String> tempTimePieceMap;
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


    private Map<Integer, String> waitingQueueManagement(int currentSystemTime,
                                                              Map<String, Task> taskMap,
                                                              Map<String, TaskInstance> waitingTaskList,
                                                              Map<String, Component> componentMap) {
        Map<Integer, String> timePieceMap = new HashMap<>();

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

            if (currentTask.getPeriod() > 0) {
                if (currentSystemTime % currentTask.getPeriod() == 0) {
                    TaskInstance newTaskInstance = new TaskInstance(currentSystemTime,
                            currentTask.getId() + "_" + currentSystemTime,
                            currentTask.getFirstState(), "Idle",
                            currentTask.getFirstState().getWcet(), currentTask);
                    waitingTaskList.put(newTaskInstance.getInstanceId(), newTaskInstance);
                    timePieceMap = mSchedule.schedule(currentSystemTime, waitingTaskList, taskMap);
                }
            }
            else {

                Component targetComponent = componentMap
                        .get(currentTask.getComponentId());
                TaskInstance newTaskInstance = isTransitted(currentSystemTime, transitions,
                        targetComponent, currentTask, currentTask.getFirstState());

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
    private Map<Integer, String> blockQueueManageMent(int currentSystemTime,
                                                           List<TaskInstance> blockTaskList,
                                                           Map<String, Task> taskMap,
                                                           Map<String, Component> componentMap,
                                                           Map<String, TaskInstance> waitingTaskList) {

        Map<Integer, String> timePieceMap = new HashMap<>();

        Iterator<TaskInstance> iterator = blockTaskList.iterator();
        while (iterator.hasNext()) {
            TaskInstance taskInstance = iterator.next();
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
                    targetComponent, taskMap.get(taskInstance.getTaskId()),
                    taskInstance.getCurrentState());
            if (newTaskInstance != null) {
                waitingTaskList.put(taskInstance.getInstanceId(), taskInstance);
                timePieceMap = mSchedule.schedule(currentSystemTime, waitingTaskList, taskMap);
                iterator.remove();
            }
        }

        return timePieceMap;
    }

    private TaskInstance isTransitted(int currentSystemTime,
                                      List<Transition> transitions,
                                      Component targetComponent,
                                      Task currentTask,
                                      State currentState) {
        TaskInstance newTaskInstance = null;
        for (Transition transition : transitions) {
            if (ExpressCalculate.getResultData(transition.getEvent()).equals("1")) {
                newTaskInstance = new TaskInstance(currentSystemTime,
                        currentTask.getId() + "_" + currentSystemTime,
                        currentState, currentState.getAttr("name"),
                        currentState.getWcet(),
                        currentTask);
                break;
            }
        }

        return newTaskInstance;
    }
}
