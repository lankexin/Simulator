package lmf;

import java.util.Map;

import static simulate.Simulator.timePiece;

public class TaskInstance {

    private String instanceId;
    private String taskId;

    private State currentState;
    private float stateLeftExcuteTime;
    private float leftExcuteTime;

    private int arriveTimestamp;
    private int executeTimestamp;
    private float deadline;
    private float wcet;
    private float period;

    private Map<String,String> dataMap;

    /**
     * 任务当前的状态：就绪、等待、运行
     */
    private String taskStateId;

    /**运行中各组件运行的过程记录
     * value: stateId
     */
    private String statePath;

    public TaskInstance(int arriveTimestamp, String instanceId,
                        State state, String stateName,
                        float leftExcuteTime,
                        Task task,
                        Map<String, String> dataMap) {
        this.instanceId = instanceId;
        this.taskId = task.getId();
        this.currentState = state;
        taskStateId = "就绪";
        statePath = stateName;
        this.stateLeftExcuteTime = leftExcuteTime;
        this.arriveTimestamp = arriveTimestamp;
        this.deadline = (float)arriveTimestamp + task.getDeadline()/timePiece;
        //System.err.println(arriveTimestamp + " " + task.getDeadline() + " " + timePiece + " " + deadline);
        this.wcet = task.getWcet();
        this.period = task.getPeriod();
        this.leftExcuteTime = task.getWcet();
        this.dataMap = dataMap;
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

    public float getDeadline() {
        return deadline;
    }

    public float getWcet() {
        return wcet;
    }

    public float getPeriod() {
        return period;
    }

    public void setStateLeftExcuteTime(float stateLeftExcuteTime) {
        this.stateLeftExcuteTime = stateLeftExcuteTime;
    }

    public Map<String, String> getDataMap() {
        return dataMap;
    }

    public void setDataMap(Map<String, String> dataMap) {
        this.dataMap = dataMap;
    }
}
