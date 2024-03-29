package util;

import lmf.Component;
import lmf.TaskInstance;
import manager.ComponentManage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 新的表达式--把变量值改为对应的数据
 */
public class ParseStr {
    public static List<String> operators() {
        List<String> operators = new ArrayList<>();
        operators.add("+");
        operators.add("-");
        operators.add("*");
        operators.add("/");
        operators.add("%");

        operators.add("!");
        operators.add("~");
        operators.add("|");
        operators.add("&");

        operators.add("(");
        operators.add(")");

        operators.add("==");
        operators.add("~=");

        operators.add(">");
        operators.add(">=");

        operators.add("<");
        operators.add("<=");

        operators.add("=");
        operators.add(".");
        return operators;
    }

    public static String parseStr(TaskInstance taskInstance,String expression, Component component) {
        if (expression == null)
            return "";
        expression = expression.replaceAll(" ", "");
        expression = expression.replaceAll("&amp;", "&");
        List<String> operators = operators();
        int length = expression.length();

        StringBuilder newStr = new StringBuilder();
        StringBuilder name = new StringBuilder();
        System.out.println("component:" + component.getName());
        int i = 0;

        for (; i < length; i++) {
            String iStr = String.valueOf(expression.charAt(i));
            boolean isDigit = expression.charAt(i) >= '0' && expression.charAt(i) <= '9';
            if (i == 0) {
                if (!operators.contains(iStr) && !isDigit) {
                    name.append(expression.charAt(i));
                } else {
                    newStr.append(expression.charAt(i));
                }
            } else {
                String i_1Str = String.valueOf(expression.charAt(i - 1));
                if (operators.contains(i_1Str) && !operators.contains(iStr) && !isDigit) {
                    name.append(expression.charAt(i));
                }

                if (operators.contains(i_1Str) && isDigit) {
                    newStr.append(expression.charAt(i));
                }
                if (((expression.charAt(i - 1) >= '0') && (expression.charAt(i - 1) <= '9')) && isDigit) {
                    newStr.append(expression.charAt(i));
                }

                if (!operators.contains(i_1Str) && !operators.contains(iStr) && !isDigit) {
                    name.append(expression.charAt(i));
                }

                if (operators.contains(iStr)) {
                    if (name.length() != 0) {
                        if (expression.charAt(i) == '=' && expression.charAt(i + 1) != '=') {
                            newStr.append(name);
                            name = new StringBuilder();
                        } else {
                            ComponentManage componentManage = new ComponentManage();
                            String nameString = name.toString();
                            System.out.println("name String " + nameString);
                            if (nameString.equals("null")) {
                                name = new StringBuilder();
                                newStr.append(nameString);
                            } else {
                                String value = componentManage.get(taskInstance,component, nameString);
                                if (value == null) {
                                    System.out.println("component name is " + component.getName());
                                    System.out.println("解析式变量名不存在");
                                    return null;
                                }
                                name = new StringBuilder();
                                newStr.append(value);
                            }

                        }
                    }
                    newStr.append(expression.charAt(i));
                }
            }

        }

        if (name.length() != 0) {
            ComponentManage componentManage = new ComponentManage();
            String nameString = name.toString();
            if (nameString.equals("null")) {
                name = new StringBuilder();
                newStr.append(nameString);
            } else {
                System.out.println("nameString--"+nameString);
                System.out.println(component.getName());
                String value = componentManage.get(taskInstance,component, nameString);
                if (value == null) {
                    System.out.println("解析式变量名不存在");
                    return null;
                }
                name = new StringBuilder();
                newStr.append(value);
            }
        }

        return newStr.toString();
    }

    public static List<String> getDataNameList(String expression) {
        if (expression == null)
            return null;
        List<String> dataNameList = new ArrayList<>();
        expression = expression.replaceAll(" ", "");
        expression = expression.replaceAll("&amp;", "&");

        List<String> operators = operators();

        int length = expression.length();

        StringBuilder newStr = new StringBuilder();
        StringBuilder name = new StringBuilder();
        int i = 0;

        for (; i < length; i++) {
            String iStr = String.valueOf(expression.charAt(i));
            boolean isDigit = expression.charAt(i) >= '0' && expression.charAt(i) <= '9';
            if (i == 0) {
                if (!operators.contains(iStr) && !isDigit) {
                    name.append(expression.charAt(i));
                } else {
                    newStr.append(expression.charAt(i));
                }
            } else {
                String i_1Str = String.valueOf(expression.charAt(i - 1));
                if (operators.contains(i_1Str) && !operators.contains(iStr) && !isDigit) {
                    name.append(expression.charAt(i));
                }

                if (operators.contains(i_1Str) && isDigit) {
                    newStr.append(expression.charAt(i));
                }
                if (((expression.charAt(i - 1) >= '0') && (expression.charAt(i - 1) <= '9')) && isDigit) {
                    newStr.append(expression.charAt(i));
                }

                if (!operators.contains(i_1Str) && !operators.contains(iStr) && !isDigit) {
                    name.append(expression.charAt(i));
                }

                if (operators.contains(iStr)) {
                    if (name.length() != 0) {
                        if (expression.charAt(i) == '=' && expression.charAt(i + 1) != '=') {
                            name = new StringBuilder();
                        } else {
                            String nameString = name.toString();
                            if (nameString.equals("null")) {
                                name = new StringBuilder();
                            } else {
                                if (!dataNameList.contains(nameString))
                                    dataNameList.add(nameString);
                                name = new StringBuilder();
                            }

                        }
                    }
                }
            }

        }

        if (name.length() != 0) {
            String nameString = name.toString();
            if (nameString.equals("null")) {
                name = new StringBuilder();
            } else {
                if (!dataNameList.contains(nameString))
                    dataNameList.add(nameString);
                name = new StringBuilder();
            }
        }

        return dataNameList;
    }

    /**
     * 判断类型--参数是解析后的表达式
     */
    public static String getExpressionType(String expression) {
        int index = expression.indexOf("=");
        if (expression.charAt(index + 1) == '=')
            return "boolType";
        else
            return "assignmentType";
    }

    /**
     * 得到赋值的数据名称，和表达式
     */
    public static Map<String, String> getAssignedData(String expresstion) {
        Map<String, String> map = new HashMap<>();
        int index = expresstion.indexOf("=");
        String dataName = expresstion.substring(0, index);
        String operation = expresstion.substring(index + 1);
        map.put(dataName, operation);
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
