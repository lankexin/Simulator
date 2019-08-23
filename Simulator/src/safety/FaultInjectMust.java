package safety;

import lmf.*;

import java.util.List;
import java.util.Map;

public interface FaultInjectMust {
    /**验证是否满足故障注入的条件，如果满足，更新触发条件的数据，
     * 提供给之后的迁移过程使用*/
    void faultInjectMust(TaskInstance taskInstance, Component component, Fault fault);

}
