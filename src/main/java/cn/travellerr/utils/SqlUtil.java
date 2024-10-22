package cn.travellerr.utils;

import cn.hutool.core.util.RandomUtil;
import cn.travellerr.Favorability;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Deprecated
public class SqlUtil {
   /* public static int itemLevel;
    public static boolean isMaking;
    public static boolean timesUp;
    public static int needTime;*/

    /**
     * 制作物品实例
     */
    public static MakingItem makingItem;

    /**
     * data文件夹路径
     */
    static String directory = Favorability.INSTANCE.getDataFolderPath().toString();

    /**
     * 判断队列里是否有物品，开始制造
     *
     * @param qqNumber 用户QQ号
     * @param money    金币数量
     * @param time     制造时长
     * @author Travellerr
     */
    public static void startMake(long qqNumber, int money, long time) {
        String dbName = "favorability.db"; // 数据库文件名
        String dbPath = Paths.get(directory, dbName).toString();
        String url = "jdbc:sqlite:" + dbPath;
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("出错了~", e);
        }
        createDirectory(directory);
        try (Connection conn = DriverManager.getConnection(url)) {
            createTable(conn);
            if (isQQNumberNew(conn, qqNumber)) {
                insertData(conn, qqNumber);
            }
            if (makingItem.isMaking()) return;
            int chance = RandomUtil.randomInt(1, 5001);
            int itemLevel = chance <= money ? 3 : 2;
            //int itemLevel = money >= 200 ? (chance >= 75 ? 3 : 2) : (chance >= 95 ? 3 : 2);
            makingItem.setItemLevel(itemLevel);
            updateData(conn, qqNumber, time, itemLevel);
        } catch (SQLException e) {
            throw new RuntimeException("出错了~", e);
        }
    }

    /**
     * 创建data文件夹
     * @author Travellerr
     * @param directory 文件夹路径
     */
    private static void createDirectory(String directory) {
        Path path = Paths.get(directory);
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            Log.error("出错了~", e);
        }
    }

    /**
     * QQ号是否为新的
     * @author Travellerr
     * @param conn 数据库链接
     * @param qqNumber 用户QQ号
     * @return 是否为最新
     * @throws SQLException 数据库异常
     */
    private static boolean isQQNumberNew(Connection conn, long qqNumber) throws SQLException {
        String selectSql = "SELECT COUNT(*) FROM Favourite WHERE QQ = ?";
        try (PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {
            selectStmt.setLong(1, qqNumber);
            try (ResultSet rs = selectStmt.executeQuery()) {
                return rs.next() && rs.getInt(1) == 0;
            }
        }
    }

    /**
     * 该用户是否在制造物品
     * @author Travellerr
     * @param conn 数据库链接
     * @param qqNumber 用户QQ号
     * @return 是否在制造物品
     * @throws SQLException 数据库异常
     */
    private static boolean isMaking(Connection conn, long qqNumber) throws SQLException {
        String checkSql = "SELECT isMaking FROM Favourite WHERE QQ = ?";
        try (PreparedStatement selectStmt = conn.prepareStatement(checkSql)) {
            selectStmt.setLong(1, qqNumber);
            try (ResultSet rs = selectStmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("isMaking");
                }
            }
        }
        return false;
    }

    /**
     * 获取物品等级
     * @author Travellerr
     * @param conn 数据库链接
     * @param qqNumber 用户QQ号
     * @return 返回获取到的物品等级
     * @throws SQLException 数据库异常
     */
    private static int getItemLevel(Connection conn, long qqNumber) throws SQLException {
        String checkSql = "SELECT itemLevel FROM Favourite WHERE QQ = ?";
        try (PreparedStatement selectStmt = conn.prepareStatement(checkSql)) {
            selectStmt.setLong(1, qqNumber);
            try (ResultSet rs = selectStmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("itemLevel");
                }
            }
        }
        return 0;
    }

    /**
     * 是否制造时间已到
     * @author Travellerr
     * @param conn 数据库连接
     * @param qqNumber 用户QQ号
     * @param clear 是否清理队列
     * @return 是否用户的制造时间到
     * @throws SQLException 数据库异常
     */
    private static List<Object> timesUp(Connection conn, long qqNumber, boolean clear, boolean isQuickly) throws SQLException {
        List<Object> list = new ArrayList<>();
        String getSql = "SELECT Time, makeTime FROM Favourite WHERE QQ = ?";
        try (PreparedStatement selectStmt = conn.prepareStatement(getSql)) {
            selectStmt.setLong(1, qqNumber);
            try (ResultSet rs = selectStmt.executeQuery()) {
                if (rs.next()) {
                    long time = rs.getLong("Time");
                    long makeTime = rs.getLong("makeTime");
                    long currentTimeStamp = System.currentTimeMillis() / 1000;
                    Log.debug(currentTimeStamp);
                    Log.debug(time);
                    if (currentTimeStamp - time >= makeTime) {
                        if (clear) {
                            clearQueue(conn, qqNumber);
                        }
                        list.add(true);
                    } else if (isQuickly) {
                        if (clear) {
                            clearQueue(conn, qqNumber);
                        }
                        list.add(true);
                    } else {
                        list.add(false);
                    }
                    list.add((int) (makeTime - (currentTimeStamp - time)) / 60);


                }
            }
        }
        if (list.isEmpty()) {
            list.add(true);
            list.add(0);
        }

        return list;
    }

    /**
     *  清理用户制造队列
     * @author Travellerr
     * @param conn 数据库连接
     * @param qqNumber 用户QQ号
     */
    private static void clearQueue(Connection conn, long qqNumber) {
        String updateSql = "UPDATE Favourite SET Time = ?, makeTime = ?, itemLevel = ?, isMaking=? WHERE QQ = ?";
        try (PreparedStatement insertStmt = conn.prepareStatement(updateSql)) {
            // 设置参数值
            insertStmt.setLong(1, 0);
            insertStmt.setLong(2, 0);
            insertStmt.setInt(3, 0);
            insertStmt.setBoolean(4, false);
            insertStmt.setLong(5, qqNumber);
            // 执行插入操作
            insertStmt.executeUpdate();
        } catch (SQLException e) {
            // 插入失败时的异常处理
            throw new RuntimeException("出错了~", e);
        }
    }

    /**
     * 创建Favourite主表
     * @param conn 数据库链接
     * @throws SQLException 抛出数据库异常
     * @author Travellerr
     */
    private static void createTable(Connection conn) throws SQLException {
        // SQL语句用于创建Favourite表
        String sql = "CREATE TABLE IF NOT EXISTS Favourite(ID INTEGER PRIMARY KEY," +
                "QQ INTEGER," +
                "exp INTEGER DEFAULT 0," +
                "Time INTEGER DEFAULT(strftime('%s','now','localtime'))," +
                "makeTime INTEGER," +
                "itemLevel INTEGER," +
                "isMaking INTEGER)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
        }
    }

    /**
     * 插入数据
     * @param conn 数据库连接
     * @param qqNumber QQ号码
     * @author Travellerr
     */
    private static void insertData(Connection conn, long qqNumber) {
        // SQL语句用于向Favourite表插入数据
        String sql = "INSERT INTO Favourite (QQ) VALUES (?)";
        try (PreparedStatement insertStmt = conn.prepareStatement(sql)) {
            // 设置参数值
            insertStmt.setLong(1, qqNumber);
            // 执行插入操作
            insertStmt.executeUpdate();
        } catch (SQLException e) {
            // 插入失败时的异常处理
            throw new RuntimeException("出错了~", e);
        }
    }

    /**
     * 更新数据
     * @param conn 数据库连接
     * @param qqNumber QQ号码
     * @param makeTime 制作时间
     * @param itemLevel 物品等级
     * @author Travellerr
     */
    private static void updateData(Connection conn, long qqNumber, long makeTime, int itemLevel) {
        // SQL语句用于更新Favourite表数据
        String updateSql = "UPDATE Favourite SET makeTime = ?, itemLevel = ?, isMaking=?, Time = ? WHERE QQ = ?";
        try (PreparedStatement insertStmt = conn.prepareStatement(updateSql)) {
            // 设置参数值
            insertStmt.setLong(1, makeTime);
            insertStmt.setInt(2, itemLevel);
            insertStmt.setBoolean(3, true);
            insertStmt.setLong(4, System.currentTimeMillis() / 1000);
            insertStmt.setLong(5, qqNumber);
            // 执行更新操作
            insertStmt.executeUpdate();
        } catch (SQLException e) {
            // 更新失败时的异常处理
            throw new RuntimeException("出错了~", e);
        }
    }

    /**
     * 更新信息
     * @param qqNumber QQ号码
     * @param clear 是否清除
     * @author Travellerr
     */
    public static void updateInfo(long qqNumber, boolean clear, boolean isQuickly) {
        // 数据库文件路径和连接URL
        String dbName = "favorability.db";
        String dbPath = Paths.get(directory, dbName).toString();
        String url = "jdbc:sqlite:" + dbPath;
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("出错了~", e);
        }
        createDirectory(directory);
        try (Connection conn = DriverManager.getConnection(url)) {
            createTable(conn);
            int level = getItemLevel(conn, qqNumber);
            boolean making = isMaking(conn, qqNumber);
            List<Object> list = timesUp(conn, qqNumber, clear, isQuickly);
            makingItem = new MakingItem(level, making, (boolean) list.get(0), (int) list.get(1));
        } catch (SQLException e) {
            throw new RuntimeException("出错了~", e);
        }
    }

    /**
     * 增加经验值
     * @param exp 经验值
     * @param qqNumber QQ号码
     * @author Travellerr
     */
    public static void addLove(long exp, long qqNumber) {
        // SQL语句用于获取指定QQ号的经验值
        String getSql = "SELECT exp FROM Favourite WHERE QQ = ?";
        // SQL语句用于更新指定QQ号的经验值
        String addSql = "UPDATE Favourite SET exp = ? WHERE QQ = ?";
        // 数据库文件路径和连接URL
        String dbName = "favorability.db";
        String dbPath = Paths.get(directory, dbName).toString();
        String url = "jdbc:sqlite:" + dbPath;
        long before = 0;
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("出错了~", e);
        }
        createDirectory(directory);
        try (Connection conn = DriverManager.getConnection(url)) {
            try (PreparedStatement selectStmt = conn.prepareStatement(getSql)) {
                selectStmt.setLong(1, qqNumber);
                try (ResultSet rs = selectStmt.executeQuery()) {
                    if (rs.next()) {
                        before = rs.getLong("exp");
                        Log.debug("获取到的经验: " + before);
                    }
                }
            }
            try (PreparedStatement insertStmt = conn.prepareStatement(addSql)) {
                insertStmt.setLong(1, before + exp);
                insertStmt.setLong(2, qqNumber);
                insertStmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取经验值
     * @param qqNumber QQ号码
     * @return 经验值
     * @author Travellerr
     */
    public static int getExp(long qqNumber) {
        // SQL语句用于获取指定QQ号的经验值
        String getSql = "SELECT exp FROM Favourite WHERE QQ = ?";
        // 数据库文件路径和连接URL
        String dbName = "favorability.db";
        String dbPath = Paths.get(directory, dbName).toString();
        String url = "jdbc:sqlite:" + dbPath;
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("出错了~", e);
        }
        createDirectory(directory);
        try (Connection conn = DriverManager.getConnection(url)) {
            createTable(conn);
            try (PreparedStatement selectStmt = conn.prepareStatement(getSql)) {
                selectStmt.setLong(1, qqNumber);
                try (ResultSet rs = selectStmt.executeQuery()) {
                    if (rs.next()) {
                        Log.debug(rs.getObject("exp"));
                        return rs.getInt("exp");
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    /**
     * 获取经验值列表
     * @return 包含经验值和QQ号的Map
     * @author Travellerr
     */
    public static Map<String, List<?>> getListSql() {
        // SQL查询语句
        String getSql = "SELECT exp, QQ FROM Favourite ORDER BY exp DESC;";

        // 数据库文件名和路径
        String dbName = "favorability.db";
        String dbPath = Paths.get("./data/cn.travellerr.Favorability/", dbName).toString();
        String url = "jdbc:sqlite:" + dbPath;

        // 存储查询结果的列表
        List<Integer> expList = new ArrayList<>();
        List<Long> uidName = new ArrayList<>();

        try {
            // 加载SQLite驱动
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("出错了~", e);
        }

        try (Connection conn = DriverManager.getConnection(url)) {
            // 创建PreparedStatement并执行查询
            PreparedStatement pstmt = conn.prepareStatement(getSql);
            ResultSet rs = pstmt.executeQuery();

            // 遍历结果集并将数据添加到列表中
            while (rs.next()) {
                expList.add(rs.getInt("exp"));
                uidName.add(rs.getLong("QQ"));
            }

            // 关闭结果集和PreparedStatement
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // 构建包含查询结果的Map
        Map<String, List<?>> information = new HashMap<>();
        information.put("expList", expList);
        information.put("uidName", uidName);

        return information;
    }


}
