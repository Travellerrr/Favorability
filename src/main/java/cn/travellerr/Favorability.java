package cn.travellerr;

import cn.chahuyun.economy.HuYanEconomy;
import cn.travellerr.LoveYou.event.GroupMessageEventListener;
import cn.travellerr.command.RegCommand;
import cn.travellerr.config.LoveYou;
import cn.travellerr.config.PluginConfig;
import cn.travellerr.config.TipsConfig;
import cn.travellerr.utils.EconomyUtil;
import cn.travellerr.utils.Log;
import cn.travellerr.utils.checkDepends;
import cn.travellerr.utils.copyGiftJson;
import cn.travellerr.version.checkLatestVersion;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.EventChannel;
import net.mamoe.mirai.event.GlobalEventChannel;
import top.mrxiaom.mirai.dailysign.MiraiDailySign;

import static cn.travellerr.cronJob.CheckVersionKt.cronJob;

public final class Favorability extends JavaPlugin {
    public static final Favorability INSTANCE = new Favorability();
    public static PluginConfig config;
    public static TipsConfig msgConfig;
    public static LoveYou loveYou;
    public static final String version = "1.0.4";

    private Favorability() {
        super(new JvmPluginDescriptionBuilder("cn.travellerr.Favorability", version)
                .name("Favorability")
                .info("好感度")
                .author("Travellerr")

                .dependsOn("xyz.cssxsh.mirai.plugin.mirai-hibernate-plugin", false)
                .dependsOn("xyz.cssxsh.mirai.plugin.mirai-economy-core", false)
                .dependsOn("cn.chahuyun.HuYanEconomy", true)
                .dependsOn("top.mrxiaom.mirai.dailysign", true)
                .build());
    }

    @Override
    public void onEnable() {
        reloadPluginConfig(cn.travellerr.config.PluginConfig.INSTANCE);
        reloadPluginConfig(cn.travellerr.config.TipsConfig.INSTANCE);
        reloadPluginConfig(cn.travellerr.config.LoveYou.INSTANCE);

        config = cn.travellerr.config.PluginConfig.INSTANCE;
        msgConfig = cn.travellerr.config.TipsConfig.INSTANCE;
        loveYou = cn.travellerr.config.LoveYou.INSTANCE;

        if (loveYou.getEnable()) {
            Log.info("LoveYou附属功能已启用");

            EventChannel<Event> eventEventChannel = GlobalEventChannel.INSTANCE.parentScope(Favorability.INSTANCE);

            eventEventChannel.registerListenerHost(new GroupMessageEventListener());
        }

        if (config.getEconomyName() == 0 && !HuYanEconomy.INSTANCE.isEnabled()) {
            checkDepends.init();
            return;
        }

        if (config.getEconomyName() == 1 && !MiraiDailySign.INSTANCE.isEnabled()) {
            checkDepends.init();
            return;
        }
        EconomyUtil.init();
        RegCommand.INSTANCE.registerCommand();
        copyGiftJson.copy();
        getLogger().info("插件已加载！!");
        checkLatestVersion.init();
        cronJob();

    }

}