package com.example.project.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Category")
data class Category (
    @PrimaryKey(autoGenerate = true)
    var category_id:Int,
    var category_name:String
)
