package lmf;

import java.util.List;
import java.util.Map;

public class Task {
    private String componentId;//任务所属的组件的id
    private String id;//递增
    private float wcet;
    private float period;
    private float deadline;

    /**
     * 该组件下所有的transition
     * key: source state id
     * value：对应的transition
     *
     * 一个源state对应多个transition
     */
    private Map<String, List<Transition>> transitionMap;

    /**
     * 该组件下所有的state
     * key: state id
     */
    private Map<String, State> stateMap;

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

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setWcet(float wcet) {
        this.wcet = wcet;
    }

    public float getPeriod() {
        return period;
    }

    public void setPeriod(float period) {
        this.period = period;
    }

    public float getDeadline() {
        return deadline;
    }

    public void setDeadline(float deadline) {
        this.deadline = deadline;
    }

    public Map<String, List<Transition>> getTransitionMap() {
        return transitionMap;
    }

    public void setTransitionMap(Map<String, List<Transition>> transitionMap) {
        this.transitionMap = transitionMap;
    }

    public Map<String, State> getStateMap() {
        return stateMap;
    }

    public void setStateMap(Map<String, State> stateMap) {
        this.stateMap = stateMap;
    }

    public List<State> getStateList() {
        return stateList;
    }

    public void setStateList(List<State> stateList) {
        this.stateList = stateList;
    }
}