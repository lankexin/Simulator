package realtime;

import com.oracle.tools.packager.mac.MacAppBundler;
import lmf.Task;
import lmf.TaskInstance;
import util.PropertiyParse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 根据输入的调度算法名，调用相应的调度算法
 * 返回第一个执行的任务id
 */

public class Schedule {

    private String scheduleAlgorithm = null;

    public Schedule() {
        scheduleAlgorithm = PropertiyParse.read("schedule algorithm");
    }

    /**
     *
     * @param currentTimeStamp 当前的系统时间，用于记录task的到达时间等
     * @param waitingTaskList 等待队列，即就绪的任务队列
     * @param taskMap 用于提取任务的状态列表等不变的信息
     * @return
     */
    public Map<Integer, TaskInstance> schedule(int currentTimeStamp,
                                                TaskInstance newTask,
                                                Map<String, TaskInstance> waitingTaskList,
                                                Map<String, Task> taskMap) {
        switch (scheduleAlgorithm) {
            case "EDF":
                return EDF.EDFSchedule(currentTimeStamp, newTask, waitingTaskList, taskMap);
            case "RMS":
                return RMS.RMSSchedule(currentTimeStamp, newTask, waitingTaskList, taskMap);
            case "LLF":
                return LLF.LLFSchedule(currentTimeStamp, newTask, waitingTaskList, taskMap);
            default:
                return null;
        }
    }


    /**
     * 根据组件的连接顺序给任务排序
     * @return 返回一个排好序的task map
     */
    public Map<String, Task> staticSchedule(Map<String, Task> taskMap) {
        Map<String, Task> taskList = new HashMap<>();

        return taskList;
    }
}
