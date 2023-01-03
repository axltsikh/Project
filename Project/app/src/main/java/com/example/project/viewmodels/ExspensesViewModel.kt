package com.example.project.viewmodels

import android.content.ContentValues
import android.graphics.Color
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import androidx.databinding.BaseObservable
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project.ModalBottomSheet
import com.example.project.ModalBottomSheet.Companion.TAG
import com.example.project.R
import com.example.project.adapters.BillsSpinnerAdapter
import com.example.project.adapters.OperationsRecyclerViewAdapter
import com.example.project.room.database.OperationsDatabase
import com.example.project.room.entity.Bill
import com.example.project.room.entity.BillGetter
import com.example.project.room.entity.FullOperation
import com.example.project.room.entity.OperationsByCategory
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class ExspensesViewModel(val database:OperationsDatabase) : ViewModel() {

    var state:Boolean=false
    lateinit var operationsAdapter: OperationsRecyclerViewAdapter
    lateinit var billsAdapter: BillsSpinnerAdapter
    sealed class Event{
        object saveToFile:Event()
        object createPieChart:Event()
    }
    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()
    var bill: BillGetter = BillGetter()
    init{
        viewModelScope.launch {
            state=false
            bill.bill_id=1
            operationsAdapter= OperationsRecyclerViewAdapter(database.OperationsDao().getOperations(bill.bill_id,state.toInt()))
            billsAdapter=BillsSpinnerAdapter(database.OperationsDao().getAllBillsWithCurrency())
            onIncomesButtonClick(null)
        }
    }
    fun getOperationsByCategory():List<OperationsByCategory>{
        return database.OperationsDao().getOperationsByCategory(bill.bill_id,state.toInt())
    }

    fun onSaveButtonClick(view:View){
        viewModelScope.launch {
            eventChannel.send(ExspensesViewModel.Event.saveToFile)
        }
    }
    fun onIncomesButtonClick(view:View?){
        Log.d(TAG, "onIncomesButtonClick: " + "click")
        state=true
        operationsAdapter.setNewList(database.OperationsDao().getOperations(bill.bill_id,state.toInt()))
        viewModelScope.launch {
            eventChannel.send(Event.createPieChart)
        }
    }
    fun onConsumptionsButtonClick(view:View){
        state=false
        operationsAdapter.setNewList(database.OperationsDao().getOperations(bill.bill_id,state.toInt()))
        viewModelScope.launch {
            eventChannel.send(Event.createPieChart)
        }
    }
    fun onBillSelectionChanged(adapterView: AdapterView<*>, view:View, position:Int, id:Long){
        bill.updateBill(billsAdapter.getItem(position))
        onIncomesButtonClick(null)
    }
    fun Boolean.toInt()=if(this) 1 else 2
}