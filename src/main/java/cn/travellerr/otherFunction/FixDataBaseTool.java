package cn.travellerr.otherFunction;

import cn.travellerr.Favorability;
import cn.travellerr.entity.Favourite;
import cn.travellerr.utils.FavouriteManager;

import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;

public class FixDataBaseTool {

    static String directory = Favorability.INSTANCE.getDataFolderPath().toString();

    public static Boolean fixDataBase() {
        ArrayList<Favourite> oldData = getInfoFromOldDataBase();

        if (oldData.get(0) == null) {
            return false;
        }

        oldData.forEach(FavouriteManager::saveData);

        return true;
    }


    private static ArrayList<Favourite> getInfoFromOldDataBase() {

        String dbName = "favorability.db"; // 数据库文件名
        String dbPath = Paths.get(directory, dbName).toString();
        String url = "jdbc:sqlite:" + dbPath;
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("出错了~", e);
        }
        try (Connection conn = DriverManager.getConnection(url)) {
            ArrayList<Favourite> favouriteArrayList = new ArrayList<>();
            String checkTableSql = "SELECT name FROM sqlite_master WHERE type='table' AND name='Favourite'";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkTableSql)) {
                ResultSet rs = checkStmt.executeQuery();
                if (!rs.next()) {
                    // Table does not exist, return ArrayList with a single value "null"
                    favouriteArrayList.add(null);
                    return favouriteArrayList;
                }
            }

            String selectSql = "SELECT * FROM Favourite";
            try (PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {
                ResultSet rs = selectStmt.executeQuery();
                while (rs.next()) {
                    long qqNumber = rs.getLong("QQ");
                    long exp = rs.getLong("exp");
                    long makeTime = rs.getLong("makeTime");
                    int makeLevel = rs.getInt("itemLevel");
                    boolean isMaking = rs.getBoolean("isMaking");
                    long startMakeTime = rs.getLong("Time");
                    favouriteArrayList.add(new Favourite(qqNumber, null, exp, makeTime, makeLevel, isMaking, new Date(startMakeTime * 1000), new Date()));
                }
            }

            return favouriteArrayList;
        } catch (Exception e) {
            throw new RuntimeException("出错了~", e);
        }
    }
}
