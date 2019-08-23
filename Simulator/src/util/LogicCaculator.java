package util;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class LogicCaculator {

    public static boolean eventProcess(String event){
        event = event.replaceAll(" ", "");

        if (event.equals("")) return true;

        if (event.contains("null")) {
            if (event.equalsIgnoreCase("null==null")) {
                return true;
            }

            if (event.equalsIgnoreCase("null~=null")) {
                return false;
            }

            if (event.equalsIgnoreCase("!(null==null)")) {
                return false;
            }

            if (event.equalsIgnoreCase("!(null~=null)")) {
                return true;
            }

            if (event.contains("==null") && event.contains("!")) {
                return true;
            }

            if (event.contains("==null")) {
                return false;
            }

            if (event.contains("~=null") && event.contains("!")) {
                return false;
            }

            if (event.contains("~=null")) {
                return true;
            }

            return false;
        }

        Map<String, Integer> operatorMap = new HashMap<>();
        operatorMap.put("|", 1);

        operatorMap.put("&", 2);

        operatorMap.put("~=", 3);
        operatorMap.put("==", 3);

        operatorMap.put(">=", 4);
        operatorMap.put("<=", 4);
        operatorMap.put(">", 4);
        operatorMap.put("<", 4);

        operatorMap.put("+", 5);
        operatorMap.put("-", 5);

        operatorMap.put("*", 6);
        operatorMap.put("/", 6);

        operatorMap.put("!", 7);

        operatorMap.put("(", 8);
        operatorMap.put(")", 8);

        Stack<Double> numbers = new Stack<>();
        Stack<String> operators = new Stack<>();

        //int pos = 0;

        for (int i = 0; i < event.length(); i++) {
            System.out.println(event.charAt(i));
            if (event.charAt(i) == '(') operators.push("(");
            else if (event.charAt(i) == '!' && event.charAt(i+1) == '=') {
                i++;
                if (operators.empty() || operators.peek().equals("(")
                        || operatorMap.get("~=") >= operatorMap.get(operators.peek())) {
                    operators.push("~=");
                } else {
                    while (!operators.empty() && !operators.peek().equals("(")
                            && operatorMap.get("~=") < operatorMap.get(operators.peek())) {
                        String sign = operators.pop();
                        Calculate(sign, numbers);
                    }
                    operators.push("~=");
                }
            }
            else if (event.charAt(i) == '=' && event.charAt(i+1) == '=') {
                i++;
                if (operators.empty() || operators.peek().equals("(")
                        || operatorMap.get("~=") >= operatorMap.get(operators.peek())) {
                    operators.push("==");
                } else {
                    while (!operators.empty() && !operators.peek().equals("(")
                            && operatorMap.get("==") < operatorMap.get(operators.peek())) {
                        String sign = operators.pop();
                        Calculate(sign, numbers);
                    }
                    operators.push("==");
                }
            }
            else if (event.charAt(i) == '>' && event.charAt(i+1) == '=') {
                i++;
                if (operators.empty() || operators.peek().equals("(")
                        || operatorMap.get("!=") >= operatorMap.get(operators.peek())) {
                    operators.push(">=");
                } else {
                    while (!operators.empty() && !operators.peek().equals("(")
                            && operatorMap.get(">=") < operatorMap.get(operators.peek())) {
                        String sign = operators.pop();
                        Calculate(sign, numbers);
                    }
                    operators.push(">=");
                }
            }
            else if (event.charAt(i) == '<' && event.charAt(i+1) == '=') {
                i++;
                if (operators.empty() || operators.peek().equals("(")
                        || operatorMap.get("~=") >= operatorMap.get(operators.peek())) {
                    operators.push("<=");
                } else {
                    while (!operators.empty() && !operators.peek().equals("(")
                            && operatorMap.get("<=") < operatorMap.get(operators.peek())) {
                        String sign = operators.pop();
                        Calculate(sign, numbers);
                    }
                    operators.push("<=");
                }
            }
            else if (event.charAt(i) == ')') {
                while (!operators.empty()) {
                    if (operators.peek().equals("(")) {
                        operators.pop();
                        break;
                    }
                    String sign = operators.pop();
                    Calculate(sign, numbers);
                }
            }
            else if (operatorMap.get(String.valueOf(event.charAt(i))) != null) {
                String currentSign = String.valueOf(event.charAt(i));
                if (operators.empty() || operators.peek().equals("(")
                        || operatorMap.get(currentSign) >= operatorMap.get(operators.peek())) {
                    operators.push(currentSign);
                } else {
                    while (!operators.empty() && !operators.peek().equals("(")
                            && operatorMap.get(currentSign) < operatorMap.get(operators.peek())) {
                        String sign = operators.pop();
                        Calculate(sign, numbers);
                    }
                    operators.push(currentSign);
                }
            }
            else {
                double num = 0;
                while (i < event.length() &&
                        (event.charAt(i) <= '9' && event.charAt(i) >= '0')) {
                    //System.out.println(event.charAt(i) + " " + i);
                    num = (num * 10 + event.charAt(i) - '0');
                    i++;
                }
                //System.out.println(pos-1 +" " +  event.charAt(pos-1));
                if (i < event.length() && event.charAt(i) == '.') {
                    int divider = 10;
                    i++;
                    while(i < event.length() &&
                            (event.charAt(i) <= '9' && event.charAt(i) >= '0')) {
                        num = (num + (double)(event.charAt(i)-'0') / divider);
                        i++;
                        divider *= 10;
                    }
                }
                if (i != event.length())
                    i--;
                numbers.push(num);
            }
        }

        while(!operators.empty()) {
            String sign = operators.pop();
            Calculate(sign, numbers);
        }

        return (numbers.peek() == 1);
    }

    private static void Calculate(String sign, Stack<Double> numbers) {
        //System.out.println(sign + " " + numbers.peek());
        if (sign.equals("!")) {
            double r = numbers.pop();
            boolean rBool;
            rBool = (r == 1);
            //System.out.println(sign + " " + rBool);
            rBool = notOperator(rBool);
            numbers.push(rBool ? 1.0 : 0.0);
        } else if (sign.equals("+") || sign.equals("-") ||
                sign.equals("*") || sign.equals("/")) {
            double r = numbers.pop();
            double l = numbers.pop();
            double ans = calculate(l, r, sign);
            //System.out.println(sign + " " + r + " " + l + " " + ans);
            numbers.push(ans);
        } else if (sign.equals(">=") || sign.equals("<=") ||
                sign.equals(">") || sign.equals("<") ||
                sign.equals("==") || sign.equals("!=")) {
            double r = numbers.pop();
            double l = numbers.pop();
            boolean rBool;
            //System.out.println(sign + " " + l + " " + r);
            rBool = logicOperateI(l, r, sign);
            numbers.push(rBool ? 1.0 : 0.0);
        } else if (sign.equals("&") || sign.equals("|")) {
            double r = numbers.pop();
            double l = numbers.pop();
            boolean rBool, lBool;
            rBool = (r == 1);
            lBool = (l == 1);
            //System.out.println(sign + " " + rBool + " " + lBool);
            rBool = logicOperatorII(rBool, lBool, sign);
            numbers.push(rBool ? 1.0 : 0.0);
        }
    }

    private static double calculate(double l, double r, String operator) {
        switch (operator) {
            case "+":
                return l + r;
            case "-":
                return l - r;
            case "*":
                return l * r;
            case "/":
                // todo: 如果r是0, 抛出异常
                return l / r;
            default:
                return 0;
        }
    }

    private static boolean logicOperateI(double l, double r, String operator) {
        switch (operator) {
            case ">":
                return l > r;
            case "<":
                return l < r;
            case ">=":
                return  l >= r;
            case "<=":
                return l <= r;
            case "==":
                return l == r;
            case "~=":
                return l != r;
            default:
                return false;
        }
    }

    private static boolean logicOperatorII(boolean l, boolean r, String operator) {
        switch (operator) {
            case "&":
                return l && r;
            case "|":
                return l || r;
            default:
                return false;
        }
    }

    private static boolean notOperator(boolean r) {
        return !r;
    }


    public static void main(String[] args) {
        System.out.println(eventProcess("1"));
    }

}
