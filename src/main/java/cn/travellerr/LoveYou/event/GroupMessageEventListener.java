package cn.travellerr.LoveYou.event;

import cn.travellerr.LoveYou.getLove.AnalyzeText;
import cn.travellerr.utils.Log;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.contact.BotIsBeingMutedException;
import net.mamoe.mirai.contact.MessageTooLargeException;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.EventCancelledException;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

import static cn.travellerr.Favorability.loveYou;

@Deprecated(since = "2024/12/27 附加功能将不再维护！", forRemoval = false)
public class GroupMessageEventListener extends SimpleListenerHost {

    long[] enableGroup = loveYou.getEnableGroup();

    /**
     * 异常处理
     *
     * @author chahuyun
     */
    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        if (exception instanceof EventCancelledException) {
            Log.error("发送消息被取消:", exception);
        } else if (exception instanceof BotIsBeingMutedException) {
            Log.error("你的机器人被禁言:", exception);
        } else if (exception instanceof MessageTooLargeException) {
            Log.error("发送消息过长:", exception);
        } else if (exception instanceof IllegalArgumentException) {
            Log.error("发送消息为空:", exception);
        }

        // 处理事件处理时抛出的异常
        Log.error(exception);
    }

    /**
     * @param event 群组消息
     * @author Travellerr
     * @implNote 实现对@机器人消息的情感处理
     */
    @EventHandler
    public void onMessage(@NotNull GroupMessageEvent event) {
        String atMsg = Pattern.quote(new At(event.getBot().getId()).serializeToMiraiCode());
        String regex = atMsg + ".*";

        if (enableGroup.length > 0) {
            for (long group : enableGroup) {
                if (event.getGroup().getId() == group && Pattern.matches(regex, event.getMessage().serializeToMiraiCode())) {
                    AnalyzeText.analyzeMsg(event);
                    break; // 如果匹配成功，立即跳出循环
                }
            }
        } else {
            if (Pattern.matches(regex, event.getMessage().serializeToMiraiCode())) {
                AnalyzeText.analyzeMsg(event);
            }
        }


    }

}
