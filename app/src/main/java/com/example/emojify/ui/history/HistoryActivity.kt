package com.example.emojify.ui.history

import EntryAdapter
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import android.widget.TextView
import com.example.emojify.R
import com.example.emojify.base.BaseActivity
import com.example.emojify.storage.Entry
import com.example.emojify.storage.StorageSystem
import com.example.emojify.ui.home.MainActivity
import kotlinx.android.synthetic.main.activity_history.*


class HistoryActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val list: ListView = findViewById(R.id.HistList) //Replace R.id.list with YOUR list
        val arrayList: ArrayList<Entry> = StorageSystem.storage.getEntries()
        if (arrayList.size == 0) {
            //entries is empty, let's put in a textview for that!
            list.visibility = View.GONE //hide the list such that it takes up no space
            val text: TextView = findViewById(R.id.NoEntries) //replace textytext with your textview ID
            text.visibility = View.VISIBLE //make it visible and take up space
        } else {
            val customAdapter = EntryAdapter(this,arrayList) //these two lines will implement the entries into the list
            list.adapter = customAdapter
        }
    }

    override fun onStart() {
        super.onStart()
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

    override fun getLayout(): Int {
        return R.layout.activity_history
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
}
