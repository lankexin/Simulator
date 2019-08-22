package simulate;

import util.NumericCaculator;
import static util.LogicCaculator.eventProcess;

public class ExpressCalculate {
    public static String getResultData(String express) {
        String value = null;
        if (express.contains("!") || express.contains("&") || express.contains("|") || express.contains(">")
                || express.contains("<") || express.contains("=")) {
            if (eventProcess(express))
                value = "1";
            else{
                value="0";
            }
        } else {
            NumericCaculator calculate = new NumericCaculator();
            value = calculate.getEventuate(express).split("=")[1];
        }
        return value;
    }
}
