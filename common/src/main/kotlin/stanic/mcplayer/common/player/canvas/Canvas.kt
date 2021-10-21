package stanic.mcplayer.common.player.canvas

import org.bukkit.Location
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import stanic.mcplayer.common.PlayerScreen.removeScreen
import stanic.mcplayer.common.player.model.VideoModel

class Canvas(
    private val blockFace: BlockFace,
    var location: Location,
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
        for (selected in sections) {
            for (player in watchers) {
                removeScreen(player, selected.frameId)
            }
        }
    }

    fun update(sections: List<CanvasSection>) {
        for (i in this.sections.indices) {
            val section = sections[i]

            section.direction = blockFace
            section.setLocation(location)
            section.setMapId()

            refresh(section)

            if (i != 0) destroy(this.sections[i - 1])
            if (i == sections.indices.last) destroy(this.sections[i])
        }

        this.sections = ArrayList(sections)
    }

}