package safety;

import lmf.Data;
import lmf.Fault;
import lmf.TaskInstance;

import java.util.List;
import java.util.Map;

/**故障注入*/
public interface FaultInjectTransition {
//    /** 得到当前任务的迁移路径*/
//    String getTransitionPath(TaskInstance taskInstance);

    /**判定是不是配置文件中指定的迁移路径*/
    boolean isTransition(TaskInstance taskInstance,String condition);
}