package cn.travellerr;

import cn.travellerr.command.RegCommand;
import cn.travellerr.config.PluginConfig;
import cn.travellerr.config.TipsConfig;
import cn.travellerr.utils.copyGiftJson;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
public final class Favorability extends JavaPlugin {
    public static final Favorability INSTANCE = new Favorability();
    public static PluginConfig config;
    public static TipsConfig msgConfig;

    private Favorability() {
        super(new JvmPluginDescriptionBuilder("cn.travellerr.Favorability", "1.0.1")
                .name("Favorability")
                .info("好感度")
                .author("Travellerr")

                .dependsOn("xyz.cssxsh.mirai.plugin.mirai-hibernate-plugin", false)
                .dependsOn("xyz.cssxsh.mirai.plugin.mirai-economy-core", false)
                .dependsOn("cn.chahuyun.HuYanEconomy", false)
                .build());
    }

    @Override
    public void onEnable() {
        reloadPluginConfig(cn.travellerr.config.PluginConfig.INSTANCE);
        reloadPluginConfig(cn.travellerr.config.TipsConfig.INSTANCE);
        config = cn.travellerr.config.PluginConfig.INSTANCE;
        msgConfig = cn.travellerr.config.TipsConfig.INSTANCE;

        RegCommand.INSTANCE.registerCommand();
        copyGiftJson.copy();
        getLogger().info("插件已加载！!");
    }
}