package cn.travellerr.make;

import cn.chahuyun.economy.utils.EconomyUtil;
import cn.hutool.core.util.RandomUtil;
import cn.travellerr.utils.sqlUtil;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.utils.ExternalResource;

import java.net.URL;

import static cn.travellerr.make.getPng.*;
import static cn.travellerr.utils.sqlUtil.*;

public class makingMachine {
    public static void use(MessageEvent event) {
        Contact subject = event.getSubject();
        MessageChain message = event.getMessage();
        User user = event.getSender();
        String code = message.serializeToMiraiCode();
        String[] s = code.split(" ");
        int money;
        money = Integer.parseInt(s[s.length - 1]);
        if(!check(user, money)) {
            subject.sendMessage(new At(user.getId()).plus(" 你的金钱还不够！"));
            return;
        }
        createGift(subject, user, money);
    }

    public static boolean check(User user, int money) {
        double RealMoney = EconomyUtil.getMoneyByUser(user);
        return RealMoney >= money;
    }

    public static void createGift(Contact subject, User user, int money) {
        try {
            int time = RandomUtil.randomInt(10, 180);
            //int time = 1;

            sqlUtil.updateInfo(user.getId(), false);
            if (!timesUp && isMaking) { //时间未到且未领取制造物品
                subject.sendMessage(new At(user.getId()).plus("队列中已有任务，请勿重复制造~"));
                return;
            }
            if (timesUp && isMaking) { //时间已到且未领取制造物品
                subject.sendMessage(new At(user.getId()).plus("制造完喽~先领取物品吧~"));
                return;
            }
            sqlUtil.startMake(user.getId(), money, time * 60L);
            subject.sendMessage(new At(user.getId()).plus(String.format("开始制造，时间：%d 分钟 \n 请在时间到后发送\"#查看制造\"获取物品", time)));
            //subject.sendMessage(new At(user.getId()).plus("啊嘞？你还没有开始制造物品吧！"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void checkItem(MessageEvent event) {
        try {
            Contact subject = event.getSubject();
            MessageChain message = event.getMessage();
            User user = event.getSender();
            sqlUtil.updateInfo(user.getId(), true);
            if (timesUp && isMaking) { //制造完毕且未领取物品
                message(itemLevel);
                URL itemUrl = new URL(getPng.url);
                ExternalResource resource = ExternalResource.create(itemUrl.openStream());
                Image item = subject.uploadImage(resource);
                subject.sendMessage(
                        new At(user.getId())
                                .plus("\n")
                                .plus(item)
                                .plus("\n")
                                .plus(name)
                                .plus("\n")
                                .plus(Describe)
                                .plus("\n等级：")
                                .plus(level)
                                .plus("\n增加好感度：")
                                .plus(String.valueOf(love))
                                .plus("\n")
                );
                sqlUtil.addLove(love, user.getId());
                resource.close();
                return;
            }
            if (!timesUp && isMaking) {
                subject.sendMessage(new At(user.getId()).plus("还没制作完成哦~"));
                return;
            }
            subject.sendMessage(new At(user.getId()).plus("制造队列为空~"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
