package manager;

import lmf.TaskInstance;
import safety.FaultInjection;

import java.util.List;

public abstract class TaskInstanceManage implements FaultInjection {
    public String getTransitionPath(TaskInstance taskInstance){
        String path = null;
        List<String> statePath=taskInstance.getStatePath();
        if(statePath!=null) {
            StringBuilder sb = new StringBuilder();
            for (String transition : statePath) {
                String[] strs = transition.split("-");
                String state = strs[0];
                sb.append(state + "->");
            }
            path = sb.toString();
        }
        return path;
    }

    public boolean isTransition(TaskInstance taskInstance,String condition){
        String path=getTransitionPath(taskInstance);
        boolean isTransition=false;
        if(path.contains(condition))
            isTransition=true;
        return isTransition;
    }

}
