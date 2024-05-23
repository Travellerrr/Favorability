package cn.travellerr.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import static cn.travellerr.Favorability.version;

public class checkLatestVersion {
    public static void init() {
        try {
            URL url = new URL("https://api.github.com/repos/Travellerrr/Favorability/releases/latest");
            InputStream stream = url.openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String msg;
            StringBuilder response = new StringBuilder();
            while ((msg = reader.readLine()) != null) {
                response.append(msg);
            }
            reader.close();
            JsonObject json = (JsonObject) JsonParser.parseString(response.toString());
            String newVersion = getStringFromJson(json, "tag_name");
            String updateMsg = getStringFromJson(json, "body");
            if (newVersion != null) {
                if (!newVersion.contains(version)) {
                    Log.warning(" 发现最新版本！版本：" + newVersion);


                    if (updateMsg != null) {
                        updateMsg = updateMsg.replace("#", "");
                        updateMsg = updateMsg.replace("\r\n", " ");
                        Log.warning(updateMsg);
                    } else {
                        Log.error(" 无法获取更新日志！");
                    }
                    return;
                }
                Log.info(" 已是最新版本！版本: " + version);
            } else {
                Log.error(" 无法获取最新版本号！");
                return;
            }
            stream.close();
        } catch (Exception e) {
            Log.error(e.fillInStackTrace().getMessage());
        }
    }

    private static String getStringFromJson(JsonObject jsonObject, String key) {
        JsonElement jsonElement = jsonObject.get(key);
        return jsonElement != null ? jsonElement.getAsString() : null;
    }
}
