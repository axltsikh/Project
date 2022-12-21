package com.example.project.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.project.R
import com.example.project.room.entity.Operations

class OperationsRecyclerViewAdapter(private val operations:List<Operations>) :
    RecyclerView.Adapter<OperationsRecyclerViewAdapter.OperationViewHolder>(){
    class OperationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var type: TextView =itemView.findViewById(R.id.operationTypeRecyclerViewItem)
        var value: TextView =itemView.findViewById(R.id.operationValueRecyclerViewItem)
        var category: TextView =itemView.findViewById(R.id.operationCategoryRecyclerViewItem)
        var date: TextView =itemView.findViewById(R.id.operationDateRecyclerViewItem)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OperationViewHolder {
        val itemView=LayoutInflater.from(parent.context).inflate(R.layout.operation_recyclerview_item,parent,false)
        return OperationViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: OperationViewHolder, position: Int) {
        holder.category.text= operations[position].operation_category
        holder.date.text= operations[position].operation_datetime
        holder.type.text= operations[position].operation_type
        holder.value.text= operations[position].operation_value.toString()
    }

    override fun getItemCount(): Int {
        return operations.size
    }
}