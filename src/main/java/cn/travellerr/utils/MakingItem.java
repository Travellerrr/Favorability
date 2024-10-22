package cn.travellerr.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MakingItem {

    /**
     * 物品等级
     *
     */
    private int itemLevel;

    /**
     * 是否在制作
     */
    private boolean isMaking;

    /**
     * 是否时间已到
     */
    @Getter
    private boolean timesUp;

    /**
     * 所需时间
     */
    @Getter
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
     * 返回<code>MakingItem</code>对象所有变量内容
     *
     * @return 对象数据
     */
    public String printAll() {

        return "物品等级: " + this.itemLevel +
                "\n是否正在制作: " + this.isMaking +
                "\n是否时间到" + this.timesUp +
                "\n所需时间" + this.needTime;
    }

}
