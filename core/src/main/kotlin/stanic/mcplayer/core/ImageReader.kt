package stanic.mcplayer.core

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

object ImageReader {

    fun read(folder: String, position: Int): Pair<File, BufferedImage>? {
        val directory = File(folder)
        if (!directory.exists()) directory.mkdir()
        val selected = directory.listFiles { _, name -> name.startsWith("$position") }.firstOrNull() ?: return null

        return selected to ImageIO.read(selected)
    }

}