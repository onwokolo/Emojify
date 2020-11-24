package com.example.emojify.ui.log

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import com.example.emojify.ApplicationStart
import com.example.emojify.ApplicationStart.Factory.context
import com.example.emojify.R
import com.example.emojify.base.BaseActivity
import com.example.emojify.storage.Entry
import com.example.emojify.storage.StorageSystem
import com.example.emojify.ui.home.EmotionClassifier
import com.example.emojify.ui.home.MainActivity
import kotlinx.android.synthetic.main.activity_data.HomeButton
import kotlinx.android.synthetic.main.activity_log.*
import org.bytedeco.javacpp.Loader
import org.bytedeco.javacpp.opencv_core
import org.bytedeco.javacpp.opencv_core.cvLoad
import org.bytedeco.javacpp.opencv_objdetect
import org.bytedeco.javacv.AndroidFrameConverter
import org.bytedeco.javacv.Frame
import org.bytedeco.javacv.Java2DFrameUtils.toMat
import org.koin.androidx.scope.currentScope
import java.io.File
import java.lang.Exception
import java.net.URL


class LogActivity : BaseActivity(), LogActivityContract.View {
    private val presenter: LogActivityContract.Presenter by currentScope.inject()
    private val storage = StorageSystem.storage
    private lateinit var imageView: ImageView
    private lateinit var grayscaleImageView: ImageView
    private var predictedTextView: TextView? = null
    private val REQUEST_IMAGE_CAPTURE = 1
    private var emotionClassifier = EmotionClassifier(this)
    private var _bitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        predictedTextView = findViewById(R.id.predicted_text)

        //Setup the emotion classifier
        emotionClassifier
            .initialize()
            .addOnFailureListener { e -> Log.e(TAG, "Error to setting up digit classifier.", e) }
    }
    override fun onStart() {
        super.onStart()
        this.imageView = CaptView
        presenter.takeView(this)

        //Comment/Uncomment/Remove the following 6 lines below...
        //I'm using them to set the display image just to one of our test images
        /*
        _bitmap = if(_bitmap != null) _bitmap
            else {
                //BitmapFactory.decodeResource(ApplicationStart.context.resources, R.drawable.neutral)
                ImagePicker.rotateResourceImage(ApplicationStart.context.resources, R.drawable.neutral)
            }
        imageView.setImageBitmap(_bitmap)

         */

        //val file: File = Loader.cacheResource("haarcascade_frontalface_default.xml")
        //val url = URL("https://raw.github.com/opencv/opencv/master/data/haarcascades/haarcascade_frontalface_alt.xml")
        //val file: File = Loader.cacheResource(url)
        //val classifierName = file.getAbsolutePath()

        HomeButton.setOnClickListener {
            val intent = Intent(
                this,
                MainActivity::class.java
            ) //first, declare an intention to do something, and set the intent as our MainActivity
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) //Any optional flags go here. This flag closes all activities of this package before running the intent (if any are running)
            startActivity(intent) //Start the intent, this will create a new process to do the intent
            finish() //Finally, finish this activity, which will call the onDestroy() method
        }
        CaptButton.setOnClickListener {
            if (imageView.drawable != null) {
                classifyImage()
            }
            else{
                predictedTextView?.text = getString(R.string.prediction_text_placeholder)
            }
        }
        ULButton.setOnClickListener {
            //TODO() request camera permissions immediately here
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                startActivityForResult(getPickImageIntent(), 1)
            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    Toast.makeText(this, "Permission is needed to grab images and camera previews",
                    Toast.LENGTH_SHORT).show()
                }
                requestPermissions(Array(1) {Manifest.permission.CAMERA}, 5);
            }

        }
    }
    @SuppressLint("RestrictedApi")
    fun getPickImageIntent(): Intent? {
        return ImagePicker.getPickImageIntent(this)
    }
    override fun onStop() {
        super.onStop()
        presenter.dropView()
    }
    override fun getLayout(): Int {
        return R.layout.activity_log
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val bitmap = ImagePicker.getImageFromResult(this, resultCode, data)
            imageView.setImageBitmap(bitmap)

            // For testing purpose...allow imageView to use
            // selected Image from camera or gallery instead of only the test image
            _bitmap = ImagePicker.getImageFromResult(this, resultCode, data)
        }
    }

    private fun classifyImage() {
        var bitmap = imageView.drawable.toBitmap()

        if ((bitmap != null) && (emotionClassifier.isInitialized)) {
            emotionClassifier
                .classifyAsync(bitmap)
                .addOnSuccessListener { resultText -> predictedTextView?.text = resultText }
                .addOnFailureListener { e ->
                    predictedTextView?.text = getString(
                        R.string.classification_error_message,
                        e.localizedMessage
                    )
                    Log.e(TAG, "Error classifying drawing.", e)
                }
        }
    }

    private fun faceDetector(bitmap: Bitmap){
        //Apply grayscale filter to imageView and save as a new ImageView
        var matrix = ColorMatrix()
        matrix.setSaturation(0.0f)
        var filter = ColorMatrixColorFilter(matrix)
        //grayscaleImageView = imageView.setColorFilter(filter)

        //Convert bitmap to frame
        val converter = AndroidFrameConverter()
        var frame = converter.convert(bitmap)

        //Convert frame to mat
        var mat = opencv_core.Mat()
        mat = toMat(frame)

        //TODO() GET THE CLASSIFIER AND DETECT THE FACES
        /*
        detectMultiScale(
            grayScaled, // input image
            rectangles, // output rectangle
            1.5, // scale factor
            5, // minimum neighbors.
            0, // flags
            opencv_core.Size(48, 48), //minimum size
            null //maximum size
        )*/
    }

    companion object {
        private const val TAG = "LogActivity"
    }
}
