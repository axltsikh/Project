package com.example.project.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.project.ModalBottomSheet.Companion.TAG
import com.example.project.R
import com.example.project.databinding.FragmentExspensesBinding
import com.example.project.room.database.OperationsDatabase
import com.example.project.room.entity.Operations
import com.example.project.viewmodels.ExspensesViewModel

class ExspensesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding:FragmentExspensesBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_exspenses, container, false)
        val db = context?.let { OperationsDatabase.getDatabase(it) }
        binding.viewmodel= db?.let { ExspensesViewModel(it,binding.root) }
        return binding.root
    }

}