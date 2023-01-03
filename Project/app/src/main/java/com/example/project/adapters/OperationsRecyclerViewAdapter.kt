package com.example.project.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.project.R
import com.example.project.room.entity.FullOperation
import java.text.SimpleDateFormat
import java.util.*

class OperationsRecyclerViewAdapter(private var operations:List<FullOperation>) :
    RecyclerView.Adapter<OperationsRecyclerViewAdapter.OperationViewHolder>(){
    class OperationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var value: TextView =itemView.findViewById(R.id.operationValueAndCurrencyRecyclerViewItem)
        var category: TextView =itemView.findViewById(R.id.operationCategoryRecyclerViewItem)
        var date: TextView =itemView.findViewById(R.id.operationDateRecyclerViewItem)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OperationViewHolder {
        val itemView=LayoutInflater.from(parent.context).inflate(R.layout.operation_recyclerview_item,parent,false)
        return OperationViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: OperationViewHolder, position: Int) {
        var formatter = SimpleDateFormat("yyyy-MM-dd")
        holder.category.text= operations[position].operation_category
        holder.date.text= formatter.format(operations[position].operation_datetime).toString()
        holder.value.text= operations[position].operation_value.toString() + " " +operations[position].operation_currency
    }

    override fun getItemCount(): Int {
        return operations.size
    }
    fun getAllOperations(): List<FullOperation> {
        return operations
    }
    fun setNewList(list:List<FullOperation>){
        operations=list
        this.notifyDataSetChanged()
    }
}