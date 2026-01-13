package com.example.wishlistapp2

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.NumberFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private val items = arrayListOf<WishItem>()
    private val money = NumberFormat.getNumberInstance(Locale("uk", "UA"))
    private lateinit var txtTotal: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recycler = findViewById<RecyclerView>(R.id.recycler)
        txtTotal = findViewById(R.id.txtTotal)
        items += WishItem(android.R.drawable.ic_menu_compass, "PlayStation 5", 19999, true)
        items += WishItem(android.R.drawable.ic_media_play, "Sony Headphones", 4999, true)
        items += WishItem(android.R.drawable.ic_menu_manage, "Logitech Mouse", 1999, false)
        items += WishItem(android.R.drawable.ic_menu_upload, "Power Bank 20000 mAh", 1299, false)
        items += WishItem(android.R.drawable.ic_menu_gallery, "LEGO Set", 2799, true)
        items += WishItem(android.R.drawable.ic_menu_today, "Smart Watch", 3599, false)
        items += WishItem(android.R.drawable.ic_menu_edit, "Book Gift Edition", 699, false)

        val adapter = WishAdapter(items) { updateTotal() }

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        updateTotal()
    }

    private fun updateTotal() {
        val sum = items.filter { it.checked }.sumOf { it.price }
        txtTotal.text = "Total: ${money.format(sum)} ₴"
    }
}
