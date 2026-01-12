package com.example.chartapp

import android.graphics.Typeface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter

class MainActivity : AppCompatActivity() {

    private lateinit var chart: LineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chart = findViewById(R.id.lineChart)

        val labels = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
        val values = listOf(120f, 180f, 90f, 250f, 200f, 340f, 280f)

        val entries = values.mapIndexed { index, v -> Entry(index.toFloat(), v) }

        val dataSet = LineDataSet(entries, "Sales").apply {
            lineWidth = 2.2f
            circleRadius = 4.2f
            circleHoleRadius = 2.2f
            setDrawValues(false)
            setDrawFilled(true)
            mode = LineDataSet.Mode.CUBIC_BEZIER

            setDrawHorizontalHighlightIndicator(false)
            setDrawVerticalHighlightIndicator(true)
            highlightLineWidth = 1.6f
        }

        chart.data = LineData(dataSet)

        chart.description = Description().apply { text = "" }

        chart.legend.apply {
            textSize = 12f
            typeface = Typeface.DEFAULT_BOLD
            formSize = 10f
        }

        chart.setTouchEnabled(true)
        chart.setPinchZoom(true)
        chart.isDoubleTapToZoomEnabled = false

        chart.axisRight.isEnabled = false

        chart.axisLeft.apply {
            textSize = 12f
            setDrawGridLines(true)
            gridLineWidth = 0.8f
            axisMinimum = 0f
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String = value.toInt().toString()
            }
        }

        chart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            granularity = 1f
            textSize = 12f
            setDrawGridLines(false)
            valueFormatter = IndexAxisValueFormatter(labels)
        }

        chart.extraTopOffset = 8f
        chart.extraBottomOffset = 8f

        chart.animateX(700)
        chart.invalidate()
    }
}
