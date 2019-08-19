package safety;

import lmf.Component;
import lmf.Data;

import java.util.List;
import java.util.Map;

public interface FaultInjectUpdate {
    /**根据配置文件中指定的方式更新数据*/
    void updateData(Component component, String operateorMethod, List<Data> dataList);
}
