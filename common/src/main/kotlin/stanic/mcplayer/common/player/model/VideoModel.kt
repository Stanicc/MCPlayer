package stanic.mcplayer.common.player.model

import stanic.mcplayer.common.player.canvas.CanvasSection
import stanic.mcplayer.core.model.VideoFrame

data class VideoModel(
    val name: String,
    var frames: HashMap<VideoFrame, List<CanvasSection>>?
)