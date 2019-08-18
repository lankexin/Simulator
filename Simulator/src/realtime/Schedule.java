package realtime;

import com.oracle.tools.packager.mac.MacAppBundler;
import lmf.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 根据输入的调度算法名，调用相应的调度算法
 * 返回第一个执行的任务id
 */

public class Schedule {

    public static Map<Integer, String> schedule(String scheduleAlgorithm,
                                                int currentTimePiece,
                                                List<Task> waitingTaskList,
                                                Map<String, Task> taskMap) {
        switch (scheduleAlgorithm) {
            case "EDF":
                return EDF.EDFSchedule(currentTimePiece, waitingTaskList, Map<String, Task> taskMap);
            case "RMS":
                return RMS.RMSSchedule(currentTimePiece, waitingTaskList, Map<String, Task> taskMap);
            case "LLF":
                return LLF.LLFSchedule(currentTimePiece, waitingTaskList, Map<String, Task> taskMap);
            default:
                return null;
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
