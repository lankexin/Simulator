package lmf;

import java.util.List;

public class Task {
    private String componentId;//任务所属的组件的id
    private String id;//递增

    private float wcet;
    private float period;
    private float deadline;
    private float leftExcuteTime;

    /**
     * 任务当前的状态：就绪、等待、运行
     */
    private String taskState;

    private int arriveTimestamp;
    private int executeTimestamp;

    private List<State> stateList;
    private String currentStateId;

    public String getComponentId() {
        return componentId;
    }

    public String getCurrentStateId() {
        return currentStateId;
    }

    public void setCurrentStateId(String currentStateId) {
        this.currentStateId = currentStateId;
    }

    public void setArriveTimestamp(int arriveTimestamp) {
        this.arriveTimestamp = arriveTimestamp;
    }

    public void setExecuteTimestamp(int executeTimestamp) {
        this.executeTimestamp = executeTimestamp;
    }

    public int getArriveTimestamp() {
        return arriveTimestamp;
    }

    public int getExecuteTimestamp() {
        return executeTimestamp;
    }

    public float getWcet() {
        return wcet;
    }

    public void setLeftExcuteTime(float leftExcuteTime) {
        this.leftExcuteTime = leftExcuteTime;
    }

    public float getLeftExcuteTime(){return leftExcuteTime;}

    public String getTaskState() {
        return taskState;
    }

    public void setTaskState(String taskState) {
        this.taskState = taskState;
    }
}