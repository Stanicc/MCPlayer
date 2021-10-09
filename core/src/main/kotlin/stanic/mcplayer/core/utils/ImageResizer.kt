package stanic.mcplayer.core.utils

import stanic.mcplayer.core.enum.ImageType
import java.awt.Image
import java.awt.image.BufferedImage
import kotlin.math.ceil

object ImageResizer {

    private const val PIXELS_PER_FRAME = 128

    fun resizeToMapScale(image: BufferedImage, xSections: Int, ySections: Int, type: ImageType): BufferedImage {
        var resized = image
        if (resized.width % PIXELS_PER_FRAME == 0 && resized.height % PIXELS_PER_FRAME == 0) return image

        val scaled = resized.getScaledInstance(xSections * PIXELS_PER_FRAME, ySections * PIXELS_PER_FRAME, Image.SCALE_DEFAULT)
        resized = BufferedImage(scaled.getWidth(null), scaled.getHeight(null), if (type == ImageType.PNG) BufferedImage.TYPE_INT_ARGB else BufferedImage.TYPE_INT_RGB)

        val g2D = resized.createGraphics()
        g2D.drawImage(scaled, 0, 0, null)
        g2D.dispose()

        return resized
    }

    fun resizeToMapScale(image: BufferedImage, scale: Int): BufferedImage {
        val scaled = image.getScaledInstance(
            ceil(image.width / PIXELS_PER_FRAME * scale.toDouble()).toInt(),
            ceil(image.height / PIXELS_PER_FRAME * scale.toDouble()).toInt(),
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