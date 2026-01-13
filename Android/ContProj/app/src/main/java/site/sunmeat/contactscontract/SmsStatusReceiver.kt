import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class SmsStatusReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            "SMS_SENT" -> Toast.makeText(context, "SMS: SENT", Toast.LENGTH_SHORT).show()
            "SMS_DELIVERED" -> Toast.makeText(context, "SMS: DELIVERED", Toast.LENGTH_SHORT).show()
        }
    }
}
