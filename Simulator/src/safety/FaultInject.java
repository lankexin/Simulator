package safety;

import lmf.Component;
import lmf.Fault;
import lmf.State;
import lmf.TaskInstance;

import java.util.List;
import java.util.Map;

public interface FaultInject {
    /**验证是否满足故障注入的条件，如果满足，更新触发条件的数据，
     * 提供给之后的迁移过程使用*/
    void faultInjection(TaskInstance taskInstance, Component component, State lastState);

    /**记录哪个注入的故障备注入进去了，哪个没有被注入进去，
     * 即上下文的条件不具备，就是说最初的数据输入不满足会让这个故障输入起效*/
//    List<String> report(String faultState);
}
