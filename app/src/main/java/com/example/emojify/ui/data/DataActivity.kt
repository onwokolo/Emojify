package com.example.emojify.ui.data

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.emojify.R
import com.example.emojify.base.BaseActivity
import com.example.emojify.storage.StorageSystem
import com.example.emojify.ui.home.MainActivity
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.activity_data.*
import org.koin.androidx.scope.currentScope
import timber.log.Timber

class DataActivity : BaseActivity(), DataActivityContract.View {

    private val presenter: DataActivityContract.Presenter by currentScope.inject()
    var pieChart: PieChart? = null
    var pieData: PieData? = null
    var pieDataSet: PieDataSet? = null
    var pieEntries: ArrayList<PieEntry>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.e("TESTING TESTING")
        //setSupportActionBar(toolbar)
        pieChart = findViewById(R.id.pieChart)
        getEmotionEntrys()
        pieDataSet = PieDataSet(pieEntries, "")
        pieData = PieData(pieDataSet)
        pieChart?.data = pieData
        pieDataSet?.let { set ->
            set.setColors(*ColorTemplate.JOYFUL_COLORS)
            set.sliceSpace = 2f
            set.valueTextColor = Color.WHITE
            set.valueTextSize = 10f
            set.sliceSpace = 5f
        }
    }

    private fun getEmotionEntrys() {
        pieEntries = ArrayList()
        val entryList = StorageSystem.storage.getEntries()
        val emotionDictionary = ArrayList<EntryCount>()
        var hasEntry = false
        entryList.forEach { entry ->
            Timber.e(entry.emotion)
            emotionDictionary.forEach {
                if (it.emotion == entry.emotion) {
                    it.count += 1
                    hasEntry = true
                }
            }
            if (!hasEntry) {
                emotionDictionary.add(EntryCount(1,entry.emotion))
            }
        }
        pieEntries?.let {
            emotionDictionary.forEach { emotion ->
                it.add(PieEntry(emotion.count.toFloat(), emotion.emotion))
            }
        }
    }
    data class EntryCount(var count: Int, val emotion: String)


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
        presenter.takeView(this)

    }



    override fun onStop() {
        super.onStop()
        presenter.dropView()
    }
    override fun getLayout(): Int {
        return R.layout.activity_data
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
