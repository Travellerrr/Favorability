package cn.travellerr.make;

import cn.hutool.core.util.RandomUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.Scanner;
import java.util.Objects;

public class getPng {
    static String name;
    static String Describe;
    static String level;
    static int love;
    static String url;
    private static String url(int id, String url){
        if(id >= 38) return "https://cdnimg.gamekee.com/wiki2.0/images/w_300/h_237/829/43637/2022/6/"+ url;
        return "https://cdnimg.gamekee.com/wiki2.0/images/w_304/h_240/829/43637/2022/6/" + url;
    }

    public static void message(){
        try (InputStream inputStream = getPng.class.getClassLoader().getResourceAsStream("gift.json");
             Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8)) {
            if (scanner.hasNext()) {
                String json = scanner.useDelimiter("\\A").next();
                JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
                int star = RandomUtil.randomInt(1,40);
                int id;
                if(star == 40) {id = RandomUtil.randomInt(39,50);}
                else {id = RandomUtil.randomInt(1,38);}
                if (jsonObject.has(String.valueOf(id))) {
                    JsonObject giftObject = jsonObject.getAsJsonObject(String.valueOf(id));
                        name = getStringFromJson(giftObject, "Name");
                        Describe = getStringFromJson(giftObject, "Describe");
                        int tempLevel = Integer.parseInt(Objects.requireNonNull(getStringFromJson(giftObject, "Level")));
                        level = getStar(tempLevel);
                        love = Integer.parseInt(Objects.requireNonNull(getStringFromJson(giftObject, "Love")));
                        url = url(id, getStringFromJson(giftObject, "Url"));

                }
            }
        } catch (IOException e) {
            throw new RuntimeException("无法找到或读取jrys.json文件", e);
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
