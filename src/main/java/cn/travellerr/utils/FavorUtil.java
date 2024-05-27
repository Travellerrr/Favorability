package cn.travellerr.utils;

import cn.travellerr.config.PluginConfig;
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

import static cn.travellerr.Favorability.config;
import static cn.travellerr.Favorability.msgConfig;


public class FavorUtil {
    public static void checkFavor(Contact subject, User user, Bot bot) {
        int exp = sqlUtil.getExp(user.getId());
        String originMsg = msgConfig.getCheckLove();
        originMsg = ReplaceMsg.Replace(originMsg, "%成员%", user.getNick());
        originMsg = ReplaceMsg.Replace(originMsg, "%机器人%", bot.getNick());
        originMsg = ReplaceMsg.Replace(originMsg, "%好感度%", FavorLevel(exp));
        originMsg = ReplaceMsg.Replace(originMsg, "%好感信息%", FavorMsg(FavorLevel(exp), user.getNick()));
        subject.sendMessage(new At(user.getId()).plus("\n").plus(originMsg));
        //subject.sendMessage(new At(user.getId()).plus(String.format("\n%s对你的好感度为: %d\n%s", bot.getNick(), FavorLevel(exp), FavorMsg(FavorLevel(exp), user.getNick()))));
    }

    private static String FavorMsg(int level, String SenseiName) {
        PluginConfig config = PluginConfig.INSTANCE;
        String msg;
        if (level > 0) {
            List<String> msgList = msgConfig.getLoveMessage();
            int index = level / config.getChangeLevel();
            if (index > msgList.size()) index = msgList.size() - 1;
            msg = msgList.get(index);
        } else {
            msg = msgConfig.getNegativeFavorMessage();
        }
        msg = ReplaceMsg.Replace(msg, "%成员%", SenseiName);
        return ReplaceMsg.Replace(msg, "%后缀%", config.getSuffix());
    }


    /*
     * 计算好感度等级
     */
    private static int FavorLevel(int exp) {
        if (exp < 0) {
            return -Math.abs(exp) / 50;
        }

        int[] thresholds = config.getLevelList();
        int level = 0;
        for (int threshold : thresholds) {
            if (exp >= threshold) {
                level++;
            } else {
                break;
            }
        }

        if (level > 50) {
            level = 50 + (exp - 29175) / config.getPerLevel();
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
        Map<String, List<?>> info = sqlUtil.getListSql();
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
            String messageContent = msgConfig.getGroupLoveMsg();//"这位是 " + nickname + " Sensei,\n阿洛娜对Ta的好感度为: " + FavorLevel(LoveExpList.get(i));

            //%成员% %后缀%,
            //%机器人%%好感%
            messageContent = ReplaceMsg.Replace(messageContent, "%成员%", nickname);
            messageContent = ReplaceMsg.Replace(messageContent, "%后缀%", config.getSuffix());
            messageContent = ReplaceMsg.Replace(messageContent, "%机器人%", subject.getBot().getNick());
            messageContent = ReplaceMsg.Replace(messageContent, "%好感%", FavorLevel(LoveExpList.get(i)));
            Message message = new PlainText(messageContent);

            // 添加消息到转发器
            forwardMessage.add(users.get(i), message);
        }

        // 发送转发的消息
        subject.sendMessage(forwardMessage.build());
    }


    public static void getAllLoveList(Contact subject) {
        // 获取数据
        Map<String, List<?>> info = sqlUtil.getListSql();
        List<Integer> LoveExpList = castIntList(info.get("expList"));
        List<Long> uidName = castLongList(info.get("uidName"));

        // 构建消息转发器
        ForwardMessageBuilder forwardMessage = new ForwardMessageBuilder(subject);

        // 确定循环上限
        int size = Math.min(LoveExpList.size(), 100);
        String suffix = config.getSuffix();
        // 遍历并发送消息
        for (int i = 0; i < size; i++) {

            String messageContent = msgConfig.getTotalLoveMsg();
            messageContent = ReplaceMsg.Replace(messageContent, "%成员%", uidName.get(i));
            messageContent = ReplaceMsg.Replace(messageContent, "%机器人%", subject.getBot().getNick());
            messageContent = ReplaceMsg.Replace(messageContent, "%好感%", FavorLevel(LoveExpList.get(i)));
            messageContent = ReplaceMsg.Replace(messageContent, "%后缀%", suffix);
            messageContent = ReplaceMsg.Replace(messageContent, "%排名%", (i + 1));


            Message message = new PlainText(messageContent);

            // 添加消息到转发器
            forwardMessage.add(uidName.get(i), "第" + (i + 1) + "名" + config.getSuffix(), message);
        }

        // 发送转发的消息
        subject.sendMessage(forwardMessage.build());
    }


    public static void cheatLove(User user, int exp, Contact subject) {
        sqlUtil.addLove(exp, user.getId());
        subject.sendMessage(new At(user.getId()).plus("\n作弊成功，增加 " + exp + "经验值"));
    }
}


