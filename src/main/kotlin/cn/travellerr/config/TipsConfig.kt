package cn.travellerr.config

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object TipsConfig : AutoSavePluginConfig("MsgConfig") {
    @ValueDescription("好感度查看\n\"%成员%\"该成员名称\n\"%机器人%\"机器人名称\n\"%好感%\"好感度\n\"%好感经验%\"%好感经验%\n\"%好感信息%\"好感度消息")
    val checkLove by value("%机器人%对你的好感度为: %好感度%\n好感经验为: %好感经验%\n%好感信息%")


    @ValueDescription("群好感度排行信息\n\"%成员%\"该成员名称\n\"%机器人%\"机器人名称\n\"%好感%\"好感度\n\"%后缀%\"设置的后缀\n")
    val groupLoveMsg by value("这位是%成员% %后缀%,\n%机器人%对Ta的好感度为: %好感%")

    @ValueDescription("全体好感度排行信息\n\"%成员%\"该成员QQ号\n\"%机器人%\"机器人名称\n\"%好感%\"好感度\n\"%后缀%\"设置的后缀\n\"%排名%\"当前排名")
    val totalLoveMsg by value("第 %排名% 名 %后缀%, \n %机器人% 对Ta的好感度为: %好感")

    //第" + (i+1) + "名"+config.getSuffix()+",\n"+subject.getBot().getNick()+"对Ta的好感度为: " + FavorLevel(LoveExpList.get(i))

    @ValueDescription("好感度为负时的消息")
    val negativeFavorMessage: String by value("%成员% %后缀% 是谁？（冷漠）")

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