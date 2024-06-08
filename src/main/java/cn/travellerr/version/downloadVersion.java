package cn.travellerr.version;

import cn.travellerr.Favorability;
import cn.travellerr.utils.Log;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;


/**
 * @author Travellerr
 * @implNote 是这样的，插件有用户用就行了，而我写代码的就考虑的多了，有没有用，脑子坏没坏……
 * @deprecated 2024-06-08
 */
@Deprecated(since = "2024-06-08")
public class downloadVersion {

    public static Boolean haveFile = false;
    public static File file;

    public static void downloadLatest(URL url, String name) {
        try (InputStream downloadFile = url.openStream()) {
            Path path = Favorability.INSTANCE.getDataFolderPath();
            if (Files.notExists(path)) {
                Files.createDirectories(path);
            }

            file = new File(path.toFile(), name);
            Files.copy(downloadFile, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            Log.warning("下载完毕！请手动替换，文件在./data/cn.travellerr.Favorability 下");
            haveFile = true;
        } catch (Exception e) {
            Log.error(e.fillInStackTrace().getMessage());
        }
    }
}
