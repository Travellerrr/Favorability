package cn.travellerr.LoveYou.utils;

import cn.travellerr.Favorability;
import cn.travellerr.utils.Log;
import com.hankcs.hanlp.mining.word2vec.DocVectorModel;
import com.hankcs.hanlp.mining.word2vec.WordVectorModel;
import net.mamoe.mirai.contact.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import static cn.travellerr.Favorability.loveYou;

@Deprecated(since = "2024/12/27 附加功能将不再维护！", forRemoval = false)
public class LoveSqlUtil {

    /**
     * 数据库路径
     */

    private static final String DB_PATH = Favorability.INSTANCE.getDataFolderPath().toAbsolutePath().toString();

    /**
     * 数据库名称
     */
    private static final String DB_NAME = "favorability.db";

    /**
     * 创建名为Love的表
     * @author Travellerr
     * @param conn 数据库连接
     * @throws SQLException 数据库异常
     */
    private static void createLoveTable(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS LoveYou(" +
                "QQ INTEGER," +
                "msg TEXT," +
                "time INTEGER DEFAULT (strftime('%s','now','localtime')))";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        }
    }


    /**
     * 上传该qq号新的对话消息
     * @author Travellerr
     * @param conn 数据库连接
     * @param msg  qq消息
     * @param qq   qq号
     * @throws SQLException 数据库异常
     */
    private static void uploadNewMsg(Connection conn, String msg, long qq) throws SQLException {
        String sql = "INSERT INTO LoveYou (QQ, msg, time) VALUES (?, ?, ?)";
        try (PreparedStatement insertStatement = conn.prepareStatement(sql)) {
            insertStatement.setLong(1, qq);
            insertStatement.setString(2, msg);
            insertStatement.setLong(3, System.currentTimeMillis());

            insertStatement.executeUpdate();
        }
    }

    /**
     * 获取该qq号下所有储存的消息
     * @author Travellerr
     * @param conn 数据库连接
     * @param qq   qq号
     * @return 数据库查询记录
     * @throws SQLException 数据库异常
     */
    private static Map<Integer, UserMsg> getUserAllMsg(Connection conn, long qq) throws SQLException {
        String sql = "SELECT msg, time FROM LoveYou WHERE QQ = ? ORDER BY time ASC";
        try (PreparedStatement insertStatement = conn.prepareStatement(sql)) {
            insertStatement.setLong(1, qq);

            try (ResultSet resultSet = insertStatement.executeQuery()) {
                int index = 0;
                Map<Integer, UserMsg> allMsg = new HashMap<>();
                while (resultSet.next()) {
                    UserMsg userMsg = new UserMsg(resultSet.getString("msg"), resultSet.getLong("time"));
                    allMsg.put(index, userMsg);
                    index++;
                }
                return allMsg;
            }
        }
    }

    /**
     * 获取数据库连接
     * @author Travellerr
     * @return 数据库连接
     * @throws SQLException 数据库异常
     */
    private static Connection getConnection() throws SQLException {
        String url = "jdbc:sqlite:" + Paths.get(LoveSqlUtil.DB_PATH, LoveSqlUtil.DB_NAME);
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            Log.error("出错了~" + e.fillInStackTrace());
        }
        return DriverManager.getConnection(url);
    }

    /**
     * 创建数据库文件
     * @author Travellerr
     */
    private static void createDataBase() {
        Path path = Paths.get(LoveSqlUtil.DB_PATH);
        if (Files.notExists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                Log.error("出错了~", e);
            }
        }

    }

    /**
     * 删除过期的消息数据
     * @author Travellerr
     * @param conn      数据库连接
     * @param qq        用户qq号
     * @param timeStamp 消息时间戳
     * @throws SQLException 数据库异常
     */
    private static void removeExpiredMsg(Connection conn, long qq, long timeStamp) throws SQLException {
        String sql = "DELETE FROM LoveYou WHERE QQ = ? AND time = ?;";

        try (PreparedStatement insertStatement = conn.prepareStatement(sql)) {
            insertStatement.setLong(1, qq);
            insertStatement.setLong(2, timeStamp);

            insertStatement.executeUpdate();
            Log.debug("Delete Message Successful!");
        }
    }


    /**
     * 储存用户消息
     * @author Travellerr
     * @param user QQ用户实例
     * @param msg  QQ消息
     */
    public static void saveMsg(User user, String msg) {
        try {
            createDataBase();
            Connection conn = getConnection();
            createLoveTable(conn);
            uploadNewMsg(conn, msg, user.getId());

            conn.close();
        } catch (Exception e) {
            Log.error("出错了~" + e.fillInStackTrace());
        }
    }

    /**
     * 检查用户消息是否过期，并删除过期消息
     * @author Travellerr
     * @param user QQ用户实例
     */
    public static void checkUserMsgTime(User user) {
        try {
            createDataBase();
            Connection conn = getConnection();
            createLoveTable(conn);
            Map<Integer, UserMsg> map = getUserAllMsg(conn, user.getId());
            for (int index : map.keySet()) {
                UserMsg userMsg = map.get(index);
                if (!userMsg.isTimesUp()) {
                    break;
                }
                removeExpiredMsg(conn, user.getId(), userMsg.getTimeStamp());
            }


            conn.close();
        } catch (Exception e) {

            Log.error("出错了~" + e.fillInStackTrace());
        }
    }

    /**
     * 比对用户消息与数据库内存储消息是否相似
     * @author Travellerr
     * @param user QQ用户实例
     * @param msg  QQ消息
     * @return 两则消息是否相似
     */
    public static boolean isSimilarityMsg(User user, String msg) {
        try {

            WordVectorModel wordVectorModel = new WordVectorModel(
                    DB_PATH
                            + loveYou.getMsgPath()
            );

            DocVectorModel compareModel = new DocVectorModel(wordVectorModel);
            createDataBase();
            Connection conn = getConnection();
            createLoveTable(conn);
            Map<Integer, UserMsg> map = getUserAllMsg(conn, user.getId());
            for (int index : map.keySet()) {
                UserMsg userMsg = map.get(index);
                float similarityValue = compareModel.similarity(msg, userMsg.getMsg());
                Log.debug("分析消息相似度- 与 " + userMsg.getMsg() + " 分析，相似度为 " + similarityValue);
                if (similarityValue >= 0.5) {
                    return true;
                }
            }
            conn.close();
        } catch (Exception e) {
            Log.error("出错了~" + e.fillInStackTrace());
        }
        return false;
    }



}
