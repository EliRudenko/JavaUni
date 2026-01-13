package site.sunmeat.contactscontract

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BirthdayAdapter(
    private val items: MutableList<BirthdayPerson> = mutableListOf()
) : RecyclerView.Adapter<BirthdayAdapter.VH>() {

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtName: TextView = itemView.findViewById(R.id.txtName)
        val txtDate: TextView = itemView.findViewById(R.id.txtDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_birthday, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.txtName.text = item.name
        holder.txtDate.text = item.birthdayPretty
    }

    override fun getItemCount(): Int = items.size

    fun submit(newItems: List<BirthdayPerson>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}
