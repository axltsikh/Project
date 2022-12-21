package com.example.project.room.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

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
data class Bill (
        @PrimaryKey(autoGenerate = true)
        var bill_id:Int,
        var bill_name:String,
        var bill_currency_id:Int,
        var bill_amount:Double
)