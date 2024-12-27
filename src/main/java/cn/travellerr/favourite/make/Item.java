package cn.travellerr.favourite.make;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
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


}
