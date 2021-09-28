package com.example.facedetection.view

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.facedetection.R
import com.example.facedetection.model.ImageProcessing
import com.example.facedetection.model.datamodel.facesinfo.FacesInfo
import com.example.facedetection.model.datamodel.facesinfo.Photo
import com.example.facedetection.model.datamodel.facesinfo.Tag

class ProcessedImageActivity : AppCompatActivity() {

    private lateinit var photo: ImageView
    private lateinit var image: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_processed_image)

        photo = findViewById(R.id.processed_image_activity_photo)
        image = findViewById(R.id.processed_image_activity_rectangles)

        if (intent.hasExtra("faces_info")) {
            val facesInfo = intent.getParcelableExtra<FacesInfo>("faces_info")
            val imageUrl =
                facesInfo!!.photos[0].url                                            // !!
            Glide.with(this).load(imageUrl).into(photo)

            Log.e("processed image", facesInfo.toString())

            //val faces = facesInfo.photos[0].tags
            val photo = facesInfo.photos[0]

            //ImageProcessing.drawRectangles(faces)

            ////////////////
            /*for(element in faces) {

            }*/
            ///////////////


            drawRectangle(photo)

        }

    }


    ////////////////////
    /*fun Bitmap.drawRectangle(): Bitmap {
        val bitmap = copy(config, true)
        val canvas = Canvas(bitmap)

        Paint().apply {
            color = Color.RED
            isAntiAlias = true

            canvas.drawRect(20f, 20f, width/3 - 20f, height - 20f, this)
        }

        return bitmap
    }*/


    fun drawRectangle(photo: Photo) {

        // tworzenie bitmap z url zdjęcia:
        //val url = URL(imageUrl)
        /*val connection = url.openConnection()
        connection.doInput = true
        connection.connect()
        val input = connection.getInputStream()
        val backgroundPhoto = BitmapFactory.decodeStream(input)*/
        //val imageUri = Uri.parse(imageUrl)
        //val im = Uri.parse(imageUrl)
        //val imageUri = ContentUris.parseId(im)

        val photoWidth = photo.width
        val photoHeight = photo.height

        val faces = photo.tags


        // rysowanie kształtu:
        val bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)

        //val bitmap = Bitmap.createBitmap(photoWidth, photoHeight, Bitmap.Config.ARGB_8888)
        //val bitmap = Bitmap.createBitmap(backgroundPhoto, 50, 50, 100, 100)
        //val bitmap = Bitmap.createBitmap(backgroundPhoto, 100, 100, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        //paint.setColor(Color.RED)
        paint.style = Paint.Style.STROKE
        //paint.strokeWidth(3)
        //canvas.drawCircle(30f, 70f, 30f, paint)
        //canvas.drawRect(7f, 20f, 30f, 20f, paint)
        //canvas.drawRect(10F, 20F, 10F, 10F, paint)
        //image.setImageURI(imageUri)
        //image.setBackgroundResource(R.drawable.ic_launcher_background)
        //image.setImageURI(imageUri)

        for (face in faces) {
            if (face.attributes.ageEst.value.toInt() < 16) {
                paint.color = Color.BLUE
            } else {
                paint.color = Color.GREEN
            }

            val centerX = face.center.x.toFloat()
            val centerY = face.center.y.toFloat()

            val width = face.width.toFloat()
            val height = face.height.toFloat()

            /*Log.e("width", width.toString())
            Log.e("height", height.toString())
            Log.e("center x", centerX.toString())
            Log.e("center y", centerY.toString())*/

            /*val left = centerX - (centerX/2)
            val right = centerX + 2*centerX
            val top = centerY - (centerY/2)
            val bottom = centerY + 2*centerY*/

            val left = centerX + width / 2
            val right = centerX - width / 2
            val top = centerY - height / 2
            val bottom = centerY + height / 2

            //canvas.drawRect(width, height, width, height, paint)
            //canvas.drawRect(10f, 0f, 50f, 90f, paint)
            canvas.drawRect(left, top, right, bottom, paint)


            //image.setBackgroundColor(Color.GREEN)
        }
        image.setImageBitmap(bitmap)

    }
    //////////////////
}