package cn.travellerr.utils;

public class MakingItem {

    private int itemLevel;
    private boolean isMaking;
    private boolean timesUp;
    private int needTime;

    /**
     * 使用指定的属性构造MakingItem。
     *
     * @param itemLevel 物品等级
     * @param isMaking  是否正在制作
     * @param timesUp   时间是否到期
     * @param needTime  制作物品所需时间
     * @author Travellerr
     */
    public MakingItem(int itemLevel, boolean isMaking, boolean timesUp, int needTime) {
        this.itemLevel = itemLevel;
        this.isMaking = isMaking;
        this.timesUp = timesUp;
        this.needTime = needTime;
    }

    /**
     * 返回MakingItem的物品等级。
     *
     * @return 物品等级
     * @author Travellerr
     */
    public int getItemLevel() {
        return itemLevel;
    }

    /**
     * 设置MakingItem的物品等级。
     *
     * @param itemLevel 物品等级
     * @author Travellerr
     */
    public void setItemLevel(int itemLevel) {
        this.itemLevel = itemLevel;
    }

    /**
     * 返回MakingItem是否正在制作。
     *
     * @return 如果正在制作返回true，否则返回false
     * @author Travellerr
     */
    public boolean isMaking() {
        return isMaking;
    }

    /**
     * 设置MakingItem是否正在制作。
     *
     * @param making 是否正在制作
     * @author Travellerr
     */
    public void setMaking(boolean making) {
        isMaking = making;
    }

    /**
     * 返回MakingItem的时间是否到期。
     *
     * @return 如果时间到期返回true，否则返回false
     * @author Travellerr
     */
    public boolean isTimesUp() {
        return timesUp;
    }

    /**
     * 设置MakingItem的时间是否到期。
     *
     * @param timesUp 时间是否到期
     * @author Travellerr
     */
    public void setTimesUp(boolean timesUp) {
        this.timesUp = timesUp;
    }

    /**
     * 返回制作物品所需的时间。
     *
     * @return 所需时间
     * @author Travellerr
     */
    public int getNeedTime() {
        return needTime;
    }

    /**
     * 设置制作物品所需的时间。
     *
     * @param needTime 所需时间
     * @author Travellerr
     */
    public void setNeedTime(int needTime) {
        this.needTime = needTime;
    }

    public String printAll() {

        return "物品等级: " + this.itemLevel +
                "\n是否正在制作: " + this.isMaking +
                "\n是否时间到" + this.timesUp +
                "\n所需时间" + this.needTime;
    }

}
