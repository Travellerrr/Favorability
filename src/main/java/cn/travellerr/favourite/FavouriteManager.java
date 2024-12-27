package cn.travellerr.favourite;

import cn.chahuyun.hibernateplus.HibernateFactory;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.travellerr.entity.Favourite;
import net.mamoe.mirai.contact.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

/**
 * 用户好感度管理器
 *
 * @author Travellerr
 * @see cn.travellerr.entity.Favourite
 * @see FavouriteManager#addLove(User, long)
 * @see FavouriteManager#addLove(Favourite, long)
 * @see FavouriteManager#getInfo(Long)
 * @see FavouriteManager#getAllInfo()
 * @see FavouriteManager#saveData(Favourite)
 */
public class FavouriteManager {

    /**
     * 开始制造物品
     *
     * @param sender 发送者
     * @param money  金钱
     * @param time   花费时间
     */
    public static void startMake(User sender, int money, long time) {
        Long qqNumber = sender.getId();

        Favourite user = getInfo(qqNumber);
        if (isQQNumberNew(user)) {
            user = new Favourite(qqNumber, sender.getNick(), 0L, 0L, 0, false, new Date(), new Date());
        }
        int chance = RandomUtil.randomInt(1, 5001);
        int itemLevel = chance <= money ? 3 : 2;
        updateStartMakingData(user, time, itemLevel);
    }

    /**
     * QQ号是否为新的
     *
     * @param user 用户
     * @return 是否为最新
     */
    private static boolean isQQNumberNew(Favourite user) {
        return user == null;
    }

    /**
     * 是否制造时间已到
     *
     * @param user 用户
     * @return 若制造时间已到则返回-1，否则返回具体时间(毫秒)
     */
    public static Long isTimesUp(Favourite user, boolean isQuicklyMake) {
        if (user == null) return -1L;
        Date endTime = DateUtil.offsetSecond(user.getStartMakeTime(), Math.toIntExact(user.getMakeTime()));

        if (isQuicklyMake || DateUtil.compare(endTime, new Date()) <= 0) {
            return -1L;
        }
        return DateUtil.between(endTime, new Date(), DateUnit.SECOND);
    }

    /**
     * 清除用户的制造队列
     *
     * @param user 用户
     */
    public static void clearQueue(Favourite user) {
        user.setMaking(false);
        user.setMakeTime(0L);
        user.setItemLevel(0);
        user.setStartMakeTime(null);
        saveData(user);
    }

    /**
     * 更新用户的开始制造数据
     *
     * @param user      用户
     * @param makeTime  制造所需时间
     * @param itemLevel 物品等级
     */
    private static void updateStartMakingData(Favourite user, long makeTime, int itemLevel) {
        user.setMakeTime(makeTime);
        user.setItemLevel(itemLevel);
        user.setMaking(true);
        user.setStartMakeTime(new Date());
        saveData(user);
    }

    /**
     * 获取用户信息
     *
     * @param qqNumber QQ号
     * @return 用户对象
     *
     * @see Favourite
     */
    public static Favourite getInfo(Long qqNumber) {
        Favourite user = HibernateFactory.selectOne(Favourite.class, qqNumber);
        if (user == null) {
            user = Favourite.builder()
                    .QQ(qqNumber)
                    .makeTime(0L)
                    .itemLevel(0)
                    .isMaking(false)
                    .build();
        }
        return user;
    }

    /**
     * 增加用户的经验值
     *
     * @param user 用户
     * @param exp  经验值
     *
     * @see Favourite
     */
    public static void addLove(Favourite user, long exp) {
        user.addExp(exp);
        saveData(user);
    }

    /**
     * 增加用户的经验值
     *
     * @param sender 发送者
     * @param exp    经验值
     *
     * @see Favourite
     */
    public static void addLove(User sender, long exp) {
        Favourite user = getInfo(sender.getId());
        user.addExp(exp);
        saveData(user);
    }

    /**
     * 保存用户数据
     *
     * @param user 用户
     */
    public static void saveData(Favourite user) {
        HibernateFactory.merge(user);
    }

    /**
     * 获取经验值列表
     *
     * @return 包含经验值和QQ号的Map
     */
    public static ArrayList<Favourite> getAllInfo() {
        ArrayList<Favourite> favourites = (ArrayList<Favourite>) HibernateFactory.selectList(Favourite.class);
        favourites.sort(Comparator.comparing(Favourite::getExp).reversed());
        return favourites;
    }
}
