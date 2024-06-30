package cn.travellerr.command

import cn.travellerr.Favorability
import cn.travellerr.config.LoveYou
import cn.travellerr.config.PluginConfig
import cn.travellerr.config.TipsConfig
import cn.travellerr.makeMachine.make.makingMachine
import cn.travellerr.makeMachine.make.makingMachine.checkItemQuickly
import cn.travellerr.utils.EconomyUtil
import cn.travellerr.utils.FavorUtil
import cn.travellerr.utils.wtfUtil
import net.mamoe.mirai.Bot
import net.mamoe.mirai.console.command.*
import net.mamoe.mirai.console.plugin.jvm.reloadPluginConfig
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.contact.getMember
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.SingleMessage

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
        val sender: User? = context.sender.user
        val bot: Bot? = context.sender.bot
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

    @Handler
    suspend fun useLoveError(sender: CommandContext) {
        val subject: Contact? = sender.sender.subject
        val originMsg: SingleMessage = sender.originalMessage[1]
        val prefix = originMsg.toString().split(" ")[0]
        if (subject is Contact) {
            subject.sendMessage("请使用 \"$prefix [所用金币数量]\"生成，不要加括号")
        }
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

object Debug : CompositeCommand(Favorability.INSTANCE, "Favor", description = "好感度配置指令") {
    @Description("重载配置")
    @SubCommand
    suspend fun reload(sender: CommandSender) {
            Favorability.INSTANCE.reloadPluginConfig(TipsConfig)
            Favorability.INSTANCE.reloadPluginConfig(PluginConfig)
            Favorability.INSTANCE.reloadPluginConfig(LoveYou)
            EconomyUtil.init()
            sender.sendMessage("重载已完成")
    }

    @SubCommand("cheatLoveExp", "好感增加", "好感度增加", "好感作弊")
    @Description("强制增加/减少好感度")
    fun cheatLoveExp(context: CommandContext, exp: Int) {
        val subject: Contact? = context.sender.subject
        val user: User? = context.sender.user
        FavorUtil.cheatLove(user, exp, subject)
    }
}

object GetLoveList : SimpleCommand(
    Favorability.INSTANCE,
    "getLoveList",
    "好感度排行",
    "好感度排行榜",
    "好感排行",
    "好感排行榜",
    "好感排名",
    "好感度排名",
    description = "本群好感排行"
) {
    @Handler
    fun reload(context: CommandContext) {
        val subject: Contact? = context.sender.subject
        val group: Group? = context.sender.getGroupOrNull()
        FavorUtil.getLoveList(subject, group)
    }
}
object GetAllLoveList : SimpleCommand(
    Favorability.INSTANCE,
    "getAllLoveList",
    "好感度全排行",
    "好感度全排行榜",
    "好感全排行",
    "好感全排行榜",
    "好感全排名",
    "好感度全排名",
    description = "全部好感排行"
) {
    @Handler
    fun reload(context: CommandContext) {
        val subject: Contact? = context.sender.subject
        FavorUtil.getAllLoveList(subject)
    }
}

object CreateQuickly : SimpleCommand(
    Favorability.INSTANCE,
    "quickCreate",
    "急速完成",
    "极速查看",
    "急速查看",
    "极速制造",
    description = "无冷却制造"
) {
    @Handler
    fun own(context: CommandContext) {
        val subject: Contact? = context.sender.subject
        val user: User? = context.sender.user
        checkItemQuickly(subject, user)
    }

    @Handler
    fun other(context: CommandContext, at: At) {
        val subject: Contact? = context.sender.subject
        val user: User? = context.sender.getGroupOrNull()?.getMember(at.target)
        checkItemQuickly(subject, user)
    }
}

/*object TestCommand : SimpleCommand(
    Favorability.INSTANCE,
    "test",
    description = "测试"
) {
    @Handler
    fun reload(context: CommandContext) {
        val subject: Contact? = context.sender.subject
        val user : User? = context.sender.user;
        EconomyUtil.register(user, subject)
    }
}*/


