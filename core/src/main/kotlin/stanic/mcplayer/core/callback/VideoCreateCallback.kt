package stanic.mcplayer.core.callback

interface VideoCreateCallback {
    fun onCreated()
    fun onDownloaded()
    fun onError(error: CreateError)
}

enum class CreateError {

    ALREADY_EXISTS, DOWNLOAD_ERROR, NOT_FOUND;

}