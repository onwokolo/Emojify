package com.example.emojify.ui.log

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import com.example.emojify.R
import com.example.emojify.base.BaseActivity
import com.example.emojify.storage.Entry
import com.example.emojify.storage.StorageSystem
import com.example.emojify.ui.home.MainActivity
import kotlinx.android.synthetic.main.activity_data.HomeButton
import kotlinx.android.synthetic.main.activity_log.*
import org.koin.androidx.scope.currentScope


class LogActivity : BaseActivity(), LogActivityContract.View {
    private val presenter: LogActivityContract.Presenter by currentScope.inject()
    private val storage = StorageSystem.storage
    private lateinit var imageView: ImageView
    private val REQUEST_IMAGE_CAPTURE = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onStart() {
        super.onStart()
        this.imageView = CaptView
        presenter.takeView(this)
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
            storage.addEntry(Entry(Entry.convertImageToByteArray(imageView), "11-11-11", "EXAMPLE EMOTION"))
            storage.commitEntries()
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
        }
    }
}
