package cn.travellerr.utils;

import net.mamoe.mirai.Mirai;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;

public class sqlUtil {

    static String directory = "./data/cn.travellerr.Favorability/";
    public static  void startMake(long qqNumber, int money) {
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

    private static void createTable(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS Favourite(ID INTEGER PRIMARY KEY," +
                "QQ INTEGER," +
                "exp INTEGER,"+
                "favor INTEGER," +
                "level INTEGER," +
                "Time TEXT DEFAULT(strftime('%Y-%m-%d %H:%M:%S','now','localtime'))," +
                "isMaking INTEGER)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
        }
    }

}
