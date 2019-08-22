package lmf;

public class TaskInstance {

    private String instanceId;
    private String taskId;

    private State currentState;
    private float stateLeftExcuteTime;

    private int arriveTimestamp;
    private int executeTimestamp;

    /**
     * 任务当前的状态：就绪、等待、运行
     */
    private String taskStateId;

    /**运行中各组件运行的过程记录
     * value: stateId
     */
    private String statePath;

    public TaskInstance(int arriveTimestamp, String instanceId, String taskId,
                        State state, String stateName, float leftExcuteTime) {
        this.instanceId = instanceId;
        this.taskId = taskId;
        this.currentState = state;
        taskStateId = "就绪";
        statePath = stateName;
        this.stateLeftExcuteTime = leftExcuteTime;
        this.arriveTimestamp = arriveTimestamp;
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

    public float getStateLeftExcuteTime() {
        return stateLeftExcuteTime;
    }

    public String getTaskState() {
        return taskStateId;
    }

    public void setTaskState(String taskStateId) {
        this.taskStateId = taskStateId;
    }

    public String getStatePath() {
        return statePath;
    }

}
