package cn.travellerr.config

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object LoveYou : AutoSavePluginConfig("LoveYouConfig") {
    @ValueDescription("是否启用LoveYou")
    val enable: Boolean by value(false)

    @ValueDescription("情感模型目录，以 本插件data目录为基准")
    val path: String by value("/LoveYou/nb-classifier-for-weibo.ser")

    @ValueDescription("好感上升对话")
    val up by value(listOf("唔……我就勉为其难接受吧！"))

    @ValueDescription("好感持平对话")
    val flat by value(listOf("已阅"))

    @ValueDescription("好感下降对话")
    val down by value(listOf("?你在说什么！也太伤我心了吧！"))
}