package stanic.mcplayer.commands.providers

import org.bukkit.entity.Player
import stanic.mcplayer.common.VideoManager
import stanic.mcplayer.common.extensions.send

class StartCommandProvider(
    private val videoManager: VideoManager,
    private val sender: Player,
    private val args: Array<out String>
) {

    operator fun invoke() {
        if (args.size < 2) sender.send("&cUse: /player start (name)")
        else {
            val name = args[1]
            videoManager.playVideo(sender, name)
        }
    }

}