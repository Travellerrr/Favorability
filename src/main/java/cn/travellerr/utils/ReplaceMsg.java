package cn.travellerr.utils;

public class ReplaceMsg {


    /**
     * 字符串放置变量
     * @author Travellerr
     * @param msg 需修改字符串
     * @param variable 修改的变量名称
     * @param object 变量内容
     * @return 修改完毕的字符串
     */
    public static String Replace(String msg, String variable, Object object) {
        return msg.replace(variable, String.valueOf(object));
    }

}
