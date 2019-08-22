package lmf;

import java.util.List;
import java.util.Map;

public class Task {
    private String componentId;//任务所属的组件的id
    private String id;//递增
    private float wcet;
    private float period;
    private float deadline;

    private String firstStateId;
    private State firstState;
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

    private List<State> stateList;

    public Task(Component component, String taskId) {
        this.componentId = component.getAttr("id");
        this.wcet = Float.valueOf(component.getAttr("wcet"));

        if (component.getAttr("period") != null) {
            this.period = Float.valueOf(component.getAttr("period"));
        } else {
            this.period = -1;
        }

        if (component.getAttr("deadline") != null) {
            this.deadline = Float.valueOf(component.getAttr("deadline"));
        } else {
            this.deadline = -1;
        }

        this.id = taskId;
        this.transitionMap = component.getTransitionMap();
        this.stateMap = component.getStateMap();

        for (String stateKey : component.getStateMap().keySet()) {
            if (component.getStateMap().get(stateKey).getAttr("name").equalsIgnoreCase("idle")) {
                this.firstStateId = stateKey;
                this.firstState = component.getStateMap().get(stateKey);
                break;
            }
        }
    }


    public Task(Component component, State state, String taskId) {
        this.componentId = component.getAttr("id");
        this.wcet = Float.valueOf(state.getAttr("wcet"));

        if (state.getAttr("period") != null) {
            this.period = Float.valueOf(state.getAttr("period"));
        } else {
            this.period = -1;
        }

        if (state.getAttr("deadline") != null) {
            this.deadline = Float.valueOf(state.getAttr("deadline"));
        } else {
            this.deadline = -1;
        }

        this.id = taskId;
        this.transitionMap = component.getTransitionMap();
        this.stateMap = state.getSubStateList();

        for (String stateKey : state.getSubStateList().keySet()) {
            if (state.getSubStateList().get(stateKey).getAttr("name").equalsIgnoreCase("idle")) {
                this.firstStateId = stateKey;
                this.firstState = state.getSubStateList().get(stateKey);
                break;
            }
        }
    }

    public String getComponentId() {
        return componentId;
    }

    public String getFirstStateId() {
        return firstStateId;
    }

    public void setFirstStateId(String firstStateId) {
        this.firstStateId = firstStateId;
    }

    public float getWcet() {
        return wcet;
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

    public State getFirstState() {
        return firstState;
    }
}