package lmf;

import safety.FaultInjection;

import java.util.List;

public abstract class TaskInstance implements FaultInjection {

    private String instanceId;
    private String taskId;

    private State currentState;
    private float leftExcuteTime;

    private int arriveTimestamp;
    private int executeTimestamp;

    /**
     * 任务当前的状态：就绪、等待、运行
     */
    private String taskStateId;

    /**运行中各组件运行的过程记录
     * value: 状态-event-data-timestamp
     */
    private static List<String> statePath;

    public TaskInstance(String instanceId, String taskId, String stateId) {
        this.instanceId = instanceId;
        this.taskId = taskId;
        taskStateId = stateId;
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
        return taskStateId;
    }

    public void setTaskState(String taskStateId) {
        this.taskStateId = taskStateId;
    }

    public static List<String> getStatePath() {
        return statePath;
    }

    public static void setStatePath(List<String> statePath) {
        TaskInstance.statePath = statePath;
    }

    public String getTransitionPath(){
        String path = null;
        if(statePath!=null) {
            StringBuilder sb = new StringBuilder();
            for (String transition : statePath) {
                String[] strs = transition.split("-");
                String state = strs[0];
                sb.append(state + "->");
            }
            path = sb.toString();
        }
        return path;
    }

    public boolean isTransition(String condition){
        String path=getTransitionPath();
        boolean isTransition=false;
        if(path.contains(condition))
            isTransition=true;
        return isTransition;
    }
}
