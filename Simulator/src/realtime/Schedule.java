package realtime;

import lmf.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * 根据输入的调度算法名，调用相应的调度算法
 * 返回第一个执行的任务id
 */

public class Schedule {

    public static String schedule(String scheduleAlgorithm,
                                Task newTask,
                                List<Task> waitingTaskList) {
        switch (scheduleAlgorithm) {
            case "EDF":
                return EDF.EDFSchedule(newTask, waitingTaskList);
            case "RMS":
                return RMS.RMSSchedule(newTask, waitingTaskList);
            case "LLF":
                return LLF.LLFSchedule(newTask, waitingTaskList);
            default:
                return "no target schedule algorithm";
        }
    }

    /**
     * 根据组件的连接顺序给任务排序
     * @return 返回一个排好序的任务列表
     */
    public static List<Task> staticSchedule() {
        List<Task> taskList = new ArrayList<>();

        return taskList;
    }
}
