package com.example.project.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Type")
data class Type(
    @PrimaryKey(autoGenerate = true)
    var type_id:Int,
    val type_name:String
)