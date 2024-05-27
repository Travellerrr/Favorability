package cn.travellerr.LoveYou.getLove;

import cn.hutool.core.util.RandomUtil;
import cn.travellerr.Favorability;
import cn.travellerr.utils.Log;
import cn.travellerr.utils.sqlUtil;
import com.hankcs.hanlp.classification.classifiers.IClassifier;
import com.hankcs.hanlp.classification.classifiers.NaiveBayesClassifier;
import com.hankcs.hanlp.classification.models.NaiveBayesModel;
import com.hankcs.hanlp.corpus.io.IOUtil;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.message.data.QuoteReply;

import java.util.List;
import java.util.Map;

import static cn.travellerr.Favorability.loveYou;

public class analyzeText {

    public static void analyzeMsg(MessageEvent event) {
        try {
            String originMsg = event.getMessage().serializeToMiraiCode();
            Contact subject = event.getSubject();
            String path = Favorability.INSTANCE.getDataFolderPath() + loveYou.getPath();
            NaiveBayesModel model = (NaiveBayesModel) IOUtil.readObjectFrom(path);
            IClassifier classifier = new NaiveBayesClassifier(model);
            String atMsg = new At(event.getBot().getId()).serializeToMiraiCode();
            originMsg = originMsg.substring(atMsg.length());

            if (originMsg.startsWith(" ")) {
                originMsg = originMsg.substring(1);
            }

            Map<String, Double> msg = classifier.predict(originMsg);

            int ans = (int) (msg.get("1") * 100 - 50);
            Log.debug("情感变化：" + ans);

            int index;
            String replyMsg;
            String expChange;
            if (ans > 0) {
                Log.debug("情感上涨");
                List<String> up = loveYou.getUp();
                index = RandomUtil.randomInt(0, up.size());

                replyMsg = up.get(index);
                expChange = characterNum("+", ans);
            } else if (ans == 0) {

                Log.debug("情感持平");
                List<String> flat = loveYou.getFlat();
                index = RandomUtil.randomInt(0, flat.size());

                replyMsg = flat.get(index);
                expChange = "=";
            } else {

                Log.debug("情感下降");
                List<String> down = loveYou.getDown();
                index = RandomUtil.randomInt(0, down.size());

                expChange = characterNum("-", ans);
                replyMsg = down.get(index);
            }
            sqlUtil.addLove(ans, event.getSender().getId());
            QuoteReply reply = new QuoteReply(event.getMessage());
            subject.sendMessage(reply.plus(new PlainText(replyMsg + "  \n【变化：" + expChange + "】")));
        } catch (Exception e) {
            Log.error(e.fillInStackTrace().getMessage());
        }
    }

    private static String characterNum(String character, int num) {
        int repeat = Math.abs(num) / 10;
        return character.repeat(Math.max(1, repeat));
    }
}
