package cn.travellerr.utils;

import cn.travellerr.Favorability;
import net.mamoe.mirai.utils.MiraiLogger;

import java.io.IOException;

public class Log {
    private static final MiraiLogger log = Favorability.INSTANCE.getLogger();
    private static final String name = "好感度系统-";

    public static void info(String msg) {
        log.info(name + msg);
    }

    public static void warning(String msg) {
        log.warning(name + msg);
    }

    public static void error(String msg, IOException e) {
        log.error(name + msg);
    }
}
