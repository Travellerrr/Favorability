package cn.travellerr;

import cn.chahuyun.economy.HuYanEconomy;
import cn.travellerr.command.RegCommand;
import cn.travellerr.config.PluginConfig;
import cn.travellerr.config.TipsConfig;
import cn.travellerr.utils.EconomyUtil;
import cn.travellerr.utils.checkDepends;
import cn.travellerr.utils.checkLatestVersion;
import cn.travellerr.utils.copyGiftJson;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import top.mrxiaom.mirai.dailysign.MiraiDailySign;

import static cn.travellerr.cronJob.CheckVersionKt.cronJob;

public final class Favorability extends JavaPlugin {
    public static final Favorability INSTANCE = new Favorability();
    public static PluginConfig config;
    public static TipsConfig msgConfig;
    public static final String version = "1.0.2";

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
        config = cn.travellerr.config.PluginConfig.INSTANCE;
        msgConfig = cn.travellerr.config.TipsConfig.INSTANCE;

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