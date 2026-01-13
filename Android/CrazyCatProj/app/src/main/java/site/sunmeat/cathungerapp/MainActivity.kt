package site.sunmeat.cathungerapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.concurrent.atomic.AtomicBoolean

class MainActivity : AppCompatActivity() {

    private lateinit var txtStatus: TextView
    private lateinit var txtLog: TextView
    private lateinit var btnFeed: Button

    private val ui = Handler(Looper.getMainLooper())

    private var lives = 9
    private var secondsToDeath = 30

    private val isRunning = AtomicBoolean(true)
    private val isCooking = AtomicBoolean(false)
    private val wasFedInTime = AtomicBoolean(false)

    private val funny = listOf(
        "Мурзик дивиться голодними очима…",
        "Мурзик почав гризти тапок…",
        "Мурзик точить кігті об диван…",
        "Мурзик драматично зітхає…",
        "Мурзик уже бачить тунця уві сні…",
        "Мурзик шипить на порожню миску…",
        "Мурзик міряє тебе поглядом: “Ну?”…",
        "Мурзик вмикає режим трагедії…"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtStatus = findViewById(R.id.txtStatus)
        txtLog = findViewById(R.id.txtLog)
        btnFeed = findViewById(R.id.btnFeed)

        updateStatus()
        logLine("Твій котик Мурзик дуже голодний. У тебе є 30 секунд 😼")

        // Поток “голод/смерть”
        Thread { hungerLoop() }.start()

        // Кнопка: готовим корм 25 секунд
        btnFeed.setOnClickListener {
            if (isCooking.get()) return@setOnClickListener

            isCooking.set(true)
            btnFeed.isEnabled = false
            logLine("Починаємо готувати супер-преміум-корм… 🍤🐟 (25с)")

            Thread { cookingLoop25s() }.start()
        }
    }

    private fun hungerLoop() {
        var secondsPassed = 0

        while (isRunning.get()) {
            Thread.sleep(1000)
            secondsPassed++

            if (!wasFedInTime.get()) {
                secondsToDeath--

                if (secondsPassed % 5 == 0) {
                    lives--
                    ui.post { logLine("МЯЯЯУ!!! 😾 (-1 життя). Життів: $lives") }
                }

                ui.post { updateStatus() }

                if (secondsToDeath <= 0 || lives <= 0) {
                    ui.post {
                        logLine("💀 Мурзик пішов у котячу Вальгаллу…")
                        throw RuntimeException("Мурзик пішов у котячу Вальгаллу…")
                    }
                    return
                }
            }
        }
    }

    private fun cookingLoop25s() {
        for (sec in 1..25) {
            if (!isRunning.get()) return
            Thread.sleep(1000)

            ui.post {
                val msg = funny[(sec - 1) % funny.size]
                logLine("$msg (готування: $sec/25)")
            }
        }

        ui.post {
            wasFedInTime.set(true)
            lives = 9
            secondsToDeath = 30
            updateStatus()
            logLine("✅ Корм готов! Мурзик наївся 😻 Життів знову: 9")
            wasFedInTime.set(false)

            isCooking.set(false)
            btnFeed.isEnabled = true
        }
    }

    private fun updateStatus() {
        txtStatus.text = "Життів: $lives | До смерті: ${secondsToDeath}с"
    }

    private fun logLine(s: String) {
        val old = txtLog.text?.toString().orEmpty()
        txtLog.text = if (old.isBlank() || old == "Логи...") s else "$old\n$s"
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning.set(false)
    }
}
