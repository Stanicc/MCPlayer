package stanic.mcplayer.core

import java.awt.Image
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.ceil

object ImageReader {

    fun read(folder: String, position: Int): Pair<File, BufferedImage>? {
        val directory = File(folder)
        if (!directory.exists()) directory.mkdir()
        val selected = directory.listFiles { _, name -> name.startsWith("$position") }.firstOrNull() ?: return null

        return selected to ImageIO.read(selected)
    }

    fun resizeToMapScale(image: BufferedImage, scale: Int): BufferedImage {
        val scaled = image.getScaledInstance(
            ceil(image.width / 128 * scale.toDouble()).toInt(),
            ceil(image.height / 128 * scale.toDouble()).toInt(),
            Image.SCALE_SMOOTH
        )

        val resized = BufferedImage(
            scaled.getWidth(null),
            scaled.getHeight(null),
            BufferedImage.TYPE_INT_ARGB
        )

        val graphics = resized.createGraphics()
        graphics.drawImage(scaled, 0, 0, null)
        graphics.dispose()

        return resized
    }

}