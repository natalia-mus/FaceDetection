package com.example.facedetection.util

import android.graphics.Bitmap
import android.graphics.Color
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import com.example.facedetection.model.datamodel.RGB

object ImagePixelizator {

    private var imageWidth = 0
    private var imageHeight = 0
    private var pixelSize = 2
    private var initialIndicesSet = ArrayList<Int>()


    /**
     * Gets bitmap as an argument and returns this bitmap pixelated; pixelSize = pixel edge size
     */
    fun pixelateImage(bitmap: Bitmap, pixelSize: Int): Bitmap {
        imageWidth = bitmap.width
        imageHeight = bitmap.height
        this.pixelSize = pixelSize
        prepareInitialIndicesSet()

        val imageAsPixels = getImageAsPixels(bitmap)
        val flattenPixels = flattenPixelsArray(imageAsPixels)
        val pixelatedFlatten = pixelate(flattenPixels)
        val pixelated = reflattenPixelsArray(pixelatedFlatten)
        val result = convertPixelsIntoBitmap(pixelated)

        return result
    }


    /**
     * Converts pixels of the given bitmap into RGB objects
     */
    private fun getImageAsPixels(bitmap: Bitmap): ArrayList<ArrayList<RGB>> {
        val pixels = ArrayList<ArrayList<RGB>>()

        for (i in 0 until imageHeight) {
            val pixelsRow = ArrayList<RGB>()

            for (j in 0 until imageWidth) {
                val r = bitmap.getPixel(j, i).red
                val g = bitmap.getPixel(j, i).green
                val b = bitmap.getPixel(j, i).blue

                val pixelColor = RGB(r, g, b)
                pixelsRow.add(pixelColor)
            }

            pixels.add(pixelsRow)
        }

        return pixels
    }


    /**
     * Flattens ArrayList of ArrayList of RGBs into one dimensional ArrayList
     */
    private fun flattenPixelsArray(pixels: ArrayList<ArrayList<RGB>>): ArrayList<RGB> {
        val flattenArray = ArrayList<RGB>()

        for (i in 0 until pixels.size) {
            for (j in 0 until pixels[i].size) {
                flattenArray.add(pixels[i][j])
            }
        }

        return flattenArray
    }


    /**
     * Re-flattens ArrayList of RGBs into multiple ArrayLists of RGBs, according to image width and height
     */
    private fun reflattenPixelsArray(pixels: ArrayList<RGB>): ArrayList<ArrayList<RGB>> {
        val reflattenArray = ArrayList<ArrayList<RGB>>()

        for (i in 0 until imageHeight) {
            val bottomRange = imageWidth * i
            val topRange = bottomRange + imageWidth
            val pixelsRow = ArrayList<RGB>()

            for (j in bottomRange until topRange) {
                pixelsRow.add(pixels[j])
            }

            reflattenArray.add(pixelsRow)
        }

        return reflattenArray
    }


    /**
     * Prepares the initial set of pixel indices, according to the pixel size
     */
    private fun prepareInitialIndicesSet() {
        val indicesSetSize = imageWidth * imageHeight
        val indicesSet = ArrayList<Int>()
        for (i in 0 until indicesSetSize) {
            indicesSet.add(i)
        }

        for (i in 0 until pixelSize) {
            val bottomRange = imageWidth * i
            val topRange = bottomRange + pixelSize
            for (j in bottomRange until topRange) {
                initialIndicesSet.add(indicesSet[j])
            }
        }
    }


    /**
     * Executes the pixelization process
     */
    private fun pixelate(pixels: ArrayList<RGB>): ArrayList<RGB> {
        val pixelIndicesSet = ArrayList<Int>()                                                      // set of single pixels indices for one huge pixel
        for (i in 0 until initialIndicesSet.size) {                                                 // at the beginning the set is the same as the initial set
            pixelIndicesSet.add(i, initialIndicesSet[i])
        }


        for (i in 0 until imageHeight / pixelSize) {
            val step = i * pixelSize * pixelSize * (imageWidth / pixelSize)

            for (j in 0 until pixelIndicesSet.size) {
                pixelIndicesSet[j] = initialIndicesSet[j] + step
            }

            for (k in 0 until imageWidth / pixelSize) {
                val pixel = ArrayList<RGB>()

                if (k != 0) {
                    for (l in 0 until pixelIndicesSet.size) {
                        pixelIndicesSet[l] += pixelSize
                        pixel.add(pixels[pixelIndicesSet[l]])
                    }

                    val color = averageColor(pixel)

                    for (l in 0 until pixelIndicesSet.size) {
                        val index = pixelIndicesSet[l]
                        pixels[index] = color
                    }

                } else {
                    for (l in 0 until pixelIndicesSet.size) {
                        pixel.add(pixels[pixelIndicesSet[l]])
                    }
                    val color = averageColor(pixel)
                    for (l in 0 until pixelIndicesSet.size) {
                        val index = pixelIndicesSet[l]
                        pixels[index] = color
                    }
                }

            }
        }

        return pixels
    }


    /**
     * Returns the average color of the given set of pixels
     */
    private fun averageColor(pixels: ArrayList<RGB>): RGB {
        var r = 0
        var g = 0
        var b = 0

        for (i in 0 until pixels.size) {
            val pixel = pixels[i]
            r += pixel.red
            g += pixel.green
            b += pixel.blue
        }

        val averageR = r / (pixelSize * pixelSize)
        val averageG = g / (pixelSize * pixelSize)
        val averageB = b / (pixelSize * pixelSize)
        val result = RGB(averageR, averageG, averageB)

        return result
    }


    /**
     * Converts pixels into bitmap
     */
    private fun convertPixelsIntoBitmap(pixels: ArrayList<ArrayList<RGB>>): Bitmap {
        val pixelsAsColors = IntArray(imageWidth * imageHeight)

        for (i in 0 until imageHeight) {
            for (j in 0 until imageWidth) {
                val index = imageWidth * i + j
                val pixel = pixels[i][j]

                val r = pixel.red
                val g = pixel.green
                val b = pixel.blue

                pixelsAsColors[index] = Color.rgb(r, g, b)
            }
        }

        val bitmap = Bitmap.createBitmap(pixelsAsColors, imageWidth, imageHeight, Bitmap.Config.RGBA_F16)

        return bitmap
    }

}