package cn.travellerr.utils;

public class ReplaceMsg {
    public static String Replace(String msg, String variable, String name) {
        return msg.replace(variable, name);
    }

    public static String Replace(String msg, String variable, int integer) {
        return msg.replace(variable, String.valueOf(integer));
    }

    public static String Replace(String msg, String variable, Long longVar) {
        return msg.replace(variable, String.valueOf(longVar));
    }


}
