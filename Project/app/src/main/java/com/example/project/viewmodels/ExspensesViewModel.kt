package com.example.project.viewmodels

import android.graphics.Color
import android.graphics.Typeface
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.project.ModalBottomSheet.Companion.TAG
import com.example.project.R
import com.example.project.adapters.OperationsRecyclerViewAdapter
import com.example.project.room.database.OperationsDatabase
import com.example.project.room.entity.Operations
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF

class ExspensesViewModel(var database: OperationsDatabase,view: View) : ViewModel() {
    var entries:ArrayList<PieEntry>
    var pieChart:PieChart
    var state:String = "Доход"
    var operationsAdapter:OperationsRecyclerViewAdapter
    init{
        pieChart = view.findViewById(R.id.piechartExs)
        entries = ArrayList()
        pieChartWork()
        operationsAdapter= OperationsRecyclerViewAdapter(database.OperationsDao().getOperations(1))
        Log.d(TAG, "size: " + database.OperationsDao().getOperations(1).size)
    }
    fun onIncomesButtonClick(view:View){
        state="Доход"
        pieChartWork()
    }
    fun onConsumptionsButtonClick(view:View){
        state="Расходы"
        pieChartWork()
    }
    fun onSaveButtonClick(view:View){

    }
    fun pieChartWork(){
        entries.clear()
        pieChart.setUsePercentValues(true)
        pieChart.getDescription().setEnabled(false)
        pieChart.setExtraOffsets(5f, 10f, 5f, 5f)

        // on below line we are setting drag for our pie chart
        pieChart.setDragDecelerationFrictionCoef(0.95f)

        // on below line we are setting hole
        // and hole color for pie chart

        // on below line we are setting circle color and alpha
        pieChart.setTransparentCircleColor(Color.WHITE)
        pieChart.setTransparentCircleAlpha(110)

        // on  below line we are setting hole radius
        pieChart.setHoleRadius(58f)
        pieChart.setTransparentCircleRadius(61f)

        // on below line we are setting center text
        pieChart.setDrawCenterText(true)

        // on below line we are setting
        // rotation for our pie chart
        pieChart.setRotationAngle(0f)

        // enable rotation of the pieChart by touch
        pieChart.setRotationEnabled(true)
        pieChart.setHighlightPerTapEnabled(false)

        // on below line we are setting animation for our pie chart
        pieChart.animateY(1400, Easing.EaseInOutQuad)

        // on below line we are disabling our legend for pie chart
        pieChart.legend.isEnabled = true
        pieChart.legend.verticalAlignment=Legend.LegendVerticalAlignment.CENTER
        pieChart.legend.horizontalAlignment=Legend.LegendHorizontalAlignment.RIGHT
        pieChart.legend.orientation = Legend.LegendOrientation.VERTICAL
        pieChart.legend.textSize=15f

        for(category in database.OperationsDao().getOperationsByCategory(1,state)){
            entries.add(PieEntry(category.amount.toFloat(),category.operation_category))
            Log.d(TAG, "EntriesSize: " + entries.size)
        }
        val dataSet = PieDataSet(entries, "Расходы")
        dataSet.valueTextSize=(0f)
        dataSet.setColors(*ColorTemplate.PASTEL_COLORS)
        pieChart.setDrawSliceText(false)
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        pieChart.setData(data)
        pieChart.invalidate()
    }
}