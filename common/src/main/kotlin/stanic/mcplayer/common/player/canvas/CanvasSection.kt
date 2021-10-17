package stanic.mcplayer.common.player.canvas

import org.bukkit.Location
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import stanic.mcplayer.common.PlayerScreen
import stanic.mcplayer.core.model.VideoFrame
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

class CanvasSection(
    frameSection: VideoFrame.FrameSection
) {

    private val x = frameSection.x
    private val y = frameSection.y
    private val pixels = PlayerScreen.getPixels(frameSection.subImage)

    var direction: BlockFace? = null
    private var location: Location? = null

    var frameId = ID_COUNTER.getAndIncrement()

    private var mapId = 0
    private var shown: MutableSet<UUID> = HashSet()

    @Synchronized
    fun show(player: Player) {
        if (shown.add(player.uniqueId))
            PlayerScreen.createScreen(frameId, mapId, player, location!!, direction, pixels)
    }

    fun clearShown() {
        shown.clear()
    }

    @Synchronized
    fun hide(player: Player) {
        if (shown.remove(player.uniqueId))
            PlayerScreen.removeScreen(player, frameId)
    }

    fun setLocation(location: Location) = when (direction) {
        BlockFace.UP -> this.location = location.clone().add(x.toDouble(), 0.0, y.toDouble())
        BlockFace.DOWN -> this.location = location.clone().add(x.toDouble(), 0.0, -y.toDouble())
        BlockFace.NORTH -> this.location = location.clone().add(-x.toDouble(), -y.toDouble(), 0.0)
        BlockFace.SOUTH -> this.location = location.clone().add(x.toDouble(), -y.toDouble(), 0.0)
        BlockFace.EAST -> this.location = location.clone().add(0.0, -y.toDouble(), -x.toDouble())
        BlockFace.WEST -> this.location = location.clone().add(0.0, -y.toDouble(), x.toDouble())
        else -> this.location = location
    }

    fun setMapId() {
        mapId = PlayerScreen.getNextMapId(location!!)
    }

    companion object {
        private const val DEFAULT_STARTING_ID = Int.MAX_VALUE / 4
        private val ID_COUNTER = AtomicInteger(DEFAULT_STARTING_ID)
    }

}