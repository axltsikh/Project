package com.example.project.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.project.R
import com.example.project.room.database.OperationsDatabase
import com.example.project.room.entity.BillGetter
import com.example.project.viewmodels.AddBillViewModel

class BillRecyclerViewAdapter(private var bills:List<BillGetter>,val database: OperationsDatabase) :
    RecyclerView.Adapter<BillRecyclerViewAdapter.OperationViewHolder>(){
    class OperationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var billName: TextView = itemView.findViewById(R.id.BillName)
        var billMoneyLeft: TextView = itemView.findViewById(R.id.BillMoneyLeft)
        var billDeletBill: Button = itemView.findViewById(R.id.DeleteBill   )
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OperationViewHolder {
        val itemView= LayoutInflater.from(parent.context).inflate(R.layout.bill_recyclerview_item,parent,false)
        return OperationViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: OperationViewHolder, position: Int) {
        holder.billName.text="Счёт: " + bills[position].bill_name
        holder.billMoneyLeft.text="Остаток: " + bills[position].bill_amount.toString() + bills[position].currency_name
        holder.billDeletBill.setOnClickListener {
            AddBillViewModel.deleteItem(database,bills[position].bill_id)
            bills=database.OperationsDao().getAllBillsWithCurrency()
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return bills.size
    }
    fun getAllOperations(): List<BillGetter> {
        return bills
    }
    fun setNewList(list:List<BillGetter>){
        bills=list
        this.notifyDataSetChanged()
    }
}