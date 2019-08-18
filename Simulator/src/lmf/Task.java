package lmf;

import java.util.List;

public class Task {
    private String componentId;//任务所属的组件的id
    private String id;//递增

    private float wcet;
    private float period;
    private float deadline;


    private List<State> stateList;

    public String getComponentId() {
        return componentId;
    }

    public float getWcet() {
        return wcet;
    }
}