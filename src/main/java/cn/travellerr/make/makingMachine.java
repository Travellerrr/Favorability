package cn.travellerr.make;

import cn.chahuyun.economy.utils.EconomyUtil;
import cn.hutool.core.util.RandomUtil;
import cn.travellerr.config.Config;
import cn.travellerr.utils.sqlUtil;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.utils.ExternalResource;

import java.net.URL;
import java.util.List;

import static cn.travellerr.Favorability.config;
import static cn.travellerr.make.getPng.*;
import static cn.travellerr.utils.sqlUtil.*;

public class makingMachine {
    public static void use(Contact subject, User user, Long coin) {
        int money = Math.toIntExact(coin);
        if (!checkEnoughMoney(money)) {
            List<String> deniedMessageList = config.getNotEnough();
            int getMessage = RandomUtil.randomInt(0, deniedMessageList.size() - 1);
            subject.sendMessage(new At(user.getId()).plus("你支出的费用太少啦！" + deniedMessageList.get(getMessage)));
            return;
        }
        if (!checkRealMoney(user, money)) {
            subject.sendMessage(new At(user.getId()).plus(String.format(" 你掏出了%f枚金币，导致了物质坍缩形成了黑洞，宇宙毁灭了……", EconomyUtil.getMoneyByUser(user) - money)));
            return;
        }
        createGift(subject, user, money);
    }

    public static boolean checkRealMoney(User user, int money) {
        double RealMoney = EconomyUtil.getMoneyByUser(user);
        return RealMoney >= money;
    }

    public static boolean checkEnoughMoney(int money) {
        return money >= Config.INSTANCE.getAtLeastCoin();
    }

    public static void createGift(Contact subject, User user, int money) {
        try {
            int time = RandomUtil.randomInt(config.getAtLeastMin(), config.getAtMostMin());
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
            subject.sendMessage(new At(user.getId()).plus(String.format("开始制造，时间：%d 分钟，物品等级: %s \n 请在时间到后发送\"#查看制造\"获取物品", time, itemLevel)));
            EconomyUtil.plusMoneyToUser(user, -money);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void checkItem(Contact subject, User user) {
        try {
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
                subject.sendMessage(new At(user.getId()).plus(String.format("还没制作完成哦~还剩%d分钟\n预计制造物品等级: %d", needTime, itemLevel)));
                return;
            }
            subject.sendMessage(new At(user.getId()).plus("制造队列为空~"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
