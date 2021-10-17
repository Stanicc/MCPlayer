package stanic.mcplayer.commands.providers

import org.bukkit.entity.Player
import stanic.mcplayer.common.VideoManager
import stanic.mcplayer.common.extensions.*

class CreateCommandProvider(
    private val videoManager: VideoManager,
    private val sender: Player,
    private val args: Array<out String>
) {

    operator fun invoke() {
        if (args.size < 3) sender.send("&cUse: &7/player create (link or id) (name) (scale or none : default = 100)")
        else {
            val url = args[1]
            val name = args[2]
            val scale = if (args.size > 3) parseInt(args[3]) else 100

            sender.send(listOf("&aDownloading the video &f$name - $url", "&7This may take a few minutes"))
            videoManager.createVideo(sender, url, name, scale)
        }
    }

    private fun parseInt(target: String): Int = try {
        Integer.parseInt(target)
    } catch (exception: Exception) { 100 }

}