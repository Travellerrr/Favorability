package cn.travellerr.utils;

import cn.hutool.core.util.RandomUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class sqlUtil {
    public static int itemLevel;
    public static boolean isMaking;
    public static boolean timesUp;
    public static int needTime;
    static String directory = "./data/cn.travellerr.Favorability/";

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
            if (isMaking) return;
            int chance = RandomUtil.randomInt(1, 100);
            itemLevel = money >= 200 ? (chance >= 75 ? 3 : 2) : (chance >= 95 ? 3 : 2);
            updateData(conn, qqNumber, time, itemLevel);
        } catch (SQLException e) {
            throw new RuntimeException("出错了~", e);
        }
    }


    private static void createDirectory(String directory) {
        Path path = Paths.get(directory);
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            Log.error("出错了~", e);
        }
    }

    private static boolean isQQNumberNew(Connection conn, long qqNumber) throws SQLException {
        String selectSql = "SELECT COUNT(*) FROM Favourite WHERE QQ = ?";
        try (PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {
            selectStmt.setLong(1, qqNumber);
            try (ResultSet rs = selectStmt.executeQuery()) {
                return rs.next() && rs.getInt(1) == 0;
            }
        }
    }

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

    private static boolean timesUp(Connection conn, long qqNumber, boolean clear) throws SQLException {
        String getSql = "SELECT Time, makeTime FROM Favourite WHERE QQ = ?";
        try (PreparedStatement selectStmt = conn.prepareStatement(getSql)) {
            selectStmt.setLong(1, qqNumber);
            try (ResultSet rs = selectStmt.executeQuery()) {
                if (rs.next()) {
                    long time = rs.getLong("Time");
                    long makeTime = rs.getLong("makeTime");
                    long currentTimeStamp = System.currentTimeMillis() / 1000;
                    System.out.println(currentTimeStamp);
                    System.out.println(time);
                    needTime = (int) (makeTime - (currentTimeStamp - time)) / 60;
                    if (currentTimeStamp - time >= makeTime) {
                        if (clear) {
                            clearQueue(conn, qqNumber);
                        }
                        return true;
                    }


                    // 在这里处理获取到的Time和makeTime字段的值
                }
            }
        }
        return false;
    }

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
    private static void createTable(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS Favourite(ID INTEGER PRIMARY KEY," +
                "QQ INTEGER," +
                "exp INTEGER DEFAULT 0," +
                //"favor INTEGER DEFAULT 0," +
                "Time INTEGER DEFAULT(strftime('%s','now','localtime'))," +
                "makeTime INTEGER," +
                "itemLevel INTEGER," +
                "isMaking INTEGER)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
        }
    }

    private static void insertData(Connection conn, long qqNumber) {
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

    private static void updateData(Connection conn, long qqNumber, long makeTime, int itemLevel) {
        String updateSql = "UPDATE Favourite SET makeTime = ?, itemLevel = ?, isMaking=?, Time = ? WHERE QQ = ?";
        try (PreparedStatement insertStmt = conn.prepareStatement(updateSql)) {
            // 设置参数值
            insertStmt.setLong(1, makeTime);
            insertStmt.setInt(2, itemLevel);
            insertStmt.setBoolean(3, true);
            insertStmt.setLong(4, System.currentTimeMillis() / 1000);
            insertStmt.setLong(5, qqNumber);
            // 执行插入操作
            insertStmt.executeUpdate();
        } catch (SQLException e) {
            // 插入失败时的异常处理
            throw new RuntimeException("出错了~", e);
        }
    }

    public static void updateInfo(long qqNumber, boolean clear) {
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
            isMaking = isMaking(conn, qqNumber);
            timesUp = timesUp(conn, qqNumber, clear);
            itemLevel = getItemLevel(conn, qqNumber);
        } catch (SQLException e) {
            throw new RuntimeException("出错了~", e);
        }
    }

    public static void addLove(int exp, long qqNumber) {
        String getSql = "SELECT exp FROM Favourite WHERE QQ = ?";
        String addSql = "UPDATE Favourite SET exp = ? WHERE QQ = ?";
        String dbName = "favorability.db"; // 数据库文件名
        String dbPath = Paths.get(directory, dbName).toString();
        String url = "jdbc:sqlite:" + dbPath;
        int before = 0;
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
                        before = rs.getInt("exp");
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

    public static int getExp(long qqNumber) {
        String getSql = "SELECT exp FROM Favourite WHERE QQ = ?";
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
