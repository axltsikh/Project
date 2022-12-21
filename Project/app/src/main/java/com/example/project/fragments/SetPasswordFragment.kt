package com.example.project.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import com.example.project.MainActivity
import com.example.project.ModalBottomSheet.Companion.TAG
import com.example.project.R
import com.example.project.databinding.FragmentSetPasswordBinding
import com.example.project.room.entity.Category
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.progressindicator.LinearProgressIndicator

class SetPasswordFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding:FragmentSetPasswordBinding =DataBindingUtil.inflate(inflater,R.layout.fragment_set_password, container, false)
        val flag:Boolean?=arguments?.getBoolean("flag")
        Log.d(TAG, "onCreateView: " + flag)
        when(arguments?.getBoolean("flag")){
            false -> binding.clickHandler= PasswordClickHandlers(requireActivity(),binding.root,false)
            else -> binding.clickHandler= PasswordClickHandlers(requireActivity(),binding.root, true)
        }
        return binding.root
    }
    class PasswordClickHandlers(con:FragmentActivity,view:View?,val flag:Boolean){
        private var firstPasswordInput:String=""
        private var repeatPasswrodInput:String=""
        private var fragmentActivity:FragmentActivity
        private var indicator:LinearProgressIndicator?
        var progress=0
        init {
            fragmentActivity = con
            indicator = view?.findViewById(R.id.indicatorPassword)
        }
        fun onNumberClick(view:View) {
            Log.d(TAG, "onNumberClick: " + flag)
            if(!flag){
                if(firstPasswordInput.length<4) {
                    progress+=25
                    firstPasswordInput += (view as Button).text
                    indicator?.setProgressCompat(progress,true)
                    Log.d(TAG, "onNumberClick: firstpass" + firstPasswordInput)
                }
                else {
                    repeatPasswrodInput += (view as Button).text
                    progress+=25
                    indicator?.setProgressCompat(progress,true)
                    Log.d(TAG, "onNumberClick: reappass" + repeatPasswrodInput)
                }
                if(firstPasswordInput.length==4){
                    progress=0
                    indicator?.setProgressCompat(progress,true)
                }
                if(firstPasswordInput.equals(repeatPasswrodInput)){
                    fragmentActivity.getSharedPreferences("ProjectPreferences",Context.MODE_PRIVATE).edit().putString("password",repeatPasswrodInput).apply()
                    fragmentActivity.supportFragmentManager.beginTransaction().replace(R.id.FrameContainer,Settings()).commit()
                    Toast.makeText(view.context,R.string.PasswordSettedString,Toast.LENGTH_SHORT).show()
                }
            }
            else{
                firstPasswordInput+=(view as Button).text
                progress+=25
                Log.d(TAG, "onNumberClick: " + progress)
                indicator?.setProgressCompat(progress,true)
                if(firstPasswordInput.length==4){
                    if(fragmentActivity.getSharedPreferences("ProjectPreferences", Context.MODE_PRIVATE).getString("password","")==firstPasswordInput)
                        fragmentActivity.supportFragmentManager.beginTransaction().replace(R.id.FrameContainer,ExspensesFragment()).commit()
                    else{
                        progress=0
                        indicator?.setProgressCompat(progress,true)
                        firstPasswordInput=""
                        Toast.makeText(fragmentActivity,"WrongPassword",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}