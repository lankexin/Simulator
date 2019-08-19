package manager;

import lmf.TaskInstance;
import safety.FaultInjectTransition;

import java.util.List;

public abstract class TaskInstanceManage implements FaultInjectTransition {

    @Override
    public String getTransitionPath(TaskInstance taskInstance){
        String path = null;
        List<String> statePath=taskInstance.getStatePath();
        if(statePath!=null) {
            StringBuilder sb = new StringBuilder();
            for (String state : statePath) {
                sb.append(state + "->");
            }
            path = sb.toString();
        }
        return path;
    }

    @Override
    public boolean isTransition(TaskInstance taskInstance,String condition){
        String path=getTransitionPath(taskInstance);
        boolean isTransition=false;
        if(path.contains(condition))
            isTransition=true;
        return isTransition;
    }

}
