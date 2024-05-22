package cn.travellerr.utils;

import cn.travellerr.Favorability;
import net.mamoe.mirai.console.MiraiConsole;

public class checkDepends {
    public static void init() {
        Log.error("依赖项未启用，请选择其一启用后到 /config/cn.travellerr.Favorability/ 编辑config.yml 选择经济前置");
        MiraiConsole.INSTANCE.getPluginManager().disablePlugin(Favorability.INSTANCE);
    }
}
