package cn.travellerr.config

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object PluginConfig : AutoSavePluginConfig("config") {
    @ValueDescription("使用经济名称\n0 :  HuYanEconomy\n 1 : MiraiDailySign")
    val economyName: Int by value(0)

    @ValueDescription("制造至少消耗金币\n")
    val atLeastCoin: Int by value(30)

    @ValueDescription("至少需要多久制造/分钟\n")
    val atLeastMin: Int by value(10)

    @ValueDescription("至多需要多久制造/分钟\n")
    val atMostMin: Int by value(180)

    @ValueDescription("人物后缀\n")
    val suffix: String by value("Sensei")

    @ValueDescription("每多少级改变一次好感度消息\n")
    val changeLevel: Int by value(5)

    @ValueDescription("当好感经验值超出下方定义数量\n每几exp升一级")
    val perLevel by value(1810)

    @ValueDescription("好感信息上升计算自定义\n")
    val levelList by value(
        intArrayOf(
            15, 45, 75, 110, 145, 180, 220, 260, 300, 360,
            450, 555, 675, 815, 975, 1155, 1360, 1590, 1845, 2130, 2445,
            2790, 3165, 3575, 4020, 4500, 5020, 5580, 6180, 6825, 7515,
            8250, 9030, 9860, 10740, 11670, 12655, 13695, 14790, 15945,
            17160, 18435, 19770, 21170, 22635, 24165, 25765, 27435, 29175
        )
    )
}