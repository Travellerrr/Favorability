package cn.travellerr;

import cn.travellerr.event.MessageEventListener;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.EventChannel;
import net.mamoe.mirai.event.GlobalEventChannel;
public final class Favorability extends JavaPlugin {
    public static final Favorability INSTANCE = new Favorability();

    private Favorability() {
        super(new JvmPluginDescriptionBuilder("cn.travellerr.Favorability", "0.1.0")
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

        EventChannel<Event> eventEventChannel = GlobalEventChannel.INSTANCE.parentScope(Favorability.INSTANCE);
        eventEventChannel.registerListenerHost(new MessageEventListener());
        //加载前置
        getLogger().info("插件已加载！!");
    }
}