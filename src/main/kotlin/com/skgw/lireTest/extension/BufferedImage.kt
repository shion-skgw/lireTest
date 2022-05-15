package com.skgw.lireTest.extension

import java.awt.Image
import java.awt.image.BufferedImage
import java.io.BufferedOutputStream
import java.io.ByteArrayOutputStream
import java.util.Base64
import javax.imageio.ImageIO

fun BufferedImage.resize(maxSize: Double): BufferedImage {
    // サイズを算出する
    val scale = this.scale()
    val width = (maxSize * scale.width).toInt()
    val height = (maxSize * scale.height).toInt()

    // リサイズを実行する
    val scaledInstance = this.getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING)
    val newImage = BufferedImage(width, height, this.type)
    newImage.graphics.drawImage(scaledInstance, 0, 0, width, height, null)
    return newImage
}

fun BufferedImage.toBase64(): String {
    ByteArrayOutputStream().use { byteArrayOutputStream ->
        BufferedOutputStream(byteArrayOutputStream).use { bufferedOutputStream ->
            ImageIO.write(this, "jpeg", bufferedOutputStream)
            bufferedOutputStream.flush()
            return String(Base64.getEncoder().encode(byteArrayOutputStream.toByteArray()))
        }
    }
}

private data class SizeScale(val width: Double, val height: Double)

private fun BufferedImage.scale(): SizeScale {
    return when {
        width > height -> SizeScale(1.0, height.toDouble() / width.toDouble())
        width < height -> SizeScale(width.toDouble() / height.toDouble(), 1.0)
        else -> SizeScale(1.0, 1.0)
    }
}
