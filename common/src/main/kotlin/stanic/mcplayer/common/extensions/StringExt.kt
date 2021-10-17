package stanic.mcplayer.common.extensions

import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

fun CommandSender.send(message: String) = sendMessage(message.replaceColor())
fun CommandSender.send(messages: List<String>) {
    for (line in messages)
        sendMessage(line.replaceColor())
}

fun String.replaceColor() = ChatColor.translateAlternateColorCodes('&', this)