package cn.travellerr.make;

import cn.chahuyun.economy.utils.EconomyUtil;
import cn.travellerr.utils.sqlUtil;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import  cn.hutool.core.util.RandomUtil;
import net.mamoe.mirai.utils.ExternalResource;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import static cn.travellerr.make.getPng.*;

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

            subject.sendMessage(new At(user.getId()).plus(String.format("开始制造，时间：%d 分钟 \n 请在时间到后发送\"#查看制造\"获取物品", time)));
            //sqlUtil.startMake(user.getId(), money);
            message();
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
            resource.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
