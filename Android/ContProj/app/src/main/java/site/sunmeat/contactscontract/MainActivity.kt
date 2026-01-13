package site.sunmeat.contactscontract

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private val adapter = BirthdayAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recycler: RecyclerView = findViewById(R.id.recycler)
        val txtInfo: TextView = findViewById(R.id.txtInfo)
        val btnScan: Button = findViewById(R.id.btnScan)

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        ensurePermissions()

        btnScan.setOnClickListener {
            if (!hasContactsPermission()) {
                ensurePermissions()
                return@setOnClickListener
            }

            thread {
                val list = ContactsRepository.getDecemberBirthdays(this)
                runOnUiThread {
                    adapter.submit(list)
                    txtInfo.text = "Найдено: ${list.size}"
                }
            }
        }

        // Ставим периодический воркер (раз в сутки)
        scheduleDailyWorker()
    }

    private fun hasContactsPermission(): Boolean =
        ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED

    private fun ensurePermissions() {
        val need = mutableListOf<String>()
        if (!hasContactsPermission()) need.add(Manifest.permission.READ_CONTACTS)

        // SMS — по заданию, но можно попросить позже, когда будем реально слать
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            need.add(Manifest.permission.SEND_SMS)
        }

        if (need.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, need.toTypedArray(), 100)
        }
    }

    private fun scheduleDailyWorker() {
        val req = PeriodicWorkRequestBuilder<BirthdayWorker>(1, TimeUnit.DAYS).build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "birthday_worker",
            ExistingPeriodicWorkPolicy.UPDATE,
            req
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Toast.makeText(this, "Permissions result updated", Toast.LENGTH_SHORT).show()
    }
}
