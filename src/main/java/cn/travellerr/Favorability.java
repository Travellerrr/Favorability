package cn.travellerr;

import cn.chahuyun.economy.HuYanEconomy;
import cn.travellerr.LoveYou.event.GroupMessageEventListener;
import cn.travellerr.command.RegCommand;
import cn.travellerr.config.*;
import cn.travellerr.title.LoveTitleManager;
import cn.travellerr.utils.*;
import cn.travellerr.version.CheckLatestVersion;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.EventChannel;
import net.mamoe.mirai.event.GlobalEventChannel;

public final class Favorability extends JavaPlugin {
    public static final Favorability INSTANCE = new Favorability();

    public static PluginConfig config;
    public static TipsConfig msgConfig;
    public static LoveYou loveYou;
    public static TitleConfig titleConfig;
    public static SqlConfig sqlConfig;

    public static final String version = "2.0.0";

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
        reloadPluginConfig(cn.travellerr.config.LoveYou.INSTANCE);
        reloadPluginConfig(cn.travellerr.config.SqlConfig.INSTANCE);
        reloadPluginConfig(TitleConfig.INSTANCE);

        config = cn.travellerr.config.PluginConfig.INSTANCE;
        msgConfig = cn.travellerr.config.TipsConfig.INSTANCE;
        loveYou = cn.travellerr.config.LoveYou.INSTANCE;
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

        if (loveYou.getEnable()) {
            Log.info("LoveYou附属功能已启用");

            EventChannel<Event> eventEventChannel = GlobalEventChannel.INSTANCE.parentScope(Favorability.INSTANCE);

            eventEventChannel.registerListenerHost(new GroupMessageEventListener());
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