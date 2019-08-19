package safety;

import lmf.Data;

import java.util.List;
import java.util.Map;

public interface FaultInjectUpdate {
    /**根据配置文件中指定的方式更新数据*/
    void updateData(String operateorMethod, List<Data> dataList);
}
