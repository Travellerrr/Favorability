package cn.travellerr.command

import cn.travellerr.Favorability
import cn.travellerr.config.Config
import cn.travellerr.make.makingMachine
import cn.travellerr.utils.FavorUtil
import cn.travellerr.utils.wtfUtil
import net.mamoe.mirai.Bot
import net.mamoe.mirai.console.command.CommandContext
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.console.command.getGroupOrNull
import net.mamoe.mirai.console.plugin.jvm.reloadPluginConfig
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.data.At

object CheckMake :
    SimpleCommand(Favorability.INSTANCE, "checkMake", "查看制造", "查看制作", "查看", description = "查看制造队列") {
    @Handler
    fun useMake(context: CommandContext) {
        val subject: Contact? = context.sender.subject
        val sender: User? = context.sender.user
        makingMachine.checkItem(subject, sender)
    }
}

object CheckLove :
    SimpleCommand(Favorability.INSTANCE, "checkLove", "查看好感度", "好感度", "好感", description = "查看好感度") {
    @Handler
    fun useLove(context: CommandContext) {
        val subject: Contact? = context.sender.subject
        val sender: User? = context.sender.user;
        val bot: Bot? = context.sender.bot;
        FavorUtil.checkFavor(subject, sender, bot)
    }
}

object Make : SimpleCommand(Favorability.INSTANCE, "makeItem", "制造", "制作", description = "使用金币制造礼物") {
    @Handler
    fun useLove(context: CommandContext, coin: Long) {
        val subject: Contact? = context.sender.subject
        val sender: User? = context.sender.user
        makingMachine.use(subject, sender, coin)
    }
}

object OpenOthers : SimpleCommand(Favorability.INSTANCE, "Doxxing", "盒", description = "写这个功能纯属脑子有病") {
    @Handler
    fun useLove(context: CommandContext, qqId: At) {
        val subject: Contact? = context.sender.subject
        val sender: User? = context.sender.user
        val group: Group? = context.sender.getGroupOrNull()
        wtfUtil.useToOther(subject, sender, group, qqId)
    }
}

object Open : SimpleCommand(Favorability.INSTANCE, "DoxxingMe", "盒我", description = "写这个功能纯属脑子有病") {
    @Handler
    fun useLove(context: CommandContext) {
        val subject: Contact? = context.sender.subject
        val sender: User? = context.sender.user
        wtfUtil.use(subject, sender)
    }
}

object ReloadConfig : SimpleCommand(Favorability.INSTANCE, "Favorability", description = "重载配置") {
    @Handler
    suspend fun reload(sender: CommandSender, msg: String) {
        if (msg == "reload") {
            Favorability.INSTANCE.reloadPluginConfig(Config)
            sender.sendMessage("重载已完成")
        }
    }
}