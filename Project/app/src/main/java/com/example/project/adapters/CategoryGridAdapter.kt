package com.example.project.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.project.R
import com.example.project.room.entity.Category

class CategoryGridAdapter(val categoryList:List<Category>, val context: Context) : BaseAdapter(){

    private var layoutInflater:LayoutInflater?=null
    private lateinit var categoryName: TextView
    private lateinit var categoryIcon: ImageView
    override fun getCount(): Int {
        return categoryList.size
    }

    override fun getItem(p0: Int): Any {
        return categoryList.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        if (layoutInflater == null) {
            layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }
        if (convertView == null) {
            convertView = layoutInflater!!.inflate(R.layout.category_grid_view_item, null)
        }
        categoryName=convertView!!.findViewById(R.id.categoryName)
        categoryIcon= convertView.findViewById(R.id.categoryImage)
        categoryName.text=categoryList.get(position).category_name
        categoryIcon.setImageResource(R.drawable.bottom_sheet_settings_icon)
        return convertView
    }

}