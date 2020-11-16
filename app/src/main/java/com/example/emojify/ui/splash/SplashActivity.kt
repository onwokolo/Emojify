package com.example.emojify.ui.splash

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import com.example.emojify.R
import com.example.emojify.base.BaseActivity
import com.example.emojify.ui.home.MainActivity
import kotlinx.android.synthetic.main.activity_splash.*
import org.koin.androidx.scope.currentScope

class SplashActivity : BaseActivity(), SplashContract.View {
    //ANIM LOGO FUNCTION
    private fun animLogo(){
        val rotate=AnimationUtils.loadAnimation(this, R.anim.rotate_anim)
        logo.animation=rotate
    }
    //CAMERA INIT
    private val cameraRequest=1888

    //GALLERY INIT
    lateinit var button: Button
    private val pickImage=100
    lateinit var logo: ImageView
    private var imageUri: Uri?=null
    private val presenter: SplashContract.Presenter by currentScope.inject() //grabs an instance of presenter
    //the presenter will always be the same object, so we use currentScope.inject() to grab the same object instead of constructing a new one

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayout())
        logo = findViewById<ImageView>(R.id.Logo);
        setActionBar(toolbar, "Splash Example", false)
        setupMainButton()
        presenter.takeView(this)
    }
    override fun setupMainButton() {

        StartButton.setOnClickListener {
            val r=(1..3).random()
            if (r==1) {
                animLogo()
            }
            if (r==2) {
                animLogo()
                animLogo()
            }
            if (r==3) {
                animLogo()
                animLogo()
                animLogo()
            }
            val intent = Intent(
                this,
                MainActivity::class.java
            ) //first, declare an intention to do something, and set the intent as our MainActivity
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) //Any optional flags go here. This flag closes all activities of this package before running the intent (if any are running)
            startActivity(intent) //Start the intent, this will create a new process to do the intent
            finish() //Finally, finish this activity, which will call the onDestroy() method
        }
    }
    override fun getLayout(): Int {
        return R.layout.activity_splash
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.dropView()
    }

}
