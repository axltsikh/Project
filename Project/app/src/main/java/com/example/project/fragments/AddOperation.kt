package com.example.project.fragments

import android.animation.AnimatorInflater
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.AppCompatSpinner
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.project.ModalBottomSheet
import com.example.project.ModalBottomSheet.Companion.TAG
import com.example.project.R
import com.example.project.adapters.BillsSpinnerAdapter
import com.example.project.adapters.CategoryGridAdapter
import com.example.project.adapters.CurrencySpinnerAdapter
import com.example.project.databinding.FragmentAddOperationBinding
import com.example.project.room.database.OperationsDatabase
import com.example.project.room.entity.BillGetter
import com.example.project.room.entity.Operation
import com.example.project.viewmodels.AddOperationViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class AddOperation : Fragment() {
    private lateinit var viewModel:AddOperationViewModel
    private lateinit var parentLinearLayout: LinearLayout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding:FragmentAddOperationBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_add_operation, container, false)
        binding.viewmodel= AddOperationViewModel(OperationsDatabase.getDatabase(requireContext()))
        viewModel=binding.viewmodel as AddOperationViewModel
        parentLinearLayout=binding.root.findViewById(R.id.ParentLinearLayout)
        viewModel.eventsFlow.onEach {
            when(it){
                is AddOperationViewModel.Event.startValidationAnimation -> startValidationAnimation()
                is AddOperationViewModel.Event.selectDate -> getOperationDate()
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
        return binding.root
    }


    fun getOperationDate(){
        val datePicker = MaterialDatePicker.Builder.datePicker().setTitleText("Дата").build()
        datePicker.show(requireActivity().supportFragmentManager,"tag")
        datePicker.addOnPositiveButtonClickListener {
            viewModel.operation.operation_datetime=it
        }
    }
    fun startValidationAnimation(){
        parentLinearLayout.startAnimation(AnimationUtils.loadAnimation(activity,R.anim.shake_animation))
    }

}