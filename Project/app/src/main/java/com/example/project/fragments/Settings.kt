package com.example.project.fragments

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import com.example.project.R
import com.example.project.databinding.FragmentSettingsBinding
import com.google.android.material.button.MaterialButtonToggleGroup

class Settings : Fragment() {

    private lateinit var context: FragmentActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding:FragmentSettingsBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_settings, container, false)
        binding.clickHandler= SettingsClickHandlers(binding.root)
        context= requireActivity()
        return binding.root
    }

    override fun onAttach(context: Context) {
        context
        super.onAttach(context)
    }
    inner class SettingsClickHandlers(view:View){
        private lateinit var handlersView:View
        init{
            handlersView=view
        }
        public fun onLanguageClick(view:View){
            Toast.makeText(view.context,"AS",Toast.LENGTH_SHORT).show()
            val buttonsGroup: MaterialButtonToggleGroup=handlersView.findViewById(R.id.LanguageButtonGroup)
            val animation= ObjectAnimator.ofInt(buttonsGroup,"visibility",View.VISIBLE)
            animation.duration=1000
            animation.start()
            val animationT= ObjectAnimator.ofFloat(buttonsGroup,"alpha",1f)
            animationT.duration=1000
            animationT.start()

        }
        public fun onPasswordClick(view:View){
            val bundle:Bundle=Bundle()
            bundle.putBoolean("flag",false)
            val fragment:Fragment=SetPasswordFragment()
            fragment.arguments=bundle
            context.supportFragmentManager.beginTransaction().replace(R.id.FrameContainer,fragment).commit()
        }
    }
}