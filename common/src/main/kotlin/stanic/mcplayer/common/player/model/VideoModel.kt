package stanic.mcplayer.common.player.model

import org.bukkit.Location
import org.bukkit.block.BlockFace
import stanic.mcplayer.common.player.canvas.CanvasSection
import stanic.mcplayer.core.enum.ImageType
import stanic.mcplayer.core.utils.ImageResizer
import java.awt.image.BufferedImage
import kotlin.math.max

data class VideoModel(
    val name: String,
    var frames: MutableList<VideoFrame>
)  {

    class VideoFrame(
        val direction: BlockFace?,
        val location: Location,
        var image: BufferedImage,
        val position: Int,
        val type: ImageType = ImageType.PNG
    ) : Comparable<VideoFrame> {

        var sections = ArrayList<CanvasSection>()

        override operator fun compareTo(other: VideoFrame): Int {
            return position.compareTo(other.position)
        }

        init {
            val xSections = max(image.width / 128, 1)
            val ySections = max(image.height / 128, 1)
            ImageResizer.resizeToMapScale(image, xSections, ySections, type)

            for (x in 0 until xSections) {
                for (y in 0 until ySections) {
                    val section = CanvasSection(
                        location.world!!, image.getSubimage(
                            x * 128, y * 128,
                            128, 128
                        ), x.toByte(), y.toByte()
                    )

                    sections.add(section)
                }
            }
        }

    }

}