package stanic.mcplayer.common.player

import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import stanic.mcplayer.common.player.canvas.Canvas
import stanic.mcplayer.common.player.canvas.CanvasSection
import stanic.mcplayer.common.player.model.VideoModel

class VideoPlayer(
    private val plugin: Plugin,
    private val canvas: Canvas,
    val video: VideoModel,
    val id: String,
    private val loop: Boolean = true
) {

    var currentFrame = 0
    var playerTask = -1

    fun start() {
        if (playerTask != -1) return
        currentFrame = 0

        object : BukkitRunnable() {
            override fun run() {
                if (currentFrame == 0) playerTask = taskId

                try {
                    val sections = next()
                    canvas.update(sections)

                    currentFrame += 1
                } catch (ignored: Exception) {
                    clearShown()

                    if (!loop) stop()
                    else {
                        for (target in Bukkit.getOnlinePlayers()) canvas.addWatcher(target)
                        currentFrame = 0
                    }
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0L, 1L)
    }

    fun stop() {
        currentFrame = 0
        clearShown()

        if (playerTask != -1) {
            Bukkit.getScheduler().cancelTask(playerTask)
            playerTask = -1
        }
    }

    fun clearShown() {
        canvas.destroyAll()

        for (frame in video.frames!!) {
           for (section in frame.value) section.clearShown()
        }
    }

    operator fun next(): List<CanvasSection> {
        val selected = video.frames!!.keys.find { it.position == currentFrame }
        return video.frames!![selected]!!
    }

}