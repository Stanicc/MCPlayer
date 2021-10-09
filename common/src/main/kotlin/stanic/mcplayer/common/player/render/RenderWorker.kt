package stanic.mcplayer.common.player.render

import org.bukkit.Location
import org.bukkit.block.BlockFace
import stanic.mcplayer.common.player.model.VideoModel
import stanic.mcplayer.core.ImageReader
import stanic.mcplayer.core.enum.ImageType
import java.io.IOException
import java.util.concurrent.Callable

class RenderWorker(
    private val folder: String,
    private val direction: BlockFace,
    private val location: Location,
    private val position: Int
) : Callable<VideoModel.VideoFrame> {

    override fun call(): VideoModel.VideoFrame? {
        val reader = ImageReader.read(folder, position) ?: return null

        val imageType = when {
            reader.first.extension.lowercase() == "png" -> ImageType.PNG
            else -> ImageType.JPEG
        }

        return VideoModel.VideoFrame(direction, location, reader.second, position, imageType)
    }

}