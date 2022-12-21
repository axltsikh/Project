package com.example.project.viewmodels

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project.adapters.CategoryGridAdapter
import com.example.project.room.database.OperationsDatabase
import com.example.project.room.entity.Category
import com.example.project.room.entity.Operation
import com.google.android.material.datepicker.MaterialDatePicker
import java.sql.Date

class AddOperationViewModel(val database:OperationsDatabase,val context: Context,val activity: FragmentActivity) : ViewModel(){
    private var categorysList:List<Category>
    var sum:String="0"
    val categoryAdapter:CategoryGridAdapter
    val currencyEntries:ArrayList<String> = ArrayList()

    //model
    val operation:Operation=Operation()
    init{
        categorysList=database.OperationsDao().getCategory()
        categoryAdapter= CategoryGridAdapter(categorysList,context)
        for(a in database.OperationsDao().getCurrency()){
            currencyEntries.add(a.currency_name)
        }

    }
    fun onIncomeClick(view:View){
        operation.operation_type_id=1
    }
    fun onConsumptionClick(view:View){
        operation.operation_type_id=2
    }
    fun onAddClick(view: View){
        Toast.makeText(context,sum,Toast.LENGTH_SHORT).show()
        operation.operation_value=sum.toDouble()
        database.OperationsDao().insertOperation(operation)
    }
    fun onCurrencyItemSelectedListener(adapterView: AdapterView<*>,view:View,position:Int,id:Long){
        operation.operation_currency_id=database.OperationsDao().getCurrency().get(position).currency_id
    }
    fun onDateSelectClick(view: View){
        val datePicker = MaterialDatePicker.Builder.datePicker().setTitleText("Дата").build()
        datePicker.show(activity.supportFragmentManager,"tag")
        datePicker.addOnPositiveButtonClickListener {
            operation.operation_datetime=Date(it).toString()
        }
    }
    fun onGridViewItemClick(adapterView: AdapterView<*>,view:View,position: Int,id: Long){
        operation.operation_category_id=categorysList.get(position).category_id
    }
}