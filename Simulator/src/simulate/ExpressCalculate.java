package simulate;


import static util.LogicCaculator.eventProcess;
import static util.LogicCaculator.stateUpdateCaculate;

public class ExpressCalculate {
    public static String getResultData(String express) {
        if (express == null || express.isEmpty())
            return "1";
        String value = null;
        if (express.contains("!") || express.contains("&") || express.contains("|") || express.contains(">")
                || express.contains("<") || express.contains("=")) {
            if (eventProcess(express))
                value = "1";
            else {
                value = "0";
            }
        } else {
//            NumericCaculator calculate = new NumericCaculator();
//            System.out.println("express------"+express);
//            value = calculate.getEventuate(express).split("=")[1];
            value =stateUpdateCaculate(express);
        }
        return value;
    }

}
