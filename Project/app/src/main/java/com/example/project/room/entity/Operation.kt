package com.example.project.room.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = arrayOf(
    ForeignKey(
        entity = Currency::class,
        parentColumns = arrayOf("currency_id"),
        childColumns = arrayOf("operation_currency_id"),
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    ),
    ForeignKey(
        entity = Category::class,
        parentColumns = arrayOf("category_id"),
        childColumns = arrayOf("operation_category_id"),
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    ),
    ForeignKey(
        entity = Type::class,
        parentColumns = arrayOf("type_id"),
        childColumns = arrayOf("operation_type_id"),
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    ),
    ForeignKey(
        entity = Bill::class,
        parentColumns = arrayOf("bill_id"),
        childColumns = arrayOf("operation_bill_id"),
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )
), tableName = "Operation"
)
data class Operation (
    @PrimaryKey(autoGenerate = true)
    var operation_id:Int,
    var operation_bill_id:Int,
    var operation_type_id:Int,
    var operation_value:Double,
    var operation_currency_id:Int,
    var operation_category_id:Int,
    var operation_datetime:Long
){
    constructor() : this(0, 1, 0, 0.0,0,0,0)
}