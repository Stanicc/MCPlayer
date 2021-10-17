package stanic.mcplayer.core

import com.madgag.gif.fmsware.GifDecoder
import org.mapdb.DB
import org.mapdb.DBMaker
import org.mapdb.HTreeMap
import stanic.mcplayer.core.model.VideoFrame
import stanic.mcplayer.core.model.VideoFrameSerializer

class VideoLoader(
    private val videoName: String,
    private val videoPath: String
) {

    private lateinit var db: DB
    private lateinit var map: HTreeMap<Int, VideoFrame>

    fun loadVideo(): HTreeMap<Int, VideoFrame> {
        db = DBMaker
            .fileDB("$videoPath/$videoName.db")
            .make()
        map = db.hashMap("map_$videoName").valueSerializer(VideoFrameSerializer()).createOrOpen() as HTreeMap<Int, VideoFrame>

        val gifDecoder = GifDecoder(map)
        gifDecoder.read("$videoPath/$videoName.gif")

        println("loaded ${map.size} frames")
        return map
    }

}