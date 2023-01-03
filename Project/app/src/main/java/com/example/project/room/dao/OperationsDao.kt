package com.example.project.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.project.room.entity.*

@Dao
public interface OperationsDao {
    @Query("SELECT * FROM Bill")
    fun getBills(): List<Bill>
    @Query("SELECT * FROM Category where Category.category_type_id=:categoryTypeID")
    fun getCategory(categoryTypeID:Int): List<Category>
    @Query("SELECT * FROM Currency")
    fun getCurrency(): List<Currency>
    @Query("SELECT Operation.operation_id,Bill.bill_name,Type.type_name,Operation.operation_value,Currency.currency_name, " +
            "Category.category_name,Operation.operation_datetime FROM Operation " +
            "inner join Type on Type.type_id=Operation.operation_type_id and Operation.operation_type_id = :typeID " +
            "inner join Category on Category.category_id=Operation.operation_category_id " +
            "inner join Currency on Currency.currency_id=Operation.operation_currency_id " +
            "inner join Bill on Bill.bill_id=Operation.operation_bill_id and Bill.bill_id = :billID " +
            "order by operation_datetime desc")
    fun getOperations(billID:Int,typeID:Int):List<FullOperation>


    @Query("SELECT Bill.bill_name,Type.type_name,sum(Operation.operation_value) as amount,Currency.currency_name, " +
            "Category.category_name,Operation.operation_datetime, " +
            "strftime('%Y-%m-%d',operation.operation_datetime/1000,'unixepoch') as date, " +
            "case cast (strftime('%w',operation.operation_datetime/1000,'unixepoch') as integer) " +
            "when 0 then 'Воскресенье' " +
            "when 1 then 'Понедельник' " +
            "when 2 then 'Вторник' " +
            "when 3 then 'Среда' " +
            "when 4 then 'Четверг' " +
            "when 5 then 'Пятница' " +
            "else 'Суббота' end as dayofweek " +
            "FROM Operation " +
            "inner join Type on Type.type_id=Operation.operation_type_id and Type.type_id=:type_id " +
            "inner join Category on Category.category_id=Operation.operation_category_id " +
            "inner join Currency on Currency.currency_id=Operation.operation_currency_id "+
            "inner join Bill on Bill.bill_id=Operation.operation_bill_id and Bill.bill_id = :billID " +
            "WHERE date BETWEEN datetime('now', '-6 days') AND datetime('now', 'localtime') " +
            "group by operation.operation_datetime\n")
    fun getLastWeekOperations(billID:Int, type_id:Int):List<OperationByDate>
    @Query("SELECT sum(Operation.operation_value) as amount,\n" +
            "            Category.category_name,Operation.operation_datetime,\n" +
            "            strftime('%Y-%m-%d',operation.operation_datetime/1000,'unixepoch') as date\n" +
            "            FROM Operation\n" +
            "            inner join Type on Type.type_id=Operation.operation_type_id and Type.type_id=:type_id\n" +
            "            inner join Category on Category.category_id=Operation.operation_category_id\n" +
            "            inner join Currency on Currency.currency_id=Operation.operation_currency_id \n" +
            "            inner join Bill on Bill.bill_id=Operation.operation_bill_id and Bill.bill_id = :billID\n" +
            "            WHERE date BETWEEN datetime('now', '-6 days') AND datetime('now', 'localtime')\n" +
            "            group by Category.category_name")
    fun getLastWeekOperationsByCategory(billID:Int,type_id:Int):List<OperationByDayCategory>


    @Query("SELECT sum(Operation.operation_value) as amount,\n" +
            "                        strftime('%Y-%m-%d',operation.operation_datetime/1000,'unixepoch') as date,\n" +
            "                        strftime('%W',operation.operation_datetime/1000,'unixepoch') as weeknumber FROM Operation\n" +
            "                        inner join Type on Type.type_id=Operation.operation_type_id and Type.type_id=:type_id\n" +
            "                        inner join Category on Category.category_id=Operation.operation_category_id\n" +
            "                        inner join Currency on Currency.currency_id=Operation.operation_currency_id\n" +
            "                        inner join Bill on Bill.bill_id=Operation.operation_bill_id and Bill.bill_id = :billID\n" +
            "                        WHERE date BETWEEN datetime('now', '-1 month') AND datetime('now', 'localtime')\n" +
            "\t\t\t\t\t\tgroup by weeknumber")
    fun getLastMonthOperations(billID:Int,type_id:Int):List<OperationByWeek>
    @Query("SELECT sum(Operation.operation_value) as amount,\n" +
            "            Category.category_name,strftime('%Y-%m-%d',operation.operation_datetime/1000,'unixepoch') as date \n" +
            "            FROM Operation\n" +
            "            inner join Type on Type.type_id=Operation.operation_type_id and Type.type_id=:type_id\n" +
            "            inner join Category on Category.category_id=Operation.operation_category_id \n" +
            "            inner join Currency on Currency.currency_id=Operation.operation_currency_id \n" +
            "            inner join Bill on Bill.bill_id=Operation.operation_bill_id and Bill.bill_id = :billID\n" +
            "            WHERE date BETWEEN datetime('now', '-1 month') AND datetime('now', 'localtime')\n" +
            "\t\t\tgroup by Category.category_name")
    fun getLastMonthOperationsByCategory(billID:Int,type_id:Int):List<OperationByDayCategory>

    @Query("SELECT Operation.operation_id,Bill.bill_name,Type.type_name,sum(Operation.operation_value) as amount,Currency.currency_name, " +
            "Category.category_name,strftime('%Y-%m',operation.operation_datetime/1000,'unixepoch') as period FROM Operation " +
            "inner join Type on Type.type_id=Operation.operation_type_id and Operation.operation_type_id=:type_id " +
            "inner join Category on Category.category_id=Operation.operation_category_id " +
            "inner join Currency on Currency.currency_id=Operation.operation_currency_id " +
            "inner join Bill on Bill.bill_id=Operation.operation_bill_id and Bill.bill_id = :billID " +
            "group by strftime('%Y-%m',operation.operation_datetime/1000,'unixepoch')" +
            "having strftime('%Y-%m',operation.operation_datetime/1000,'unixepoch') > datetime('now','-6 months')")
    fun getHalfYearOperations(billID:Int,type_id: Int):List<OperationByMonth>
    @Query("SELECT sum(Operation.operation_value) as amount,\n" +
            "            Category.category_name,Operation.operation_datetime,\n" +
            "            strftime('%Y-%m-%d',operation.operation_datetime/1000,'unixepoch') as date\n" +
            "            FROM Operation\n" +
            "            inner join Type on Type.type_id=Operation.operation_type_id and Type.type_id=:type_id\n" +
            "            inner join Category on Category.category_id=Operation.operation_category_id\n" +
            "            inner join Currency on Currency.currency_id=Operation.operation_currency_id \n" +
            "            inner join Bill on Bill.bill_id=Operation.operation_bill_id and Bill.bill_id = :billID\n" +
            "            WHERE date BETWEEN datetime('now', '-6 months') AND datetime('now', 'localtime')\n" +
            "            group by Category.category_name")
    fun getHalfYearOperationsByCategory(billID:Int,type_id:Int):List<OperationByDayCategory>



    @Query("SELECT Type.type_name,sum(Operation.operation_value) as amount, " +
            "Category.category_name FROM Operation " +
            "inner join Type on Type.type_id=Operation.operation_type_id and operation.operation_type_id = :type " +
            "inner join Category on Category.category_id=Operation.operation_category_id " +
            "inner join Bill on Bill.bill_id=Operation.operation_bill_id and Bill.bill_id = :billID " +
            "GROUP BY Category.category_name")
    fun getOperationsByCategory(billID:Int, type:Int):List<OperationsByCategory>


    @Query("SELECT Bill.bill_id,Currency.currency_name,Bill.bill_amount,Bill.bill_name from Bill inner join Currency on " +
            "Currency.currency_id=Bill.bill_currency_id")
    fun getAllBillsWithCurrency():List<BillGetter>
    @Query("UPDATE Bill set bill_amount = :newAmount where bill_id=:billID")
    fun updateBill(newAmount:Double,billID:Int)
    @Query("DELETE FROM Bill where Bill.bill_id=:billID")
    fun deleteBillByID(billID: Int)
    @Insert
    fun insertOperation(operation: Operation)
    @Insert
    fun insertBill(bill: Bill)
    @Query("DELETE FROM Operation")
    fun del()
}