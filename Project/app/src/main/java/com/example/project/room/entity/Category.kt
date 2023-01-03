package com.example.project.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = arrayOf(
    ForeignKey(
        entity = Type::class,
        parentColumns = arrayOf("type_id"),
        childColumns = arrayOf("category_type_id"),
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )
),tableName = "Category"
)
data class Category (
    @PrimaryKey(autoGenerate = true)
    var category_id:Int,
    var category_name:String,
    var category_type_id:Int,
    var category_icon_resource:String
)
