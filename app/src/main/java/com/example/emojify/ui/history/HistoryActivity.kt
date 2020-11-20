package com.example.emojify.ui.history

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.example.emojify.R
import com.example.emojify.base.BaseActivity
import com.example.emojify.ui.home.MainActivity
import kotlinx.android.synthetic.main.activity_history.*
import org.koin.androidx.scope.currentScope
import java.time.LocalDate


class HistoryActivity : BaseActivity(), HistoryActivityContract.View {

    private val presenter: HistoryActivityContract.Presenter by currentScope.inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onStart() {
        super.onStart()
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
    }

    override fun onStop() {
        super.onStop()
        presenter.dropView()
    }
    override fun getLayout(): Int {
        return R.layout.activity_history
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