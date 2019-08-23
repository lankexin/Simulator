package lmf;

import java.util.HashMap;
import java.util.Map;

public class State {
    private String id;
    private String name;
    //private String component;//state所属的组件

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
        if (attrs.get("id") != null) {
            id = attrs.get("id");
        } else {
            id = "";
        }
        return id;
    }

    public String getName() {
        if (attrs.get("name") != null) {
            name = attrs.get("name");
        } else {
            name = "";
        }
        return name;
    }

//    public String getComponent() {
//        return component;
//    }
//
//    public void setComponent(String component) {
//        this.component = component;
//    }

    public String getEntryEvent() {
        return entryEvent;
    }

    public String getDoEvent() {
        return doEvent;
    }

    public String getExitEvent() {
        return exitEvent;
    }

    public float getWcet() {
        if (attrs.get("wcet") != null) {
            wcet = Float.valueOf(attrs.get("wcet").substring(0, attrs.get("wcet").length()-2));
            return wcet;
        }
        return -1;
    }

    public float getPeriod() {
        if (attrs.get("period") != null) {
            wcet = Float.valueOf(attrs.get("period").substring(0, attrs.get("period").length()-2));
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

    public boolean isFaultState() {
        if (attrs.get("faultState") != null) {
            isFaultState = true;
        } else {
            isFaultState = false;
        }

        isFaultState = (attrs.get("faultState") != null);
        return isFaultState;
    }
}
