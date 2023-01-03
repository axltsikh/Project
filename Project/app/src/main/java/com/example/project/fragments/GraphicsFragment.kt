package com.example.project.fragments

import android.content.ContentValues.TAG
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.example.project.R
import com.example.project.databinding.FragmentGraphicsBinding
import com.example.project.room.database.OperationsDatabase
import com.example.project.room.entity.OperationByDate
import com.example.project.room.entity.OperationByMonth
import com.example.project.viewmodels.GraphicsViewModel
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.helper.StaticLabelsFormatter
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


class GraphicsFragment : Fragment() {
    private lateinit var viewModel:GraphicsViewModel
    private lateinit var graphic:GraphView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding:FragmentGraphicsBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_graphics,container,false)
        graphic=binding.root.findViewById(R.id.Graphic)
        binding.viewmodel=GraphicsViewModel(OperationsDatabase.getDatabase(requireContext()))
        viewModel=binding.viewmodel as GraphicsViewModel
        viewModel.eventsFlow
            .onEach{
                when(it){
                    is GraphicsViewModel.Event.lastWeek->{
                        createGraphDay()}
                    is GraphicsViewModel.Event.sixMonths->{
                        createGraphMonth()
                    }
                    is GraphicsViewModel.Event.lastMonth->{
                        createGraphWeek()
                    }
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
        return binding.root
    }
    fun createGraphMonth(){
        graphic.removeAllSeries()
        if(viewModel.operationsByMonth.size<=1){
            Toast.makeText(requireContext(),"Недостаточно информации для отображения!",Toast.LENGTH_SHORT).show()
            return
        }
        graphic.removeAllSeries()
        val points=LineGraphSeries<DataPoint>()
        var iterator:Double=0.0
        for(operation in viewModel.operationsByMonth){
            points.appendData(DataPoint(iterator,operation.operation_value),true,viewModel.operationsByMonth.size)
            iterator ++
        }
        points.isDrawDataPoints=true
        points.color=Color.BLACK
        points.dataPointsRadius=7f
        points.thickness=5
        graphic.addSeries(points)
        val staticLabelsFormatter = StaticLabelsFormatter(graphic)
        val horizontalLabels:Array<String>? = Array(viewModel.operationsByMonth.size){"0"}
        var i = 0
        for(operation in viewModel.operationsByMonth){
            horizontalLabels?.set(i, operation.operation_datetime)
            i+=1
        }

        staticLabelsFormatter.setHorizontalLabels(horizontalLabels)
        graphic.gridLabelRenderer.labelFormatter=staticLabelsFormatter
        graphic.gridLabelRenderer.textSize=20f
        graphic.gridLabelRenderer.numHorizontalLabels=viewModel.operationsByMonth.size
    }
    fun createGraphDay(){
        graphic.removeAllSeries()
        Log.d(TAG, "createGraphDay: " + viewModel.operationsByDay.size)
        if(viewModel.operationsByDay.size<=1){
            Toast.makeText(requireContext(),"Недостаточно информации для отображения!",Toast.LENGTH_SHORT).show()
            return
        }
        graphic.removeAllSeries()
        val points=LineGraphSeries<DataPoint>()
        var iterator:Double=0.0
        for(operation in viewModel.operationsByDay){
            points.appendData(DataPoint(iterator,operation.operation_value),false,viewModel.operationsByDay.size)
            iterator ++
        }
        points.isDrawDataPoints=true
        points.color=Color.BLACK
        points.dataPointsRadius=7f
        points.thickness=5
        graphic.addSeries(points)
        val staticLabelsFormatter = StaticLabelsFormatter(graphic)
        val horizontalLabels:Array<String>? = Array(viewModel.operationsByDay.size){"0"}
//        val verticalLabels:Array<String>? = Array(viewModel.operationsByDay.size){"0"}
        var i = 0
        for(operation in viewModel.operationsByDay){
            horizontalLabels?.set(i, operation.operationDayOfWeek)
//            verticalLabels?.set(i,operation.operation_value.toString())
            i+=1
        }
        staticLabelsFormatter.setHorizontalLabels(horizontalLabels)
        graphic.gridLabelRenderer.labelFormatter=staticLabelsFormatter
        graphic.gridLabelRenderer.textSize=20f
        graphic.gridLabelRenderer.numHorizontalLabels=viewModel.operationsByDay.size
    }
    fun createGraphWeek(){
        graphic.removeAllSeries()
        if(viewModel.operationsByWeek.size<=1){
            Toast.makeText(requireContext(),"Недостаточно информации для отображения!",Toast.LENGTH_SHORT).show()
            return
        }
        graphic.removeAllSeries()
        val points=LineGraphSeries<DataPoint>()
        var iterator:Double=0.0
        for(operation in viewModel.operationsByWeek){
            points.appendData(DataPoint(iterator,operation.operation_value),true,viewModel.operationsByWeek.size)
            iterator ++
        }
        points.isDrawDataPoints=true
        points.color=Color.BLACK
        points.dataPointsRadius=7f
        points.thickness=5
        graphic.addSeries(points)
        val staticLabelsFormatter = StaticLabelsFormatter(graphic)
        val horizontalLabels:Array<String>? = Array(viewModel.operationsByWeek.size){"0"}
        var i = 0
        for(operation in viewModel.operationsByWeek){
            horizontalLabels?.set(i, "${i+1} неделя")
            i+=1
        }

        staticLabelsFormatter.setHorizontalLabels(horizontalLabels)
        graphic.gridLabelRenderer.labelFormatter=staticLabelsFormatter
        graphic.gridLabelRenderer.textSize=15f
        graphic.gridLabelRenderer.numHorizontalLabels=viewModel.operationsByWeek.size
    }
}