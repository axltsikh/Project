package com.example.project.utilities

import android.view.View
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.example.project.BR

class AddBillBindableProperties:BaseObservable() {
    @get:Bindable
    var errorText:String=""
        set(value){
            field=value
            notifyPropertyChanged(BR.errorText)
        }
    @get:Bindable
    var startingSum=""
        set(value){
            field=value
            notifyPropertyChanged(BR.startingSum)
        }
    @get:Bindable
    var visibility= View.INVISIBLE
        set(value){
            field=value
            notifyPropertyChanged(BR.visibility)
        }
}