package com.example.project.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import com.example.project.ModalBottomSheet.Companion.TAG
import com.example.project.R
import com.example.project.databinding.FragmentAddBillBinding
import com.example.project.room.database.OperationsDatabase
import com.example.project.viewmodels.AddBillViewModel
import com.example.project.viewmodels.AddOperationViewModel
import kotlinx.coroutines.flow.onEach

class AddBill : Fragment() {
    lateinit var parentLinearLayout:LinearLayout
    lateinit var viewModel:AddBillViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding:FragmentAddBillBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_add_bill,container,false)
        binding.viewmodel= AddBillViewModel(OperationsDatabase.getDatabase(requireContext()))
        viewModel=binding.viewmodel as AddBillViewModel
        parentLinearLayout=binding.root.findViewById(R.id.addBillParent)
        viewModel.eventsFlow.onEach {
            when(it){
                is AddBillViewModel.Event.startValidationAnimation -> startValidationAnimation()
            }
        }
        return binding.root
    }
    fun startValidationAnimation(){
        parentLinearLayout.startAnimation(AnimationUtils.loadAnimation(activity,R.anim.shake_animation))
    }
}