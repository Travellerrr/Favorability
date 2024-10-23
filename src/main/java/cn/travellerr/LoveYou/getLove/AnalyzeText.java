package cn.travellerr.LoveYou.getLove;

import cn.hutool.core.util.RandomUtil;
import cn.travellerr.Favorability;
import cn.travellerr.LoveYou.utils.LoveSqlUtil;
import cn.travellerr.utils.FavouriteManager;
import cn.travellerr.utils.Log;
import com.hankcs.hanlp.classification.classifiers.IClassifier;
import com.hankcs.hanlp.classification.classifiers.NaiveBayesClassifier;
import com.hankcs.hanlp.classification.models.NaiveBayesModel;
import com.hankcs.hanlp.corpus.io.IOUtil;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.message.data.QuoteReply;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static cn.travellerr.Favorability.loveYou;

/**
 * 实现对消息的情感分析以及对用户好感信息的操作
 *
 * @author Travellerr
 */
public class AnalyzeText {


    private static final String path = Favorability.INSTANCE.getDataFolderPath() + loveYou.getLovePath();
    private static final NaiveBayesModel model = (NaiveBayesModel) IOUtil.readObjectFrom(path);
    private static final IClassifier classifier = new NaiveBayesClassifier(model);
    private static final ExecutorService executorService = Executors.newFixedThreadPool(10); // 创建一个固定大小的线程池

    /**
     * 进行消息情感分析并处理好感度增幅
     * @author Travellerr
     * @param event 消息实例
     */
    public static void analyzeMsg(MessageEvent event) {
        executorService.submit(() -> {
            try {
                String originMsg = event.getMessage().serializeToMiraiCode();
                Contact subject = event.getSubject();
                User user = event.getSender();
                String atMsg = new At(event.getBot().getId()).serializeToMiraiCode();
                originMsg = originMsg.substring(atMsg.length());

                if (originMsg.startsWith(" ")) {
                    originMsg = originMsg.substring(1);
                }

                LoveSqlUtil.checkUserMsgTime(user);
                if (LoveSqlUtil.isSimilarityMsg(user, originMsg)) {
                    List<String> msg = loveYou.getSimilarity();

                    int index = RandomUtil.randomInt(0, msg.size());
                    subject.sendMessage(msg.get(index));
                    return;
                }

                Log.debug("开始进行情感分析");

                Map<String, Double> msg = classifier.predict(originMsg);

                Log.debug("HanLP分析变化: " + msg.get("1"));
                int ans = (int) ((mapSentimentScore(msg.get("1"), loveYou.getLoveMax(), loveYou.getLoveMin())) * 200 - 100);
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

                FavouriteManager.addLove(event.getSender(), ans);
                QuoteReply reply = new QuoteReply(event.getMessage());
                subject.sendMessage(reply.plus(new PlainText(replyMsg + "  \n【变化：" + expChange + "】")));

                LoveSqlUtil.saveMsg(user, originMsg);
            } catch (Exception e) {
                Log.error(e.fillInStackTrace().getMessage());
            }
        });

    }

    /**
     * @author Travellerr
     * @param character 重复字符
     * @param num 重复次数
     * @return 组合的字符串
     */
    private static String characterNum(String character, int num) {
        int repeat = Math.abs(num) / 10;
        return character.repeat(Math.max(1, repeat));
    }

    /**
     * @author Travellerr
     * @param ans 分析情感结果
     * @param targetMax 映射最大值
     * @param targetMin 映射最小值
     * @return 映射后数值
     */

    private static double mapSentimentScore(double ans, int targetMax, int targetMin) {

        double fluctuationTimes = RandomUtil.randomDouble(0.7, 1);
        double mappedScore = ans * fluctuationTimes;

        targetMax = targetMax / 100;
        targetMin = targetMin / 100;

        if (ans <= 0.46) {
            mappedScore = ans;
        }

        mappedScore = addRandomFluctuation(mappedScore, targetMax, targetMin);

        //判断中性情感
        if (0.511 >= ans && ans >= 0.498) {
            mappedScore = 0.5;
        }
        Log.debug("浮动变化: " + mappedScore);
        return mappedScore;
    }

    /**
     * @author Travellerr
     * @param ans 情感数值
     * @param targetMax 映射最大值
     * @param targetMin 映射最小值
     * @return 浮动结果
     */

    private static double addRandomFluctuation(double ans, int targetMax, int targetMin) {
        double fluctuation = RandomUtil.randomDouble(0 - loveYou.getFluctuation(), loveYou.getFluctuation());
        return Math.max(Math.min(ans + fluctuation, targetMax), targetMin);
    }
}
