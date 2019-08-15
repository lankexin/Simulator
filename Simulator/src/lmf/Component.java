package lmf;

import java.util.List;
import java.util.Map;

public class Component {

    private String id;
    private String name;

    /** 组件当前所处状态 */
    private List<State> currentState;

    /**
     * 该组件下所有的transition
     * key: source state id
     * value：对应的transition
     */
    private Map<String, Transition> transitionMap;

    /**
     * 该组件下所有的state
     * key: state id
     */
    private Map<String, State> stateMap;

    /**
     * 该组件下所有的task
     * key: task id
     */
    private Map<String, Task> taskMap;

    /**
     * 该组件下的所有data
     * key: data name
     */
    private Map<String, Data> dataMap;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<State> getCurrentState() {
        return currentState;
    }

    public void setCurrentState(List<State> currentState) {
        this.currentState = currentState;
    }

    public Map<String, Transition> getTransitionMap() {
        return transitionMap;
    }

    public void setTransitionMap(Map<String, Transition> transitionMap) {
        this.transitionMap = transitionMap;
    }

    public Map<String, State> getStateMap() {
        return stateMap;
    }

    public void setStateMap(Map<String, State> stateMap) {
        this.stateMap = stateMap;
    }

    public Map<String, Task> getTaskMap() {
        return taskMap;
    }

    public void setTaskMap(Map<String, Task> taskMap) {
        this.taskMap = taskMap;
    }

    public Map<String, Data> getDataMap() {
        return dataMap;
    }

    public void setDataMap(Map<String, Data> dataMap) {
        this.dataMap = dataMap;
    }
}
