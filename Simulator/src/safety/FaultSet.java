package safety;

import com.google.common.base.Splitter;
import lmf.Data;
import lmf.Fault;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static util.PropertiyParse.readProperty;

public class FaultSet {
    /** 获取故障注入的列表--* key-lastState的name * value Fault实体 */
    public static Map<String, Fault> getFaultInjectMap() {
        Integer faultSize = parseInt(readProperty("faultInjection.size"));
//        System.out.println(faultSize);
        Map<String, Fault> faultInjectMap = new LinkedHashMap<>();
        for (int i = 1; i <= faultSize; i++) {
//            System.out.println(i);
            String random = readProperty("faultInjection.randomProbability-" + i);
//            System.out.println(random);

            /**只有生成了指定概率范围的事件，才会想故障注入列表添加数据*/
            if (getRandom(random)) {
                String context = readProperty("faultInjection.context-" + i);
                List<String> stateList = Splitter.on(",").splitToList(context);
//                System.out.println(stateList);
                String faultMode=readProperty("faultInjection.mode-"+i);
                String faultType=readProperty("faultInjection.type-"+i);
                String conditionType = readProperty("faultInjection.conditionType-" + i);
                String condition = "";
                if ("_relatedData".equals(conditionType)) {
                    condition = readProperty("faultInjection.relatedDataRange-" + i);
                    System.out.println(condition);
                } else if ("_transitionPath".equals(conditionType)) {
                    condition = readProperty("faultInjection.transitionPath-" + i);
                }
                String operatorMethod = readProperty("faultInjection.operateorMethod-" + i);
                String dataSize = readProperty("faultInjection.dataSize-" + i);
                Integer size = parseInt(dataSize);
                List<String> dataList = new ArrayList<>();

                for (int j = 1; j <= size; j++) {
                    String data = readProperty("faultInjection.data-" + i + "-" + j);
                    System.out.println(data);
                    dataList.add(data);
                    System.out.println(dataList);
                }

                for (String state : stateList) {
                    Fault fault = new Fault(state, faultMode,faultType,conditionType, condition, operatorMethod,
                            dataSize, dataList);
                    faultInjectMap.put(state, fault);
                    System.out.println(fault.getConditionType() + fault.getDataSize()
                            + fault.getLastState() + fault.getCondition());
                }
            }

        }
        System.out.println(faultInjectMap);
        return faultInjectMap;
    }

    /**根据概率产生随机事件，是否注入故障*/
    public static boolean getRandom(String randomProbability) {
        Double random = parseDouble(randomProbability);
        return Math.random() < random;
    }

    public static void main(String[] args) {
        FaultSet faultSet = new FaultSet();
        faultSet.getFaultInjectMap();
    }
}
