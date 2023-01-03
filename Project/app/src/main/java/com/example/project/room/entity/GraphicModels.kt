package com.example.project.room.entity

import androidx.room.ColumnInfo

data class OperationByMonth (
    var operation_id:Int,
    @ColumnInfo(name = "bill_name")
    var operation_bill:String,
    @ColumnInfo(name = "type_name")
    var operation_type:String,
    @ColumnInfo(name = "amount")
    var operation_value:Double,
    @ColumnInfo(name = "currency_name")
    var operation_currency:String,
    @ColumnInfo(name = "category_name")
    var operation_category:String,
    @ColumnInfo(name = "period")
    var operation_datetime:String
)
data class OperationByDate (
    @ColumnInfo(name = "bill_name")
    var operation_bill:String,
    @ColumnInfo(name = "type_name")
    var operation_type:String,
    @ColumnInfo(name = "amount")
    var operation_value:Double,
    @ColumnInfo(name = "currency_name")
    var operation_currency:String,
    @ColumnInfo(name = "category_name")
    var operation_category:String,
    @ColumnInfo(name = "operation_datetime")
    var operation_datetime:Long,
    @ColumnInfo(name = "date")
    var operation_date:String,
    @ColumnInfo(name = "dayofweek")
    val operationDayOfWeek:String
)
data class OperationByDayCategory(
    @ColumnInfo(name = "amount")
    var operation_value: Double,
    @ColumnInfo(name = "category_name")
    var category_name:String,
    @ColumnInfo(name = "date")
    var operation_date:String
)
data class OperationByWeek(
    @ColumnInfo(name = "amount")
    var operation_value: Double,
    @ColumnInfo(name = "date")
    var date:String,
    @ColumnInfo(name = "weeknumber")
    var weeknumber:String,
)