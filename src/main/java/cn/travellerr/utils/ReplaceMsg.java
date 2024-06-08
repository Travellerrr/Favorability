package cn.travellerr.utils;

public class ReplaceMsg {

    /**
     * 字符串放置变量
     *
     * @param msg      需修改字符串
     * @param variable 修改的变量名称
     * @param name     变量内容
     * @return 修改完毕的字符串
     * @author Travellerr
     */
    public static String Replace(String msg, String variable, String name) {
        return msg.replace(variable, name);
    }

    /**
     * 字符串放置变量
     * @author Travellerr
     * @param msg 需修改字符串
     * @param variable 修改的变量名称
     * @param integer 变量内容
     * @return 修改完毕的字符串
     */
    public static String Replace(String msg, String variable, int integer) {
        return msg.replace(variable, String.valueOf(integer));
    }

    /**
     * 字符串放置变量
     * @author Travellerr
     * @param msg 需修改字符串
     * @param variable 修改的变量名称
     * @param longVar 变量内容
     * @return 修改完毕的字符串
     */
    public static String Replace(String msg, String variable, Long longVar) {
        return msg.replace(variable, String.valueOf(longVar));
    }


}
