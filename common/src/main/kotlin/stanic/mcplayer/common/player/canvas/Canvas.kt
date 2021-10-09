package stanic.mcplayer.common.player.canvas

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import stanic.mcplayer.common.PlayerScreen.removeScreen
import stanic.mcplayer.common.player.model.VideoModel
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class Canvas(
    val blockFace: BlockFace,
    @Transient var location: Location,
    val video: VideoModel
) {

    private var sections = ArrayList<CanvasSection>()
    private var watchers = HashSet<Player>()

    fun addWatcher(player: Player) {
        watchers.add(player)
    }
    fun removeWatcher(player: Player) {
        watchers.remove(player)
    }

    fun refresh(section: CanvasSection) {
        for (player in watchers)
            section.show(player)
    }

    fun destroy(section: CanvasSection) {
        for (player in watchers)
            removeScreen(player, section.frameId)
    }

    fun destroyAll() {
        for (section in sections) {
            for (player in watchers) {
                removeScreen(player, section.frameId)
            }
        }
    }

    fun update(sections: List<CanvasSection>) {
        for (i in this.sections.indices) {
            destroy(this.sections[i])

            val section = sections[i]
            section.direction = blockFace
            section.defineLocation(location)
            section.setMapId()

            refresh(section)
        }

        this.sections = ArrayList(sections)
    }

    private fun writeObject(out: ObjectOutputStream) {
        out.defaultWriteObject()
        writeLocation(out, location)
    }

    private fun readObject(inputStream: ObjectInputStream) {
        inputStream.defaultReadObject()
        location = readLocation(inputStream)
        watchers = HashSet()
    }

    companion object {
        fun writeLocation(out: ObjectOutputStream, location: Location) {
            out.writeObject(location.world!!.name)
            out.writeInt(location.blockX)
            out.writeInt(location.blockY)
            out.writeInt(location.blockZ)
            out.writeFloat(location.yaw)
            out.writeFloat(location.pitch)
        }

        fun readLocation(inputStream: ObjectInputStream): Location {
            val name = inputStream.readObject() as String
            val world = Bukkit.getWorld(name)
            return Location(
                world,
                inputStream.readInt().toDouble(), inputStream.readInt().toDouble(), inputStream.readInt().toDouble(),
                inputStream.readFloat(), inputStream.readFloat()
            )
        }
    }

}