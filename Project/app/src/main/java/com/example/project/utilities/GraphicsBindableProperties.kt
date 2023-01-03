package com.example.project.utilities

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.example.project.BR

class GraphicsBindableProperties:BaseObservable() {

    @get:Bindable
    var outputText:String=""
        set(value){
            field=value
            notifyPropertyChanged(BR.outputText)
        }
}