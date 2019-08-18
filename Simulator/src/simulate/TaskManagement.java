package simulate;

import lmf.Task;
import lmf.Transition;
import realtime.Schedule;
import safety.FaultInjection;
import util.EventProcess;

import java.util.List;
import java.util.Map;

public class TaskManagement {

    public static void taskManagement(Map<String, Task> taskMap, ) {
        for (String taskKey : taskMap.keySet()) {
            // todo：检查task是否在运行中，如果在，检查他是否超过了deadline， 超过就记录

            //得到当前时间片应该执行的任务ID
            String taskId=getCurrentTask();

            //获取当前任务的当前状态Id
            String currentStateId = taskMap.get(taskKey).getCurrentStateId();

            //获取当前状态的所有迁移列表
            List<Transition> transitions = componentMap.get(taskMap.get(taskKey).getComponentId()).getTrantionById(currentStateId);
            //故障注入
            if (faultMap.get(currentStateId) != null) {
                //更改数据
                FaultInjection.inject();
            }

            if (currentSystemTime % taskMap.get(taskKey).getPeriod() == 0) {
                Schedule.schedule(scheduleAlgorithm, taskMap.get(task), taskQueue);
                taskMap.get(taskKey).setCurrentStateId();
            }

            for (Transition transition : transitions) {
                if (EventProcess.eventProcess(transition.getEvent, dataMap)) {
                    Schedule.schedule(scheduleAlgorithm, taskMap.get(task), taskQueue);
                    break;
                }
            }
        }
    }
}
