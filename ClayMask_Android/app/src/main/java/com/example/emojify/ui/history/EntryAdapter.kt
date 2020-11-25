import android.content.Context
import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListAdapter
import android.widget.TextView
import com.example.emojify.R
import com.example.emojify.storage.Entry


internal class EntryAdapter(val context: Context, private val arrayList: ArrayList<Entry>) :
    ListAdapter {
    override fun areAllItemsEnabled(): Boolean {
        return false
    }

    override fun isEnabled(position: Int): Boolean {
        return true
    }

    override fun registerDataSetObserver(observer: DataSetObserver) {}
    override fun unregisterDataSetObserver(observer: DataSetObserver) {}
    override fun getCount(): Int {
        return arrayList.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var returnView: View? = convertView
        val entry: Entry = arrayList[position]
        if (returnView == null) {
            val layoutInflater = LayoutInflater.from(context)
            returnView = layoutInflater.inflate(R.layout.list_row, null)
            val thumbnail: ImageView = returnView.findViewById(R.id.list_image)
            val date: TextView = returnView.findViewById(R.id.date)
            val emotion: TextView = returnView.findViewById(R.id.emotion)
            date.text = entry.date
            emotion.text = entry.emotion
            Entry.setImageViewFromByteArray(entry.thumbnail,thumbnail)
        }
        return returnView
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getViewTypeCount(): Int {
        return arrayList.size
    }

    override fun isEmpty(): Boolean {
        return false
    }

}