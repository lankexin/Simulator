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
}
