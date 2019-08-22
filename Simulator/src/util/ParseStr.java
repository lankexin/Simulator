package util;

import lmf.Component;
import manager.ComponentManage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**新的表达式--把变量值改为对应的数据*/
public class ParseStr {
    public static String parseStr(String s, Component component) {
        s = s.replaceAll(" ", "");

        List<String> operators = new ArrayList<>();
        int length = s.length();
        operators.add("+");
        operators.add("-");
        operators.add("*");
        operators.add("/");

        operators.add("!");
        operators.add("|");
        operators.add("&");

        operators.add("(");
        operators.add(")");

        operators.add("==");
        operators.add("!=");

        operators.add(">");
        operators.add(">=");

        operators.add("<");
        operators.add("<=");

        operators.add("=");
        operators.add(".");

        StringBuilder newStr = new StringBuilder();
        StringBuilder name = new StringBuilder();

        int i = 0;

        for (; i < length; i++) {
            String i_1Str = String.valueOf(s.charAt(i - 1));
            String iStr = String.valueOf(s.charAt(i));
            boolean isDigit=s.charAt(i) >= '0' && s.charAt(i) <= '9';
            if (i == 0) {
                if (!operators.contains(iStr) && !isDigit) {
                    name.append(s.charAt(i));
                } else {
                    newStr.append(s.charAt(i));
                }
            } else {
                if (operators.contains(i_1Str) && !operators.contains(iStr) && !isDigit) {
                    name.append(s.charAt(i));
                }

                if (operators.contains(i_1Str) && isDigit) {
                    newStr.append(s.charAt(i));
                }
//                if(operators.contains(i_1Str) && operators.contains(iStr)){
//                    newStr.append(s.charAt(i));
//                }

                if (!operators.contains(i_1Str) && !operators.contains(iStr)) {
                    name.append(s.charAt(i));
                }

                if (operators.contains(iStr)) {
                    if (name.length() != 0) {
                        if (s.charAt(i) == '=' && s.charAt(i + 1) != '=') {
                            newStr.append(name);
                            name = new StringBuilder();
                        } else {
                            ComponentManage componentManage = new ComponentManage();
                            String nameString = name.toString();
                            String value = componentManage.get(component, nameString);
                            if(value==null){
                                System.out.println("解析式变量名不存在");
                                return null;
                            }
                            name = new StringBuilder();
                            newStr.append(value);
                        }
                    }
                    newStr.append(s.charAt(i));
                }
            }

        }

        if (name.length() != 0) {
            ComponentManage componentManage = new ComponentManage();
            String nameString = name.toString();
            String value = componentManage.get(component, nameString);
            if(value==null){
                System.out.println("解析式变量名不存在");
                return null;
            }
            name = new StringBuilder();
            newStr.append(value);
        }

        return newStr.toString();
    }

    /**判断类型--参数是解析后的表达式*/
    public static String getExpressionType(String expression){
        int index=expression.indexOf("=");
        if(expression.charAt(index+1)=='=')
            return "boolType";
        else
            return "assignmentType";
    }

    /**得到赋值的数据名称，和表达式*/
    public static Map<String,String> getAssignedData(String expresstion){
        Map<String,String> map=new HashMap<>();
        int index=expresstion.indexOf("=");
        String dataName=expresstion.substring(0,index);
        String operation=expresstion.substring(index+1);
        map.put(dataName,operation);
        return map;
    }


    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        map.put("aaa", "0");
        map.put("sss", "3");
        map.put("bbb", "3.33");
        String s = "aaa=sss+bbb-7*(9.1+1)";
//        System.out.println(getStr(s,map));
    }
}