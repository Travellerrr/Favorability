package cn.travellerr.command

import net.mamoe.mirai.console.command.CommandManager

object RegCommand {
    fun registerCommand() {
        CommandManager.registerCommand(CheckMake)
        CommandManager.registerCommand(CheckLove)
        CommandManager.registerCommand(Make)
        CommandManager.registerCommand(Open)
        CommandManager.registerCommand(OpenOthers)
        CommandManager.registerCommand(Debug)
        CommandManager.registerCommand(GetLoveList)
        CommandManager.registerCommand(GetAllLoveList)
        CommandManager.registerCommand(CreateQuickly)
    }
}