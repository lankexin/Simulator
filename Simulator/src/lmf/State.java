package lmf;

import java.util.List;

public class State {
    private String id;
    private String name;
    private String component;//state所属的组件

    private String entryEvent;
    private String doEvent;
    private String exitEvent;

    private float wcet;
    private float period;

    private String faultType;
    private boolean isFaultState;
    private boolean isCompositState;//是否是复合状态

    private List<State> subStateList;
    private float leftExcuteTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEntryEvent() {
        return entryEvent;
    }

    public void setEntryEvent(String entryEvent) {
        this.entryEvent = entryEvent;
    }

    public String getDoEvent() {
        return doEvent;
    }

    public void setDoEvent(String doEvent) {
        this.doEvent = doEvent;
    }

    public String getExitEvent() {
        return exitEvent;
    }

    public void setExitEvent(String exitEvent) {
        this.exitEvent = exitEvent;
    }

    public float getWcet() {
        return wcet;
    }

    public float getLeftExcuteTime() {
        return leftExcuteTime;
    }

    public void setLeftExcuteTime(float leftExcuteTime) {
        this.leftExcuteTime = leftExcuteTime;
    }
}
