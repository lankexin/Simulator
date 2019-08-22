package lmf;

import java.util.HashMap;
import java.util.Map;

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

    private Map<String, String> attrs;

    private Map<String, State> subStateMap;

    public State() {
        attrs = new HashMap<>();
        subStateMap = new HashMap<>();
    }

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

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
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
        if (attrs.get("wcet") != null) {
            wcet = Float.valueOf(attrs.get("wcet").substring(0, attrs.get("wcet").length()-2));
            return wcet;
        }
        return -1;
    }

    public void setAttr(String key, String value) {
        attrs.put(key, value);
    }

    public String getAttr(String key) {
        return attrs.get(key);
    }

    public Map<String, State> getSubStateList() {
        return subStateMap;
    }

    public String getFaultType() {
        return faultType;
    }

    public void setFaultType(String faultType) {
        this.faultType = faultType;
    }

    public boolean isFaultState() {
        return isFaultState;
    }

    public void setFaultState(boolean faultState) {
        isFaultState = faultState;
    }
}
