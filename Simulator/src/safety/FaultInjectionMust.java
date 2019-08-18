package safety;

import lmf.Data;
import lmf.Fault;

import java.util.List;
import java.util.Map;

public interface FaultInjectionMust {
    /**验证是否满足故障注入的条件，如果满足，更新触发条件的数据，
     * 提供给之后的迁移过程使用*/
    void faultInjection(String taskKey, Fault fault, Map<String, Data> dataMap,
                        Map<String, List<String>> statePath);

    /** 得到当前任务的迁移路径*/
    String getTransitionPath(List<String> transitionPath);

    /**判定是不是配置文件中指定的迁移路径*/
    boolean isTransition(String path,String condition);

    /**根据配置文件中指定的方式更新数据*/
    void updateData(String operateorMethod, Map<String, Data> dataMap , List<Data> dataList);

    /**记录哪个注入的故障备注入进去了，哪个没有被注入进去，
     * 即上下文的条件不具备，就是说最初的数据输入不满足会让这个故障输入起效*/
    List<String> report(String faultState);
}
