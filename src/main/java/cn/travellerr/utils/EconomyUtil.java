package cn.travellerr.utils;

import cn.chahuyun.economy.HuYanEconomy;
import net.mamoe.mirai.contact.User;
import xyz.cssxsh.mirai.economy.EconomyService;
import xyz.cssxsh.mirai.economy.service.EconomyAccount;
import xyz.cssxsh.mirai.economy.service.EconomyContext;
import xyz.cssxsh.mirai.economy.service.EconomyCurrency;
import xyz.cssxsh.mirai.economy.service.IEconomyService;

import static cn.travellerr.Favorability.config;

public class EconomyUtil {

    /**
     * 经济服务
     */
    private static final IEconomyService service = EconomyService.INSTANCE;


    /**
     * 经济上下文
     */
    private static EconomyContext context;

    /**
     * 经济货币
     */
    private static EconomyCurrency currency;
    private static final int economyName = config.getEconomyName();

    /**
     * 获取用户货币
     *
     * @param user 用户
     * @return 货币数量
     * @author Travellerr
     */
    public static Double getMoney(User user) {
        init();
        EconomyAccount account = service.account(user);
        return context.get(account, currency);
    }

    /**
     * 增加用户货币
     * @author Travellerr
     * @param user 用户
     * @param money 货币数量
     */
    public static void plusMoney(User user, int money) {
        init();
        EconomyAccount account = service.account(user);

        context.plusAssign(account, currency, money);
    }

    /**
     * 注册货币
     * @author Travellerr
     */
    public static void init() {
        if (economyName == 0) {
            context = service.custom(HuYanEconomy.INSTANCE);
            currency = service.getBasket().get("hy-gold");
        } else {
            context = service.global();
            currency = service.getBasket().get("mirai-coin");
        }
    }


}
