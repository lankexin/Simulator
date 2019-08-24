package manager;

import lmf.*;
import realtime.Schedule;
import simulate.ExpressCalculate;
import util.ParseStr;

import java.util.*;

import static simulate.Simulator.timePiece;

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
        System.out.println("start management");
        Map<Integer, String> timePieceMap = new HashMap<>();

        waitingQueueManagement(currentSystemTime, taskMap, waitingTaskList, componentMap);
        System.out.println("finish waiting queue management");

        blockQueueManageMent(currentSystemTime, blockTaskList, taskMap, componentMap, waitingTaskList);
        System.out.println("finish block queue management");

        System.err.println("task management " + currentSystemTime + " " + waitingTaskList);
        timePieceMap = mSchedule.schedule(currentSystemTime, waitingTaskList);
        System.err.println("task management " + timePieceMap);
        return timePieceMap;
    }


    private void waitingQueueManagement(int currentSystemTime,
                                                              Map<String, Task> taskMap,
                                                              Map<String, TaskInstance> waitingTaskList,
                                                              Map<String, Component> componentMap) {
        //System.out.println("start waiting queue management");

        System.out.println(taskMap.size());
        for (String taskKey : taskMap.keySet()) {
            // todo：遍历task map，看是否有task满足触发条件，满足则生成对应的task instance，并加入等待队列中。

            Task currentTask = taskMap.get(taskKey);
            Component targetComponent = componentMap
                    .get(currentTask.getComponentId());

            //获取当前状态的所有迁移列表
            List<Transition> transitions = currentTask.getTransitionMap().get(currentTask.getFirstStateId());

            /** 故障注入
             if (faultMap.get(currentStateId) != null) {
             //更改数据
             FaultInjection.inject();
             } */

            if (currentTask.getPeriod() > 0) {
                System.err.println("period task " + targetComponent.getName() + " " + currentSystemTime + " " + currentTask.getPeriod() +
                        " ");
                if ((int)(currentSystemTime % (currentTask.getPeriod()/timePiece)) == 0) {

                    TaskInstance newTaskInstance = isPeriodTransitted(currentSystemTime, transitions,
                            targetComponent, currentTask, currentTask.getFirstState());
                    if (newTaskInstance != null) {
                        waitingTaskList.put(newTaskInstance.getInstanceId(), newTaskInstance);
                    }
                }
            }
            else {
//                System.out.println("start is transitted");
                TaskInstance newTaskInstance = isTransitted(currentSystemTime, transitions,
                        targetComponent, currentTask, currentTask.getFirstState());
//                System.out.println("finish is transitted");

                if (newTaskInstance != null) {
                    waitingTaskList.put(newTaskInstance.getInstanceId(), newTaskInstance);
                }
            }
        }

        System.out.println("finish waiting queue management");

    }


    /**
     * 遍历阻塞队列，看是否满足触发条件
     */
    private void blockQueueManageMent(int currentSystemTime,
                                                           List<TaskInstance> blockTaskList,
                                                           Map<String, Task> taskMap,
                                                           Map<String, Component> componentMap,
                                                           Map<String, TaskInstance> waitingTaskList) {

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
                iterator.remove();
            }
        }

    }

    private TaskInstance isPeriodTransitted(int currentSystemTime,
                                      List<Transition> transitions,
                                      Component targetComponent,
                                      Task currentTask,
                                      State currentState) {
        System.out.println("transition size is " + transitions.size());
        TaskInstance newTaskInstance = null;
        for (Transition transition : transitions) {
            //System.out.println(transition.getSource() + " " + transition.getDest());
//            System.out.println("start getResult data " + transition.getEvent());

            String express = ParseStr.parseStr(null,transition.getEvent(), targetComponent);
            System.out.println(express);
            String result = ExpressCalculate.getResultData(express);

//            System.out.println("finish getResult data " + result);
            if (result.equals("1")) {
                List<String> dataNameList = ParseStr.getDataNameList(transition.getEvent());
                /**
                 * key是data的名字，value是data的值
                 */
                Map<String, String> dataMap = new HashMap<>();

                for (String dataName : dataNameList) {
                    if (targetComponent.getDataMap().get(dataName).isChannel()) {
                        dataMap.put(dataName, targetComponent.getDataMap().get(dataName).getValue());
                        targetComponent.getDataMap().get(dataName).setValue("null");
                    }
                }

                newTaskInstance = new TaskInstance(currentSystemTime,
                            currentTask.getId() + "_" + currentSystemTime,
                            currentTask.getFirstState(), "Idle",
                            currentTask.getFirstState().getWcet(), currentTask, dataMap);
                break;
            }
        }

        System.out.println();

        return newTaskInstance;
    }

    private TaskInstance isTransitted(int currentSystemTime,
                                      List<Transition> transitions,
                                      Component targetComponent,
                                      Task currentTask,
                                      State currentState) {
        //System.out.println("transition size is " + transitions.size());
        TaskInstance newTaskInstance = null;
        for (Transition transition : transitions) {
            //System.out.println(transition.getSource() + " " + transition.getDest());
            System.out.println("start getResult data " + transition.getEvent() + " " + targetComponent.getName());

            String express = ParseStr.parseStr(null,transition.getEvent(), targetComponent);
            System.out.println(express);
            String result = ExpressCalculate.getResultData(express);

            System.out.println("finish getResult data " + result);
            if (result.equals("1")) {
                List<String> dataNameList = ParseStr.getDataNameList(transition.getEvent());
                /**
                 * key是data的名字，value是data的值
                 */
                Map<String, String> dataMap = new HashMap<>();

                for (String dataName : dataNameList) {
                    if (targetComponent.getDataMap().get(dataName).isChannel()) {
                        dataMap.put(dataName, targetComponent.getDataMap().get(dataName).getValue());
                        targetComponent.getDataMap().get(dataName).setValue("null");
                    }
                }

                newTaskInstance = new TaskInstance(currentSystemTime,
                        currentTask.getId() + "_" + currentSystemTime,
                        currentState, currentState.getAttr("name"),
                        currentState.getWcet(),
                        currentTask, dataMap);
                break;
            }
        }

        System.out.println();

        return newTaskInstance;
    }
}
