package stanic.mcplayer.core.model

import java.io.ByteArrayOutputStream
import java.io.Serializable

data class SerializableVideoFrame(
    var image: ByteArrayOutputStream,
    val position: Int,
    val delay: Int = 0
) : Comparable<VideoFrame>, Serializable {

    override operator fun compareTo(other: VideoFrame): Int {
        return position.compareTo(other.position)
    }

}