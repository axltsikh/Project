package com.example.project.utilities

import android.view.View
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.example.project.BR

class AddOperationBindableProperties: BaseObservable() {
    @get:Bindable
    var currencySelection=0
        set(value){
            field=value
            notifyPropertyChanged(BR.currencySelection)
        }
    @get:Bindable
    var operationSum=""
        set(value){
            field=value
            notifyPropertyChanged(BR.operationSum)
        }
    @get:Bindable
    var elementsVisibility= View.GONE
        set(value){
            field=value
            notifyPropertyChanged(BR.elementsVisibility)
        }
    @get:Bindable
    var afterConversionValue:String=""
        set(value){
            field=value
            notifyPropertyChanged(BR.afterConversionValue)
        }
    @get:Bindable
    var baseCurrency:String=""
        set(value){
            field=value
            notifyPropertyChanged(BR.baseCurrency)
        }
    @get:Bindable
    var baseToRequired:Double=1.0
        set(value){
            field=value
            notifyPropertyChanged(BR.baseToRequired)
        }
    @get:Bindable
    var errorText:String=""
    public set(value){
        field=value
        notifyPropertyChanged(BR.errorText)
    }
    @get:Bindable
    var errorVisibility=View.INVISIBLE
        public set(value){
            field=value
            notifyPropertyChanged(BR.errorVisibility)
        }
}