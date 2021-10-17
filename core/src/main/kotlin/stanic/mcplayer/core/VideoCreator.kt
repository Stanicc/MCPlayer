package stanic.mcplayer.core

import com.github.kokorin.jaffree.StreamType
import com.github.kokorin.jaffree.ffmpeg.*
import com.madgag.gif.fmsware.AnimatedGifEncoder
import stanic.mcplayer.core.callback.CreateError
import stanic.mcplayer.core.callback.DownloaderCallback
import stanic.mcplayer.core.callback.VideoCreateCallback
import stanic.mcplayer.core.service.YoutubeDownloaderService
import stanic.mcplayer.core.utils.ImageResizer
import java.io.File
import java.nio.file.Paths

class VideoCreator(
    private val ffmpegPath: String
) {

    fun create(name: String, url: String, path: String, scale: Int, callback: VideoCreateCallback) {
        if (File(path).exists()) {
            callback.onError(CreateError.ALREADY_EXISTS)
            return
        }

        val videoId = if (url.startsWith("http", true)) url.split("watch?v=")[1] else url

        YoutubeDownloaderService().download(videoId, File(path), object : DownloaderCallback {
            override fun onError(throwable: Throwable) {
                if (throwable == NullPointerException()) callback.onError(CreateError.NOT_FOUND)
                else callback.onError(CreateError.DOWNLOAD_ERROR)
                throwable.printStackTrace()
            }

            override fun onFinish(file: File) {
                callback.onDownloaded()

                val bin = Paths.get(ffmpegPath)
                val input = Paths.get(file.path)

                val animatedGif = AnimatedGifEncoder()
                animatedGif.start("$path/$name.gif")
                animatedGif.setDelay(1000/20)

                var framePosition = 0
                FFmpeg.atPath(bin)
                    .addInput(UrlInput.fromPath(input))
                    .addArguments("-i", file.path)
                    .addArguments("-filter:v", "fps=fps=20")
                    .addOutput(
                        FrameOutput.withConsumer(object : FrameConsumer {
                            override fun consumeStreams(streams: MutableList<Stream>?) {
                            }

                            override fun consume(frame: Frame?) {
                                if (frame == null) {
                                    animatedGif.finish()
                                    callback.onCreated()
                                }
                                else {
                                    if (frame.image != null) {
                                        framePosition += 1

                                        val image = ImageResizer.resizeToMapScale(frame.image, scale)
                                        animatedGif.addFrame(image)
                                    }
                                }
                            }
                        })
                            .setFrameRate(20)
                            .disableStream(StreamType.AUDIO)
                            .disableStream(StreamType.SUBTITLE)
                            .disableStream(StreamType.DATA)
                    )
                    .executeAsync()
            }
        })
    }

}