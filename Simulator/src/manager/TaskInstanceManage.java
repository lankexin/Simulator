package manager;

import lmf.TaskInstance;
import safety.FaultInjectTransition;

import java.util.List;

public class TaskInstanceManage implements FaultInjectTransition {

    @Override
    public boolean isTransition(TaskInstance taskInstance,String condition){
        String path=taskInstance.getStatePath();
        int lengthPath=path.length();
        int lengthCondition=condition.length();
        boolean isTransition=false;
        //判断当前的路径正好走到配置文件中要求的迁移路径
        if(path.contains(condition) && path.indexOf(condition)==(lengthPath-lengthCondition))
            isTransition=true;
        return isTransition;
    }

}
