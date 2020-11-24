package com.example.emojify.ui.log

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import com.example.emojify.R
import com.example.emojify.base.BaseActivity
import com.example.emojify.storage.Entry
import com.example.emojify.storage.StorageSystem
import com.example.emojify.ui.home.EmotionClassifier
import com.example.emojify.ui.home.MainActivity
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import kotlinx.android.synthetic.main.activity_data.HomeButton
import kotlinx.android.synthetic.main.activity_log.*
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*


class LogActivity : BaseActivity() {
    private val storage = StorageSystem.storage
    private lateinit var imageView: ImageView
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
            .addOnFailureListener { e -> Timber.e(e, "Error to setting up digit classifier.") }
    }
    override fun onStart() {
        super.onStart()
        this.imageView = CaptView

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
    private fun getPickImageIntent(): Intent? {
        return ImagePicker.getPickImageIntent(this)
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
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startActivityForResult(getPickImageIntent(), 1)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val bitmap = ImagePicker.getImageFromResult(this, resultCode, data)
            imageView.setImageBitmap(bitmap)

            // For testing purpose...allow imageView to use
            // selected Image from camera or gallery instead of only the test image
            _bitmap = ImagePicker.getImageFromResult(this, resultCode, data)
        }
    }


    @SuppressLint("SetTextI18n")
    private fun classifyImage(){
        // High-accuracy landmark detection and face classification
        val highAccuracyOpts = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE)
            .build()
        val image = InputImage.fromBitmap(imageView.drawable.toBitmap(),0)
        val detector = FaceDetection.getClient(highAccuracyOpts)
        var bounds: Rect?
        var croppedBmp: Bitmap
        detector.process(image)
            .addOnSuccessListener { faces ->
                Timber.e("I SUCCEEDED")
                // Task completed successfully
                for (face in faces) {
                    bounds = face.boundingBox
                    bounds?.let {
                            croppedBmp = Bitmap.createBitmap(
                            imageView.drawable.toBitmap(),
                            it.left,
                            it.top,
                            it.width(),
                            it.height()
                        )

                        //imageView.setImageBitmap(croppedBmp)
                        if (emotionClassifier.isInitialized) {
                            emotionClassifier
                                .classifyAsync(croppedBmp)
                                .addOnSuccessListener { resultText -> predictedTextView?.text = resultText
                                    val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                                    val date = Date()
                                    val dateString = sdf.format(date);
                                    storage.addEntry(Entry(Entry.convertImageToByteArray(imageView,40),dateString, resultText))
                                    storage.commitEntries()}
                                .addOnFailureListener { e ->
                                    predictedTextView?.text = getString(
                                        R.string.classification_error_message,
                                        e.localizedMessage
                                    )
                                    Timber.e(e, "Error classifying drawing.")
                                }
                        }
                    }
                    Timber.e("I HAVE A FACE")
                }
                val faceCount = if(faces != null) faces.size else 0
                if (faceCount == 0) {
                    predictedTextView?.text = "NO FACE FOUND"
                    Timber.e("NO FACE FOUND")
                }
            }
            .addOnFailureListener { e ->
                // Task failed with an exception
                Timber.e(e)
            }
    }
}
