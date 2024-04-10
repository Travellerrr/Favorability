package cn.travellerr.utils;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.At;

import static cn.chahuyun.economy.HuYanEconomy.config;

public class FavorUtil {
    public static void checkFavor(MessageEvent event) {
        Contact subject = event.getSubject();
        User user = event.getSender();
        int exp = sqlUtil.getExp(user.getId());

        Bot bot = Bot.getInstance(config.getBot());
        subject.sendMessage(new At(user.getId()).plus(String.format("\n%s对你的好感度为: %d", bot.getNick(), FavorLevel(exp))));
    }

    private static int FavorLevel(int exp) {
        int[] thresholds = {15, 45, 75, 110, 145, 180, 220, 260, 300, 360, 450, 555, 675, 815, 975, 1155, 1360, 1590, 1845, 2130, 2445, 2790, 3165, 3575, 4020, 4500, 5020, 5580, 6180, 6825, 7515, 8250, 9030, 9860, 10740, 11670, 12655, 13695, 14790, 15945, 17160, 18435, 19770, 21170, 22635, 24165, 25765, 27435, 29175};

        int level = 1;
        for (int threshold : thresholds) {
            if (exp >= threshold) {
                level++;
            } else {
                break;
            }
        }

        if (level > 50) {
            level = 50 + (exp - 29175) / 1810;
        }

        return level;
    }

}
