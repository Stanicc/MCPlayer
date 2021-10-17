package stanic.mcplayer.common.player.controller

import org.bukkit.plugin.Plugin
import stanic.mcplayer.common.VideoManager
import stanic.mcplayer.common.player.VideoPlayer
import stanic.mcplayer.common.player.canvas.Canvas
import stanic.mcplayer.common.player.model.VideoModel

class PlayerController(
    private val plugin: Plugin,
    private val videoManager: VideoManager
) {

    fun createPlayer(canvas: Canvas, video: VideoModel): VideoPlayer {
        return VideoPlayer(plugin, canvas, video, "p${videoManager.players.size + 1}")
    }

    fun start(player: VideoPlayer) {
        player.start()
    }

    fun stop(player: VideoPlayer) {
        player.clearShown()
        player.stop()
    }

}