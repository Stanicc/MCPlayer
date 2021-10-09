package stanic.mcplayer.common.player.canvas

import org.bukkit.Location
import org.bukkit.World
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import stanic.mcplayer.common.PlayerScreen
import java.awt.image.BufferedImage
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

class CanvasSection(
    val world: World,
    val image: BufferedImage?,
    val x: Byte,
    val y: Byte
) {

    private val pixels = PlayerScreen.getPixels(image!!)

    var direction: BlockFace? = null
    var location: Location? = null

    var frameId = ID_COUNTER.getAndIncrement()

    @Transient
    var mapId = 0
        private set

    @Transient
    var shown: MutableSet<UUID> = HashSet()

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

    fun defineLocation(location: Location) = when (direction) {
            BlockFace.UP -> this.location = location.clone().add(x.toDouble(), 0.0, y.toDouble())
            BlockFace.DOWN -> this.location = location.clone().add(x.toDouble(), 0.0, -y.toDouble())
            BlockFace.NORTH -> this.location = location.clone().add(-x.toDouble(), -y.toDouble(), 0.0)
            BlockFace.SOUTH -> this.location = location.clone().add(x.toDouble(), -y.toDouble(), 0.0)
            BlockFace.EAST -> this.location = location.clone().add(0.0, -y.toDouble(), -x.toDouble())
            BlockFace.WEST -> this.location = location.clone().add(0.0, -y.toDouble(), x.toDouble())
            else -> this.location = location
        }

    private fun writeObject(out: ObjectOutputStream) {
        out.defaultWriteObject()
    }

    private fun readObject(inputStream: ObjectInputStream) {
        inputStream.defaultReadObject()
        frameId = ID_COUNTER.getAndIncrement()
        shown = HashSet()
    }

    fun setMapId() {
        mapId = PlayerScreen.getNextMapId(location!!)
    }

    companion object {
        private const val serialVersionUID = 1337133713371337L
        private const val DEFAULT_STARTING_ID = Int.MAX_VALUE / 4
        private val ID_COUNTER = AtomicInteger(DEFAULT_STARTING_ID)
    }

}