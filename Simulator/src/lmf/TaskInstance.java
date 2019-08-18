package lmf;

public class TaskInstance {

    private String instanceId;
    private String taskId;

    private State currentState;
    private float leftExcuteTime;

    private int arriveTimestamp;
    private int executeTimestamp;

    /**
     * 任务当前的状态：就绪、等待、运行
     */
    private String taskState;

    public TaskInstance(String instanceId, String taskId) {
        this.instanceId = instanceId;
        this.taskId = taskId;
    }

}
