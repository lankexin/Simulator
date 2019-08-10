package safety;

import common.DataStore;
import lmf.Data;
import lmf.Fault;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 故障注入
 */
public class FaultInjection implements DataStore {
    /**验证是否满足故障注入的条件，如果满足，更新触发条件的数据，
     * 提供给之后的迁移过程使用*/
    public void faultInjection(String taskKey, Fault fault, Map<String, Data> dataMap,
                               Map<String, List<String>> statePath) {
        if (fault != null) {
            String conditionType = fault.getConditionType();
            String operateorMethod = fault.getOperateorMethod();
            //根据注入条件类型的不同来分别检查
            if (conditionType.equals("relatedData")) {
                String condition = fault.getCondition();
                String relatedDataName = condition.split(";")[0];
                //判定当前这个环境数据值是否满足注入条件的范围
                boolean isInRange = dataMap.get(relatedDataName).isInRange(condition);
                if (isInRange) {
                    List<Data> dataList = fault.getDataName_type_value();
                    updateData(operateorMethod,dataMap , dataList);
                }
            }

            if (conditionType.equals("transitionPath")) {
                String condition = fault.getCondition();
                List<String> transitionPath = statePath.get(taskKey);
                String path = getPath(transitionPath);
                boolean istransition = isTransition(path, condition);
                if (istransition) {
                    List<Data> dataList = fault.getDataName_type_value();
                    updateData(operateorMethod,dataMap , dataList);
                }
            }
        }
    }

    /**
     * 得到当前任务的迁移路径
     */
    public String getPath(List<String> transitionPath) {
        String path = null;
        if(transitionPath!=null) {
            StringBuilder sb = new StringBuilder();
            for (String transition : transitionPath) {
                String[] strs = transition.split("-");
                String state = strs[0];
                sb.append(state + "->");
            }
            path = sb.toString();
        }
        return path;
    }

    /**判定是不是配置文件中指定的迁移路径*/
    public boolean isTransition(String path,String condition){
        boolean isTransition=false;
        if(path.contains(condition))
            isTransition=true;
        return isTransition;
    }

    /**根据配置文件中指定的方式更新数据*/
    public void updateData(String operateorMethod, Map<String, Data> dataMap , List<Data> dataList) {
        if(("assignValue").equals(operateorMethod))
            for(Data data:dataList){
                String dataName=data.getName();
                dataMap.put(dataName,data);
            }
        else if(("operateData").equals(operateorMethod))
            for(Data data:dataList) {
                String dataName = data.getName();
                String oldValue=dataMap.get(dataName).getValue();
                String operate = data.getValue();
                String operatorExcute=operate.substring(3);
                String dataType=data.getValueType();
                //对要更改的数据进行计算
                String dataValue=calculate(oldValue,operatorExcute);
                Data newData=new Data(dataName,dataType,dataValue);
                dataMap.put(dataName,newData);
            }
    }

    /**
     * 记录哪个注入的故障备注入进去了，哪个没有被注入进去，
     * 即上下文的条件不具备，就是说最初的数据输入不满足会让这个故障输入起效
     */
    public List<String> report(String faultState) {
        List<String> faultInjectLog = new ArrayList<>();
        faultInjectLog.add(faultState);
        return faultInjectLog;
    }

    public static void main(String[] args) {
        FaultInjection faultInjection = new FaultInjection();
    }
}