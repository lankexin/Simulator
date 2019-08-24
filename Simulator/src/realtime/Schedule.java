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
        scheduleAlgorithm = PropertiyParse.readProperty("realtime.schedule.algorithm");
    }

    /**
     *
     * @param currentTimePiece 当前的系统时间，用于记录task的到达时间等
     * @param waitingTaskList 等待队列，即就绪的任务队列
     * @return
     */
    public Map<Integer, String> schedule(int currentTimePiece,
                                                Map<String, TaskInstance> waitingTaskList) {
        switch (scheduleAlgorithm) {
            case "EDF":
                return EDF.EDFSchedule(currentTimePiece, waitingTaskList);
            case "RMS":
                return RMS.RMSSchedule(currentTimePiece, waitingTaskList);
            case "LLF":
                return LLF.LLFSchedule(currentTimePiece, waitingTaskList);
            default:
                return null;
        }
    }
}
