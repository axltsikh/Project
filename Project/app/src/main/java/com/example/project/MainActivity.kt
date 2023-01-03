package com.example.project

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.res.AssetManager
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.project.ModalBottomSheet.Companion.TAG
import com.example.project.ModalBottomSheet.Companion.show
import com.example.project.databinding.ActivityMainBinding
import com.example.project.fragments.ExspensesFragment
import com.example.project.fragments.SetPasswordFragment
import com.example.project.rertofit.Convert
import com.example.project.rertofit.GsonService
import com.example.project.room.dao.OperationsDao
import com.example.project.room.database.OperationsDatabase
import com.example.project.room.entity.Currency
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val binding:ActivityMainBinding=DataBindingUtil.setContentView(this,R.layout.activity_main);
        binding.clickHandler=MainActivityClickHandlers(this)
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
            showMenu()
            supportFragmentManager.beginTransaction()
                .replace(R.id.FrameContainer, ExspensesFragment()).commit()
        }
    }
    fun showMenu(){
        findViewById<FloatingActionButton>(R.id.menuButton).visibility=View.VISIBLE
    }
    class MainActivityClickHandlers(val activity: MainActivity){
        fun onMenuButtonClick(view: View){
            ModalBottomSheet.show(activity)
        }
    }
}
