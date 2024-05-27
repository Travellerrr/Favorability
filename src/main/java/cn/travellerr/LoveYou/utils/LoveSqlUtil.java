package cn.travellerr.LoveYou.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LoveSqlUtil {
    private static void createLoveTable(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS LoveYou(" +
                "QQ INTEGER," +
                "msg TEXT," +
                "Time INTEGER DEFAULT (strftime('%s','now','localtime')))";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
        }
    }
}
