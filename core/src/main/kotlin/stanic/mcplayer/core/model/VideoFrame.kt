package stanic.mcplayer.core.model

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.mapdb.DataInput2
import org.mapdb.DataOutput2
import org.mapdb.Serializer
import stanic.mcplayer.core.enum.ImageType
import stanic.mcplayer.core.utils.ImageResizer
import java.awt.image.BufferedImage
import java.io.*
import javax.imageio.ImageIO
import kotlin.math.max

data class VideoFrame(
    var image: BufferedImage,
    val position: Int,
    val delay: Int = 0
) : Comparable<VideoFrame> {

    var sections = ArrayList<FrameSection>()

    override operator fun compareTo(other: VideoFrame): Int {
        return position.compareTo(other.position)
    }

    init {
        val xSections = max(image.width / 128, 1)
        val ySections = max(image.height / 128, 1)
        image = ImageResizer.resizeToMapScale(image, xSections, ySections, ImageType.PNG)

        for (x in 0 until xSections) {
            for (y in 0 until ySections) {
                val section = FrameSection(
                    image.getSubimage(
                        x * 128, y * 128,
                        128, 128
                    ),
                    x.toByte(), y.toByte(),
                )

                sections.add(section)
            }
        }
    }

    class FrameSection(
        var subImage: BufferedImage,
        val x: Byte,
        val y: Byte
    )

}

internal class VideoFrameSerializer : Serializer<VideoFrame?>, Serializable {

    override fun serialize(output: DataOutput2, value: VideoFrame) {
        val byteArrayOutputStream = ByteArrayOutputStream()
        ImageIO.write(value.image, "png", byteArrayOutputStream)

        val serializableVideoFrame = SerializableVideoFrame(
            byteArrayOutputStream,
            value.position,
            value.delay
        )

        output.writeUTF(Gson().toJson(serializableVideoFrame))
    }

    override fun deserialize(input: DataInput2, available: Int): VideoFrame {
        val serializableVideoFrame = Gson().fromJson<SerializableVideoFrame>(
            input.readUTF(),
            object : TypeToken<SerializableVideoFrame>() {}.type
        )

        val inputStream = ByteArrayInputStream(serializableVideoFrame.image.toByteArray())

        return VideoFrame(
            ImageIO.read(inputStream),
            serializableVideoFrame.position,
            serializableVideoFrame.delay
        )
    }
}