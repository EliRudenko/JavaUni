package com.example.chartapp

import android.content.Context
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF

class SimpleMarkerView(context: Context, layoutResource: Int) : MarkerView(context, layoutResource) {

    private val txtTitle: TextView = findViewById(R.id.txtMarkerTitle)
    private val txtValue: TextView = findViewById(R.id.txtMarkerValue)
    private var labels: List<String> = emptyList()

    fun setLabels(labels: List<String>) {
        this.labels = labels
    }

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        val x = e?.x?.toInt() ?: 0
        val day = labels.getOrNull(x) ?: "Day"
        val value = e?.y?.toInt()?.toString() ?: "-"
        txtTitle.text = day
        txtValue.text = "$value units"
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF(-(width / 2f), -height.toFloat())
    }
}
