package cn.travellerr;

import cn.chahuyun.economy.HuYanEconomy;
import cn.travellerr.command.RegCommand;
import cn.travellerr.config.PluginConfig;
import cn.travellerr.config.SqlConfig;
import cn.travellerr.config.TipsConfig;
import cn.travellerr.config.TitleConfig;
import cn.travellerr.title.LoveTitleManager;
import cn.travellerr.utils.CheckDepends;
import cn.travellerr.utils.CopyGiftJson;
import cn.travellerr.utils.EconomyUtil;
import cn.travellerr.utils.HibernateUtil;
import cn.travellerr.version.CheckLatestVersion;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;

public final class Favorability extends JavaPlugin {
    public static final Favorability INSTANCE = new Favorability();

    public static PluginConfig config;
    public static TipsConfig msgConfig;
    public static TitleConfig titleConfig;
    public static SqlConfig sqlConfig;

    public static final String version = "2.0.1";

    private Favorability() {
        super(new JvmPluginDescriptionBuilder("cn.travellerr.Favorability", version)
                .name("Favorability")
                .info("好感度")
                .author("Travellerr")

                .dependsOn("xyz.cssxsh.mirai.plugin.mirai-hibernate-plugin", false)
                .dependsOn("xyz.cssxsh.mirai.plugin.mirai-economy-core", false)
                .dependsOn("cn.chahuyun.HuYanEconomy", true)
                .build());
    }

    @Override
    public void onEnable() {
        reloadPluginConfig(cn.travellerr.config.PluginConfig.INSTANCE);
        reloadPluginConfig(cn.travellerr.config.TipsConfig.INSTANCE);
        reloadPluginConfig(cn.travellerr.config.SqlConfig.INSTANCE);
        reloadPluginConfig(TitleConfig.INSTANCE);

        config = cn.travellerr.config.PluginConfig.INSTANCE;
        msgConfig = cn.travellerr.config.TipsConfig.INSTANCE;
        sqlConfig = SqlConfig.INSTANCE;
        titleConfig = TitleConfig.INSTANCE;
        RegCommand.INSTANCE.registerCommand();

        if (config.getEconomyName() == 0) {
            if (!HuYanEconomy.INSTANCE.isEnabled()) {
                CheckDepends.init();
                return;
            }

            LoveTitleManager.init();
        }


        HibernateUtil.init(this);
        EconomyUtil.init();
        CheckLatestVersion.init();
        LoveTitleManager.init();
        CopyGiftJson.copy();
        getLogger().info("插件已加载！!");
        CheckLatestVersion.init();

    }

}