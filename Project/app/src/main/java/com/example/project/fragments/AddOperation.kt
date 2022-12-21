package com.example.project.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatSpinner
import androidx.databinding.DataBindingUtil
import com.example.project.R
import com.example.project.databinding.FragmentAddOperationBinding
import com.example.project.room.database.OperationsDatabase
import com.example.project.viewmodels.AddOperationViewModel

class AddOperation : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding:FragmentAddOperationBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_add_operation, container, false)
        val db = context?.let { OperationsDatabase.getDatabase(it) }
        binding.viewmodel= db?.let { context?.let { it1 -> AddOperationViewModel(it, it1, requireActivity()) } }
        return binding.root
    }
}