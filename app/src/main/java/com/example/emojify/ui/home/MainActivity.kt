package com.example.emojify.ui.home

import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import com.example.emojify.R
import com.example.emojify.base.BaseActivity
import com.example.emojify.ui.data.DataActivity
import com.example.emojify.ui.history.HistoryActivity
import com.example.emojify.ui.log.LogActivity

class MainActivity : BaseActivity() {
//    private var emotionClassifier = EmotionClassifier(this)

    override fun onStart() {
        super.onStart()

        val logbutton = findViewById<Button>(R.id.LogButton)
        logbutton.setOnClickListener{
            val intent = Intent(this, LogActivity::class.java)
            startActivity(intent)

        }
        val histbutton = findViewById<Button>(R.id.HistButton)
        histbutton.setOnClickListener{
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)

        }
        //BUTTON TO DATA PAGE
        val databutton = findViewById<Button>(R.id.DataButton)
        databutton.setOnClickListener{
            val intent = Intent(this, DataActivity::class.java)
            startActivity(intent)

        }

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
