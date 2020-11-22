package com.example.emojify.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import com.example.emojify.R
import com.example.emojify.base.BaseActivity
import com.example.emojify.ui.data.DataActivity
import com.example.emojify.ui.history.HistoryActivity
import com.example.emojify.ui.log.LogActivity
import org.koin.androidx.scope.currentScope

class MainActivity : BaseActivity(), MainActivityContract.View {

    private val presenter: MainActivityContract.Presenter by currentScope.inject()
//    private var emotionClassifier = EmotionClassifier(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onStart() {
        super.onStart()
        presenter.takeView(this)
        //LOG BUTTON TO LOG PAGE
        val logbutton = findViewById<Button>(R.id.LogButton)
        logbutton.setOnClickListener{
            val intent = Intent(this, LogActivity::class.java)
            startActivity(intent)
            finish()
        }
        //BUTTON TO HISTORY PAGE
        val histbutton = findViewById<Button>(R.id.HistButton)
        histbutton.setOnClickListener{
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
            finish()
        }
        //BUTTON TO DATA PAGE
        val databutton = findViewById<Button>(R.id.DataButton)
        databutton.setOnClickListener{
            val intent = Intent(this, DataActivity::class.java)
            startActivity(intent)
            finish()
        }
        //SAMPLE IMAGE BUTTON MUST REMOVE BEFORE PRESENTATION
        val sampleButton = findViewById<Button>(R.id.SampleImageButton)
        sampleButton.setOnClickListener{
            //set sample text here for your output
            val sampleText = findViewById<TextView>(R.id.SampleImageText)
            sampleText.text = "SAMPLE TEXT";
            val angry = R.drawable.angry
            val angryg = R.drawable.angryg
            val confused = R.drawable.confused
            val disgusted = R.drawable.digustedg
            val happy = R.drawable.happy
            val happyg = R.drawable.happyg
            val neutral = R.drawable.neutral
            val sadg = R.drawable.sadg
            val scaredg = R.drawable.scaredg
            val surprised = R.drawable.surprised
            val surprisedg = R.drawable.surprisedg
            //USE THIS IMAGES OR ADD OTHERS IF YOU WISH FOR YOUR EMOTION TESTING
        }
    }

    override fun onStop() {
        super.onStop()
        presenter.dropView()
    }
    override fun getLayout(): Int {
        return R.layout.activity_main
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
}
