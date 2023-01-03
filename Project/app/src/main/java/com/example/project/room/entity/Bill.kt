package com.example.project.room.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.example.project.BR

@Entity(foreignKeys = arrayOf(
        ForeignKey(
    entity = Currency::class,
    parentColumns = arrayOf("currency_id"),
    childColumns = arrayOf("bill_currency_id"),
    onDelete = CASCADE,
    onUpdate = CASCADE
        )
),tableName = "Bill"
)
class Bill (
        bill_id:Int,
        bill_name:String,
        bill_currency_id:Int,
        bill_amount:Double
):BaseObservable(){
    @PrimaryKey(autoGenerate = true)
    @get:Bindable
    var bill_id:Int=0
        set(value){
            field=value
            notifyPropertyChanged(BR.bill_id)
        }
    @get:Bindable
    var bill_name:String=""
        set(value){
            field=value
            notifyPropertyChanged(BR.bill_name)
        }
    @get:Bindable
    var bill_currency_id:Int=1
        set(value){
            field=value
            notifyPropertyChanged(BR.bill_currency_id)
        }
    @get:Bindable
    var bill_amount:Double=0.0
        set(value){
            field=value
            notifyPropertyChanged(BR.bill_amount)
        }
    constructor():this(0,"",1,0.0)
    fun setNewBill(){
        bill_id=0
        bill_name=""
        bill_currency_id=1
        bill_amount=0.0
    }
}