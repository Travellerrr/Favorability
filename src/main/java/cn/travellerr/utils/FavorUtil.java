package cn.travellerr.utils;

import cn.travellerr.config.Config;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.At;

import java.nio.file.Paths;
import java.sql.*;
import java.util.List;


public class FavorUtil {
    public static void checkFavor(Contact subject, User user, Bot bot) {
        int exp = sqlUtil.getExp(user.getId());

        subject.sendMessage(new At(user.getId()).plus(String.format("\n%s对你的好感度为: %d\n%s", bot.getNick(), FavorLevel(exp), FavorMsg(FavorLevel(exp), user.getNick()))));
    }

    private static String FavorMsg(int level, String SenseiName) {
        Config config = Config.INSTANCE;
        List<String> msgList = config.getLoveMessage();
        int index = level % config.getChangeLevel();
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

    public static void getList(MessageEvent event) {
        String getSql = "SELECT exp FROM Favourite ORDER BY exp DESC;";
        String dbName = "favorability.db"; // 数据库文件名
        String dbPath = Paths.get("./data/cn.travellerr.Favorability/", dbName).toString();
        String url = "jdbc:sqlite:" + dbPath;
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("出错了~", e);
        }
        try (Connection conn = DriverManager.getConnection(url)) {
            PreparedStatement pstmt = conn.prepareStatement(getSql);
            pstmt.executeUpdate();
            pstmt.close();

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int before = rs.getInt("exp");
            }
            rs.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
