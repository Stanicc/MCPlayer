package stanic.mcplayer.common

import com.sun.beans.finder.FieldFinder.findField
import net.minecraft.server.v1_16_R3.*
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.block.BlockFace
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld
import org.bukkit.craftbukkit.v1_16_R3.block.CraftBlock
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer
import org.bukkit.entity.Player
import org.bukkit.map.MapPalette
import org.bukkit.map.MapView
import stanic.mcplayer.common.constants.STARTING_ID
import stanic.mcplayer.common.utils.setFieldValue
import java.awt.Color
import java.awt.image.BufferedImage
import java.util.*
import java.util.Collections.emptyList
import java.util.concurrent.atomic.AtomicInteger

object PlayerScreen {

    private val ENTITY_ID = findField(Entity::class.java, "as")
    private val MAPS = HashMap<UUID, AtomicInteger>(4)
    private val PREVIOUS_MAPS = HashMap<Location, Int>()

    fun getWorldMap(id: Int): MapView? {
        return Bukkit.getMap(id)
    }

    fun getNextMapId(location: Location): Int {
        var id = PREVIOUS_MAPS[location]
        if (id != null) return id

        id = MAPS.computeIfAbsent(location.world!!.uid) { AtomicInteger(STARTING_ID) }.getAndIncrement()
        PREVIOUS_MAPS[location] = id

        return id
    }

    fun getPixels(image: BufferedImage): ByteArray {
        val pixelCount = image.width * image.height
        val pixels = IntArray(pixelCount).also { image.getRGB(0, 0, image.width, image.height, it, 0, image.width) }
        val colors = ByteArray(pixelCount)

        for (i in 0 until pixelCount) {
            colors[i] = MapPalette.matchColor(Color(pixels[i], true))
        }

        return colors
    }

    fun createScreen(
        frameId: Int, mapId: Int,
        player: Player, location: Location,
        direction: BlockFace?, pixels: ByteArray?
    ) {
        val item = ItemStack(Items.FILLED_MAP)
        item.orCreateTag.setInt("map", mapId)

        val frame = EntityItemFrame(
            (player.world as CraftWorld).handle,
            BlockPosition(location.x, location.y, location.z),
            CraftBlock.blockFaceToNotch(direction)
        )
        frame.setItem(item, false, false)
        setFieldValue(ENTITY_ID, frame, frameId)

        val connection = (player as CraftPlayer).handle.playerConnection
        connection.sendPacket(
            PacketPlayOutSpawnEntity(
                frame, EntityTypes.ITEM_FRAME,
                frame.direction.c(), frame.getBlockPosition()
            )
        )
        connection.sendPacket(PacketPlayOutEntityMetadata(frame.id, frame.dataWatcher, true))
        connection.sendPacket(PacketPlayOutMap(mapId, 3.toByte(), false, false, emptyList(), pixels, 0, 0, 128, 128))
    }

    fun removeScreen(player: Player, frame: Int) = removeScreen(player, intArrayOf(frame))
    fun removeScreen(player: Player, frames: IntArray) {
        (player as CraftPlayer).handle.playerConnection.sendPacket(PacketPlayOutEntityDestroy(*frames))
    }

}