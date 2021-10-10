package stanic.mcplayer.core.service

import com.github.kiulian.downloader.YoutubeDownloader
import com.github.kiulian.downloader.downloader.YoutubeProgressCallback
import com.github.kiulian.downloader.downloader.request.RequestVideoFileDownload
import com.github.kiulian.downloader.downloader.request.RequestVideoInfo
import com.github.kiulian.downloader.model.videos.formats.Format
import stanic.mcplayer.core.callback.DownloaderCallback
import java.io.File

class YoutubeDownloaderService {

    fun download(videoId: String, downloadFolder: File, callback: DownloaderCallback) {
        val downloader = YoutubeDownloader()
        val request = RequestVideoInfo(videoId)
        val video = downloader.getVideoInfo(request).data()
        if (video == null) {
            callback.onError(NullPointerException())
            return
        }

        //https://gist.github.com/sidneys/7095afe4da4ae58694d128b1034e01e2
        var format = video.findFormatByItag(18) ?: video.findFormatByItag(83)
        if (format == null) format = video.formats()[0]

        requestDownload(downloader, format, downloadFolder, callback)
    }

    private fun requestDownload(downloader: YoutubeDownloader, format: Format, downloadFolder: File, callback: DownloaderCallback) {
        val request = RequestVideoFileDownload(format)
            .saveTo(downloadFolder)
            .callback(object : YoutubeProgressCallback<File> {
                override fun onDownloading(progress: Int) {
                }

                override fun onFinished(data: File) {
                    callback.onFinish(data)
                }

                override fun onError(throwable: Throwable) {
                    callback.onError(throwable)
                }
            })
            .async()

        downloader.downloadVideoFile(request)
    }

}