package cn.travellerr;

import cn.travellerr.command.RegCommand;
import cn.travellerr.config.Config;
import cn.travellerr.utils.copyGiftJson;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
public final class Favorability extends JavaPlugin {
    public static final Favorability INSTANCE = new Favorability();
    public static Config config;

    private Favorability() {
        super(new JvmPluginDescriptionBuilder("cn.travellerr.Favorability", "1.0.0")
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
        reloadPluginConfig(cn.travellerr.config.Config.INSTANCE);
        config = cn.travellerr.config.Config.INSTANCE;

        RegCommand.INSTANCE.registerCommand();
        //加载前置
        copyGiftJson.copy();
        getLogger().info("插件已加载！!");
    }
}