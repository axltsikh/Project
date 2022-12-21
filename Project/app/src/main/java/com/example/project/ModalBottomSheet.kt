package com.example.project

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.project.fragments.ExspensesFragment
import com.example.project.databinding.ModalBottomSheetContentBinding
import com.example.project.fragments.AddOperation
import com.example.project.fragments.Settings
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class ModalBottomSheet : BottomSheetDialogFragment() {
    private lateinit var binding:ModalBottomSheetContentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(inflater,R.layout.modal_bottom_sheet_content,container,false)
        val view=binding.root
        binding.bottomSheet=this
        return view
    }
    fun homeClickListener(view:View){
        changeScreen(ExspensesFragment())
    }
    fun addOperationClickListener(view:View){
        changeScreen(AddOperation())
    }
    fun categoryClickListener(view:View){
        Log.d(TAG, "categoryClickListener: " + "category")
    }
    fun graphicsClickListener(view:View){
        Log.d(TAG, "categoryClickListener: " + "graphics")
    }
    fun settingsClickListener(view:View){
        changeScreen(Settings())
    }
    fun changeScreen(fragment: Fragment){
        activity?.supportFragmentManager
            ?.beginTransaction()
            ?.replace(R.id.FrameContainer, fragment)
            ?.addToBackStack(null)
            ?.commit()
    }
    companion object {
        val modal= ModalBottomSheet()
        const val TAG = "ModalBottomSheet"
        fun show(activity: MainActivity){
            modal.dialog?.window?.setBackgroundDrawableResource(R.color.WarmGray)
            modal.show(activity.supportFragmentManager,TAG)
        }
    }
}