package stanic.mcplayer.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import stanic.mcplayer.commands.providers.CreateCommandProvider
import stanic.mcplayer.commands.providers.LoadCommandProvider
import stanic.mcplayer.commands.providers.StartCommandProvider
import stanic.mcplayer.commands.providers.StopCommandProvider
import stanic.mcplayer.common.VideoManager
import stanic.mcplayer.common.extensions.*

class PlayerCommand(
    private val videoManager: VideoManager
) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) sender.send("&cThis command is allowed only in-game")
        else {
            if (args.isEmpty()) {
                sender.send(listOf(
                    "&4Args empty, use:",
                    " ",
                    "&8* &7/player create (link or id) (name) (scale or none : default = 100)",
                    "&8* &7/player load (name)",
                    "&8* &7/player start (name)",
                    "&8* &7/player stop (player id)",
                    " "
                ))
                return false
            }

            when (args[0]) {
                "create" -> CreateCommandProvider(videoManager, sender, args).invoke()
                "load", "render" -> LoadCommandProvider(videoManager, sender, args).invoke()
                "start", "play", "run" -> StartCommandProvider(videoManager, sender, args).invoke()
                "stop" -> StopCommandProvider(videoManager, sender, args).invoke()
            }
        }
        return true
    }

}