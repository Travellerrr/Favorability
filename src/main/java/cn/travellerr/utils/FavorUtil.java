package cn.travellerr.utils;

import cn.travellerr.config.Config;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.*;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.ForwardMessageBuilder;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.PlainText;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.travellerr.utils.sqlUtil.getListSql;


public class FavorUtil {
    public static void checkFavor(Contact subject, User user, Bot bot) {
        int exp = sqlUtil.getExp(user.getId());

        subject.sendMessage(new At(user.getId()).plus(String.format("\n%s对你的好感度为: %d\n%s", bot.getNick(), FavorLevel(exp), FavorMsg(FavorLevel(exp), user.getNick()))));
    }

    private static String FavorMsg(int level, String SenseiName) {
        Config config = Config.INSTANCE;
        List<String> msgList = config.getLoveMessage();
        int index = level / config.getChangeLevel();
        if (index > msgList.size()) index = msgList.size();
        String msg = msgList.get(index);
        return ReplaceString(msg, SenseiName);
    }

    /*
     * 变量替换
     */
    private static String ReplaceString(String msg, String Name) {
        Config config = Config.INSTANCE;
        String suffix = config.getSuffix();
        String replaced = msg.replace("%成员%", Name);
        replaced = replaced.replace("%后缀%", suffix);
        return replaced;
    }


    /*
     * 计算好感度等级
     */
    private static int FavorLevel(int exp) {
        int[] thresholds = {15, 45, 75, 110, 145, 180, 220, 260, 300, 360,
                450, 555, 675, 815, 975, 1155, 1360, 1590, 1845, 2130, 2445,
                2790, 3165, 3575, 4020, 4500, 5020, 5580, 6180, 6825, 7515,
                8250, 9030, 9860, 10740, 11670, 12655, 13695, 14790, 15945,
                17160, 18435, 19770, 21170, 22635, 24165, 25765, 27435, 29175};
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

    private static List<Integer> castIntList(List<?> list) {
        List<Integer> result = new ArrayList<>();
        for (Object element : list) {
            result.add((Integer) element);
        }
        return result;
    }

    private static List<Long> castLongList(List<?> list) {
        List<Long> result = new ArrayList<>();
        for (Object element : list) {
            result.add((Long) element);
        }
        return result;
    }

    public static void getLoveList(Contact subject, Group group) {
        // 获取数据
        Map<String, List<?>> info = getListSql();
        List<Integer> LoveExpList = castIntList(info.get("expList"));
        List<Long> uidName = castLongList(info.get("uidName"));

        // 构建消息转发器
        ForwardMessageBuilder forwardMessage = new ForwardMessageBuilder(subject);

        // 获取群成员列表
        ContactList<NormalMember> contactList = group.getMembers();
        List<NormalMember> normalMemberList = contactList.stream().toList();

        // 过滤并排序群成员
        List<User> users = normalMemberList.stream()
                .filter(member -> uidName.contains(member.getId()))
                .sorted(Comparator.comparingInt(member -> uidName.indexOf(member.getId())))
                .collect(Collectors.toList());

        // 确定循环上限
        int size = Math.min(Math.min(LoveExpList.size(), users.size()), 100);

        // 遍历并发送消息
        for (int i = 0; i < size; i++) {
            // 构建消息内容
            String nickname = users.get(i).getNick();
            String messageContent = "这位是 " + nickname + " Sensei,\n阿洛娜对Ta的好感度为: " + FavorLevel(LoveExpList.get(i));
            Message message = new PlainText(messageContent);

            // 添加消息到转发器
            forwardMessage.add(users.get(i), message);
        }

        // 发送转发的消息
        subject.sendMessage(forwardMessage.build());
    }


}


