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
import com.example.project.room.entity.Category
import com.example.project.room.entity.Currency

class CurrencySpinnerAdapter(var currencys:List<Currency>) :BaseAdapter() {
    override fun getCount(): Int {
        return currencys.size
    }

    override fun getItem(position: Int): Currency {
        return currencys[position]
    }
    fun getSize():Int {
        return currencys.size
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    constructor():this(listOf()){

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
        itemHolder.name.text = currencys.get(position).currency_name

        return view
    }
    private class ItemHolder(view: View?){
        val name: TextView
        init{
            name=view?.findViewById(R.id.billName) as TextView
        }
    }
    fun setNewList(newCurrencys:List<Currency>){
        currencys=newCurrencys
        this.notifyDataSetChanged()
    }
}