
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

fun convertToGrayscale(inputImageFile: File, outputImageFile: File) {
    try {
        val originalImage = ImageIO.read(inputImageFile)
        if (originalImage == null) {
            println("Error: Could not read the input image.")
            return
        }

        val width = originalImage.width
        val height = originalImage.height
        val grayscaleImage = BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY)

        for (x in 0..<width) {
            for (y in 0..<height) {
                val color = Color(originalImage.getRGB(x, y))
                val gray = (0.299 * color.red + 0.587 * color.green + 0.114 * color.blue).toInt()
                val newColor = Color(gray, gray, gray)
                grayscaleImage.setRGB(x, y, newColor.rgb)
            }
        }

        ImageIO.write(grayscaleImage, "jpg", outputImageFile) // You can change the output format
        println("Image successfully converted to grayscale and saved to: ${outputImageFile.absolutePath}")

    } catch (e: Exception) {
        println("Error processing the image: ${e.message}")
        e.printStackTrace()
    }
}

fun main() {
    val inputImage = File("C:\\Users\\4444444\\Downloads\\IMG_20241110_190558 (1).jpg")
    val outputImage = File("C:\\Users\\4444444\\Desktop\\Book App Data\\resized_image.jpg") // Specify a filename for the output
    val originalImage = ImageIO.read(inputImage)
    if (originalImage == null) {
        println("Error: Could not read the input image.")
        return
    }

    val width = originalImage.width
    val height = originalImage.height
    println("Width = $width, Height = $height")
    convertToGrayscale(inputImage, outputImage)
}