package lmf;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Component  {

    private String id;
    private String name;

    private Map<String, String> attrs;
    private Map<String, Linkpoint> linkpointMap;

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

    /**
     * 该组件下的所有data
     * key: data name
     */
    private Map<String, Data> dataMap;

    public Component() {
        attrs = new HashMap<>();
        transitionMap = new HashMap<>();
        stateMap = new HashMap<>();
        dataMap = new HashMap<>();
        linkpointMap = new HashMap<>();
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

    public Map<String, Data> getDataMap() {
        return dataMap;
    }

    public void setDataMap(Map<String, Data> dataMap) {
        this.dataMap = dataMap;
    }

    public Map<String, List<Transition>> getTransitionMap() {
        return transitionMap;
    }

    public Map<String, State> getStateMap() {
        return stateMap;
    }

    public String getAttr(String key) {
        return attrs.get(key);
    }

    public void setAttr(String key, String value) {
        attrs.put(key, value);
    }

    public Map<String, Linkpoint> getLinkpointMap() {
        return linkpointMap;
    }
}
