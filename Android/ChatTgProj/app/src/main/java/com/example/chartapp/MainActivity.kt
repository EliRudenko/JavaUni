package com.example.chartapp

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val avatarPalette by lazy {
        intArrayOf(
            getColor(R.color.tg_avatar_1),
            getColor(R.color.tg_avatar_2),
            getColor(R.color.tg_avatar_3),
            getColor(R.color.tg_avatar_4),
            getColor(R.color.tg_avatar_5),
            getColor(R.color.tg_avatar_6)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Toast.makeText(this, "MainActivity loaded", Toast.LENGTH_SHORT).show()

        val listView = findViewById<ListView>(R.id.listChats)
        val debug = findViewById<TextView>(R.id.txtDebug)
        debug.text = "Loaded: OK"

        val data = arrayListOf(
            chat(0, "Family chat", "Dinner is ready", "Вчора", 0, false, false),
            chat(0, "Олена", "Thanks!", "01 гру", 0, true, true),
            chat(0, "Me", "Where are you?", "22:05", 1, false, false),
            chat(0, "Uni", "Ok", "20:02", 7, true, true),
            chat(0, "Одесса ИНФО | LIVE", "Локаційно чисто на даний момент.", "23:36", 208, false, false)
        )

        val from = arrayOf("avatar", "name", "last_message", "time", "unread_count", "check_text", "initials")
        val to = intArrayOf(
            R.id.imgAvatar,
            R.id.txtName,
            R.id.txtLastMessage,
            R.id.txtTime,
            R.id.unreadBadge,
            R.id.txtCheck,
            R.id.txtInitials
        )

        val adapter = SimpleAdapter(this, data, R.layout.item_chat, from, to)

        adapter.setViewBinder { view, value, _ ->
            when (view.id) {
                R.id.imgAvatar -> {
                    val resId = value as Int
                    val img = view as ImageView
                    img.visibility = if (resId == 0) View.GONE else View.VISIBLE
                    if (resId != 0) img.setImageResource(resId)
                    true
                }

                R.id.txtInitials -> {
                    val initials = value as String
                    val tv = view as TextView
                    tv.text = initials
                    tv.visibility = if (initials.isBlank()) View.GONE else View.VISIBLE
                    true
                }

                R.id.unreadBadge -> {
                    val count = value as Int
                    val badge = view as TextView
                    badge.visibility = if (count > 0) View.VISIBLE else View.GONE
                    badge.text = count.toString()
                    true
                }

                // ТВОЙ БЛОК
                R.id.txtCheck -> {
                    val check = value as String
                    val tv = view as TextView
                    if (check.isBlank()) {
                        tv.visibility = View.GONE
                    } else {
                        tv.visibility = View.VISIBLE
                        tv.text = check
                    }
                    true
                }

                R.id.txtName -> {
                    val name = value as String
                    val tv = view as TextView
                    tv.text = name

                    val row = tv.rootView
                    val avatarContainer = row.findViewById<View>(R.id.avatarContainer)
                    val img = row.findViewById<ImageView>(R.id.imgAvatar)

                    if (img.visibility == View.GONE) {
                        val color = pickAvatarColor(name)
                        avatarContainer.backgroundTintList = ColorStateList.valueOf(color)
                    } else {
                        avatarContainer.backgroundTintList = null
                    }
                    true
                }

                else -> false
            }
        }

        listView.adapter = adapter
    }

    private fun chat(
        avatar: Int,
        name: String,
        lastMessage: String,
        time: String,
        unread: Int,
        isSent: Boolean,
        isRead: Boolean
    ): HashMap<String, Any> {

        val initials = if (avatar == 0) makeInitials(name) else ""
        val checkText = if (!isSent) "" else if (isRead) "✓✓" else "✓"

        return hashMapOf(
            "avatar" to avatar,
            "name" to name,
            "last_message" to lastMessage,
            "time" to time,
            "unread_count" to unread,
            "is_sent" to isSent,
            "is_read" to isRead,
            "initials" to initials,
            "check_text" to checkText
        )
    }

    private fun makeInitials(name: String): String {
        val cleaned = name.replace(Regex("[^\\p{L}\\p{N}\\s]"), " ").trim()
        val parts = cleaned.split(Regex("\\s+")).filter { it.isNotBlank() }
        if (parts.isEmpty()) return ""
        val a = parts[0].firstOrNull()?.toString() ?: ""
        val b = parts.getOrNull(1)?.firstOrNull()?.toString() ?: ""
        return (a + b).uppercase()
    }

    private fun pickAvatarColor(name: String): Int {
        val idx = (name.hashCode().absoluteValue) % avatarPalette.size
        return avatarPalette[idx]
    }

    private val Int.absoluteValue: Int
        get() = if (this < 0) -this else this
}
