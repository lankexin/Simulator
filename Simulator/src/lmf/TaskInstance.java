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

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    public float getLeftExcuteTime() {
        return leftExcuteTime;
    }

    public void setLeftExcuteTime(float leftExcuteTime) {
        this.leftExcuteTime = leftExcuteTime;
    }

    public int getArriveTimestamp() {
        return arriveTimestamp;
    }

    public void setArriveTimestamp(int arriveTimestamp) {
        this.arriveTimestamp = arriveTimestamp;
    }

    public int getExecuteTimestamp() {
        return executeTimestamp;
    }

    public void setExecuteTimestamp(int executeTimestamp) {
        this.executeTimestamp = executeTimestamp;
    }

    public String getTaskState() {
        return taskState;
    }

    public void setTaskState(String taskState) {
        this.taskState = taskState;
    }
}
