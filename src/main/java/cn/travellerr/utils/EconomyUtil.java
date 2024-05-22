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
    private static final IEconomyService service = EconomyService.INSTANCE;
    private static EconomyContext context;
    private static EconomyCurrency currency;

    public static Double getMoney(User user) {
        EconomyAccount account = service.account(user);
        return context.get(account, currency);
    }

    public static void plusMoney(User user, int money) {
        EconomyAccount account = service.account(user);

        context.plusAssign(account, currency, money);
    }

    public static void init() {
        int economyName = config.getEconomyName();
        if (economyName == 0) {
            context = service.custom(HuYanEconomy.INSTANCE);
            currency = service.getBasket().get("hy-gold");
        } else {
            context = service.global();
            currency = service.getBasket().get("mirai-coin");
        }
    }
}
