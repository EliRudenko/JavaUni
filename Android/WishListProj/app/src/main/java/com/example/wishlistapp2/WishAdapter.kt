package com.example.wishlistapp2

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wishlistapp2.databinding.ItemWishBinding
import java.text.NumberFormat
import java.util.Locale

class WishAdapter(
    private val items: List<WishItem>,
    private val onChanged: () -> Unit
) : RecyclerView.Adapter<WishAdapter.VH>() {

    private val money = NumberFormat.getNumberInstance(Locale("uk", "UA"))

    class VH(val b: ItemWishBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemWishBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]

        holder.b.img.setImageResource(item.imageRes)
        holder.b.txtName.text = item.name
        holder.b.txtPrice.text = "${money.format(item.price)} ₴"

        holder.b.check.setOnCheckedChangeListener(null)
        holder.b.check.isChecked = item.checked

        holder.b.check.setOnCheckedChangeListener { _, isChecked ->
            item.checked = isChecked
            onChanged()
        }
    }

    override fun getItemCount(): Int = items.size
}
