package cn.travellerr.config

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object TitleConfig : AutoSavePluginConfig("titleConfig") {

    @ValueDescription("是否开启称号渐变")
    var gradient: Boolean by value(true)

    @ValueDescription("是否开启名称颜色变化")
    var impactName: Boolean by value(false)
}