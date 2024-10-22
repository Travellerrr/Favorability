package cn.travellerr.utils;

import cn.chahuyun.hibernateplus.HibernateFactory;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.travellerr.entity.Favourite;
import net.mamoe.mirai.contact.User;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqlUtilNew {

    /**
     * 开始制造物品
     *
     * @param sender 发送者
     * @param money  金钱
     * @param time   花费时间
     * @return 是否成功开始制造
     * @author Travellerr
     */
    public static Boolean startMake(User sender, int money, long time) {
        Long qqNumber = sender.getId();

        Favourite user = getInfo(qqNumber);
        if (isQQNumberNew(user)) {
            user = new Favourite(qqNumber, sender.getNick(), 0L, 0L, 0, false, new Date(), new Date());
        } else if (user.isMaking()) return false;
        int chance = RandomUtil.randomInt(1, 5001);
        int itemLevel = chance <= money ? 3 : 2;
        //int itemLevel = money >= 200 ? (chance >= 75 ? 3 : 2) : (chance >= 95 ? 3 : 2);
        updateStartMakingData(user, time, itemLevel);
        return true;
    }

    /**
     * QQ号是否为新的
     *
     * @param user 用户
     * @return 是否为最新
     * @author Travellerr
     */
    private static boolean isQQNumberNew(Favourite user) {
        return user == null;
    }

    /**
     * 是否制造时间已到
     *
     * @param user 用户
     * @return 若制造时间已到则返回-1，否则返回具体时间(毫秒)
     * @author Travellerr
     */
    private static Long isTimesUp(Favourite user, boolean isQuicklyMake) {
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
     * @return 更新后的用户对象
     * @author Travellerr
     */
    private static Favourite clearQueue(Favourite user) {
        user.setMaking(false);
        user.setMakeTime(0L);
        user.setItemLevel(0);
        user.setStartMakeTime(null);
        return user;
    }

    /**
     * 更新用户的开始制造数据
     *
     * @param user      用户
     * @param makeTime  制造所需时间
     * @param itemLevel 物品等级
     * @author Travellerr
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
     * @author Travellerr
     */
    public static Favourite getInfo(Long qqNumber) {
        return HibernateFactory.selectOne(Favourite.class, qqNumber);
    }

    /**
     * 增加用户的经验值
     *
     * @param user 用户
     * @param exp  经验值
     * @return 更新后的用户对象
     * @author Travellerr
     */
    public static Favourite addLove(Favourite user, long exp) {
        user.setExp(user.getExp() + exp);
        return user;
    }

    /**
     * 保存用户数据
     *
     * @param user 用户
     * @author Travellerr
     */
    public static void saveData(Favourite user) {
        HibernateFactory.merge(user);
    }

    /**
     * 获取经验值列表
     *
     * @return 包含经验值和QQ号的Map
     * @author Travellerr
     */
    public static Map<Long, Long> getExpList() {
        List<Favourite> users = HibernateFactory.selectList(Favourite.class);
        if (users == null) return null;
        Map<Long, Long> map = new HashMap<>();
        users.forEach(user -> map.put(user.getQQ(), user.getExp()));
        return map;
    }
}
