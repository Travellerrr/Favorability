package cn.travellerr.utils;

import net.mamoe.mirai.contact.AvatarSpec;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.data.UserProfile;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.utils.ExternalResource;

import java.net.URL;
import java.time.Year;

public class wtfUtil {
    public static void use(MessageEvent event) {
        try {
            Contact subject = event.getSubject();
            User user = event.getSender();
            UserProfile profile = user.queryProfile();
            int age = profile.getAge();
            String email = profile.getEmail();
            String nickName = profile.getNickname();
            String sign = profile.getSign();
            UserProfile.Sex sex = profile.getSex();
            int QLevel = profile.getQLevel();
            Year year = Year.now();
            ExternalResource resource = ExternalResource.create(new URL(user.getAvatarUrl(AvatarSpec.LARGE)).openStream());
            Image avatar = subject.uploadImage(resource);
            MessageChainBuilder messages = new MessageChainBuilder();
            messages.append(new At(user.getId()));
            messages.append("\n");
            messages.append(avatar);
            messages.append("\n姓名：");
            messages.append(nickName);
            messages.append("\n性别：");
            messages.append(sexToString(sex));
            messages.append("\n年龄：");
            messages.append(String.valueOf(age));
            messages.append("\n出生日期：");
            messages.append(String.valueOf(year.getValue() - age));
            messages.append("\n等级：");
            messages.append(String.valueOf(QLevel));
            messages.append("\n个性签名：");
            if (sign.isEmpty()) {
                messages.append("无");
            } else {
                messages.append(sign);
            }
            messages.append("\n身份证号：");
            messages.append(String.valueOf(user.getId()));
            if (!email.isEmpty()) {
                messages.append("\n联系方式：");
                messages.append(email);
            }
            subject.sendMessage(messages.build());
            resource.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String sexToString(UserProfile.Sex sex) {
        if (sex == UserProfile.Sex.MALE) return "男";
        if (sex == UserProfile.Sex.FEMALE) return "女";
        return "中性";
    }
}
