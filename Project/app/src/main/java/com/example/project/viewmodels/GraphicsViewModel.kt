package com.example.project.viewmodels

import android.content.ContentValues.TAG
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.databinding.Bindable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project.BR
import com.example.project.adapters.BillsSpinnerAdapter
import com.example.project.room.database.OperationsDatabase
import com.example.project.room.entity.*
import com.example.project.utilities.GraphicsBindableProperties
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class GraphicsViewModel(val database:OperationsDatabase):ViewModel() {
    sealed class Event{
        object lastWeek:Event()
        object sixMonths:Event()
        object lastMonth:Event()
    }
    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    var bill:BillGetter= BillGetter()
    lateinit var billsAdapter: BillsSpinnerAdapter
    var bindableProperties:GraphicsBindableProperties= GraphicsBindableProperties()
    private var typeID:Boolean=true
    lateinit var operationsByDay: List<OperationByDate>
    lateinit var operationsByMonth: List<OperationByMonth>
    lateinit var operationsByWeek: List<OperationByWeek>
    init{
        GlobalScope.launch {
            typeID=true
            bill.bill_id=1
            billsAdapter=BillsSpinnerAdapter(database.OperationsDao().getAllBillsWithCurrency())
            operationsByMonth=database.OperationsDao().getHalfYearOperations(bill.bill_id,typeID.toInt())
            operationsByDay=database.OperationsDao().getLastWeekOperations(bill.bill_id,typeID.toInt())
            operationsByWeek=database.OperationsDao().getLastMonthOperations(bill.bill_id,typeID.toInt())
            onLastWeekClick(null)
        }
    }
    fun onBillSelectionChanged(adapterView: AdapterView<*>, view:View, position:Int, id:Long){
        Log.d(TAG, "onBillSelectionChanged: " + bill.bill_id)
        bill.updateBill(billsAdapter.getItem(position))
        onLastWeekClick(null)
        Log.d(TAG, "onBillSelectionChanged: " + bill.bill_id)
    }
    fun onIncomesClick(view:View){
        typeID=true
    }
    fun onConsumptionClick(view:View){
        typeID=false
    }
    fun Boolean.toInt()=if(this) 1 else 2
    fun onLastWeekClick(view: View?){
        operationsByDay=database.OperationsDao().getLastWeekOperations(bill.bill_id,typeID.toInt())
        Log.d(TAG, "onLastWeekClick: " + operationsByDay.size)
        viewModelScope.launch {
            eventChannel.send(Event.lastWeek)
        }
        var finalSum=0.0
        for(operation in operationsByDay)
            finalSum+=operation.operation_value
        val maxValue = database.OperationsDao().getLastWeekOperationsByCategory(bill.bill_id,typeID.toInt()).maxByOrNull { it.operation_value }
        bindableProperties.outputText="За последние 7 дней вы ${if(typeID) "заработали" else "потратили"} ${finalSum} ${bill.currency_name}  \n" +
                "В среднем вы ${if(typeID) "получали" else "тратили"} ${finalSum/operationsByDay.size} ${bill.currency_name} в день\n" +
                "Больше всего вы ${if(typeID) "получали доходы с категории" else "тратили на категорию"} " +
                "${maxValue?.category_name}: ${maxValue?.operation_value} ${bill.currency_name}\n"
    }
    fun onSixMonthsClick(view:View){
        Log.d(TAG, "onLastWeekClick: " + bill.bill_id)
        operationsByMonth=database.OperationsDao().getHalfYearOperations(bill.bill_id,typeID.toInt())
        viewModelScope.launch {
            eventChannel.send(Event.sixMonths)
        }
        Log.d(TAG, "onSixMonthsClick: " + operationsByMonth.size)
        var finalSum=0.0
        for(operation in operationsByMonth)
            finalSum+=operation.operation_value
        val maxValue = database.OperationsDao().getHalfYearOperationsByCategory(bill.bill_id,typeID.toInt()).maxByOrNull { it.operation_value }
        bindableProperties.outputText="За последние 6 месяцев вы ${if(typeID) "заработали" else "потратили"} $finalSum ${bill.currency_name}  \n" +
                "В среднем вы ${if(typeID) "получали" else "тратили"} ${finalSum/operationsByMonth.size} ${bill.currency_name} в месяц\n" +
                "Больше всего вы ${if(typeID) "получали доходы с категории" else "тратили на категорию"} " +
                "${maxValue?.category_name}: ${maxValue?.operation_value} ${bill.currency_name}\n"
    }
    fun onLastMonthClick(view: View){
        Log.d(TAG, "onLastWeekClick: " + bill.bill_id)
        operationsByWeek=database.OperationsDao().getLastMonthOperations(bill.bill_id,typeID.toInt())
        Log.d(TAG, "onLastMonthClick: " + operationsByWeek.size)
        viewModelScope.launch {
            eventChannel.send(Event.lastMonth)
        }
        var finalSum=0.0
        for(operation in operationsByWeek)
            finalSum+=operation.operation_value
        val maxValue = database.OperationsDao().getLastMonthOperationsByCategory(bill.bill_id,typeID.toInt()).maxByOrNull { it.operation_value }
        bindableProperties.outputText="За последний месяц вы ${if(typeID) "заработали" else "потратили"} ${finalSum} ${bill.currency_name}  \n" +
                "В среднем вы ${if(typeID) "получали" else "тратили"} ${finalSum/operationsByMonth.size} ${bill.currency_name} в неделю\n" +
                "Больше всего вы ${if(typeID) "получали доходы с категории" else "тратили на категорию"} ${maxValue?.category_name}: ${maxValue?.operation_value} ${bill.currency_name}\n"
    }
}