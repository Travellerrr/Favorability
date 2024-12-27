package cn.travellerr.favourite.make;

//import cn.chahuyun.economy.utils.EconomyUtil;

import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.travellerr.config.PluginConfig;
import cn.travellerr.entity.Favourite;
import cn.travellerr.favourite.FavouriteManager;
import cn.travellerr.utils.EconomyUtil;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.utils.ExternalResource;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import static cn.travellerr.Favorability.config;
import static cn.travellerr.Favorability.msgConfig;

public class MakingMachine {

    /**
     * 使用 <strong><i>/制造 [金币]</i></strong> 指令
     *
     * @param subject 联系对象
     * @param user    用户
     * @param coin    金币数量
     * @author Travellerr
     */
    public static void use(Contact subject, User user, Long coin) {
        int money = Math.toIntExact(coin);
        if (!checkEnoughMoney(money)) {
            List<String> deniedMessageList = msgConfig.getNotEnough();
            int getMessage = RandomUtil.randomInt(0, deniedMessageList.size() - 1);
            subject.sendMessage(new At(user.getId()).plus("你支出的费用太少啦！" + deniedMessageList.get(getMessage)));
            return;
        }
        if (!checkRealMoney(user, money)) {
            subject.sendMessage(new At(user.getId()).plus(String.format(" 你掏出了%f枚金币，导致了物质坍缩形成了黑洞，宇宙毁灭了……", EconomyUtil.getMoney(user) - money)));
            return;
        }
        createGift(subject, user, money);
    }

    /**
     * 是否拥有足够的金钱
     * @author Travellerr
     * @param user 用户
     * @param money 指令中填写的金币数量
     * @return 是否有足够金钱
     */
    private static boolean checkRealMoney(User user, int money) {
        double RealMoney = EconomyUtil.getMoney(user);
        return RealMoney >= money;
    }

    /**
     * 金币是否达到设置的最低制造标准
     * @author Travellerr
     * @param money 指令中填写的金币数量
     * @return 是否达到最低标准
     */
    private static boolean checkEnoughMoney(int money) {
        return money >= PluginConfig.INSTANCE.getAtLeastCoin();
    }

    /**
     * 制造礼物
     * @author Travellerr
     * @param subject 联系对象
     * @param user 用户
     * @param money 指令中填写的金币数量
     */
    private static void createGift(Contact subject, User user, int money) {
        long time = RandomUtil.randomInt(config.getAtLeastMin(), config.getAtMostMin()) * 60L;
            //int time = 1;

        Favourite userInfo = FavouriteManager.getInfo(user.getId());
        if (userInfo != null) {
            if (userInfo.isMaking()) {
                boolean isTimesUp = FavouriteManager.isTimesUp(userInfo, false) == -1;
                if (!isTimesUp) { //时间未到且未领取制造物品
                    subject.sendMessage(new At(user.getId()).plus("队列中已有任务，请勿重复制造~"));
                    return;
                }
                subject.sendMessage(new At(user.getId()).plus("制造完喽~先领取物品吧~"));
                return;
            }
        }
        FavouriteManager.startMake(user, money, time);
        userInfo = FavouriteManager.getInfo(user.getId());
        subject.sendMessage(new At(user.getId()).plus(String.format("开始制造，时间：%s ，物品等级: %s \n 请在时间到后发送\"#查看制造\"获取物品", DateUtil.formatBetween(time * 1000, BetweenFormatter.Level.SECOND), userInfo.getItemLevel())));
            EconomyUtil.plusMoney(user, -money);
    }

    /**
     * 检查制造物品是否完成，并修改好感度发送礼物
     * @author Travellerr
     * @param subject 联系对象
     * @param user 用户
     */
    public static void checkItem(Contact subject, User user) {
        try {
            Favourite userInfo = FavouriteManager.getInfo(user.getId());

            userInfo.printAll();

            Long needTimes = FavouriteManager.isTimesUp(userInfo, false);
            boolean isTimesUp = needTimes == -1;
            if (isTimesUp && userInfo.isMaking()) { //制造完毕且未领取物品
                FavouriteManager.clearQueue(userInfo);
                GetPng.message(userInfo.getItemLevel());
                URL itemUrl = new URL(GetPng.item.getUrl());
                try (ExternalResource resource = ExternalResource.create(itemUrl.openStream())) {
                    sendImage(subject, user, resource);
                } catch (IOException e) {
                    subject.sendMessage(
                            new At(user.getId())
                                    .plus("\n")
                                    .plus("[图片消失了XwX, 原因: " + e.getMessage() + "]")
                                    .plus("\n")
                                    .plus(GetPng.item.getName())
                                    .plus("\n")
                                    .plus(GetPng.item.getDescribe())
                                    .plus("\n等级：")
                                    .plus(GetPng.item.getLevel())
                                    .plus("\n增加好感度：")
                                    .plus(String.valueOf(GetPng.item.getLove()))
                                    .plus("\n")
                    );
                    FavouriteManager.addLove(userInfo, GetPng.item.getLove());
                    throw new RuntimeException(e);
                }
                return;
            }
            if (!isTimesUp && userInfo.isMaking()) {
                subject.sendMessage(new At(user.getId()).plus(String.format("还没制作完成哦~还剩%s\n预计制造物品等级: %d", DateUtil.formatBetween(needTimes * 1000, BetweenFormatter.Level.SECOND), userInfo.getItemLevel())));
                return;
            }
            subject.sendMessage(new At(user.getId()).plus("制造队列为空~"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void sendImage(Contact subject, User user, ExternalResource resource) {
        Image item = subject.uploadImage(resource);
        subject.sendMessage(
                new At(user.getId())
                        .plus("\n")
                        .plus(item)
                        .plus("\n")
                        .plus(GetPng.item.getName())
                        .plus("\n")
                        .plus(GetPng.item.getDescribe())
                        .plus("\n等级：")
                        .plus(GetPng.item.getLevel())
                        .plus("\n增加好感度：")
                        .plus(String.valueOf(GetPng.item.getLove()))
                        .plus("\n")
        );
        FavouriteManager.addLove(user, GetPng.item.getLove());
    }

    public static void checkItemQuickly(Contact subject, User user) {
        try {
            Favourite userInfo = FavouriteManager.getInfo(user.getId());

            userInfo.printAll();

            if (userInfo.isMaking()) { //制造完毕且未领取物品
                FavouriteManager.clearQueue(userInfo);
                GetPng.message(userInfo.getItemLevel());
                URL itemUrl = new URL(GetPng.item.getUrl());
                ExternalResource resource = ExternalResource.create(itemUrl.openStream());
                sendImage(subject, user, resource);
                resource.close();
                return;
            }
            subject.sendMessage(new At(user.getId()).plus("制造队列为空~"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
