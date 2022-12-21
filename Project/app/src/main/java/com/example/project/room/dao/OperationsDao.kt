package com.example.project.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.project.room.entity.*

@Dao
public interface OperationsDao {
//    @Query("SELECT * FROM Operation")
//    fun getOperations(): List<OperationGetter>
    @Query("SELECT * FROM Bill")
    fun getBills(): List<Bill>
    @Query("SELECT * FROM Category")
    fun getCategory(): List<Category>
    @Query("SELECT * FROM Currency")
    fun getCurrency(): List<Currency>
    @Query("SELECT Operation.operation_id,Bill.bill_name,Type.type_name,Operation.operation_value,Currency.currency_name, " +
            "Category.category_name,Operation.operation_datetime FROM Operation inner join Type on Type.type_id=Operation.operation_type_id " +
            "inner join Category on Category.category_id=Operation.operation_category_id " +
            "inner join Currency on Currency.currency_id=Operation.operation_currency_id " +
            "inner join Bill on Bill.bill_id=Operation.operation_bill_id and Bill.bill_id = :billID")
    fun getOperations(billID:Int):List<Operations>
    @Query("SELECT Type.type_name,sum(Operation.operation_value) as amount, " +
            "Category.category_name FROM Operation inner join Type on Type.type_id=Operation.operation_type_id and Type.type_name = :type " +
            "inner join Category on Category.category_id=Operation.operation_category_id " +
            "inner join Bill on Bill.bill_id=Operation.operation_bill_id and Bill.bill_id = :billID " +
            "GROUP BY Category.category_name")
    fun getOperationsByCategory(billID:Int, type:String):List<OperationsByCategory>
    @Insert
    fun insertOperation(operation: Operation)
    @Query("DELETE FROM Operation")
    fun del()
}