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
import com.example.project.databinding.ModalBottomSheetContentBinding
import com.example.project.fragments.*
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
    fun addBillClickListener(view:View){
        changeScreen(AddBill())
    }
    fun graphicsClickListener(view:View){
        changeScreen(GraphicsFragment())
    }
    fun settingsClickListener(view:View){
        val bundle:Bundle=Bundle()
        bundle.putBoolean("flag",false)
        val fragment:Fragment= SetPasswordFragment()
        fragment.arguments=bundle
        changeScreen(fragment)
    }
    fun changeScreen(fragment: Fragment){
        activity?.supportFragmentManager
            ?.beginTransaction()
            ?.setCustomAnimations(R.anim.slide_from_top,R.anim.slide_to_top)
            ?.replace(R.id.FrameContainer, fragment)
            ?.addToBackStack(null)
            ?.commit()
        close()
    }
    companion object {
        val modal= ModalBottomSheet()
        const val TAG = "ModalBottomSheet"
        fun show(activity: MainActivity){
            modal.show(activity.supportFragmentManager,TAG)
        }
        fun close(){
            modal.dismiss()
        }
    }
}