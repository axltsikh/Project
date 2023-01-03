package com.example.project.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.project.R
import com.example.project.room.entity.Category


class CategoryGridAdapter(var categoryList:List<Category>) : BaseAdapter(){
    lateinit var categoryName: TextView
    lateinit var categoryIcon: ImageView
    override fun getCount(): Int {
        return categoryList.size
    }
    constructor():this(listOf()){

    }
    override fun getItem(p0: Int): Category {
        return categoryList.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = LayoutInflater.from(parent?.context).inflate(R.layout.category_grid_view_item, null)
        }
        categoryName=convertView!!.findViewById(R.id.categoryName)
        categoryIcon= convertView.findViewById(R.id.categoryImage)
        categoryName.text= categoryList[position].category_name
        parent?.context?.resources?.getIdentifier(categoryList.get(position).category_icon_resource,"drawable",parent.context.packageName)
            ?.let { categoryIcon.setImageResource(it) }
        return convertView
    }
    fun setNewList(categorys:List<Category>){
        categoryList=categorys
        this.notifyDataSetChanged()
    }
}