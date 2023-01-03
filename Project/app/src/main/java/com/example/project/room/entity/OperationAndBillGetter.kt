package com.example.project.room.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.room.ColumnInfo
import com.example.project.BR

data class FullOperation (
    var operation_id:Int,
    @ColumnInfo(name = "bill_name")
    var operation_bill:String,
    @ColumnInfo(name = "type_name")
    var operation_type:String,
    @ColumnInfo(name = "operation_value")
    var operation_value:Double,
    @ColumnInfo(name = "currency_name")
    var operation_currency:String,
    @ColumnInfo(name = "category_name")
    var operation_category:String,
    @ColumnInfo(name = "operation_datetime")
    var operation_datetime:Long
)
data class OperationsByCategory (
    @ColumnInfo(name = "type_name")
    var operation_type:String,
    @ColumnInfo(name = "amount")
    var amount:Double,
    @ColumnInfo(name = "category_name")
    var operation_category:String,
)
class BillGetter(bill_id:Int,
                 currency_name:String,
                 bill_amount:Double,
                 bill_name:String):BaseObservable(){
    @get:Bindable
    var bill_id:Int=0
        set(value){
            field=value
            notifyPropertyChanged(BR.bill_id)
        }
    @get:Bindable
    var currency_name:String=""
        set(value){
            field=value
            notifyPropertyChanged(BR.currency_name)
        }
    @get:Bindable
    var bill_amount:Double=0.0
        set(value){
            field=value
            notifyPropertyChanged(BR.bill_amount)
        }
    @get:Bindable
    var bill_name:String=""
        set(value){
            field=value
            notifyPropertyChanged(BR.bill_name)
        }
    constructor(): this(1,"BYN",0.0,"Основной")
    fun updateBill(bill:BillGetter){
        bill_id=bill.bill_id
        bill_amount=bill.bill_amount
        bill_name=bill.bill_name
        currency_name=bill.currency_name
        notifyChange()
    }
}