package cn.travellerr.event;

import cn.travellerr.make.makingMachine;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MusicKind;
import net.mamoe.mirai.message.data.MusicShare;
import org.jetbrains.annotations.NotNull;

import cn.chahuyun.economy.utils.EconomyUtil;

import java.io.File;
import java.util.regex.Pattern;

public class MessageEventListener extends SimpleListenerHost {
    @EventHandler
    public void onMessage(@NotNull MessageEvent event) {
        User sender = event.getSender();
        Contact subject = event.getSubject();
        String msg = event.getMessage().serializeToMiraiCode();

        switch (msg) {
            case "#制造":
                return;
            case "#查看背包":
            case "#背包":
                return;
            case "#测试":
                    User user = event.getSender();
                    subject.sendMessage(new At(user.getId()).plus(String.valueOf(EconomyUtil.getMoneyByUser(user))));
                return ;
        }
        String make = "#制造 (\\d+)|#制作 (\\d+)";
        if(Pattern.matches(make, msg))
        {
            makingMachine.use(event);
            return;
        }

        String Cheat = "#作弊 (\\d+)";
        if(Pattern.matches(Cheat,msg))
        {
            MessageChain message = event.getMessage();
            User user = event.getSender();
            String code = message.serializeToMiraiCode();
            String[] s = code.split(" ");
            int money;
            money = Integer.parseInt(s[s.length - 1]);
            EconomyUtil.plusMoneyToUser(user, money);
            subject.sendMessage("成功");
        }


    }
}
