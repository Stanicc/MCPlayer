package stanic.mcplayer.common

import org.bukkit.Bukkit
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import stanic.mcplayer.common.extensions.send
import stanic.mcplayer.common.player.VideoPlayer
import stanic.mcplayer.common.player.canvas.Canvas
import stanic.mcplayer.common.player.controller.PlayerController
import stanic.mcplayer.common.player.model.VideoModel
import stanic.mcplayer.common.player.render.RenderManager
import stanic.mcplayer.core.VideoCreator
import stanic.mcplayer.core.callback.CreateError
import stanic.mcplayer.core.callback.VideoCreateCallback
import java.io.File
import java.util.ArrayList
import java.util.HashMap
import kotlin.math.roundToInt

class VideoManager(
    private val plugin: Plugin
) {

    private val videosPath = "${plugin.dataFolder.path}/videos"

    var canvases = HashMap<String, Canvas>()
    var players = HashMap<String, VideoPlayer>()
    private var rendering = ArrayList<String>()

    private val playerController = PlayerController(plugin, this)

    fun createVideo(sender: Player, url: String, name: String, scale: Int) {
        val ffmpegPath = "${plugin.dataFolder.path}/ffmpeg/bin"
        val path = "$videosPath/$name"

        VideoCreator(ffmpegPath).create(name, url, path, scale, object : VideoCreateCallback {
            override fun onError(error: CreateError) {
                sender.send(when (error) {
                    CreateError.NOT_FOUND -> "&cThis video does not exists"
                    CreateError.DOWNLOAD_ERROR -> "&cAn error has occurred with the download. Create cancelled"
                    CreateError.ALREADY_EXISTS -> "&cThis name already exists in videos folder"
                })
            }

            override fun onDownloaded() {
                sender.send("&aDownload finished! Converting video to the supported format (GIF)...")
            }

            override fun onCreated() {
                sender.send("&aVideo created! Run &b/player load $name &ato load it and &b/player start $name &ato play")
            }
        })
    }

    fun loadVideo(sender: Player, name: String, directions: Array<BlockFace>) {
        if (canvases.values.find { it.video.name == name } != null) {
            sender.send("&cThis video is already loaded")
            return
        }
        if (rendering.contains(name)) {
            sender.send("&cThis video is already being loaded")
            return
        }
        sender.send("&aLoading video $name...")

        val path = "$videosPath/$name"

        if (!File(path, "$name.gif").exists()) {
            sender.send("&cThis video was not found")
            return
        }

        val videoModel = VideoModel(name, null)
        val canvas = Canvas(
            directions[(sender.location.yaw / 90f).roundToInt() and 0x3].oppositeFace,
            sender.location,
            videoModel
        )

        canvases["canvas_${canvases.size + 1}"] = canvas
        RenderManager(this, name).renderImagesAsync(path).invokeOnCompletion {
            sender.send("&aVideo loaded! Run &b/player start $name &ato play it")
        }
    }

    fun playVideo(sender: Player, name: String) {
        val canvas = canvases.values.find { it.video.name == name }
        if (canvas == null) {
            sender.send("&cThis video is not loaded! Use &7/player load &cto load it")
            return
        }

        val video = canvas.video
        val player = playerController.createPlayer(canvas, video).also { players[it.id] = it }

        for (target in Bukkit.getOnlinePlayers())
            canvas.addWatcher(target)

        playerController.start(player)
        sender.send("&aVideo &f$name &astarted! Run &b/player stop ${player.id} &fto stop it")
    }

    fun stopVideo(sender: Player, id: String) {
        if (!players.containsKey(id)) {
            sender.send("&cThis player does not exists")
            return
        }

        val player = players[id]!!.also { players.remove(id) }
        playerController.stop(player)

        sender.send("&aVideo &f${player.video.name} &astopped")
    }

}