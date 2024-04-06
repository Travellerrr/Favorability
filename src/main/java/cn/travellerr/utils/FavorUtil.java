package cn.travellerr.utils;

import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.At;

public class FavorUtil {
    public static void checkFavor(MessageEvent event) {
        Contact subject = event.getSubject();
        User user = event.getSender();
        int exp = sqlUtil.getExp(user.getId());
        subject.sendMessage(new At(user.getId()).plus(String.format("\n阿洛娜对你的好感度为: %d", exp)));
    }
}
