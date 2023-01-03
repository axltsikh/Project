package com.example.project.fragments

import android.content.ContentValues
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.project.ModalBottomSheet
import com.example.project.R
import com.example.project.adapters.BillsSpinnerAdapter
import com.example.project.adapters.OperationsRecyclerViewAdapter
import com.example.project.databinding.FragmentExspensesBinding
import com.example.project.room.database.OperationsDatabase
import com.example.project.room.entity.BillGetter
import com.example.project.viewmodels.ExspensesViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.LocalDateTime

class ExspensesFragment : Fragment() {
    private lateinit var viewModel:ExspensesViewModel
    private lateinit var pieChart: PieChart
    private var entries:ArrayList<PieEntry> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding:FragmentExspensesBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_exspenses, container, false)
        binding.viewmodel= ExspensesViewModel(OperationsDatabase.getDatabase(requireContext()))
        viewModel=binding.viewmodel as ExspensesViewModel
        pieChart=binding.root.findViewById(R.id.piechartExs)
        viewModel.eventsFlow.onEach {
            when(it){
                is ExspensesViewModel.Event.saveToFile -> saveToFile()
                is ExspensesViewModel.Event.createPieChart -> createPieChart()
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
        return binding.root
    }
    fun saveToFile(){
        val externalUri: Uri = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        val relative = Environment.DIRECTORY_DOCUMENTS
        val contentValues :ContentValues = ContentValues()
        contentValues.put(MediaStore.Files.FileColumns.DISPLAY_NAME, "Operations " + LocalDateTime.now() + ".txt");
        contentValues.put(MediaStore.Files.FileColumns.MIME_TYPE, "application/text");
        contentValues.put(MediaStore.Files.FileColumns.TITLE, "Test");
        contentValues.put(MediaStore.Files.FileColumns.RELATIVE_PATH, relative);
        val fileUri: Uri? = requireActivity().contentResolver.insert(externalUri, contentValues)
        try{
            val outputStream = requireActivity().contentResolver.openOutputStream(fileUri!!);
            for(operation in viewModel.operationsAdapter.getAllOperations()){
                val buffer:String = "Счёт: " + operation.operation_bill + "\n" + "Тип операции: " + operation.operation_type + "\n" + "Сумма: " +
                        "" + operation.operation_value + operation.operation_currency + "\n" + "Категория: " + operation.operation_category + "\n" + "Дата: " + operation.operation_datetime +"\n\n\n"
                outputStream?.write(buffer.toByteArray());
            }
            outputStream?.close();
            MaterialAlertDialogBuilder(requireActivity(),R.style.ThemeOverlay_App_MaterialAlertDialog)
                .setMessage(R.string.OperationsSavedToFileAlert)
                .setPositiveButton(R.string.AcceptDialogButton){ dialog,which ->
                    dialog.cancel()
                }.show()
        } catch (exception:Exception) {
            exception.printStackTrace();
        }
    }
    fun createPieChart(){
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
        pieChart.legend.verticalAlignment= Legend.LegendVerticalAlignment.CENTER
        pieChart.legend.horizontalAlignment= Legend.LegendHorizontalAlignment.RIGHT
        pieChart.legend.orientation = Legend.LegendOrientation.VERTICAL
        pieChart.legend.textSize=15f
        for(category in viewModel.getOperationsByCategory()){
            entries.add(PieEntry(category.amount.toFloat(),category.operation_category))
        }
        val dataSet: PieDataSet = if(!viewModel.state)
            PieDataSet(entries, "Расходы")
        else
            PieDataSet(entries, "Доходы")
        dataSet.valueTextSize=(0f)
        dataSet.setColors(*ColorTemplate.PASTEL_COLORS)
        pieChart.setDrawSliceText(false)
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        pieChart.setData(data)
        pieChart.invalidate()
    }
}