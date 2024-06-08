package cn.travellerr.utils;

import cn.travellerr.config.PluginConfig;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.ForwardMessageBuilder;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.PlainText;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static cn.travellerr.Favorability.config;
import static cn.travellerr.Favorability.msgConfig;


public class FavorUtil {

    /**
     * 查看用户好感度
     *
     * @param subject 联系对象
     * @param user    用户
     * @param bot     机器人
     * @author Travellerr
     */
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

    /**
     * 合成的好感信息
     * @author Travellerr
     * @param level  好感等级
     * @param SenseiName 用户后缀
     * @return 合成完毕的好感信息
     */
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


    /**
     * 计算好感等级
     * @author Travellerr
     * @param exp 好感经验
     * @return 好感等级
     */
    private static int FavorLevel(int exp) {
        if (exp < 0) {
            return -Math.abs(exp) / config.getNegativeExp();
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

    /**
     * 将未知 列表 转换为 Integer类型 列表
     * @author ChatGPT
     * @param list 未知列表
     * @return 整数列表
     */
    private static List<Integer> castIntList(List<?> list) {
        List<Integer> result = new ArrayList<>();
        for (Object element : list) {
            result.add((Integer) element);
        }
        return result;
    }

    /**
     * 将未知 列表 转换为 Long 列表
     * @author ChatGPT
     * @param list 未知列表
     * @return 长整数列表
     */
    private static List<Long> castLongList(List<?> list) {
        List<Long> result = new ArrayList<>();
        for (Object element : list) {
            result.add((Long) element);
        }
        return result;
    }

    /**
     * 获取对应群聊好感排行
     * @author Travellerr
     * @param subject 联系对象
     * @param group 群聊
     */
    public static void getLoveList(Contact subject, Group group) {
        // 获取数据
        Map<String, List<?>> info = sqlUtil.getListSql();
        List<Integer> LoveExpList = castIntList(info.get("expList"));
        List<Long> uidName = castLongList(info.get("uidName"));
        Map<Long, Integer> userLoveList = IntStream.range(0, Math.min(uidName.size(), LoveExpList.size()))
                .boxed()
                .collect(Collectors.toMap(uidName::get, LoveExpList::get));

        // 构建消息转发器
        ForwardMessageBuilder forwardMessage = new ForwardMessageBuilder(subject);

        // 获取群成员列表
        List<User> users = group.getMembers().stream()
                .filter(member -> uidName.contains(member.getId()))
                .sorted(Comparator.comparingInt(member -> uidName.indexOf(member.getId())))
                .limit(100)
                .collect(Collectors.toList());

        // 遍历并发送消息
        users.forEach(user -> {
            String nickname = user.getNick();
            String messageContent = msgConfig.getGroupLoveMsg();
            messageContent = ReplaceMsg.Replace(messageContent, "%成员%", nickname);
            messageContent = ReplaceMsg.Replace(messageContent, "%后缀%", config.getSuffix());
            messageContent = ReplaceMsg.Replace(messageContent, "%机器人%", subject.getBot().getNick());
            messageContent = ReplaceMsg.Replace(messageContent, "%好感%", FavorLevel(userLoveList.get(user.getId())));
            Message message = new PlainText(messageContent);
            forwardMessage.add(user, message);
        });

        // 发送转发的消息
        subject.sendMessage(forwardMessage.build());
    }


    /**
     * 获取数据库中全部用户排行榜
     * @author Travellerr
     * @param subject 联系对象
     */
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


    /**
     * 好感作弊 <i>仅限调试用！</i>
     * @author Travellerr
     * @param user 用户
     * @param exp 增减经验
     * @param subject 联系对象
     */
    public static void cheatLove(User user, int exp, Contact subject) {
        sqlUtil.addLove(exp, user.getId());
        subject.sendMessage(new At(user.getId())
                .plus("\n作弊成功，增加 " + exp + "经验值\n折合等级约为 ")
                .plus(FavorLevel(exp) + " 级\n，现在等级为")
                .plus(FavorLevel(sqlUtil.getExp(user.getId())) + " 级")
        );
    }
}


