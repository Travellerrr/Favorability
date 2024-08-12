package cn.travellerr.utils;

import java.awt.*;

public class ImageUtil {

    /**
     * 将color换成16进制的颜色，不带#
     *
     * @param color 颜色
     * @return 16进制的颜色
     * @author Moyuyanli
     */
    public static String colorHex(Color color) {
        // 将颜色的红、绿、蓝分量转换为16进制字符串
        String red = Integer.toHexString(color.getRed());
        String green = Integer.toHexString(color.getGreen());
        String blue = Integer.toHexString(color.getBlue());


        // 补齐两位数
        if (red.length() == 1) red = "0" + red;
        if (green.length() == 1) green = "0" + green;
        if (blue.length() == 1) blue = "0" + blue;

        // 返回格式化的16进制颜色字符串
        return red + green + blue;
    }
}
