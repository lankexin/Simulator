package realtime;

import lmf.Task;
import lmf.TaskInstance;
import util.PropertiyParse;

import java.util.Map;

/**
 * 根据输入的调度算法名，调用相应的调度算法
 * 返回第一个执行的任务id
 */

public class Schedule {

    private String scheduleAlgorithm = null;

    public Schedule() {
        scheduleAlgorithm = PropertiyParse.readProperty("schedule algorithm");
    }

    /**
     *
     * @param currentTimeStamp 当前的系统时间，用于记录task的到达时间等
     * @param waitingTaskList 等待队列，即就绪的任务队列
     * @param taskMap 用于提取任务的状态列表等不变的信息
     * @return
     */
    public Map<Integer, String> schedule(int currentTimeStamp,
                                                Map<String, TaskInstance> waitingTaskList,
                                                Map<String, Task> taskMap) {
        switch (scheduleAlgorithm) {
            case "EDF":
                return EDF.EDFSchedule(currentTimeStamp, waitingTaskList, taskMap);
            case "RMS":
                return RMS.RMSSchedule(currentTimeStamp, waitingTaskList, taskMap);
            case "LLF":
                return LLF.LLFSchedule(currentTimeStamp, waitingTaskList, taskMap);
            default:
                return null;
        }
    }
}
