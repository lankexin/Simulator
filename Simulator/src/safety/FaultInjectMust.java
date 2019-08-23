package safety;

import lmf.*;

import java.util.List;
import java.util.Map;

public interface FaultInjectMust {
    /**强行注入--不管环境条件是什么*/
    void faultInjectMust(TaskInstance taskInstance, Component component, Fault fault);
}
