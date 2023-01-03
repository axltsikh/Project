package com.example.project.viewmodels

import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.databinding.BaseObservable
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project.ModalBottomSheet.Companion.TAG
import com.example.project.adapters.BillRecyclerViewAdapter
import com.example.project.adapters.CurrencySpinnerAdapter
import com.example.project.room.database.OperationsDatabase
import com.example.project.room.entity.Bill
import com.example.project.room.entity.BillGetter
import com.example.project.utilities.AddBillBindableProperties
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AddBillViewModel(var database: OperationsDatabase):ViewModel() {

    var bill = Bill()
    var bindableProperties:AddBillBindableProperties
    var currencySpinnerAdapter: CurrencySpinnerAdapter = CurrencySpinnerAdapter()
    var billRecyclerViewAdapter:BillRecyclerViewAdapter
    sealed class Event{
        object startValidationAnimation:Event()
    }
    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow=eventChannel.receiveAsFlow()
    init{
        bindableProperties= AddBillBindableProperties()
        billRecyclerViewAdapter= BillRecyclerViewAdapter(database.OperationsDao().getAllBillsWithCurrency(),database)
        billRecyclerViewAdapter.setNewList(database.OperationsDao().getAllBillsWithCurrency())
        currencySpinnerAdapter.setNewList(database.OperationsDao().getCurrency())
    }
    fun onCurrencySelectionChanged(adapterView: AdapterView<*>, view:View, position:Int, id:Long){
        bill.bill_currency_id=currencySpinnerAdapter.getItem(position).currency_id
    }
    fun onAddBillClick(view:View){
        if(!bindableProperties.startingSum.equals(""))
            bill.bill_amount=bindableProperties.startingSum.toDouble()
        if(bill.bill_name.equals("")){
            viewModelScope.launch {
                bindableProperties.errorText="Введите название счёта"
                bindableProperties.visibility=View.VISIBLE
                eventChannel.send(Event.startValidationAnimation)
            }
            return
        }
        bindableProperties.visibility=View.GONE
        database.OperationsDao().insertBill(bill)
        bill.setNewBill()
        billRecyclerViewAdapter.setNewList(database.OperationsDao().getAllBillsWithCurrency())
        bindableProperties.startingSum=""
    }
    companion object{
        fun deleteItem(database: OperationsDatabase,id:Int){
            database.OperationsDao().deleteBillByID(id)
        }
    }
}