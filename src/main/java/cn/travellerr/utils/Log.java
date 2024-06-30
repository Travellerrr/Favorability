package cn.travellerr.utils;

import cn.travellerr.Favorability;
import net.mamoe.mirai.utils.MiraiLogger;

import java.io.IOException;

public class Log {
    private static final MiraiLogger log = Favorability.INSTANCE.getLogger();
    private static final String name = "好感度系统-";

    public static void info(Object msg) {
        log.info(name + msg);
    }

    public static void warning(Object msg) {
        log.warning(name + msg);
    }

    public static void error(Object msg, IOException e) {
        log.error(name + msg);
        e.fillInStackTrace();
    }

    public static void error(Object msg, Throwable e) {
        log.error(name + msg);
        e.fillInStackTrace();
    }

    public static void error(Object msg) {
        log.error(name + msg);
    }

    public static void debug(Object msg) {
        log.debug(name + msg);
    }
}
