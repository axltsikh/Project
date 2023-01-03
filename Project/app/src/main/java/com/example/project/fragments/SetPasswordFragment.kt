package com.example.project.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.BaseObservable
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import com.example.project.MainActivity
import com.example.project.ModalBottomSheet.Companion.TAG
import com.example.project.R
import com.example.project.databinding.FragmentSetPasswordBinding
import com.google.android.material.progressindicator.LinearProgressIndicator

class SetPasswordFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding:FragmentSetPasswordBinding =DataBindingUtil.inflate(inflater,R.layout.fragment_set_password, container, false)
        when(arguments?.getBoolean("flag")){
            false -> binding.clickHandler= PasswordClickHandlers(requireActivity(),binding.root,false)
            else -> binding.clickHandler= PasswordClickHandlers(requireActivity(),binding.root, true)
        }
        return binding.root
    }
    class PasswordClickHandlers(val activity:FragmentActivity,val parentView: View, val flag:Boolean):BaseObservable(){
        private var firstPasswordInput:String=""
        private var repeatPasswrodInput:String=""
        var notificationText:String=activity.resources.getString(R.string.InputPasswordString)
        private var indicator:LinearProgressIndicator = parentView.findViewById(R.id.indicatorPassword)
        var progress=0
        fun onNumberClick(view:View) {
            if(!flag){
                progress+=25
                indicator.setProgressCompat(progress,true)
                if(firstPasswordInput.length==4){
                    repeatPasswrodInput += (view as Button).text
                }
                if(firstPasswordInput.length<4) {
                    firstPasswordInput += (view as Button).text
                }
                if(firstPasswordInput.length==4 && repeatPasswrodInput.length==0){
                    setNewText(activity.resources.getString(R.string.RepeatPasswordString))
                    progress=0
                    indicator.setProgressCompat(progress,false)
                }
                if(repeatPasswrodInput.length==4) {
                    if (firstPasswordInput.equals(repeatPasswrodInput)) {
                        activity.getSharedPreferences("ProjectPreferences", Context.MODE_PRIVATE)
                            .edit().putString("password", repeatPasswrodInput).apply()
                        activity.supportFragmentManager.beginTransaction()
                            .replace(R.id.FrameContainer, ExspensesFragment()).commit()
                        Toast.makeText(
                            view.context,
                            R.string.PasswordSettedString,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else{
                        progress=0
                        indicator.invalidate()
                        parentView.startAnimation(AnimationUtils.loadAnimation(activity,R.anim.shake_animation))
                    }
                }
            }
            else{
                firstPasswordInput+=(view as Button).text
                progress+=25
                indicator.setProgressCompat(progress,true)
                if(firstPasswordInput.length==4){
                    if(activity.getSharedPreferences("ProjectPreferences", Context.MODE_PRIVATE).getString("password","")==firstPasswordInput){
                        activity as MainActivity
                        activity.showMenu()
                        activity.supportFragmentManager.beginTransaction().replace(R.id.FrameContainer,ExspensesFragment()).commit()
                    }
                    else{
                        parentView.startAnimation(AnimationUtils.loadAnimation(activity,R.anim.shake_animation))
                        progress=0
                        indicator.setProgressCompat(progress,false)
                        firstPasswordInput=""
                        setNewText(activity.resources.getString(R.string.WrongPassword))
                    }
                }
            }
        }
        private fun setNewText(newText:String){
            notificationText=newText
            notifyChange()
        }
    }
}