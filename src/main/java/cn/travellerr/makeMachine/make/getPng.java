package cn.travellerr.makeMachine.make;

import cn.hutool.core.util.RandomUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Scanner;

public class getPng {
    static String name;
    static String Describe;
    static String level;
    static int love;
    static String url;
    private static String url(int id, String url){
        if (!url.startsWith("http")) {
            if (id >= 38) return "https://cdnimg.gamekee.com/wiki2.0/images/w_300/h_237/829/43637/2022/6/" + url;
            return "https://cdnimg.gamekee.com/wiki2.0/images/w_304/h_240/829/43637/2022/6/" + url;
        }
        return url;
    }

    public static void message(int itemLevel) {
        try (InputStream inputStream = getPng.class.getClassLoader().getResourceAsStream("gift.json")) {
            if (inputStream != null) {
                try (Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8)) {
                    if (scanner.hasNext()) {
                        // 读取JSON文件内容并解析为JsonObject
                        String json = scanner.useDelimiter("\\A").next();
                        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();

                        int id;
                        if (itemLevel == 40) {
                            id = RandomUtil.randomInt(39, 50);
                        } else {
                            id = RandomUtil.randomInt(1, 38);
                        }

                        // 检查是否存在对应id的数据
                        if (jsonObject.has(String.valueOf(id))) {
                            JsonObject giftObject = jsonObject.getAsJsonObject(String.valueOf(id));

                            // 从Json中获取数据并赋值给相应变量
                            name = getStringFromJson(giftObject, "Name");
                            Describe = getStringFromJson(giftObject, "Describe");
                            int tempLevel = Integer.parseInt(Objects.requireNonNull(getStringFromJson(giftObject, "Level")));
                            level = getStar(tempLevel);
                            love = Integer.parseInt(Objects.requireNonNull(getStringFromJson(giftObject, "Love")));
                            url = url(id, Objects.requireNonNull(getStringFromJson(giftObject, "Url")));
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("无法找到或读取gift.json文件", e);
        }
    }


    private static String getStar(int level) {
        return "☆".repeat(Math.max(0, level));
    }
    private static String getStringFromJson(JsonObject jsonObject, String key) {
        JsonElement jsonElement = jsonObject.get(key);
        return jsonElement != null ? jsonElement.getAsString() : null;
    }
}
