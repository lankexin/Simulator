package safety;

import lmf.Component;
import lmf.Data;
import lmf.TaskInstance;

import java.util.List;

public interface FaultInjectUpdate {
    /**根据配置文件中指定的方式更新数据*/
    void updateData(TaskInstance taskInstance,Component component, List<String> dataList);
}
