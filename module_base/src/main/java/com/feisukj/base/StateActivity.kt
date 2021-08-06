package com.feisukj.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gyf.immersionbar.ImmersionBar

abstract class StateActivity(contentLayoutId:Int):AppCompatActivity(contentLayoutId) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ImmersionBar.with(this)
            .statusBarColor(getStatusBarColor())
            .statusBarDarkFont(isStatusBarDarkFont())
            .init()
    }

    protected open fun getStatusBarColor()=android.R.color.transparent

    protected open fun isStatusBarDarkFont()=true
}