package cn.travellerr.utils;

import cn.travellerr.config.PluginConfig;
import cn.travellerr.entity.Favourite;
import cn.travellerr.title.LoveTitleManager;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.ForwardMessageBuilder;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.PlainText;

import java.util.*;
import java.util.stream.Collectors;

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
        Favourite userInfo = SqlUtilNew.getInfo(user.getId());
        long exp = userInfo == null ? 0 : userInfo.getExp();
        String originMsg = msgConfig.getCheckLove();
        long level = FavorLevel(exp);
        originMsg = ReplaceMsg.Replace(originMsg, "%成员%", user.getNick());
        originMsg = ReplaceMsg.Replace(originMsg, "%机器人%", bot.getNick());
        originMsg = ReplaceMsg.Replace(originMsg, "%好感度%", level);
        originMsg = ReplaceMsg.Replace(originMsg, "%好感经验%", exp);
        originMsg = ReplaceMsg.Replace(originMsg, "%好感信息%", FavorMsg(FavorLevel(exp), user.getNick()));
        subject.sendMessage(new At(user.getId()).plus("\n").plus(originMsg));

        // 壶言经济启用检查
        if (config.getEconomyName() != 0) return;
        String titleMsg = LoveTitleManager.addLoveTitle(user, (int) level);
        if (titleMsg != null) {
            subject.sendMessage(new At(user.getId()).plus("\n").plus(titleMsg));
        }

        //subject.sendMessage(new At(user.getId()).plus(String.format("\n%s对你的好感度为: %d\n%s", bot.getNick(), FavorLevel(exp), FavorMsg(FavorLevel(exp), user.getNick()))));
    }

    /**
     * 合成的好感信息
     * @author Travellerr
     * @param level  好感等级
     * @param SenseiName 用户后缀
     * @return 合成完毕的好感信息
     */
    private static String FavorMsg(long level, String SenseiName) {
        PluginConfig config = PluginConfig.INSTANCE;
        String msg;
        if (level > 0) {
            List<String> msgList = msgConfig.getLoveMessage();
            int index = (int) (level / config.getChangeLevel());
            if (index > msgList.size() - 1) index = msgList.size() - 1;
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
    public static long FavorLevel(long exp) {
        if (exp < 0) {
            return -Math.abs(exp) / config.getNegativeExp();
        }

        int[] thresholds = config.getLevelList();
        long level = 0;
        for (int threshold : thresholds) {
            if (exp >= threshold) {
                level++;
            } else {
                break;
            }
        }

        if (level >= 49) {
            level = 49 + (exp - 29175) / config.getPerLevel();
        }

        return level;
    }

    /**
     * 获取对应群聊好感排行
     * @author Travellerr
     * @param subject 联系对象
     * @param group 群聊
     */
    public static void getLoveList(Contact subject, Group group) {
        // 获取数据
        ArrayList<Favourite> userInfos = SqlUtilNew.getAllInfo();

        ArrayList<Long> uidName = new ArrayList<>();
        userInfos.forEach(user -> uidName.add(user.getQQ()));
        Map<Long, Long> exps = new HashMap<>();
        userInfos.forEach(user -> exps.put(user.getQQ(), user.getExp()));

        // 构建消息转发器
        ForwardMessageBuilder forwardMessage = new ForwardMessageBuilder(subject);

        // 获取群成员列表
        List<Member> users = group.getMembers().stream()
                .filter(member -> uidName.contains(member.getId()))
                .sorted(Comparator.comparingInt(member -> uidName.indexOf(member.getId())))
                .limit(100)
                .collect(Collectors.toList());

        // 遍历并发送消息
        users.forEach(user -> {
            String nickname = user.getNameCard();
            if (nickname.isEmpty()) {
                nickname = user.getNick();
            }
            String messageContent = msgConfig.getGroupLoveMsg();
            messageContent = ReplaceMsg.Replace(messageContent, "%成员%", nickname);
            messageContent = ReplaceMsg.Replace(messageContent, "%后缀%", config.getSuffix());
            messageContent = ReplaceMsg.Replace(messageContent, "%机器人%", subject.getBot().getNick());
            messageContent = ReplaceMsg.Replace(messageContent, "%好感%", FavorLevel(exps.get(user.getId())));
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
        ArrayList<Favourite> userInfos = SqlUtilNew.getAllInfo();

        ArrayList<Long> uidName = new ArrayList<>();
        userInfos.forEach(user -> uidName.add(user.getQQ()));
        Map<Long, Long> exps = new HashMap<>();
        userInfos.forEach(user -> exps.put(user.getQQ(), user.getExp()));

        // 构建消息转发器
        ForwardMessageBuilder forwardMessage = new ForwardMessageBuilder(subject);

        // 确定循环上限
        int size = Math.min(exps.size(), 100);
        String suffix = config.getSuffix();
        // 遍历并发送消息
        for (int i = 0; i < size; i++) {
            Long userId = uidName.get(i);
            String messageContent = msgConfig.getTotalLoveMsg() + "\n (" + uidName.get(i) + ")";
            messageContent = ReplaceMsg.Replace(messageContent, "%成员%", userId);
            messageContent = ReplaceMsg.Replace(messageContent, "%机器人%", subject.getBot().getNick());
            messageContent = ReplaceMsg.Replace(messageContent, "%好感%", FavorLevel(exps.get(userId)));
            messageContent = ReplaceMsg.Replace(messageContent, "%后缀%", suffix);
            messageContent = ReplaceMsg.Replace(messageContent, "%排名%", (i + 1));


            Message message = new PlainText(messageContent);

            // 添加消息到转发器
            forwardMessage.add(uidName.get(i), "第" + (i + 1) + "名" + " " + config.getSuffix(), message);
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
    public static void cheatLove(User user, Long exp, Contact subject) {
        SqlUtilNew.addLove(user, exp);
        subject.sendMessage(new At(user.getId())
                .plus("\n作弊成功，增加 " + exp + "经验值\n折合等级约为 ")
                .plus(FavorLevel(exp) + " 级\n，现在等级为")
                .plus(FavorLevel(SqlUtilNew.getInfo(user.getId()).getExp()) + " 级")
        );
    }
}


