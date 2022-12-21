package com.example.project

import android.app.Activity
import android.content.Context
import android.content.res.AssetManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.project.ModalBottomSheet.Companion.TAG
import com.example.project.fragments.ExspensesFragment
import com.example.project.fragments.SetPasswordFragment
import com.example.project.room.dao.OperationsDao
import com.example.project.room.database.OperationsDatabase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val pass: String? =
            getSharedPreferences("ProjectPreferences", Context.MODE_PRIVATE).getString(
                "password",
                ""
            )
        if (pass != "") {
            val bundle: Bundle = Bundle()
            bundle.putBoolean("flag", true)
            val fragment: Fragment = SetPasswordFragment()
            fragment.arguments = bundle
            supportFragmentManager.beginTransaction().replace(R.id.FrameContainer, fragment)
                .commit()
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.FrameContainer, ExspensesFragment()).commit()
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(event!=null){
            if(event.action==MotionEvent.ACTION_UP){
                val modal:ModalBottomSheet= ModalBottomSheet()
                ModalBottomSheet.show(this)
            }
        }
        return true
    }
}
