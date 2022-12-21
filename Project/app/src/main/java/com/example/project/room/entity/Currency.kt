package com.example.project.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Currency")
data class Currency (
    @PrimaryKey(autoGenerate = true)
    var currency_id:Int,
    var currency_name:String
)