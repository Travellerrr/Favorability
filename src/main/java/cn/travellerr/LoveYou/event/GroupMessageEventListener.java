package cn.travellerr.LoveYou.event;

import cn.travellerr.LoveYou.getLove.analyzeText;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public class GroupMessageEventListener extends SimpleListenerHost {

    /**
     * @param event 群组消息
     * @author Travellerr
     * @implNote 实现对@机器人消息的情感处理
     */
    @EventHandler
    public void onMessage(@NotNull GroupMessageEvent event) {
        if (event.getGroup().getId() == 616331486 || event.getGroup().getId() == 756552150) {
            String atMsg = Pattern.quote(new At(event.getBot().getId()).serializeToMiraiCode());
            String regex = atMsg + ".*";
            if (Pattern.matches(regex, event.getMessage().serializeToMiraiCode())) {
                analyzeText.analyzeMsg(event);
            }
        }

    }

}
