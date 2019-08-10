package util;

import lmf.Data;

import java.util.Map;

/**
 * 对给定的表达式进行计算
 */

public class Caculate<T extends Number> {

    private static Caculate caculate = new Caculate();

    public static String caculate(String expression, Map<String, Data> dataMap) {
        // todo : 解析表达式，根据data的值以及表达式进行计算
        return "answer";
    }

    public static <T extends Number> double add(T t1, T t2) {
        return t1.doubleValue() + t2.doubleValue();
    }

    public static <T extends Number> double reduce(T t1, T t2) {
        return t1.doubleValue() + t2.doubleValue();
    }

    public static <T extends Number> double multiple(T t1, T t2) {
        return t1.doubleValue() + t2.doubleValue();
    }

    public static <T extends Number> double divide(T t1, T t2) {
        return t1.doubleValue() + t2.doubleValue();
    }
}
