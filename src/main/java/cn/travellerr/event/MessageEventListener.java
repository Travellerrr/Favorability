package cn.travellerr.event;

import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.MessageEvent;
import org.jetbrains.annotations.NotNull;


@Deprecated(since = "改入MCL指令系统")
public class MessageEventListener extends SimpleListenerHost {

    @EventHandler
    public void onMessage(@NotNull MessageEvent event) {

        String msg = event.getMessage().serializeToMiraiCode();
        Contact subject = event.getSubject();

       /* switch (msg) {
            case "#查看背包":
            case "#背包":
                return;
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
            case "#制造":
                User user = event.getSender();
                subject.sendMessage(new At(user.getId()).plus("Sensei，请使用\"/制造 [金币数量]\"来制造哦~\n例如：\n/制造 100\n/制造 30 "));
                return;
            case "#好感度排行":
            case "#排行":
                FavorUtil.getList(event);
                return;
        }
        String make = "#制造 (\\d+)|#制作 (\\d+)";
        if(Pattern.matches(make, msg))
        {
            makingMachine.use(event);
            return;
        }

        String userToUserTransferRegex = "#盒 (\\[mirai:at:\\d+])? ";
        String userToUserTransferRegexWithoutSpace = "#盒 (\\[mirai:at:\\d+])?";
        if (Pattern.matches(userToUserTransferRegex, msg)) {
            Log.info("盒指令");
            wtfUtil.useToOther(event, true);
            return;
        } else if (Pattern.matches(userToUserTransferRegexWithoutSpace, msg)) {
            Log.info("盒指令");
            wtfUtil.useToOther(event, false);
        }
        */

        /*
         * 针对QQ官方机器人框架的指令识别
         */

//        switch (msg) {
//            case "/查看背包":
//            case "/背包":
//                return;
//            /*case "#测试":
//                    User user = event.getSender();
//                    subject.sendMessage(new At(user.getId()).plus(String.valueOf(EconomyUtil.getMoneyByUser(user))));
//                return ;*/
//            case "/查看制造":
//            case "/查看制作":
//            case "/查看":
//                makingMachine.checkItem(event);
//                return;
//            case "/好感度":
//            case "/查看好感度":
//            case "/好感":
//                FavorUtil.checkFavor(event);
//                return;
//            case "/盒我":
//                wtfUtil.use(event);
//                return;
//            case "/制造":
//                User user = event.getSender();
//                subject.sendMessage(new At(user.getId()).plus("Sensei，请使用\"/制造 [金币数量]\"来制造哦~\n例如：\n/制造 100\n/制造 30 "));
//        }
//        String make = "/制造 (\\d+)|/制作 (\\d+)";
//        if(Pattern.matches(make, msg))
//        {
//            makingMachine.use(event);
//            return;
//        }
//




    }

}
