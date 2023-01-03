package com.example.project.adapters

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.project.R
import com.example.project.room.entity.Bill
import com.example.project.room.entity.BillGetter
import com.example.project.room.entity.Currency

class BillsSpinnerAdapter(var bills:List<BillGetter>) : BaseAdapter() {
    override fun getCount(): Int {
        return bills.size
    }

    override fun getItem(position: Int): BillGetter {
        return bills[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    constructor(context:Context):this(listOf()){

    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val itemHolder: ItemHolder
        if (convertView == null) {
            view = LayoutInflater.from(parent?.context).inflate(R.layout.bills_spinner_item, parent, false)
            itemHolder = ItemHolder(view)
            view?.tag = itemHolder
        } else {
            view = convertView
            itemHolder = view.tag as ItemHolder
        }
        itemHolder.name.text = "Счёт: " + bills.get(position).bill_name

        return view
    }
    private class ItemHolder(view:View?){
        val name: TextView
        init{
            name=view?.findViewById(R.id.billName) as TextView
        }
    }
    fun setNewList(newBills:List<BillGetter>){
        bills=newBills
        this.notifyDataSetChanged()
    }
}