package site.sunmeat.contactscontract

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.pm.PackageManager
import android.telephony.SmsManager
import androidx.core.content.ContextCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.util.Calendar

class BirthdayWorker(appContext: Context, params: WorkerParameters) : Worker(appContext, params) {

    override fun doWork(): Result {
        // 1) Берём декабрьских
        val dec = ContactsRepository.getDecemberBirthdays(applicationContext)

        // 2) Если сегодня декабрь и совпал день — отправим SMS (если есть номер)
        val cal = Calendar.getInstance()
        val month = cal.get(Calendar.MONTH) + 1 // Calendar: 0..11
        val day = cal.get(Calendar.DAY_OF_MONTH)

        if (month != 12) return Result.success()

        val canSms = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED
        if (!canSms) return Result.success()

        for (p in dec) {
            val dm = extractDayMonth(p.birthdayRaw) ?: continue
            if (dm.first == 12 && dm.second == day) {
                val phone = ContactsRepository.getFirstPhoneForContact(applicationContext, p.contactId) ?: continue
                sendSms(phone, "З Днем Народження, ${p.name}! 🎉")
            }
        }

        return Result.success()
    }

    private fun extractDayMonth(dateRaw: String): Pair<Int, Int>? {
        // вернём Pair(month, day)
        // 1999-12-20 => (12,20)
        // --12-20 => (12,20)
        val parts = dateRaw.split("-").filter { it.isNotEmpty() }
        return when {
            parts.size >= 3 -> {
                val m = parts[1].toIntOrNull() ?: return null
                val d = parts[2].toIntOrNull() ?: return null
                m to d
            }
            parts.size == 2 -> {
                val m = parts[0].toIntOrNull() ?: return null
                val d = parts[1].toIntOrNull() ?: return null
                m to d
            }
            else -> null
        }
    }

    private fun sendSms(phone: String, text: String) {
        val sentPI = PendingIntent.getBroadcast(
            applicationContext, 1,
            android.content.Intent("SMS_SENT"),
            PendingIntent.FLAG_IMMUTABLE
        )

        val deliveredPI = PendingIntent.getBroadcast(
            applicationContext, 2,
            android.content.Intent("SMS_DELIVERED"),
            PendingIntent.FLAG_IMMUTABLE
        )

        SmsManager.getDefault().sendTextMessage(phone, null, text, sentPI, deliveredPI)
    }
}
