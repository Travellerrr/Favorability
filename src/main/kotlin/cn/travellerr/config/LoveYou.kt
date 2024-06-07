package cn.travellerr.config

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object LoveYou : AutoSavePluginConfig("LoveYouConfig") {
    @ValueDescription("是否启用LoveYou")
    val enable: Boolean by value(false)

    @ValueDescription("情感模型目录，以 本插件data目录为基准")
    val lovePath: String by value("/LoveYou/nb-classifier-for-weibo.ser")

    @ValueDescription("信息比对模型，以 本插件data目录为基准")
    val msgPath: String by value("/LoveYou/hanlp.txt")

    @ValueDescription("单次情感增加最大值")
    val loveMax: Int by value(100)

    @ValueDescription("单次情感增加最小值")
    val loveMin: Int by value(-100)

    @ValueDescription("情感上下浮动值 (单次情感0-1)\n太大会导致正面消息被作为负面情感，建议不要改动")
    val fluctuation: Double by value(0.03)

    @ValueDescription("对话记录保存时长 (秒)")
    val duration: Int by value(180)

    @ValueDescription("好感上升对话")
    val up by value(listOf("唔……我就勉为其难接受吧！"))

    @ValueDescription("好感持平对话")
    val flat by value(listOf("已阅"))

    @ValueDescription("好感下降对话")
    val down by value(listOf("?你在说什么！也太伤我心了吧！"))

    @ValueDescription("对话信息重复消息")
    val similarity by value(listOf("咕呣呣……这句话我已经听腻啦！"))
}