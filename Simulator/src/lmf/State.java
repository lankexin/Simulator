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
}
