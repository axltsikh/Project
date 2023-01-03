package com.example.project.viewmodels


import android.animation.ObjectAnimator
import android.content.res.Resources
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project.ModalBottomSheet.Companion.TAG
import com.example.project.R
import com.example.project.adapters.BillsSpinnerAdapter
import com.example.project.adapters.CategoryGridAdapter
import com.example.project.adapters.CurrencySpinnerAdapter
import com.example.project.rertofit.Convert
import com.example.project.rertofit.GsonService
import com.example.project.room.database.OperationsDatabase
import com.example.project.room.entity.BillGetter
import com.example.project.room.entity.Operation
import com.example.project.utilities.AddOperationBindableProperties
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

class AddOperationViewModel(private val database:OperationsDatabase) : ViewModel() {

    sealed class Event{
        object selectDate:Event()
        object startValidationAnimation:Event()
    }
    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow=eventChannel.receiveAsFlow()
    //adapters
    lateinit var categoryAdapter: CategoryGridAdapter
    lateinit var currencySpinnerAdapter: CurrencySpinnerAdapter
    lateinit var billsSpinnerAdapter: BillsSpinnerAdapter
    var lastClickedCategory:View?=null
    //model
    var bindableProperties:AddOperationBindableProperties = AddOperationBindableProperties()
    var operation: Operation = Operation()
    var bill: BillGetter = BillGetter()
    var billselection=1

    init{
        viewModelScope.launch {
            categoryAdapter= CategoryGridAdapter(database.OperationsDao().getCategory(operation.operation_type_id))
            currencySpinnerAdapter=CurrencySpinnerAdapter(database.OperationsDao().getCurrency())
            billsSpinnerAdapter=BillsSpinnerAdapter(database.OperationsDao().getAllBillsWithCurrency())
            onConsumptionClick(null)
        }
    }
    fun onConsumptionClick(view:View?){
        operation.operation_type_id=2
        categoryAdapter.setNewList(database.OperationsDao().getCategory(operation.operation_type_id))
    }
    fun onIncomeClick(view:View){
        operation.operation_type_id=1
        categoryAdapter.setNewList(database.OperationsDao().getCategory(operation.operation_type_id))
    }
    fun onAddClick(view: View){
        if(!bindableProperties.operationSum.equals(""))
            operation.operation_value=bindableProperties.operationSum.toDouble()
        if(bindableProperties.elementsVisibility==View.VISIBLE)
            operation.operation_value=bindableProperties.afterConversionValue.toDouble()
        if(!validateOperation(view)) {
            viewModelScope.launch {
                eventChannel.send(Event.startValidationAnimation)
            }
            return
        }
        when(operation.operation_type_id){
            1->{
                database.OperationsDao().updateBill(bill.bill_amount+operation.operation_value,operation.operation_bill_id)
                database.OperationsDao().insertOperation(operation)
            }
            2->{
                if(bill.bill_amount<operation.operation_value){
                    setErrorMessageText("На счету недостаточно средств",view)
                    return
                }
                database.OperationsDao().updateBill(bill.bill_amount-operation.operation_value,operation.operation_bill_id)
                database.OperationsDao().insertOperation(operation)
            }
        }
        billsSpinnerAdapter.setNewList(database.OperationsDao().getAllBillsWithCurrency())
        bill.updateBill(billsSpinnerAdapter.getItem(billselection))

        bindableProperties.operationSum=""
    }
    fun onDateSelectClick(view: View){
        viewModelScope.launch {
            eventChannel.send(Event.selectDate)
        }
    }
    fun onGridViewItemClick(adapterView: AdapterView<*>,view:View,position: Int,id: Long){
        if(lastClickedCategory==null){
            viewModelScope.launch {
                lastClickedCategory=view
                scaleAnimation(view,1.1f)
                elevationAnimation(view,50f)
            }
        }
        else{
            viewModelScope.launch {
                scaleAnimation(lastClickedCategory!!,1f)
                elevationAnimation(lastClickedCategory!!,1f)
                lastClickedCategory=view
                scaleAnimation(lastClickedCategory!!,1.1f)
                elevationAnimation(lastClickedCategory!!,50f)
            }
        }
        operation.operation_category_id=categoryAdapter.getItem(position).category_id
    }
    fun scaleAnimation(view:View,value:Float){
        val objectAnimatorX = ObjectAnimator.ofFloat(view, "scaleX", value)
        objectAnimatorX.duration = 100
        objectAnimatorX.start()
        val objectAnimatorY = ObjectAnimator.ofFloat(view, "scaleY", value)
        objectAnimatorY.duration = 100
        objectAnimatorY.start()
    }
    fun elevationAnimation(view:View,value: Float){
        val objectAnimatorX = ObjectAnimator.ofFloat(view, "elevation", value)
        objectAnimatorX.duration = 100
        objectAnimatorX.start()
        val objectAnimatorY = ObjectAnimator.ofFloat(view, "elevation", value)
        objectAnimatorY.duration = 100
        objectAnimatorY.start()
    }
    fun onCurrencySelectionChanged(adapterView: AdapterView<*>, view:View, position:Int, id:Long){
        if(currencySpinnerAdapter.getItem(position).currency_name!=bill.currency_name){
            viewModelScope.launch {
                getConvertion(bill.currency_name,currencySpinnerAdapter.getItem(position).currency_name)
            }
        }
        else{
            bindableProperties.elementsVisibility=View.GONE
        }
    }
    fun onBillSelectionChanged(adapterView: AdapterView<*>, view:View, position:Int, id:Long){
        billselection=position
        Log.d(TAG, "onBillSelectionChanged: update")
        bill.updateBill(billsSpinnerAdapter.getItem(position))
        when(bill.currency_name){
            "BYN"-> {
                bindableProperties.currencySelection = 0
                operation.operation_currency_id=1
            }
            "RUB"-> {
                bindableProperties.currencySelection = 1
                operation.operation_currency_id=2
            }
            "EUR"->{
                bindableProperties.currencySelection=3
                operation.operation_currency_id=4
            }
            "USD"->{
                bindableProperties.currencySelection=2
                operation.operation_currency_id=3
            }
            "JPY"->{
                bindableProperties.currencySelection=4
                operation.operation_currency_id=5
            }
            "GBP"->{
                bindableProperties.currencySelection=5
                operation.operation_currency_id=6
            }
            "PLN"->{
                bindableProperties.currencySelection=6
                operation.operation_currency_id=7
            }
        }
        operation.operation_bill_id=bill.bill_id
    }

    //Retrofit
    fun getConvertion(firstcurrency:String,secondcurrency:String):Double{
        var resultExchange:Double=1.0
        val retrofit= Retrofit.Builder()
            .baseUrl("https://www.exchangerate-api.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val exchange=retrofit.create(GsonService::class.java)
        val result=exchange.getExchange(secondcurrency,firstcurrency)
        result.enqueue(object: Callback<Convert> {
            override fun onResponse(call: Call<Convert>, response: Response<Convert>){
                val buffer=response.body()
                resultExchange=buffer?.conversion_rate!!
                bindableProperties.elementsVisibility=View.VISIBLE
                bindableProperties.baseToRequired=resultExchange
                bindableProperties.baseCurrency=firstcurrency
                val df=DecimalFormat("#.##", DecimalFormatSymbols(Locale.ENGLISH))
                df.roundingMode=RoundingMode.CEILING
                bindableProperties.baseToRequired =df.format(resultExchange).toDouble()
                if(!bindableProperties.operationSum.equals("")) {
                    val roundedValue = (bindableProperties.operationSum.toDouble() * bindableProperties.baseToRequired)
                    bindableProperties.afterConversionValue=(roundedValue).toString()
                }
            }

            override fun onFailure(call: Call<Convert>, t: Throwable) {
                Log.d(TAG, "onFailure: " + t.message)
            }
        })
        return resultExchange
    }

    fun onValueTextChanged(s:CharSequence,start:Int,before:Int,count:Int){
        if(bindableProperties.elementsVisibility==View.VISIBLE){
            if(!s.toString().equals("")) {
                bindableProperties.afterConversionValue = (bindableProperties.baseToRequired * s.toString().toDouble()).toString()
           }
        }
    }
    //Валидация
    fun validateOperation(view:View):Boolean{
        if(operation.operation_type_id==0){
            setErrorMessageText("Выберите тип операции",view)
            return false
        }
        if(operation.operation_datetime==0L){
            setErrorMessageText("Выберите дату",view)
            return false
        }
        if(operation.operation_value.equals(0.0)){
            setErrorMessageText("Введите сумму операции",view)
            return false
        }
        if(operation.operation_category_id.equals(0)){
            setErrorMessageText("Выберите категорию",view)
            return false
        }
        return true
    }
    fun setErrorMessageText(text:String,view:View){
        bindableProperties.errorText=text
        bindableProperties.errorVisibility=View.VISIBLE
    }
}