package manager;

import lmf.TaskInstance;
import safety.FaultInjectTransition;

import java.util.List;

public class TaskInstanceManage implements FaultInjectTransition {

    @Override
    public boolean isTransition(TaskInstance taskInstance,String condition){
        String path=taskInstance.getStatePath();
        boolean isTransition=false;
        if(path.contains(condition))
            isTransition=true;
        return isTransition;
    }

}
