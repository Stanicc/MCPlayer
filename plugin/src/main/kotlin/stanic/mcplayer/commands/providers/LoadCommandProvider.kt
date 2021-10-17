package stanic.mcplayer.commands.providers

import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import stanic.mcplayer.common.VideoManager
import stanic.mcplayer.common.extensions.*

class LoadCommandProvider(
    private val videoManager: VideoManager,
    private val sender: Player,
    private val args: Array<out String>
) {

    operator fun invoke() {
        if (args.size != 2) sender.send("&cUse: /player load (name)")
        else {
            val name = args[1]
            val directions = arrayOf(BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST)

            videoManager.loadVideo(sender, name, directions)
        }
    }

}