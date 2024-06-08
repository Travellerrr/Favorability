package cn.travellerr.makeMachine.make;

public class Item {

    /**
     * 物品名称
     */
    private final String name;

    /**
     * 物品描述
     */
    private final String describe;

    /**
     * 物品等级
     */
    private final String level;

    /**
     * 物品增加的exp
     */
    private final int love;

    /**
     * 物品的url
     */
    private final String url;

    /**
     * 制造物品的实例
     *
     * @param name     物品名称
     * @param describe 物品描述
     * @param level    物品等级
     * @param love     物品增加的exp
     * @param url      物品图片的链接
     * @author Travellerr
     */
    public Item(String name, String describe, String level, int love, String url) {
        this.name = name;
        this.describe = describe;
        this.level = level;
        this.love = love;
        this.url = url;
    }

    /**
     * 获取物品的名称
     *
     * @return 物品名称
     * @author Travellerr
     */
    public String getName() {
        return name;
    }

    /**
     * 获取物品描述
     *
     * @return 物品描述
     * @author Travellerr
     */
    public String getDescribe() {
        return describe;
    }

    /**
     * 获取物品等级
     *
     * @return 物品等级
     * @author Travellerr
     */
    public String getLevel() {
        return level;
    }

    /**
     * 获取物品好感exp
     *
     * @return 物品的好感exp
     * @author Travellerr
     */
    public int getLove() {
        return love;
    }

    /**
     * 获取物品图片的链接
     *
     * @return 物品链接
     * @author Travellerr
     */
    public String getUrl() {
        return url;
    }
}
