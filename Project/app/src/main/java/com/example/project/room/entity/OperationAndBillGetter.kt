package com.example.project.room.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Relation

data class Operations (
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
    var operation_datetime:String
)
data class OperationsByCategory (
    @ColumnInfo(name = "type_name")
    var operation_type:String,
    @ColumnInfo(name = "amount")
    var amount:Double,
    @ColumnInfo(name = "category_name")
    var operation_category:String,
)