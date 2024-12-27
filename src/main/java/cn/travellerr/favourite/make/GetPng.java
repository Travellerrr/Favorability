package cn.travellerr.favourite.make;

import cn.hutool.core.util.RandomUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Scanner;

public class GetPng {

    /**
     * 物品实例
     */
    static Item item;

    /**
     * 拼接图片链接地址
     *
     * @param id  json文件中礼物的编号
     * @param url 传入的url (完整/不完整)
     * @return 完整的url
     * @author Travellerr
     */
    private static String url(int id, String url){
        if (!url.startsWith("http")) {
            if (id >= 38) return "https://cdnimg.gamekee.com/wiki2.0/images/w_300/h_237/829/43637/2022/6/" + url;
            return "https://cdnimg.gamekee.com/wiki2.0/images/w_304/h_240/829/43637/2022/6/" + url;
        }
        return url;
    }

    /**
     * 创建物品实例
     * @author Travellerr
     * @param itemLevel 物品等级
     */
    public static void message(int itemLevel) {
        try (InputStream inputStream = GetPng.class.getClassLoader().getResourceAsStream("gift.json")) {
            if (inputStream != null) {
                try (Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8)) {
                    if (scanner.hasNext()) {
                        // 读取JSON文件内容并解析为JsonObject
                        String json = scanner.useDelimiter("\\A").next();
                        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();

                        int id;
                        if (itemLevel > 2) {
                            id = RandomUtil.randomInt(39, 51);
                        } else {
                            id = RandomUtil.randomInt(1, 39);
                        }

                        // 检查是否存在对应id的数据
                        if (jsonObject.has(String.valueOf(id))) {
                            JsonObject giftObject = jsonObject.getAsJsonObject(String.valueOf(id));

                            // 从Json中获取数据并赋值给相应变量
                            String name = getStringFromJson(giftObject, "Name");
                            String Describe = getStringFromJson(giftObject, "Describe");
                            int tempLevel = Integer.parseInt(Objects.requireNonNull(getStringFromJson(giftObject, "Level")));
                            String level = getStar(tempLevel);
                            int love = Integer.parseInt(Objects.requireNonNull(getStringFromJson(giftObject, "Love")));
                            String url = url(id, Objects.requireNonNull(getStringFromJson(giftObject, "Url")));

                            item = new Item(name, Describe, level, love, url);

                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("无法找到或读取gift.json文件", e);
        }
    }

    /**
     * 计算物品星值
     * @author Travellerr
     * @param level  物品等级（数字）
     * @return 物品等级（字符串）
     */
    private static String getStar(int level) {
        return "☆".repeat(Math.max(0, level));
    }

    /**
     * 从Json中获取字符串
     * @author Travellerr
     * @param jsonObject Json对象
     * @param key Json键值
     * @return 获取到的字符串
     */
    private static String getStringFromJson(JsonObject jsonObject, String key) {
        JsonElement jsonElement = jsonObject.get(key);
        return jsonElement != null ? jsonElement.getAsString() : null;
    }
}
