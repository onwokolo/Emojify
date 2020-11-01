package com.example.emojify.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.emojify.R
import com.example.emojify.base.BaseActivity
import com.example.emojify.ui.home.MainActivity
import kotlinx.android.synthetic.main.activity_splash.*
import org.koin.androidx.scope.currentScope

class SplashActivity : BaseActivity(), SplashContract.View {

    private val presenter: SplashContract.Presenter by currentScope.inject() //grabs an instance of presenter
    //the presenter will always be the same object, so we use currentScope.inject() to grab the same object instead of constructing a new one

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayout())
        setActionBar(toolbar, "Splash Example", false)
        setupMainButton()
        presenter.takeView(this)
    }
    override fun setupMainButton() {
        MainButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java) //first, declare an intention to do something, and set the intent as our MainActivity
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
