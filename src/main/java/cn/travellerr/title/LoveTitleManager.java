package cn.travellerr.title;

import cn.chahuyun.economy.HuYanEconomy;
import cn.chahuyun.economy.entity.UserInfo;
import cn.chahuyun.economy.entity.title.TitleTemplate;
import cn.chahuyun.economy.entity.title.TitleTemplateSimpleImpl;
import cn.chahuyun.economy.manager.TitleManager;
import cn.chahuyun.economy.manager.UserManager;
import cn.chahuyun.economy.plugin.TitleTemplateManager;
import cn.travellerr.utils.ImageUtil;
import cn.travellerr.utils.Log;
import net.mamoe.mirai.contact.User;

import java.awt.*;

public class LoveTitleManager {
    /**
     * 初始化方法，注册n个称号模板。
     * <p>
     * 该方法在程序启动时调用，用于注册n个称号模板.
     *
     * @see TitleTemplateManager#registerTitleTemplate(TitleTemplate[])
     */
    public static void init() {
        // 注册两个称号模板
        TitleTemplateManager.registerTitleTemplate(
                new TitleTemplateSimpleImpl(
                        LoveTitleCode.LOVE_CODE[0],
                        LoveTitleCode.LOVE_EXPIRED,
                        "好感20",
                        true,
                        false,
                        "[好感20]",
                        ImageUtil.colorHex(new Color(255, 0, 0)),
                        ImageUtil.colorHex(new Color(255, 221, 0))
                ),
                new TitleTemplateSimpleImpl(
                        LoveTitleCode.LOVE_CODE[1],
                        LoveTitleCode.LOVE_EXPIRED,
                        "好感40",
                        true,
                        false,
                        "[好感40]",
                        ImageUtil.colorHex(new Color(163, 210, 75)),
                        ImageUtil.colorHex(new Color(54, 189, 185))
                ),
                new TitleTemplateSimpleImpl(
                        LoveTitleCode.LOVE_CODE[2],
                        LoveTitleCode.LOVE_EXPIRED,
                        "好感60",
                        true,
                        false,
                        "[好感60]",
                        ImageUtil.colorHex(new Color(75, 210, 174)),
                        ImageUtil.colorHex(new Color(54, 97, 189))
                ),
                new TitleTemplateSimpleImpl(
                        LoveTitleCode.LOVE_CODE[3],
                        LoveTitleCode.LOVE_EXPIRED,
                        "好感80",
                        true,
                        false,
                        "[好感80]",
                        ImageUtil.colorHex(new Color(75, 122, 210)),
                        ImageUtil.colorHex(new Color(185, 54, 189))
                )
        );
    }


    /**
     * 添加好感称号方法。
     * 该方法用于根据用户的好感等级添加相应的称号。
     *
     * @param user      用户对象
     * @param loveLevel 用户的好感等级
     */
    public static String addLoveTitle(User user, int loveLevel) {
        // 获取用户信息
        UserInfo userInfo = UserManager.getUserInfo(user);
        // 计算用户的好感称号分割
        int loveTitleSplit = (loveLevel / 20) - 1;
        // 如果好感称号分割小于1，则返回
        if (loveTitleSplit < 0) {
            return null;
        }

        Log.debug("好感分割: " + loveTitleSplit);

        // 获取称号数量
        int loveCodeIndex = LoveTitleCode.LOVE_CODE.length - 1;

        Log.debug("称号最大索引: " + loveCodeIndex);
        // 如果好感等级大于称号数量，则取最后一个称号
        if (loveTitleSplit > loveCodeIndex) {
            loveTitleSplit = loveCodeIndex;
        }
        String loveCode = LoveTitleCode.LOVE_CODE[loveTitleSplit];

        if (TitleManager.checkTitleIsExist(userInfo, loveCode)) {
            Log.debug("用户" + user.getId() + "已经拥有" + loveCode);
            return null;
        }

        // 添加称号
        Log.debug("添加称号: " + loveCode);
        boolean addLoveTitleSuccess = TitleManager.addTitleInfo(userInfo, loveCode);
        // 如果添加称号失败，则输出错误日志
        if (!addLoveTitleSuccess) {
            Log.error("添加称号失败");
            return null;
        }

        // 输出成功日志
        Log.debug("用户" + user.getId() + "添加称号成功! ID: " + LoveTitleCode.LOVE_CODE[loveTitleSplit]);
        return "您已获得 " + loveCode + " 称号!\n请使用\""
                + HuYanEconomy.config.getPrefix()
                + "我的称号\"查看您的称号信息。";
    }

}