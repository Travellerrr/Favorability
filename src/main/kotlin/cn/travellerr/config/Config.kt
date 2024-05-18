package cn.travellerr.config

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object Config : AutoSavePluginConfig("config") {
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

    @ValueDescription("好感度消息，每 \"changeLevel\" 级一条消息\n")
    val LoveMessage by value(
        listOf(
            "%成员% %后缀%您……您好……",
            "%成员% %后缀%是一个好人",
            "%成员% %后缀%一直对我很好呢！",
            "%成员% %后缀%, 很关心我呢……",
            "%成员% %后缀%, 稀饭！",
            "最喜欢 %成员% %后缀%了！",
            "呜哇！%成员% %后缀% 也太好了！好喜欢 %成员% %后缀%！",
            "わたしは %成员% %后缀% せんせいが大好きです! "
        )
    )

    @ValueDescription("金币不够至少所需提示")
    val notEnough by value(
        listOf(
            "没有商家愿意接单",
            "导致在路上被风刮走了，费了很大劲才捡回来",
            "中途遇到土匪，看见你摇了摇头转身走了",
            "不好意思出门找商家做礼物",
            "商家吓得像见了鬼，纷纷逃之夭夭。",
            "商家欲哭无泪，宁愿关门大吉。",
            "商家眼神闪烁，仿佛看到了世界末日。",
            "商家们一个个避而远之，生怕倒霉。",
            "商家们纷纷摇头叹息，不知所措。",
            "商家们眼神暗淡，装作没看见。",
            "商家们一个个闭门不出，宁可躲起来。",
            "商家们面露难色，仿佛碰到了麻烦。",
            "商家们一个个摇头苦笑，无可奈何。"
        )
    )
}