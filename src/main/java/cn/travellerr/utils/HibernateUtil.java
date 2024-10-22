package cn.travellerr.utils;

import cn.chahuyun.hibernateplus.Configuration;
import cn.chahuyun.hibernateplus.DriveType;
import cn.chahuyun.hibernateplus.HibernatePlusService;
import cn.travellerr.Favorability;
import cn.travellerr.config.SqlConfig;

import java.nio.file.Path;

public class HibernateUtil {
    /**
     * Hibernate初始化
     *
     * @param favorability 插件
     * @author Moyuyanli
     */
    public static void init(Favorability favorability) {
        SqlConfig config = Favorability.sqlConfig;

        Configuration configuration = HibernatePlusService.createConfiguration(favorability.getClass());
        configuration.setPackageName("cn.travellerr.entity");

        DriveType dataType = config.getDataType();
        configuration.setDriveType(dataType);
        Path dataFolderPath = favorability.getDataFolderPath();
        switch (dataType) {
            case MYSQL:
                configuration.setAddress(config.getMysqlUrl());
                configuration.setUser(config.getMysqlUser());
                configuration.setPassword(config.getMysqlPassword());
                break;
            case H2:
                configuration.setAddress(dataFolderPath.resolve("Favorability.h2").toString());
                break;
            case SQLITE:
                configuration.setAddress(dataFolderPath.resolve("Favorability").toString());
                break;
        }

        HibernatePlusService.loadingService(configuration);
    }
}
