package stanic.mcplayer.common.player.render

import org.bukkit.Location
import org.bukkit.block.BlockFace
import org.bukkit.command.CommandSender
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import stanic.mcplayer.common.player.model.VideoModel

class RenderManager(
    private val plugin: Plugin,
    val location: Location,
    val blockFace: BlockFace,
    val sender: CommandSender,
    val name: String
) {

    fun renderImages(folder: String) {
        val frames: MutableList<VideoModel.VideoFrame> = ArrayList()

        object : BukkitRunnable() {
            override fun run() {
                var position = 0

                do {
                    val videoFrame = RenderWorker(folder, blockFace, location, ++position).call()
                    if (videoFrame != null) frames.add(videoFrame)
                } while (videoFrame != null)

                cancel()
            }
        }.runTaskAsynchronously(plugin)
    }

}