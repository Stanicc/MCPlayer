package stanic.mcplayer.common.player.render

import org.mapdb.HTreeMap
import stanic.mcplayer.core.VideoLoader
import stanic.mcplayer.core.model.VideoFrame
import java.util.concurrent.Callable

class RenderWorker(
    private val videoName: String,
    private val videoPath: String
) : Callable<HTreeMap<Int, VideoFrame>> {

    override fun call(): HTreeMap<Int, VideoFrame> {
        return VideoLoader(videoName, videoPath).loadVideo()
    }

}