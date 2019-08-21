package util;

import lmf.Component;
import manager.ComponentManage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetName {
    public static String getStr(String s, Component component) {
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

        for (int i = 0; i < length; i++) {

            if(i==0){
                if (!operators.contains(String.valueOf(s.charAt(i))) && !(s.charAt(i)>='0' && s.charAt(i)<='9')) {
                    name.append(s.charAt(i));
                }
                else{
                    newStr.append(s.charAt(i));
                }
            }

            else{
                String i_1Str=String.valueOf(s.charAt(i-1));
                String iStr=String.valueOf(s.charAt(i));
                if (operators.contains(i_1Str) && !operators.contains(iStr) && !(s.charAt(i)>='0'&& s.charAt(i)<='9')) {
                    name.append(s.charAt(i));
                }
                if(operators.contains(i_1Str) && ((s.charAt(i)>='0') && (s.charAt(i)<='9'))){
                    newStr.append(s.charAt(i));
                }
//                if(operators.contains(String.valueOf(s.charAt(i-1))) && operators.contains(String.valueOf(s.charAt(i)))){
//                    newStr.append(s.charAt(i));
//                }
                if(!operators.contains(i_1Str) && !operators.contains(iStr)){
                    name.append(s.charAt(i));
                }
                if(operators.contains(iStr)) {
                    if (name.length() != 0) {
                        if(s.charAt(i)=='=' && s.charAt(i+1)!='='){
                            newStr.append(name);
                            name=new StringBuilder();
                        }
                        else {
                            ComponentManage componentManage=new ComponentManage();
                            String nameString=name.toString();
                            String value = componentManage.get(component,nameString);
                            name = new StringBuilder();
                            newStr.append(value);
                        }
                    }
                    newStr.append(s.charAt(i));
                }
            }

        }
        return newStr.toString();
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
