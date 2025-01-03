package cn.travellerr.command

import cn.travellerr.Favorability
import cn.travellerr.config.PluginConfig
import cn.travellerr.config.TipsConfig
import cn.travellerr.config.TitleConfig
import cn.travellerr.favourite.FavorUtil
import cn.travellerr.favourite.make.MakingMachine
import cn.travellerr.favourite.make.MakingMachine.checkItemQuickly
import cn.travellerr.otherFunction.DoxxingUtil
import cn.travellerr.utils.EconomyUtil
import cn.travellerr.utils.Log
import net.mamoe.mirai.Bot
import net.mamoe.mirai.console.command.*
import net.mamoe.mirai.console.plugin.jvm.reloadPluginConfig
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.contact.getMember
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.SingleMessage

/**
 * 查看制造队列的命令对象。
 */
object CheckMake :
    SimpleCommand(Favorability.INSTANCE, "checkMake", "查看制造", "查看制作", "查看", description = "查看制造队列") {
    /**
     * 处理查看制造队列的命令。
     * @param context 命令上下文。
     */
    @Handler
    fun use(context: CommandContext) {
        val subject: Contact? = context.sender.subject
        val sender: User? = context.sender.user
        MakingMachine.checkItem(subject, sender)
    }
}

/**
 * 查看好感度的命令对象。
 */
object CheckLove :
    SimpleCommand(
        Favorability.INSTANCE,
        "checkLove",
        "查看好感度",
        "查看好感",
        "好感度",
        "好感",
        description = "查看好感度"
    ) {
    /**
     * 处理查看好感度的命令。
     * @param context 命令上下文。
     */
    @Handler
    fun use(context: CommandContext) {
        val subject: Contact? = context.sender.subject
        val sender: User? = context.sender.user
        val bot: Bot? = context.sender.bot
        FavorUtil.checkFavor(subject, sender, bot)
    }
}

/**
 * 制造礼物的命令对象。
 */
object Make : SimpleCommand(Favorability.INSTANCE, "makeItem", "制造", "制作", description = "使用金币制造礼物") {
    /**
     * 处理制造礼物的命令。
     * @param context 命令上下文。
     * @param coin 所用金币数量。
     */
    @Handler
    fun use(context: CommandContext, coin: Long) {
        val subject: Contact? = context.sender.subject
        val sender: User? = context.sender.user
        MakingMachine.use(subject, sender, coin)
    }

    /**
     * 处理制造礼物命令的错误情况。
     * @param sender 命令发送者。
     */
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

object DoxxingOthers : SimpleCommand(Favorability.INSTANCE, "Doxxing", "盒", description = "写这个功能纯属脑子有病") {
    @Handler
    fun use(context: CommandContext, qqId: At) {
        val subject: Contact? = context.sender.subject
        val sender: User? = context.sender.user
        val group: Group? = context.sender.getGroupOrNull()
        DoxxingUtil.useToOther(subject, sender, group, qqId)
    }

    @Handler
    fun use(context: CommandContext) {
        val subject: Contact? = context.sender.subject
        val sender: User? = context.sender.user
        DoxxingUtil.use(subject, sender)
    }
}

/**
 * 好感度debug指令。
 */
object Debug : CompositeCommand(Favorability.INSTANCE, "Favor", description = "好感度配置指令") {
    /**
     * 重载配置。
     * @param sender 命令发送者。
     */
    @Description("重载配置")
    @SubCommand
    suspend fun reload(sender: CommandSender) {
        Log.warning("正在重新加载配置文件! 部分配置需要重新启动才能生效! ")
        Favorability.INSTANCE.reloadPluginConfig(TipsConfig)
        Favorability.INSTANCE.reloadPluginConfig(PluginConfig)
        Favorability.INSTANCE.reloadPluginConfig(TitleConfig)
        EconomyUtil.init()
        Log.info("重载完成!")
        sender.sendMessage("重载已完成")
    }

    /**
     * 强制增加/减少好感度。
     * @param context 命令上下文。
     * @param exp 好感度变化值。
     */
    @SubCommand("cheatLoveExp", "好感增加", "好感度增加", "好感作弊")
    @Description("强制增加/减少好感度")
    fun cheatLoveExp(context: CommandContext, exp: Long) {
        val subject: Contact? = context.sender.subject
        val user: User? = context.sender.user
        FavorUtil.cheatLove(user, exp, subject)
    }

    @SubCommand("cheatLoveExp", "好感增加", "好感度增加", "好感作弊")
    @Description("强制增加/减少好感度")
    fun cheatLoveExp(context: CommandContext, at: At, exp: Long) {
        val subject: Contact? = context.sender.subject
        val user: User = context.sender.getGroupOrNull()?.getMember(at.target)!!
        FavorUtil.cheatLove(user, exp, subject)
    }
}

/**
 * 好感度排行。
 */
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
    /**
     * 查看好感度排行。
     * @param context 命令上下文。
     */
    @Handler
    fun reload(context: CommandContext) {
        val subject: Contact? = context.sender.subject
        val group: Group? = context.sender.getGroupOrNull()
        FavorUtil.getLoveList(subject, group)
    }
}

/**
 * 全好感排行。
 */
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
    /**
     * 查看全好感度排行。
     * @param context 命令上下文。
     */
    @Handler
    fun use(context: CommandContext) {
        val subject: Contact? = context.sender.subject
        FavorUtil.getAllLoveList(subject)
    }
}

/**
 * 无冷却制造。
 */
object CreateQuickly : SimpleCommand(
    Favorability.INSTANCE,
    "quickCreate",
    "急速完成",
    "极速查看",
    "急速查看",
    "极速制造",
    description = "无冷却制造"
) {
    /**
     * 无冷却制造。
     * @param context 命令上下文。
     */
    @Handler
    fun own(context: CommandContext) {
        val subject: Contact? = context.sender.subject
        val user: User? = context.sender.user
        checkItemQuickly(subject, user)
    }

    /**
     * 无冷却制造。
     * @param context 命令上下文。
     * @param at 被操作的用户。
     */
    @Handler
    fun other(context: CommandContext, at: At) {
        val subject: Contact? = context.sender.subject
        val user: User? = context.sender.getGroupOrNull()?.getMember(at.target)
        checkItemQuickly(subject, user)
    }
}


/*object FixOldDataBase : SimpleCommand(
    Favorability.INSTANCE,
    "fixOldDataBase",
    "修复好感数据库",
    "修复好感数据",
    "迁移好感数据",
    description = "好感数据转移"
) {
    @Handler
    suspend fun use(context: CommandContext) {
        val sender = context.sender
        val success = FixDataBaseTool.fixDataBase()

        if (success) {
            sender.sendMessage("好感数据转移成功")
        } else {
            sender.sendMessage("好感数据转移失败")
        }

    }
}

*/