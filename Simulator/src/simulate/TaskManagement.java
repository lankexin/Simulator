package simulate;

import lmf.Component;
import lmf.Task;
import lmf.TaskInstance;
import lmf.Transition;
import org.omg.CORBA.MARSHAL;
import realtime.Schedule;
import safety.FaultInjection;
import util.EventProcess;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskManagement implements FaultInjection{

    private Schedule mSchedule;

    public TaskManagement() {
        mSchedule =  new Schedule();
    }

    public Map<Integer, TaskInstance> waitingQueueManagement(int currentSystemTime,
                                                             Map<String, Task> taskMap,
                                                             Map<String, TaskInstance> waitingTaskList,
                                                             Component targetComponent) {
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
                TaskInstance newTaskInstance = new TaskInstance(currentTask.getId()+"_"+String.valueOf(currentSystemTime),
                        currentTask.getId(), currentTask.getFirstStateId());
                timePieceMap = mSchedule.schedule(currentSystemTime, newTaskInstance, waitingTaskList, taskMap);
            }
            else {
                for (Transition transition : transitions) {
                    if (EventProcess.eventProcess(transition.getEvent(), dataMap)) {
                        TaskInstance newTaskInstance = new TaskInstance(currentTask.getId() + "_" + String.valueOf(currentSystemTime),
                                currentTask.getId(), currentTask.getFirstStateId());
                        timePieceMap = mSchedule.schedule(currentSystemTime, taskMap.get(task), taskQueue);
                        break;
                    }
                }
            }
        }

        return timePieceMap;
    }

    public void blockQueueManageMent(List<TaskInstance> blockTaskList,
                                     Map<String, Task> taskMap) {
        // todo: 遍历阻塞队列，看是否满足触发条件
    }
}
