package cn.travellerr.event;

import cn.travellerr.make.makingMachine;
import cn.travellerr.utils.FavorUtil;
import cn.travellerr.utils.wtfUtil;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.MessageEvent;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public class MessageEventListener extends SimpleListenerHost {
    @EventHandler
    public void onMessage(@NotNull MessageEvent event) {
        String msg = event.getMessage().serializeToMiraiCode();

        switch (msg) {
            case "#查看背包":
            case "#背包":
                return;
            /*case "#测试":
                    User user = event.getSender();
                    subject.sendMessage(new At(user.getId()).plus(String.valueOf(EconomyUtil.getMoneyByUser(user))));
                return ;*/
            case "#查看制造":
            case "#查看制作":
            case "#查看":
                makingMachine.checkItem(event);
                return;
            case "#好感度":
            case "#查看好感度":
            case "#好感":
                FavorUtil.checkFavor(event);
                return;
            case "#盒我":
                wtfUtil.use(event);
                return;
        }
        String make = "#制造 (\\d+)|#制作 (\\d+)";
        if(Pattern.matches(make, msg))
        {
            makingMachine.use(event);
        }

       /* String Cheat = "#作弊 (\\d+)";
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
        }*/


    }
}
