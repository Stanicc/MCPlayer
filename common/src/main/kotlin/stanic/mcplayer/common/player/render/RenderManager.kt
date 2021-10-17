package stanic.mcplayer.common.player.render

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import org.mapdb.HTreeMap
import stanic.mcplayer.common.VideoManager
import stanic.mcplayer.common.player.canvas.Canvas
import stanic.mcplayer.common.player.canvas.CanvasSection
import stanic.mcplayer.core.model.VideoFrame

class RenderManager(
    private val videoManager: VideoManager,
    private val name: String
) {

    fun renderImages(folder: String) {
        val video = videoManager.canvases.values.find { it.video.name == name }?.video!!
        val frames = RenderWorker(name, folder).call()

        val map = HashMap<VideoFrame, List<CanvasSection>>()
        for (selected in frames.values.sortedBy { it!!.position }) {
            val list = ArrayList<CanvasSection>()
            selected!!.sections.forEach { list.add(CanvasSection(it)) }

            map[selected] = list
        }
        video.frames = map
    }

    fun renderImagesAsync(folder: String) = GlobalScope.async {
        try {
            val video = videoManager.canvases.values.find { it.video.name == name }?.video!!
            val frames = RenderWorker(name, folder).call()

            val map = HashMap<VideoFrame, List<CanvasSection>>()
            for (selected in frames.values.sortedBy { it!!.position }) {
                val list = ArrayList<CanvasSection>()
                selected!!.sections.forEach { list.add(CanvasSection(it)) }

                map[selected] = list
            }
            video.frames = map
        } catch (exception: Exception) {
            exception.printStackTrace()
        }

        cancel("Render finished")
    }

}