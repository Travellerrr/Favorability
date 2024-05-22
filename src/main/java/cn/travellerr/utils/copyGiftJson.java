package cn.travellerr.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class copyGiftJson {
    public static void copy() {
        Path path = Path.of("./data/cn.travellerr.Favorability/gift.json");
        if (!Files.exists(path)) {
            path = Path.of("./data/cn.travellerr.Favorability");
            String sourcePath = "gift.json";
            String destinationPath = "./data/cn.travellerr.Favorability/gift.json";
            try {
                Files.createDirectories(path);
                // 获取类资源文件的输入流
                InputStream inputStream = copyGiftJson.class.getClassLoader().getResourceAsStream(sourcePath);
                if (inputStream == null) {
                    Log.error("出错啦~: 未找到Gift资源");
                    return;
                }

                // 拷贝输入流到目标路径
                Path destination = Path.of(destinationPath);
                Files.copy(inputStream, destination, StandardCopyOption.REPLACE_EXISTING);
                Log.info("礼物Json文件拷贝成功！");
            } catch (IOException e) {
                Log.error("出错啦~", e);
            }
        }
    }
}